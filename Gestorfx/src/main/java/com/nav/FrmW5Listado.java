package com.nav;

import com.nav.dao.PersonaDao;
import com.nav.entidades.Personal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FrmW5Listado {

    @FXML private Button btnVolver;
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
}
