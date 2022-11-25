package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoriaDao;
import com.company.inventory.modelo.Categoria;
import com.company.inventory.response.CategoriaResponseRest;

//indicar que la clase es una de servicio
@Service
public class CategoriaServiceImp implements ICategoriaService{
	
	@Autowired
	private ICategoriaDao categoriaDao;
	
	@Override
	@Transactional(readOnly = true) //debe ser de spring
	public ResponseEntity<CategoriaResponseRest> obtenerCategorias(){
		CategoriaResponseRest response = new CategoriaResponseRest();
		try {
			List<Categoria> categorias= (List<Categoria>) categoriaDao.findAll();
			response.getCategoriaResponse().setCategoria(categorias);
			response.setMetadata("OK", "00", "Respuesta exitosa");
		} catch (Exception e) {
			response.setMetadata("ERROR", "-1", "Error al consultar");
			e.printStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true) //debe ser de spring
	public ResponseEntity<CategoriaResponseRest> categoriaXId(Long id) {
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			//findById regresa un obj de tipo optional por eso asi se maneja
			Optional<Categoria> categoria=categoriaDao.findById(id);
			//si encontro algo isPresent
				if(categoria.isPresent()) {
					System.out.println("siiiii");
					list.add(categoria.get());
					System.out.println("categoria"+list.size());
					response.getCategoriaResponse().setCategoria(list);
					response.setMetadata("OK", "00", "Respuesta exitosa");
				}else {
					response.setMetadata("ERROR", "-1", "Error categoria no encontrada por ID");
					return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.NOT_FOUND);
				}
		} catch (Exception e) {
			response.setMetadata("ERROR", "-1", "Error al consultar por ID");
			e.printStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.OK);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Categoria> findById(Long id) {
		
		return categoriaDao.findById(id);
	}

	@Override
	@Transactional(readOnly = false) //debe ser de spring para save debe ser false
	public ResponseEntity<CategoriaResponseRest> save(Categoria categoria) {
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			Categoria categoriaSave = categoriaDao.save(categoria);
			
			//quiere decir que trae alguna respiuesta y se guardo
			if (categoriaSave!= null) {
				list.add(categoriaSave);
				response.getCategoriaResponse().setCategoria(list);
				response.setMetadata("OK", "00", "Registro exitoso");
			}else {
				response.setMetadata("ERROR", "-1", "Error al guardar la categoria");
				return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMetadata("ERROR", "-1", "Error al guardar categoria");
			e.printStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = false) //debe ser de spring para update  debe ser false
	public ResponseEntity<CategoriaResponseRest> update(Categoria categorianueva, Long id) {
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			System.out.println("que id me llega"+id);
			
			//buscar la categoria
			Optional<Categoria> categoriaOriginal=categoriaDao.findById(id);
			
			//si encontro algo isPresent se puede actualziar
			if(categoriaOriginal.isPresent()) {
				//categoriaOriginal se le setean los nuevos valores que vienen en categoria
				categoriaOriginal.get().setNombre(categorianueva.getNombre());
				categoriaOriginal.get().setDescripcion(categorianueva.getDescripcion());
				
				System.out.println("que e slo nuevo"+categoriaOriginal.get().getNombre());
				
				//linea para actualziar la categoria
				Categoria cateUpdate=categoriaDao.save(categoriaOriginal.get());
				
				//saber si se actualizo si es dif de nul si lo hizo
				if(cateUpdate!=null) {
					//llenar mi clase de respuesta
					list.add(cateUpdate);
					response.getCategoriaResponse().setCategoria(list);
					response.setMetadata("OK", "00", "Categoria actualizada con exito");
				//en caso de no poder actualziar
				}else {
					response.setMetadata("ERROR", "-1", "Error al actualizar categoria");
					return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.BAD_REQUEST);
				}
			//si no se encuentra la categoria
			}else {
				response.setMetadata("ERROR", "-1", "Error al encontrar la categoria");
				return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.setMetadata("ERROR", "-1", "Error al guardar categoria");
			e.printStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = false) //debe ser de spring para update  debe ser false
	public ResponseEntity<CategoriaResponseRest> eliminar(Long id) {
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		try {			
			categoriaDao.deleteById(id);			
			response.setMetadata("OK", "00", "Registro eliminado.");
			
		} catch (Exception e) {
			response.setMetadata("ERROR", "-1", "Error al eliminar por ID");
			e.printStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response,HttpStatus.OK);
	}

	
	
	


}
