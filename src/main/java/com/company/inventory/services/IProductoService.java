package com.company.inventory.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.modelo.Producto;
import com.company.inventory.response.ProductoResponseRest;

public interface IProductoService {
	
	
	public ResponseEntity<ProductoResponseRest> save(Producto producto, Long categoriaId);
	
	public ResponseEntity<ProductoResponseRest> obtenerProductoXId(Long idProducto);
	
	public ResponseEntity<ProductoResponseRest> obtenerProductoXnombre(String nombre);
	
	public ResponseEntity<ProductoResponseRest> eliminarProductoXId(Long idProducto);
	
	public ResponseEntity<ProductoResponseRest> obtenerProductos();
	
	public ResponseEntity<ProductoResponseRest> actualizarProducto(Producto producto, Long productoId, Long categoriaId);

}
