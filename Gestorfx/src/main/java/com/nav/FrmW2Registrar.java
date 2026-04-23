package com.nav;

import com.nav.dao.PersonaDao;
import com.nav.entidades.Personal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FrmW2Registrar {

    @FXML private Button btnVolver;
    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtSueldo;
    @FXML private TableView<Personal> tblPersonal;
    @FXML private TableColumn<Personal, String> colDni;
    @FXML private TableColumn<Personal, String> colNombre;
    @FXML private TableColumn<Personal, String> colSueldo;

    private final PersonaDao dao = new PersonaDao();
    private final ObservableList<Personal> listaObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        tblPersonal.setItems(listaObservable);
        actualizarTabla();
    }

    @FXML
    void btnAgregarAction(ActionEvent event) {
        if (!camposCompletos()) {
            return;
        }

        String dni = txtDni.getText().trim();
        if (dao.buscarPorDni(dni) != null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Duplicado", "Ya existe un colaborador con ese DNI.");
            return;
        }

        Personal nuevo = new Personal(dni, txtNombre.getText().trim(), txtSueldo.getText().trim());
        dao.agregar(nuevo);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Personal registrado correctamente.");
        actualizarTabla();
        limpiarCampos();
    }

    @FXML
    void btnVolverAction(ActionEvent event) {
        cerrarVentana();
    }

    private boolean camposCompletos() {
        if (txtDni.getText().trim().isEmpty() ||
            txtNombre.getText().trim().isEmpty() ||
            txtSueldo.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Complete DNI, Nombre y Sueldo.");
            return false;
        }
        return true;
    }

    private void actualizarTabla() {
        listaObservable.setAll(dao.listar());
        tblPersonal.refresh();
    }

    private void limpiarCampos() {
        txtDni.clear();
        txtNombre.clear();
        txtSueldo.clear();
        txtDni.requestFocus();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
