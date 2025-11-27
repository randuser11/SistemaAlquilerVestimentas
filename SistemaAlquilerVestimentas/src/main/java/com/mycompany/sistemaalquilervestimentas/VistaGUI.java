package com.mycompany.sistemaalquilervestimentas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Optional;

public class VistaGUI extends Application {
    private GestorInventario gestor;
    private TableView<Prenda> tablaPrendas;
    private ObservableList<Prenda> listaPrendas;
    private Label lblEstadisticas;

    @Override
    public void start(Stage primaryStage) {
        gestor = new GestorInventario();
        listaPrendas = FXCollections.observableArrayList();

        primaryStage.setTitle("Sistema de Alquiler de Vestimentas");
        
        BorderPane root = new BorderPane();
        root.setTop(crearBarraSuperior());
        root.setCenter(crearPanelCentral());
        root.setRight(crearPanelDerecho());
        root.setBottom(crearBarraInferior());

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        actualizarTabla();
        actualizarEstadisticas();
    }

    private HBox crearBarraSuperior() {
        HBox barra = new HBox(15);
        barra.setPadding(new Insets(15));
        barra.setStyle("-fx-background-color: #2c3e50;");
        barra.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("🎭 GESTIÓN DE ALQUILER DE VESTIMENTAS");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        barra.getChildren().add(titulo);
        return barra;
    }

    private VBox crearPanelCentral() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));

        // Barra de búsqueda y filtros
        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por ID...");
        txtBuscar.setPrefWidth(200);

        ComboBox<String> cmbFiltro = new ComboBox<>();
        cmbFiltro.getItems().addAll("Todas", "Disponibles", "Alquiladas");
        cmbFiltro.setValue("Todas");

        Button btnBuscar = new Button("🔍 Buscar");
        btnBuscar.setOnAction(e -> buscarPrenda(txtBuscar.getText()));

        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> filtrarPrendas(cmbFiltro.getValue()));

        Button btnActualizar = new Button("🔄 Actualizar");
        btnActualizar.setOnAction(e -> actualizarTabla());

        barraBusqueda.getChildren().addAll(
            new Label("Buscar:"), txtBuscar, btnBuscar,
            new Separator(), new Label("Filtro:"), cmbFiltro, btnFiltrar, btnActualizar
        );

        // Tabla de prendas
        tablaPrendas = crearTablaPrendas();

        panel.getChildren().addAll(barraBusqueda, tablaPrendas);
        VBox.setVgrow(tablaPrendas, Priority.ALWAYS);
        
        return panel;
    }

    private TableView<Prenda> crearTablaPrendas() {
        TableView<Prenda> tabla = new TableView<>();
        tabla.setItems(listaPrendas);

        TableColumn<Prenda, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(80);

        TableColumn<Prenda, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(150);

        TableColumn<Prenda, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(100);

        TableColumn<Prenda, String> colTalla = new TableColumn<>("Talla");
        colTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        colTalla.setPrefWidth(70);

        TableColumn<Prenda, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colColor.setPrefWidth(100);

        TableColumn<Prenda, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioAlquiler"));
        colPrecio.setPrefWidth(80);

        TableColumn<Prenda, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> {
            String estado = cellData.getValue().isDisponible() ? "DISPONIBLE" : "ALQUILADA";
            return new javafx.beans.property.SimpleStringProperty(estado);
        });
        colEstado.setPrefWidth(100);

        TableColumn<Prenda, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteActual"));
        colCliente.setPrefWidth(120);

        tabla.getColumns().addAll(colId, colNombre, colTipo, colTalla, colColor, 
                                   colPrecio, colEstado, colCliente);

        return tabla;
    }

    private VBox crearPanelDerecho() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(250);
        panel.setStyle("-fx-background-color: #ecf0f1;");

        Label lblTitulo = new Label("ACCIONES");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnAgregar = crearBoton("➕ Agregar Prenda", "#27ae60");
        btnAgregar.setOnAction(e -> mostrarDialogoAgregar());

        Button btnAlquilar = crearBoton("📤 Alquilar", "#3498db");
        btnAlquilar.setOnAction(e -> mostrarDialogoAlquilar());

        Button btnDevolver = crearBoton("📥 Devolver", "#f39c12");
        btnDevolver.setOnAction(e -> mostrarDialogoDevolver());

        Button btnEliminar = crearBoton("🗑️ Eliminar", "#e74c3c");
        btnEliminar.setOnAction(e -> eliminarPrendaSeleccionada());

        Button btnReporte = crearBoton("📊 Generar Reporte", "#9b59b6");
        btnReporte.setOnAction(e -> mostrarReporte());

        Separator separador = new Separator();

        lblEstadisticas = new Label();
        lblEstadisticas.setStyle("-fx-font-size: 12px;");
        lblEstadisticas.setWrapText(true);

        panel.getChildren().addAll(lblTitulo, btnAgregar, btnAlquilar, btnDevolver, 
                                    btnEliminar, separador, btnReporte, 
                                    new Separator(), lblEstadisticas);

        return panel;
    }

    private Button crearBoton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-font-weight: bold; -fx-padding: 10px;");
        return btn;
    }

    private HBox crearBarraInferior() {
        HBox barra = new HBox();
        barra.setPadding(new Insets(10));
        barra.setStyle("-fx-background-color: #34495e;");

        Label lblInfo = new Label("© 2025 Sistema de Alquiler de Vestimentas");
        lblInfo.setStyle("-fx-text-fill: white;");

        barra.getChildren().add(lblInfo);
        return barra;
    }

    private void mostrarDialogoAgregar() {
        Dialog<Prenda> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nueva Prenda");
        dialog.setHeaderText("Ingrese los datos de la prenda");

        ButtonType btnAgregar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAgregar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtId = new TextField();
        TextField txtNombre = new TextField();
        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("Vestido", "Traje", "Disfraz", "Otro");
        cmbTipo.setValue("Vestido");
        TextField txtTalla = new TextField();
        TextField txtColor = new TextField();
        TextField txtPrecio = new TextField();

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Tipo:"), 0, 2);
        grid.add(cmbTipo, 1, 2);
        grid.add(new Label("Talla:"), 0, 3);
        grid.add(txtTalla, 1, 3);
        grid.add(new Label("Color:"), 0, 4);
        grid.add(txtColor, 1, 4);
        grid.add(new Label("Precio:"), 0, 5);
        grid.add(txtPrecio, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAgregar) {
                try {
                    String id = txtId.getText();
                    String nombre = txtNombre.getText();
                    String tipo = cmbTipo.getValue();
                    String talla = txtTalla.getText();
                    String color = txtColor.getText();
                    double precio = Double.parseDouble(txtPrecio.getText());

                    return new Prenda(id, nombre, tipo, talla, color, precio);
                } catch (NumberFormatException e) {
                    mostrarError("Error", "El precio debe ser un número válido");
                    return null;
                }
            }
            return null;
        });

        Optional<Prenda> resultado = dialog.showAndWait();
        resultado.ifPresent(prenda -> {
            if (gestor.agregarPrenda(prenda)) {
                mostrarInfo("Éxito", "Prenda agregada correctamente");
                actualizarTabla();
                actualizarEstadisticas();
            } else {
                mostrarError("Error", "Ya existe una prenda con ese ID");
            }
        });
    }

    private void mostrarDialogoAlquilar() {
        Prenda seleccionada = tablaPrendas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Error", "Debe seleccionar una prenda");
            return;
        }

        if (!seleccionada.isDisponible()) {
            mostrarError("Error", "La prenda ya está alquilada");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Alquilar Prenda");
        dialog.setHeaderText("Alquilar: " + seleccionada.getNombre());

        ButtonType btnAlquilar = new ButtonType("Alquilar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAlquilar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtCliente = new TextField();
        Spinner<Integer> spnDias = new Spinner<>(1, 365, 7);

        grid.add(new Label("Cliente:"), 0, 0);
        grid.add(txtCliente, 1, 0);
        grid.add(new Label("Días:"), 0, 1);
        grid.add(spnDias, 1, 1);

        Label lblTotal = new Label();
        spnDias.valueProperty().addListener((obs, oldVal, newVal) -> {
            double total = seleccionada.getPrecioAlquiler() * newVal;
            lblTotal.setText(String.format("Total: $%.2f", total));
        });
        lblTotal.setText(String.format("Total: $%.2f", seleccionada.getPrecioAlquiler() * 7));

        grid.add(lblTotal, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == btnAlquilar) {
            String cliente = txtCliente.getText();
            int dias = spnDias.getValue();

            if (cliente.trim().isEmpty()) {
                mostrarError("Error", "Debe ingresar el nombre del cliente");
                return;
            }

            if (gestor.alquilarPrenda(seleccionada.getId(), cliente, dias)) {
                double total = seleccionada.getPrecioAlquiler() * dias;
                mostrarInfo("Éxito", String.format("Prenda alquilada. Total: $%.2f", total));
                actualizarTabla();
                actualizarEstadisticas();
            }
        }
    }

    private void mostrarDialogoDevolver() {
        Prenda seleccionada = tablaPrendas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Error", "Debe seleccionar una prenda");
            return;
        }

        if (seleccionada.isDisponible()) {
            mostrarError("Error", "La prenda no está alquilada");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Devolver Prenda");
        alert.setHeaderText("¿Confirmar devolución?");
        alert.setContentText("Prenda: " + seleccionada.getNombre() + "\nCliente: " + 
                             seleccionada.getClienteActual());

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (gestor.devolverPrenda(seleccionada.getId())) {
                mostrarInfo("Éxito", "Prenda devuelta correctamente");
                actualizarTabla();
                actualizarEstadisticas();
            }
        }
    }

    private void eliminarPrendaSeleccionada() {
        Prenda seleccionada = tablaPrendas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Error", "Debe seleccionar una prenda");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Prenda");
        alert.setHeaderText("¿Confirmar eliminación?");
        alert.setContentText("Prenda: " + seleccionada.getNombre());

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (gestor.eliminarPrenda(seleccionada.getId())) {
                mostrarInfo("Éxito", "Prenda eliminada correctamente");
                actualizarTabla();
                actualizarEstadisticas();
            } else {
                mostrarError("Error", "No se puede eliminar una prenda alquilada");
            }
        }
    }

    private void buscarPrenda(String id) {
        if (id.trim().isEmpty()) {
            actualizarTabla();
            return;
        }

        Prenda prenda = gestor.buscarPrenda(id);
        if (prenda != null) {
            listaPrendas.clear();
            listaPrendas.add(prenda);
        } else {
            mostrarError("No encontrado", "No existe una prenda con ese ID");
        }
    }

    private void filtrarPrendas(String filtro) {
        listaPrendas.clear();
        List<Prenda> prendas;

        switch (filtro) {
            case "Disponibles":
                prendas = gestor.obtenerPrendasDisponibles();
                break;
            case "Alquiladas":
                prendas = gestor.obtenerPrendasAlquiladas();
                break;
            default:
                prendas = gestor.obtenerTodasLasPrendas();
        }

        listaPrendas.addAll(prendas);
    }

    private void mostrarReporte() {
        Reporte reporte = gestor.generarReporte();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reporte del Sistema");
        alert.setHeaderText("Estadísticas Generales");
        alert.setContentText(reporte.toString());
        alert.showAndWait();
    }

    private void actualizarTabla() {
        listaPrendas.clear();
        listaPrendas.addAll(gestor.obtenerTodasLasPrendas());
    }

    private void actualizarEstadisticas() {
        Reporte reporte = gestor.generarReporte();
        lblEstadisticas.setText(String.format(
            "📊 ESTADÍSTICAS\n\n" +
            "Total: %d\n" +
            "Disponibles: %d\n" +
            "Alquiladas: %d\n" +
            "Alquileres: %d\n" +
            "Ingresos: $%.2f",
            reporte.getTotalPrendas(),
            reporte.getPrendasDisponibles(),
            reporte.getPrendasAlquiladas(),
            reporte.getTotalAlquileres(),
            reporte.getIngresoTotal()
        ));
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}