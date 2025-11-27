package com.mycompany.sistemaalquilervestimentas;

public class Reporte {
    private int totalPrendas;
    private int prendasDisponibles;
    private int prendasAlquiladas;
    private int totalAlquileres;
    private double ingresoTotal;
    private String prendaMasAlquilada;
    private int vecesAlquiladaMasPopular;

    public Reporte(int totalPrendas, int prendasDisponibles, int prendasAlquiladas, 
                   int totalAlquileres, double ingresoTotal, String prendaMasAlquilada, 
                   int vecesAlquiladaMasPopular) {
        this.totalPrendas = totalPrendas;
        this.prendasDisponibles = prendasDisponibles;
        this.prendasAlquiladas = prendasAlquiladas;
        this.totalAlquileres = totalAlquileres;
        this.ingresoTotal = ingresoTotal;
        this.prendaMasAlquilada = prendaMasAlquilada;
        this.vecesAlquiladaMasPopular = vecesAlquiladaMasPopular;
    }

    // Getters
    public int getTotalPrendas() { return totalPrendas; }
    public int getPrendasDisponibles() { return prendasDisponibles; }
    public int getPrendasAlquiladas() { return prendasAlquiladas; }
    public int getTotalAlquileres() { return totalAlquileres; }
    public double getIngresoTotal() { return ingresoTotal; }
    public String getPrendaMasAlquilada() { return prendaMasAlquilada; }
    public int getVecesAlquiladaMasPopular() { return vecesAlquiladaMasPopular; }

    @Override
    public String toString() {
        return String.format(
            "=== REPORTE DEL SISTEMA ===\n" +
            "Total de prendas: %d\n" +
            "Prendas disponibles: %d\n" +
            "Prendas alquiladas: %d\n" +
            "Total de alquileres realizados: %d\n" +
            "Ingresos totales: $%.2f\n" +
            "Prenda más alquilada: %s (%d veces)",
            totalPrendas, prendasDisponibles, prendasAlquiladas, 
            totalAlquileres, ingresoTotal, prendaMasAlquilada, vecesAlquiladaMasPopular
        );
    }
}