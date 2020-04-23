package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.services.DepartamentoServicos;

public class FormularioDepartamentoControle implements Initializable{
	
	private Departamento entidade;
	
	private DepartamentoServicos servico;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErroNome;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	public void setDepartamentoServico(DepartamentoServicos servico) {
		this.servico= servico;
	}
	
	public void subscribeChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava vazia");
			}
		if (servico == null) {
			throw new IllegalStateException("Serviço está vazio");
		}
		try {
			entidade = getFormularioDados();
			servico.saveOrUpdate(entidade);
			notifyChangeListener();
			Utils.currentStage(evento).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando dados", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyChangeListener() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
		
	}
	private Departamento getFormularioDados() {
		Departamento obj = new Departamento();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNomeDep(txtNome.getText());
		return obj;
	}
	@FXML
	public void onBtCancelAction(ActionEvent evento) {
		Utils.currentStage(evento).close();		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormularioDados() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade não pode ser nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText((entidade.getNomeDep()));
	}
	
	
	

}
