package com.mycompany.sistemaalquilervestimentas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Prenda {
    private String id;
    private String nombre;
    private String tipo;
    private String talla;
    private String color;
    private double precioAlquiler;
    private boolean disponible;
    private String clienteActual;
    private LocalDate fechaAlquiler;
    private LocalDate fechaDevolucion;
    private int vecesAlquilada;

    public Prenda(String id, String nombre, String tipo, String talla, String color, double precioAlquiler) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.talla = talla;
        this.color = color;
        this.precioAlquiler = precioAlquiler;
        this.disponible = true;
        this.vecesAlquilada = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getTalla() { return talla; }
    public String getColor() { return color; }
    public double getPrecioAlquiler() { return precioAlquiler; }
    public boolean isDisponible() { return disponible; }
    public String getClienteActual() { return clienteActual; }
    public LocalDate getFechaAlquiler() { return fechaAlquiler; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public int getVecesAlquilada() { return vecesAlquilada; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setTalla(String talla) { this.talla = talla; }
    public void setColor(String color) { this.color = color; }
    public void setPrecioAlquiler(double precioAlquiler) { this.precioAlquiler = precioAlquiler; }

    // Métodos de negocio
    public void alquilar(String cliente, int dias) {
        this.disponible = false;
        this.clienteActual = cliente;
        this.fechaAlquiler = LocalDate.now();
        this.fechaDevolucion = LocalDate.now().plusDays(dias);
        this.vecesAlquilada++;
    }

    public void devolver() {
        this.disponible = true;
        this.clienteActual = null;
        this.fechaAlquiler = null;
        this.fechaDevolucion = null;
    }

    public double calcularTotalAlquiler(int dias) {
        return precioAlquiler * dias;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String estado = disponible ? "DISPONIBLE" : "ALQUILADA";
        String info = String.format("ID: %s | %s | Tipo: %s | Talla: %s | Color: %s | Precio: $%.2f | Estado: %s | Alquileres: %d",
                id, nombre, tipo, talla, color, precioAlquiler, estado, vecesAlquilada);
        
        if(!disponible) {
            info += String.format("\n   Cliente: %s | Alquilado: %s | Devolución: %s",
                    clienteActual, fechaAlquiler.format(formatter), fechaDevolucion.format(formatter));
        }
        return info;
    }
}
