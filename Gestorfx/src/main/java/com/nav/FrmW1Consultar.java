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

public class FrmW1Consultar {

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

        Personal resultado = dao.buscarPorDni(dni);
        if (resultado != null) {
            txtNombre.setText(resultado.getNombre());
            txtSueldo.setText(resultado.getSueldo());
            tblPersonal.getSelectionModel().select(resultado);
            tblPersonal.scrollTo(resultado);
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se encontró un colaborador con ese DNI.");
            txtNombre.clear();
            txtSueldo.clear();
            tblPersonal.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void btnVolverAction(ActionEvent event) {
        cerrarVentana();
    }

    private void actualizarTabla() {
        listaObservable.setAll(dao.listar());
        tblPersonal.refresh();
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
