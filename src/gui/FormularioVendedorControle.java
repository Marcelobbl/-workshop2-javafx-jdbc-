package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidationException;
import model.services.DepartamentoServicos;
import model.services.VendedorServicos;

public class FormularioVendedorControle implements Initializable {

	private Vendedor entidade;

	private VendedorServicos servico;

	private DepartamentoServicos departamentoServico;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelErroNome;

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroDataNascimento;

	@FXML
	private Label labelErroSalarioBase;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Departamento> obsList;

	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setServicos(VendedorServicos servico, DepartamentoServicos departamentoServico) {
		this.servico = servico;
		this.departamentoServico = departamentoServico;
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

		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando dados", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}

	}

	private Vendedor getFormularioDados() {
		Vendedor obj = new Vendedor();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("Nome", "O campo não pode ser vazio!");
		}
		obj.setNome(txtNome.getText());

		if (exception.getErrors().size() > 0) {
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
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtSalarioBase);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void updateFormularioDados() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade não pode ser nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText((entidade.getNome()));
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));
		if (entidade.getData() != null) {
			dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getData().toInstant(), ZoneId.systemDefault()));
		}
		if (entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartamento.setValue(entidade.getDepartamento());
		}
	}

	public void loadAssociatedObjects() {
		if (departamentoServico == null) {
			throw new IllegalStateException("O serviço departamento esta nulo!");
		}
		List<Departamento> list = departamentoServico.buscaTodos();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Nome")) {
			labelErroNome.setText(errors.get("Nome"));
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeDep());
			}
		};

		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
