package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.company.inventory.modelo.Producto;

//CrudRepository clase q lo usara y el tipo de su id
public interface IProductoDao extends CrudRepository<Producto,Long>{
	
	
	//buscar por nombre un producto
	@Query("SELECT p FROM Producto p WHERE p.nombre LIKE %?1%")
	List<Producto> buscarXNombreLike(String nombre);
	
	List<Producto> findByNombreContaining(String nombre);

}
