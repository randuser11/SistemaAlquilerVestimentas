package com.mycompany.sistemaalquilervestimentas;

import java.util.List;

public class ControladorInventario {
    private GestorInventario gestor;
    private VistaConsola vista;

    public ControladorInventario(GestorInventario gestor, VistaConsola vista) {
        this.gestor = gestor;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerOpcion();
            procesarOpcion(opcion);
        } while(opcion != 0);
        
        vista.mostrarMensaje("¡Hasta luego!");
        vista.cerrar();
    }

    private void procesarOpcion(int opcion) {
        switch(opcion) {
            case 1: agregarPrenda(); break;
            case 2: listarInventario(); break;
            case 3: buscarPrenda(); break;
            case 4: alquilarPrenda(); break;
            case 5: devolverPrenda(); break;
            case 6: listarPrendasAlquiladas(); break;
            case 7: eliminarPrenda(); break;
            case 8: generarReporte(); break;
            case 9: buscarPorFiltros(); break;
            case 0: break;
            default: vista.mostrarError("Opción no válida");
        }
    }

    private void agregarPrenda() {
        String id = vista.leerTexto("ID: ");
        String nombre = vista.leerTexto("Nombre: ");
        String tipo = vista.leerTexto("Tipo (vestido/traje/disfraz/otro): ");
        String talla = vista.leerTexto("Talla: ");
        String color = vista.leerTexto("Color: ");
        double precio = vista.leerDouble("Precio de alquiler: ");

        Prenda prenda = new Prenda(id, nombre, tipo, talla, color, precio);
        if(gestor.agregarPrenda(prenda)) {
            vista.mostrarExito("Prenda agregada correctamente");
        } else {
            vista.mostrarError("Ya existe una prenda con ese ID");
        }
    }

    private void listarInventario() {
        List<Prenda> prendas = gestor.obtenerTodasLasPrendas();
        vista.mostrarListaPrendas(prendas);
    }

    private void buscarPrenda() {
        String id = vista.leerTexto("Ingrese ID de la prenda: ");
        Prenda prenda = gestor.buscarPrenda(id);
        if(prenda != null) {
            vista.mostrarPrenda(prenda);
        } else {
            vista.mostrarError("Prenda no encontrada");
        }
    }

    private void alquilarPrenda() {
        String id = vista.leerTexto("ID de la prenda: ");
        String cliente = vista.leerTexto("Nombre del cliente: ");
        int dias = vista.leerEntero("Días de alquiler: ");
        
        if(gestor.alquilarPrenda(id, cliente, dias)) {
            Prenda prenda = gestor.buscarPrenda(id);
            double total = prenda.calcularTotalAlquiler(dias);
            vista.mostrarExito(String.format("Prenda alquilada. Total: $%.2f", total));
        } else {
            vista.mostrarError("No se pudo alquilar la prenda");
        }
    }

    private void devolverPrenda() {
        String id = vista.leerTexto("ID de la prenda: ");
        if(gestor.devolverPrenda(id)) {
            vista.mostrarExito("Prenda devuelta correctamente");
        } else {
            vista.mostrarError("No se pudo devolver la prenda");
        }
    }

    private void listarPrendasAlquiladas() {
        List<Prenda> prendas = gestor.obtenerPrendasAlquiladas();
        vista.mostrarListaPrendas(prendas);
    }

    private void eliminarPrenda() {
        String id = vista.leerTexto("ID de la prenda: ");
        if(gestor.eliminarPrenda(id)) {
            vista.mostrarExito("Prenda eliminada correctamente");
        } else {
            vista.mostrarError("No se pudo eliminar la prenda");
        }
    }

    private void generarReporte() {
        Reporte reporte = gestor.generarReporte();
        vista.mostrarReporte(reporte);
    }

    private void buscarPorFiltros() {
        vista.mostrarMensaje("\n1. Por tipo\n2. Por talla\n3. Por color");
        int opcion = vista.leerOpcion();
        List<Prenda> resultado = null;
        
        switch(opcion) {
            case 1:
                String tipo = vista.leerTexto("Tipo: ");
                resultado = gestor.buscarPorTipo(tipo);
                break;
            case 2:
                String talla = vista.leerTexto("Talla: ");
                resultado = gestor.buscarPorTalla(talla);
                break;
            case 3:
                String color = vista.leerTexto("Color: ");
                resultado = gestor.buscarPorColor(color);
                break;
        }
        
        if(resultado != null) {
            vista.mostrarListaPrendas(resultado);
        }
    }
}