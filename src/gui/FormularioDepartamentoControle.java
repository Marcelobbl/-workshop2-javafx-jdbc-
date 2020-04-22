package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

public class FormularioDepartamentoControle implements Initializable{
	
	private Departamento entidade;
			
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

	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");		
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
