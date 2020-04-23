package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.exceptions.ValidationException;
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
		
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
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
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
				
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("Nome", "O campo não pode ser vazio!");
		}
		obj.setNomeDep(txtNome.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
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
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("Nome")) {
		labelErroNome.setText(errors.get("Nome"));
	}
	
	
	}	

}
