package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.VendedorServicos;

public class ListaVendedorControle implements Initializable, DataChangeListener {

	private VendedorServicos servico;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnNome;

	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;

	@FXML
	private TableColumn<Vendedor, Date> tableColumnDataAniversario;

	@FXML
	private TableColumn<Vendedor, Double> tableColumnBaseSalario;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEDIT;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsLista;

	@FXML
	public void onBtNewAction(ActionEvent evento) {
		Stage parentStage = Utils.currentStage(evento);
		Vendedor obj = new Vendedor();
		createDialogForm(obj, "/gui/VendedorFormulario.fxml", parentStage);
	}

	public void setVendedor(VendedorServicos servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataAniversario.setCellValueFactory(new PropertyValueFactory<>("data"));
		Utils.formatTableColumnDate(tableColumnDataAniversario, "dd/MM/yyyy");
		tableColumnBaseSalario.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(tableColumnBaseSalario, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (servico == null) {
			throw new IllegalStateException("Serviço está nulo");
		}
		List<Vendedor> list = servico.buscaTodos();
		obsLista = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsLista);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Vendedor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FormularioVendedorControle controle = loader.getController();
			controle.setVendedor(obj);
			controle.setVendedorServico(new VendedorServicos());
			controle.subscribeChangeListener(this);
			controle.updateFormularioDados();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(("Entre com os dados do vendedor "));
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/VendedorFormulario.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que voçê quer deletar");

		if (result.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("Serviço esta nulo!.");
			}
			try {
				servico.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error remove objetos", null, e.getMessage(), AlertType.ERROR);
			}
		}

	}

}