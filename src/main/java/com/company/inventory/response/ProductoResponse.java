package com.company.inventory.response;

import java.util.List;

import com.company.inventory.modelo.Producto;


public class ProductoResponse {
	List<Producto> productos;

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
}
