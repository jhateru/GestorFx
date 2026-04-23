package com.nav;

import com.nav.App;
import com.nav.entidades.Personal;
import com.nav.dao.PersonaDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FrmPersonal {

    @FXML private VBox rootPrincipal;
    @FXML private Button btnBienvenida;
    @FXML private AnchorPane panelContenido;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDni;
    @FXML private TextField txtSueldo;

    @FXML private TableView<Personal> tblPersonal;
    @FXML private TableColumn<Personal, String> colDni;
    @FXML private TableColumn<Personal, String> colNombre;
    @FXML private TableColumn<Personal, String> colSueldo;

    private PersonaDao dao = new PersonaDao();
    private ObservableList<Personal> listaObservable = FXCollections.observableArrayList();
    private Parent contenidoOriginal;
    private int indiceContenido;
    private boolean mostrandoBienvenida = false;

    @FXML
    public void initialize() {
        contenidoOriginal = panelContenido;
        indiceContenido = rootPrincipal.getChildren().indexOf(panelContenido);
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        tblPersonal.setItems(listaObservable);
        
        actualizarTabla();
    }

    @FXML
    void btnAgregarAction(ActionEvent event) {
        if (!validarCamposObligatorios()) {
            return;
        }
        Personal p = new Personal(
                txtDni.getText().trim(),
                txtNombre.getText().trim(),
                txtSueldo.getText().trim()
        );
        dao.agregar(p);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Personal agregado correctamente.");
        actualizarTabla();
        limpiarCampos();
    }

    @FXML
    void btnConsultarAction(ActionEvent event) {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato requerido", "Ingrese el DNI para realizar la búsqueda.");
            return;
        }

        Personal p = dao.buscarPorDni(dni);
        if (p != null) {
            txtNombre.setText(p.getNombre());
            txtSueldo.setText(p.getSueldo());
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se encontró un empleado con ese DNI.");
        }
    }

    @FXML
    void btnModificarAction(ActionEvent event) {
        if (!validarCamposObligatorios()) {
            return;
        }
        Personal p = new Personal(
                txtDni.getText().trim(),
                txtNombre.getText().trim(),
                txtSueldo.getText().trim()
        );
        boolean exito = dao.actualizar(p);

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Datos actualizados correctamente.");
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No se pudo actualizar (verifique el DNI).");
        }
        
    }

    @FXML
    void btnEliminarAction(ActionEvent event) {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato requerido", "Ingrese el DNI para eliminar.");
            return;
        }

        boolean exito = dao.eliminar(dni);
        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado eliminado.");
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No se encontró el DNI indicado.");
        }
    }

    @FXML
    void btnMostrarAction(ActionEvent event) {
        actualizarTabla();
    }

    @FXML
    void btnBienvenidaAction(ActionEvent event) {
        if (mostrandoBienvenida) {
            return;
        }
        mostrarPantallaBienvenida();
    }

    @FXML
    void abrirVentanaConsultar(ActionEvent event) {
        abrirVentana("/com/nav/FrmW1Consultar.fxml", "Consultar Personal");
    }

    @FXML
    void abrirVentanaRegistrar(ActionEvent event) {
        abrirVentana("/com/nav/FrmW2Registrar.fxml", "Registrar Personal");
    }

    @FXML
    void abrirVentanaModificar(ActionEvent event) {
        abrirVentana("/com/nav/FrmW3Modificar.fxml", "Modificar Personal");
    }

    @FXML
    void abrirVentanaEliminar(ActionEvent event) {
        abrirVentana("/com/nav/FrmW4Eliminar.fxml", "Eliminar Personal");
    }

    @FXML
    void abrirVentanaListado(ActionEvent event) {
        abrirVentana("/com/nav/FrmW5Listado.fxml", "Listado General");
    }

    private void actualizarTabla() {
        listaObservable.setAll(dao.listar());
        tblPersonal.refresh();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDni.setText("");
        txtSueldo.setText("");
        txtDni.requestFocus();
    }

    private boolean validarCamposObligatorios() {
        if (txtDni.getText().trim().isEmpty() ||
            txtNombre.getText().trim().isEmpty() ||
            txtSueldo.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Complete DNI, Nombre y Sueldo.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void abrirVentana(String recurso, String titulo) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource(recurso));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            Stage owner = obtenerStagePrincipal();
            if (owner != null) {
                stage.initOwner(owner);
            }
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana: " + e.getMessage());
        }
    }

    private void mostrarPantallaBienvenida() {
        StackPane contenedor = new StackPane();
        contenedor.setStyle("-fx-background-color: linear-gradient(#ffffff, #e5ecf4);");

        VBox mensajeBox = new VBox(20);
        mensajeBox.setAlignment(Pos.CENTER);

        Label mensaje = new Label("Bienvenido al Gestor de Personal by Jeyson R Navarro.");
        mensaje.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> restaurarVistaPrincipal());

        mensajeBox.getChildren().addAll(mensaje, btnVolver);
        contenedor.getChildren().add(mensajeBox);

        VBox.setVgrow(contenedor, Priority.ALWAYS);
        rootPrincipal.getChildren().set(indiceContenido, contenedor);
        mostrandoBienvenida = true;
        btnBienvenida.setDisable(true);
    }

    private void restaurarVistaPrincipal() {
        if (contenidoOriginal != null) {
            VBox.setVgrow(contenidoOriginal, Priority.ALWAYS);
            rootPrincipal.getChildren().set(indiceContenido, contenidoOriginal);
            mostrandoBienvenida = false;
            btnBienvenida.setDisable(false);
        }
    }

    private Stage obtenerStagePrincipal() {
        Scene scene = rootPrincipal.getScene();
        return scene != null ? (Stage) scene.getWindow() : null;
    }
}
