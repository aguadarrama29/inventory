package com.company.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.modelo.Categoria;

//CrudRepository clase q lo usara y el tipo de su id
public interface ICategoriaDao extends CrudRepository<Categoria,Long>{
	

}
