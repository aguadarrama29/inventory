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
import com.company.inventory.dao.IProductoDao;
import com.company.inventory.modelo.Categoria;
import com.company.inventory.modelo.Producto;
import com.company.inventory.response.ProductoResponseRest;
import com.company.inventory.utils.Util;

@Service
public class ProductoServiceImp implements IProductoService {
	
	//inyectar con el contructor
	private ICategoriaDao categoriaDao;	
	
	//inyectar dao producto
	@Autowired
	private IProductoDao iProductoDao;

	//inyecta ICategoriaDao categoriaDao y lo aisgna a this.categoriaDao
	public ProductoServiceImp(ICategoriaDao categoriaDao) {
		super();
		this.categoriaDao = categoriaDao;
	}
	/*
	 lo anterior sustituye a 
	@Autowired
	private ICategoriaDao categoriaDao;
	 */



	@SuppressWarnings("unused")
	@Override
	@Transactional(readOnly = false) //debe ser de spring para save debe ser false
	public ResponseEntity<ProductoResponseRest> save(Producto producto, Long categoriaId) {
		ProductoResponseRest response = new ProductoResponseRest();
		List<Producto> list= new ArrayList<>();
		
		try {
			//buscar la categoria por id para setter al producto
			Optional<Categoria> categoria= categoriaDao.findById(categoriaId);
			//si se encontro la categoria
			if(categoria.isPresent()) {
				producto.setCategoria(categoria.get());
			}else {
				response.setMetadata("respuesta error", "-1", "Categoria no encontrada, asociada al producto");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			//guardar producto
			Producto psave=iProductoDao.save(producto);
			//si es diferente de null se guardo
			if(producto != null) {
				list.add(psave);
				response.getProducto().setProductos(list);
				response.setMetadata("respuesta Exito!", "00", "Producto Guardado.");
			}else {
				response.setMetadata("respuesta error", "-1", "Producto no guardado");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al guardar producto");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}



	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductoResponseRest> obtenerProductoXId(Long idProducto) {
		ProductoResponseRest response = new ProductoResponseRest();
		List<Producto> list= new ArrayList<>();
		
		try {
			//buscar producto por Id
			Optional<Producto> pro= iProductoDao.findById(idProducto);
			//si se encontro el producto
			if(pro.isPresent()) {
				//descomprimir la imagen para presentarla
				byte[] imagenDescom= Util.decompressZLib(pro.get().getFoto());
				pro.get().setFoto(imagenDescom);
				list.add(pro.get());//el objeto en su totalidad con el .get()
				response.getProducto().setProductos(list);
				response.setMetadata("respuesta exitosa!", "00", "Producto encontrado");
			}else {
				response.setMetadata("respuesta error", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al obtener producto");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}



	@Override
	public ResponseEntity<ProductoResponseRest> obtenerProductoXnombre(String nombre) {
		ProductoResponseRest response = new ProductoResponseRest();
		List<Producto> list= new ArrayList<>();
		
		try {
			//buscar producto por nombre
			//List<Producto> pro= iProductoDao.buscarXNombreLike(nombre);
			List<Producto> pro= iProductoDao.findByNombreContaining(nombre);

			if(pro.size()>0) {	
				
				//funcion lambda ->    el stram sustituye el foreach
				pro.stream().forEach( (p)->{
					//descomprimir la imagen para presentarla
					byte[] imagenDescom= Util.decompressZLib(p.getFoto());
					p.setFoto(imagenDescom);
					list.add(p);
				} );
				
				response.getProducto().setProductos(list);
				response.setMetadata("respuesta exitosa!", "00", "Productos encontrados");
			}else {
				response.setMetadata("respuesta error", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al obtener productos");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}



	@Override
	@Transactional
	public ResponseEntity<ProductoResponseRest> eliminarProductoXId(Long idProducto) {
		ProductoResponseRest response = new ProductoResponseRest();
		
		try {
			//eliminar producto por Id
			iProductoDao.deleteById(idProducto);
			response.setMetadata("respuesta exitosa!", "00", "Producto eliminado");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al eliminar producto");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}



	@Override
	public ResponseEntity<ProductoResponseRest> obtenerProductos() {
		ProductoResponseRest response = new ProductoResponseRest();
		List<Producto> list= new ArrayList<>();
		
		try {
			//obtener lista de productos
			List<Producto> pro= (List<Producto>) iProductoDao.findAll();

			if(pro.size()>0) {	
				
				//funcion lambda ->    el stram sustituye el foreach
				pro.stream().forEach( (p)->{
					//descomprimir la imagen para presentarla
					byte[] imagenDescom= Util.decompressZLib(p.getFoto());
					p.setFoto(imagenDescom);
					list.add(p);
				} );
				
				response.getProducto().setProductos(list);
				response.setMetadata("respuesta exitosa!", "00", "Productos encontrados");
			}else {
				response.setMetadata("respuesta error", "-1", "Productos no encontrado");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al obtener productos");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}



	@Override
	@Transactional
	public ResponseEntity<ProductoResponseRest> actualizarProducto(Producto productoReques, Long productoId, Long categoriaId) {
		ProductoResponseRest response = new ProductoResponseRest();
		List<Producto> list= new ArrayList<>();
		
		try {
			
			Optional<Categoria> categoria=categoriaDao.findById(categoriaId);
			
			//si se encontro el producto
			if(categoria.isPresent()) {
				productoReques.setCategoria(categoria.get());
			}else {
				response.setMetadata("respuesta error", "-1", "Categoria no encontrada, asociada al producto");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			
			//buscar producto a actualziar
			Optional<Producto> proEncon=iProductoDao.findById(productoId);
			
			//si es diferente de null se encontro
			if(proEncon.isPresent()) {
				proEncon.get().setCantidad(productoReques.getCantidad());
				proEncon.get().setCategoria(productoReques.getCategoria());
				proEncon.get().setFoto(productoReques.getFoto());// ya viee en base 64
				proEncon.get().setNombre(productoReques.getNombre());
				proEncon.get().setPrecio(productoReques.getPrecio());
				
				//actualizar 
				Producto proActualizado=iProductoDao.save(proEncon.get());
				
				//si no e snulo se actualizo bien
				if(proActualizado!= null) {
					list.add(proActualizado);
					response.getProducto().setProductos(list);
					response.setMetadata("respuesta Exito!", "00", "Producto Actualizado.");
				}else {
					response.setMetadata("respuesta error", "-1", "Producto no Actualizado");
					return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.BAD_REQUEST);
				}
				
			}else {
				response.setMetadata("respuesta error", "-1", "Producto no Actualizado");
				return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMetadata("respuesta error", "-1", "Error al Actualizado producto");
			return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);//error 500
		}
		
		return new ResponseEntity<ProductoResponseRest>(response,HttpStatus.OK);
	}

}
