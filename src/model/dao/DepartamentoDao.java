package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDao {

	void insert(Departamento obj);
	void update(Departamento obj);
	void deletePorId(Integer id);
	Departamento buscaPorId(Integer id);
	List<Departamento> buscaTodos();
}
