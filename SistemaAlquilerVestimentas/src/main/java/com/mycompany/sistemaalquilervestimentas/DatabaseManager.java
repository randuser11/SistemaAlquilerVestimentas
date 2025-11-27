package com.mycompany.sistemaalquilervestimentas;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:inventario.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            crearTablas();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private void crearTablas() {
        String sqlPrendas = """
            CREATE TABLE IF NOT EXISTS prendas (
                id TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                tipo TEXT NOT NULL,
                talla TEXT NOT NULL,
                color TEXT NOT NULL,
                precio_alquiler REAL NOT NULL,
                disponible INTEGER NOT NULL,
                cliente_actual TEXT,
                fecha_alquiler TEXT,
                fecha_devolucion TEXT,
                veces_alquilada INTEGER DEFAULT 0
            )
        """;

        String sqlAlquileres = """
            CREATE TABLE IF NOT EXISTS alquileres (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_prenda TEXT NOT NULL,
                nombre_prenda TEXT NOT NULL,
                cliente TEXT NOT NULL,
                fecha TEXT NOT NULL,
                dias INTEGER NOT NULL,
                total REAL NOT NULL,
                FOREIGN KEY (id_prenda) REFERENCES prendas(id)
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlPrendas);
            stmt.execute(sqlAlquileres);
            System.out.println("Tablas creadas exitosamente");
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // ========== OPERACIONES CRUD PARA PRENDAS ==========

    public boolean insertarPrenda(Prenda prenda) {
        String sql = """
            INSERT INTO prendas (id, nombre, tipo, talla, color, precio_alquiler, 
                                disponible, cliente_actual, fecha_alquiler, fecha_devolucion, veces_alquilada)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, prenda.getId());
            pstmt.setString(2, prenda.getNombre());
            pstmt.setString(3, prenda.getTipo());
            pstmt.setString(4, prenda.getTalla());
            pstmt.setString(5, prenda.getColor());
            pstmt.setDouble(6, prenda.getPrecioAlquiler());
            pstmt.setInt(7, prenda.isDisponible() ? 1 : 0);
            pstmt.setString(8, prenda.getClienteActual());
            pstmt.setString(9, prenda.getFechaAlquiler() != null ? prenda.getFechaAlquiler().toString() : null);
            pstmt.setString(10, prenda.getFechaDevolucion() != null ? prenda.getFechaDevolucion().toString() : null);
            pstmt.setInt(11, prenda.getVecesAlquilada());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar prenda: " + e.getMessage());
            return false;
        }
    }

    public Prenda buscarPrenda(String id) {
        String sql = "SELECT * FROM prendas WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construirPrendaDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar prenda: " + e.getMessage());
        }
        return null;
    }

    public List<Prenda> obtenerTodasLasPrendas() {
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT * FROM prendas";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prendas.add(construirPrendaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prendas: " + e.getMessage());
        }
        return prendas;
    }

    public boolean actualizarPrenda(Prenda prenda) {
        String sql = """
            UPDATE prendas SET nombre = ?, tipo = ?, talla = ?, color = ?, 
                             precio_alquiler = ?, disponible = ?, cliente_actual = ?,
                             fecha_alquiler = ?, fecha_devolucion = ?, veces_alquilada = ?
            WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, prenda.getNombre());
            pstmt.setString(2, prenda.getTipo());
            pstmt.setString(3, prenda.getTalla());
            pstmt.setString(4, prenda.getColor());
            pstmt.setDouble(5, prenda.getPrecioAlquiler());
            pstmt.setInt(6, prenda.isDisponible() ? 1 : 0);
            pstmt.setString(7, prenda.getClienteActual());
            pstmt.setString(8, prenda.getFechaAlquiler() != null ? prenda.getFechaAlquiler().toString() : null);
            pstmt.setString(9, prenda.getFechaDevolucion() != null ? prenda.getFechaDevolucion().toString() : null);
            pstmt.setInt(10, prenda.getVecesAlquilada());
            pstmt.setString(11, prenda.getId());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar prenda: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPrenda(String id) {
        String sql = "DELETE FROM prendas WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar prenda: " + e.getMessage());
            return false;
        }
    }

    public List<Prenda> obtenerPrendasDisponibles() {
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT * FROM prendas WHERE disponible = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prendas.add(construirPrendaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prendas disponibles: " + e.getMessage());
        }
        return prendas;
    }

    public List<Prenda> obtenerPrendasAlquiladas() {
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT * FROM prendas WHERE disponible = 0";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prendas.add(construirPrendaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prendas alquiladas: " + e.getMessage());
        }
        return prendas;
    }

    // ========== OPERACIONES PARA ALQUILERES ==========

    public boolean insertarAlquiler(Alquiler alquiler) {
        String sql = """
            INSERT INTO alquileres (id_prenda, nombre_prenda, cliente, fecha, dias, total)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, alquiler.getIdPrenda());
            pstmt.setString(2, alquiler.getNombrePrenda());
            pstmt.setString(3, alquiler.getCliente());
            pstmt.setString(4, alquiler.getFecha().toString());
            pstmt.setInt(5, alquiler.getDias());
            pstmt.setDouble(6, alquiler.getTotal());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar alquiler: " + e.getMessage());
            return false;
        }
    }

    public List<Alquiler> obtenerTodosLosAlquileres() {
        List<Alquiler> alquileres = new ArrayList<>();
        String sql = "SELECT * FROM alquileres";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String idPrenda = rs.getString("id_prenda");
                String nombrePrenda = rs.getString("nombre_prenda");
                String cliente = rs.getString("cliente");
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                int dias = rs.getInt("dias");
                double total = rs.getDouble("total");
                
                alquileres.add(new Alquiler(idPrenda, nombrePrenda, cliente, fecha, dias, total));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alquileres: " + e.getMessage());
        }
        return alquileres;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Prenda construirPrendaDesdeResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String tipo = rs.getString("tipo");
        String talla = rs.getString("talla");
        String color = rs.getString("color");
        double precio = rs.getDouble("precio_alquiler");
        
        Prenda prenda = new Prenda(id, nombre, tipo, talla, color, precio);
        
        // Restaurar estado de alquiler si aplica
        boolean disponible = rs.getInt("disponible") == 1;
        if (!disponible) {
            String cliente = rs.getString("cliente_actual");
            String fechaAlquilerStr = rs.getString("fecha_alquiler");
            String fechaDevolucionStr = rs.getString("fecha_devolucion");
            
            if (fechaAlquilerStr != null && fechaDevolucionStr != null) {
                LocalDate fechaAlquiler = LocalDate.parse(fechaAlquilerStr);
                LocalDate fechaDevolucion = LocalDate.parse(fechaDevolucionStr);
                int dias = (int) (fechaDevolucion.toEpochDay() - fechaAlquiler.toEpochDay());
                prenda.alquilar(cliente, dias);
            }
        }
        
        // Restaurar veces alquilada
        int vecesAlquilada = rs.getInt("veces_alquilada");
        for (int i = 0; i < vecesAlquilada - (disponible ? 0 : 1); i++) {
            // Ajustar el contador si ya se incrementó con alquilar()
        }
        
        return prenda;
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE ESTADÍSTICAS ==========

    public int contarPrendas() {
        String sql = "SELECT COUNT(*) as total FROM prendas";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar prendas: " + e.getMessage());
        }
        return 0;
    }

    public double calcularIngresoTotal() {
        String sql = "SELECT SUM(total) as ingreso_total FROM alquileres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("ingreso_total");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular ingreso total: " + e.getMessage());
        }
        return 0.0;
    }

    public Prenda obtenerPrendaMasAlquilada() {
        String sql = "SELECT * FROM prendas ORDER BY veces_alquilada DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return construirPrendaDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prenda más alquilada: " + e.getMessage());
        }
        return null;
    }
}