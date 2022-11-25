package com.company.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.company.inventory.modelo.Producto;
import com.company.inventory.response.ProductoResponseRest;
import com.company.inventory.services.IProductoService;
import com.company.inventory.utils.ProductoExcelExportar;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ProductoRestController {
	
	@Autowired
	private IProductoService iProductoService;
	
	//metodo para guardar producto
	@PostMapping("/guardaProducto")
	public ResponseEntity<ProductoResponseRest> save(
			@RequestParam("foto") MultipartFile foto,
			@RequestParam("nombre") String nombre,
			@RequestParam("precio") int precio,
			@RequestParam("cantidad") int cantidad,
			@RequestParam("categoriaId") Long idCategoria)throws IOException
	{
		
		Producto p= new Producto();
		p.setNombre(nombre);
		p.setPrecio(precio);
		p.setCantidad(cantidad);
		p.setFoto(com.company.inventory.utils.Util.compressZLib(foto.getBytes()));
		System.out.println("foto"+p.getFoto());
		
		ResponseEntity<ProductoResponseRest> response = iProductoService.save(p, idCategoria);
		return response;
		
	}
	
	
	//metodo para obtener el producto por id
	@GetMapping("/obtenerproductos/{id}")
	public ResponseEntity<ProductoResponseRest> obtenerproductos(@PathVariable Long id){
		ResponseEntity<ProductoResponseRest> response = iProductoService.obtenerProductoXId(id);
		return response;
	}
	
	
	//metodo para lista de  producto por nombre
	@GetMapping("/productosXNombre/{nombre}")
	public ResponseEntity<ProductoResponseRest> obtenerproductos(@PathVariable String nombre){
		ResponseEntity<ProductoResponseRest> response = iProductoService.obtenerProductoXnombre(nombre);
		return response;
	}
	
	
	//metodo para eliminar el producto por id
	@DeleteMapping("/eliminarProducto/{id}")
	public ResponseEntity<ProductoResponseRest> eliminarProducto(@PathVariable Long id){
		ResponseEntity<ProductoResponseRest> response = iProductoService.eliminarProductoXId(id);
		return response;
	}

	
	//metodo para obtener todos los productos
	@GetMapping("/obtenerAllProductos")
	public ResponseEntity<ProductoResponseRest> obtenerAllProductos(){
		ResponseEntity<ProductoResponseRest> response = iProductoService.obtenerProductos();
		return response;
	}
	
	
	//metodo para actualizar producto
	@PutMapping("/actualizaProducto/{id}")
	public ResponseEntity<ProductoResponseRest> actualizaProducto(
			@RequestParam("foto") MultipartFile foto,
			@RequestParam("nombre") String nombre,
			@RequestParam("precio") int precio,
			@RequestParam("cantidad") int cantidad,
			@RequestParam("categoriaId") Long idCategoria,
			@PathVariable Long id)throws IOException
	{
		
		Producto p= new Producto();
		p.setNombre(nombre);
		p.setPrecio(precio);
		p.setCantidad(cantidad);
		p.setFoto(com.company.inventory.utils.Util.compressZLib(foto.getBytes())); //se comprime la imagen
		
		ResponseEntity<ProductoResponseRest> response = iProductoService.actualizarProducto(p,id, idCategoria);
		return response;
		
	}
	
	
	
	
	/**
	 * export product in excel file
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/productos/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_product";
		response.setHeader(headerKey, headerValue);
		
		ResponseEntity<ProductoResponseRest> products = iProductoService.obtenerProductos();
		
		ProductoExcelExportar excelExporter = new ProductoExcelExportar(
				products.getBody().getProducto().getProductos());
		
		excelExporter.exportar(response);
				
		
	}
}
