package com.company.inventory.services;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.company.inventory.modelo.Categoria;
import com.company.inventory.response.CategoriaResponseRest;

public interface ICategoriaService {
	
	//ResponseEntity permite una respuesta custom en este caso CategoriaResponseRest
	public ResponseEntity<CategoriaResponseRest> obtenerCategorias();
	
	public ResponseEntity<CategoriaResponseRest> categoriaXId(Long id);
	
	public Optional<Categoria> findById(Long id);
	
	public ResponseEntity<CategoriaResponseRest> save(Categoria categoria);
	
	public ResponseEntity<CategoriaResponseRest> update(Categoria categoria, Long id);
	
	public ResponseEntity<CategoriaResponseRest> eliminar(Long id);

}
