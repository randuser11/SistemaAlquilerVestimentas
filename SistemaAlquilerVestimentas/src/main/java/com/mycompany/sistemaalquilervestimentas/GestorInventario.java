package com.mycompany.sistemaalquilervestimentas;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class GestorInventario {
    private DatabaseManager db;

    public GestorInventario() {
        this.db = new DatabaseManager();
    }

    // Métodos CRUD de Prendas
    public boolean agregarPrenda(Prenda prenda) {
        // Verificar si ya existe
        if(db.buscarPrenda(prenda.getId()) != null) {
            return false;
        }
        return db.insertarPrenda(prenda);
    }

    public Prenda buscarPrenda(String id) {
        return db.buscarPrenda(id);
    }

    public boolean eliminarPrenda(String id) {
        Prenda prenda = db.buscarPrenda(id);
        if(prenda == null || !prenda.isDisponible()) {
            return false;
        }
        return db.eliminarPrenda(id);
    }

    public boolean actualizarPrenda(String id, String nombre, String tipo, String talla, 
                                     String color, double precio) {
        Prenda prenda = db.buscarPrenda(id);
        if(prenda == null) {
            return false;
        }
        prenda.setNombre(nombre);
        prenda.setTipo(tipo);
        prenda.setTalla(talla);
        prenda.setColor(color);
        prenda.setPrecioAlquiler(precio);
        return db.actualizarPrenda(prenda);
    }

    // Métodos de Consulta
    public List<Prenda> obtenerTodasLasPrendas() {
        return db.obtenerTodasLasPrendas();
    }

    public List<Prenda> obtenerPrendasDisponibles() {
        return db.obtenerPrendasDisponibles();
    }

    public List<Prenda> obtenerPrendasAlquiladas() {
        return db.obtenerPrendasAlquiladas();
    }

    public List<Prenda> buscarPorTipo(String tipo) {
        return db.obtenerTodasLasPrendas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public List<Prenda> buscarPorTalla(String talla) {
        return db.obtenerTodasLasPrendas().stream()
                .filter(p -> p.getTalla().equalsIgnoreCase(talla))
                .collect(Collectors.toList());
    }

    public List<Prenda> buscarPorColor(String color) {
        return db.obtenerTodasLasPrendas().stream()
                .filter(p -> p.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    // Métodos de Alquiler
    public boolean alquilarPrenda(String id, String cliente, int dias) {
        Prenda prenda = db.buscarPrenda(id);
        if(prenda == null || !prenda.isDisponible()) {
            return false;
        }
        
        prenda.alquilar(cliente, dias);
        double total = prenda.calcularTotalAlquiler(dias);
        
        // Guardar alquiler en historial
        Alquiler alquiler = new Alquiler(id, prenda.getNombre(), cliente, LocalDate.now(), dias, total);
        db.insertarAlquiler(alquiler);
        
        // Actualizar prenda en la base de datos
        return db.actualizarPrenda(prenda);
    }

    public boolean devolverPrenda(String id) {
        Prenda prenda = db.buscarPrenda(id);
        if(prenda == null || prenda.isDisponible()) {
            return false;
        }
        prenda.devolver();
        return db.actualizarPrenda(prenda);
    }

    // Métodos de Reporte
    public Reporte generarReporte() {
        List<Prenda> todasPrendas = db.obtenerTodasLasPrendas();
        int total = todasPrendas.size();
        int disponibles = (int) todasPrendas.stream().filter(Prenda::isDisponible).count();
        int alquiladas = total - disponibles;
        
        List<Alquiler> alquileres = db.obtenerTodosLosAlquileres();
        int totalAlquileres = alquileres.size();
        
        double ingresoTotal = db.calcularIngresoTotal();
        
        Prenda masPop = db.obtenerPrendaMasAlquilada();
        String nombreMasPopular = masPop != null && masPop.getVecesAlquilada() > 0 
                ? masPop.getNombre() : "N/A";
        int vecesAlquilada = masPop != null ? masPop.getVecesAlquilada() : 0;
        
        return new Reporte(total, disponibles, alquiladas, totalAlquileres, 
                          ingresoTotal, nombreMasPopular, vecesAlquilada);
    }

    public List<Alquiler> obtenerHistorialAlquileres() {
        return db.obtenerTodosLosAlquileres();
    }

    public int getCantidadPrendas() {
        return db.contarPrendas();
    }
    
    public void cerrar() {
        db.cerrarConexion();
    }
}