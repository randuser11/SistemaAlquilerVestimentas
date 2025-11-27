package com.mycompany.sistemaalquilervestimentas;

import java.time.LocalDate;

public class Alquiler {
    private String idPrenda;
    private String nombrePrenda;
    private String cliente;
    private LocalDate fecha;
    private int dias;
    private double total;

    public Alquiler(String idPrenda, String nombrePrenda, String cliente, LocalDate fecha, int dias, double total) {
        this.idPrenda = idPrenda;
        this.nombrePrenda = nombrePrenda;
        this.cliente = cliente;
        this.fecha = fecha;
        this.dias = dias;
        this.total = total;
    }

    // Getters
    public String getIdPrenda() { return idPrenda; }
    public String getNombrePrenda() { return nombrePrenda; }
    public String getCliente() { return cliente; }
    public LocalDate getFecha() { return fecha; }
    public int getDias() { return dias; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return String.format("Prenda: %s | Cliente: %s | Fecha: %s | Días: %d | Total: $%.2f",
                nombrePrenda, cliente, fecha, dias, total);
    }
}