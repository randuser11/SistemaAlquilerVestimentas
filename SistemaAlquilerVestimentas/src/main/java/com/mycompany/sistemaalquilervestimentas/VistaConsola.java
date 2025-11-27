package com.mycompany.sistemaalquilervestimentas;

import java.util.Scanner;
import java.util.List;

public class VistaConsola {
    private Scanner scanner;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE ALQUILER DE VESTIMENTAS ===");
        System.out.println("1. Agregar prenda");
        System.out.println("2. Listar inventario");
        System.out.println("3. Buscar prenda");
        System.out.println("4. Alquilar prenda");
        System.out.println("5. Devolver prenda");
        System.out.println("6. Ver prendas alquiladas");
        System.out.println("7. Eliminar prenda");
        System.out.println("8. Generar reporte");
        System.out.println("9. Buscar por filtros");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public int leerOpcion() {
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return opcion;
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return valor;
    }

    public double leerDouble(String mensaje) {
        System.out.print(mensaje);
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Limpiar buffer
        return valor;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("ÉXITO: " + mensaje);
    }

    public void mostrarPrenda(Prenda prenda) {
        System.out.println(prenda);
    }

    public void mostrarListaPrendas(List<Prenda> prendas) {
        if(prendas.isEmpty()) {
            System.out.println("No hay prendas para mostrar");
            return;
        }
        System.out.println("\n=== LISTADO DE PRENDAS ===");
        prendas.forEach(System.out::println);
    }

    public void mostrarReporte(Reporte reporte) {
        System.out.println(reporte);
    }

    public void cerrar() {
        scanner.close();
    }
}
