package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDao {

	void insert(Vendedor obj);
	void update(Vendedor obj);
	void deletePorId(Integer id);
	Vendedor buscaPorId(Integer id);
	List<Vendedor> buscaTodos();
	List<Vendedor> buscaPorDepartamento(Departamento departamento);

}
