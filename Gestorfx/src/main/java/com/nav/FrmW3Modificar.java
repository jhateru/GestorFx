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

public class FrmW3Modificar {

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
    void btnConsultarAction(ActionEvent event) {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato requerido", "Ingrese el DNI para consultar.");
            return;
        }

        Personal encontrado = dao.buscarPorDni(dni);
        if (encontrado != null) {
            txtNombre.setText(encontrado.getNombre());
            txtSueldo.setText(encontrado.getSueldo());
            tblPersonal.getSelectionModel().select(encontrado);
            tblPersonal.scrollTo(encontrado);
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se halló un colaborador con ese DNI.");
            txtNombre.clear();
            txtSueldo.clear();
            tblPersonal.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void btnModificarAction(ActionEvent event) {
        if (!camposCompletos()) {
            return;
        }

        Personal actualizado = new Personal(txtDni.getText().trim(), txtNombre.getText().trim(), txtSueldo.getText().trim());
        boolean ok = dao.actualizar(actualizado);
        if (ok) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Datos modificados correctamente.");
            actualizarTabla();
            tblPersonal.getSelectionModel().select(actualizado);
            tblPersonal.scrollTo(actualizado);
            limpiarCampos();
            
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un registro con ese DNI.");
        }
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
