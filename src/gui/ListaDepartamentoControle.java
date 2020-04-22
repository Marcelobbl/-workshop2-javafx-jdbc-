package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoServicos;

public class ListaDepartamentoControle implements Initializable{
	
	private DepartamentoServicos servico;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	 public void setDepartamento(DepartamentoServicos servico) {
		 this.servico = servico;
	 }
	 
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomeDep"));
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	public void updateTableView() {
		if (servico == null) {
			throw new IllegalStateException("Serviço está nulo");
		}
		List<Departamento> list = servico.encontrarTodos();
		obsLista = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsLista);
	}

}