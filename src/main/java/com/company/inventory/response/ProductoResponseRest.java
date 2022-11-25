package com.company.inventory.response;

public class ProductoResponseRest extends ResponseRest{
	private ProductoResponse producto= new ProductoResponse();

	public ProductoResponse getProducto() {
		return producto;
	}

	public void setProducto(ProductoResponse producto) {
		this.producto = producto;
	}
}
