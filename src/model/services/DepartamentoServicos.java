package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoServicos {

	public List<Departamento> encontrarTodos() {

		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Pc"));
		list.add(new Departamento(3, "Eletronicos"));
		return list;

	}
}