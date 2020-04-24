package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entities.Departamento;


public class DepartamentoServicos {
	
	private DepartamentoDao dao = DaoFactory.createDepartamentoDao();

	public List<Departamento> buscaTodos() {
		return dao.buscaTodos();
	}
	public void saveOrUpdate(Departamento obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	public void remove(Departamento obj) {
		dao.deletePorId(obj.getId());
	}
}