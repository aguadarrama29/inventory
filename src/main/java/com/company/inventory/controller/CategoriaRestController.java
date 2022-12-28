package com.company.inventory.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.modelo.Categoria;
import com.company.inventory.response.CategoriaResponseRest;
import com.company.inventory.services.ICategoriaService;
import com.company.inventory.utils.CategoriaExcelExportar;

//todas las peticiones de este servidor se permitan
@CrossOrigin(origins = {"https://front-inventario-my-proyecto.apps-crc.testing","http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CategoriaRestController {
	
	@Autowired
	private ICategoriaService categoriaService;
	
	//metodo para todas las categorias
	@GetMapping("/obtenerCategorias")
	public ResponseEntity<CategoriaResponseRest> obtenerCategorias(){
		ResponseEntity<CategoriaResponseRest> response = categoriaService.obtenerCategorias();
		return response;
	}
	
	//metodo para buscar por id
	@GetMapping("/categoria/{id}")
	public ResponseEntity<CategoriaResponseRest> categoriaXId(@PathVariable Long id){
		ResponseEntity<CategoriaResponseRest> response = categoriaService.categoriaXId(id);
		return response;
	}
	
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<Categoria> alum = categoriaService.findById(id);
		if (alum.isPresent()) {
			return ResponseEntity.ok(alum.get());//200
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	
	//metodo para guardar categoria
	@PostMapping("/guardarCategoria")
	public ResponseEntity<CategoriaResponseRest> guardarCategoria(@RequestBody Categoria categoria){
		ResponseEntity<CategoriaResponseRest> response = categoriaService.save(categoria);
		return response;
	}
	
	
	//metodo para actualizar categoria
	@PutMapping("/categorias/{id}")
	public ResponseEntity<CategoriaResponseRest> actualizarCategoria(@RequestBody Categoria categoria, @PathVariable Long id){
		ResponseEntity<CategoriaResponseRest> response = categoriaService.update(categoria, id);
		return response;
	}
	
	//metodo para eliminar categoria
	@DeleteMapping("/eliminarCategoria/{id}")
	public ResponseEntity<CategoriaResponseRest> actualizarCategoria(@PathVariable Long id){
		ResponseEntity<CategoriaResponseRest> response = categoriaService.eliminar(id);
		return response;
	}
	
	//METODO PARA EXPORTAR A EXCEL
	@GetMapping("/categoria/export/excel")
	public void exportarExcel(HttpServletResponse response)throws IOException{
		
		response.setContentType("application/octet-stream");//represetna un archivo excel q exportare
		
		String headerKey = "content-disposition";
		String headerValue= "attachment; filename=catalogo_categorias"; //nombre del archivo
		response.setHeader(headerKey, headerValue);
		
		//obtener todas las categorias
		ResponseEntity<CategoriaResponseRest> responseCategoria = categoriaService.obtenerCategorias();
		
		//a la clase de utils se inicializa su contructor con las categorias encontradas
		//se obtiene del body.response.getCategoria
		CategoriaExcelExportar excelExportar= new CategoriaExcelExportar(
				responseCategoria.getBody().getCategoriaResponse().getCategoria());
		
		//se manda a llamar el meotdo donde se arma todo el excel, ya tendra datos
		excelExportar.exportar(response);
		
	}

}
