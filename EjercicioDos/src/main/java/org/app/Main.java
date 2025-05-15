package org.app;

import org.app.dto.ProductoDTO;
import org.app.services.Logica;
import org.app.utils.LocalDateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logica logica = Logica.getInstance();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1 -> mostrarProductosConCategorias();
                case 2 -> mostrarTodosProductosConCategorias();
                case 3 -> buscarProductosPorPrecio();
                case 4 -> buscarProductosPorStockYPrecio();
                case 5 -> buscarProductosPorNombreYCategoria();
                case 6 -> continuar = false;
                default -> System.out.println("Opción no válida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Gestión de Productos ===");
        System.out.println("1. Mostrar productos con categorías");
        System.out.println("2. Mostrar todos los productos con categorías");
        System.out.println("3. Buscar productos por precio");
        System.out.println("4. Buscar productos por stock y precio");
        System.out.println("5. Buscar productos por nombre y categoría");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void mostrarProductosConCategorias() {
        System.out.println("\n=== Lista de Productos con Categorías ===");
        List<ProductoDTO> productos = logica.obtenerProductosConCategorias();
        mostrarProductos(productos);
    }

    private static void mostrarTodosProductosConCategorias() {
        System.out.println("\n=== Lista Completa de Productos con Categorías ===");
        List<ProductoDTO> productos = logica.obtenerTodosProductosConCategorias();
        mostrarProductos(productos);
    }

    private static void buscarProductosPorPrecio() {
        System.out.println("\n=== Buscar Productos por Precio ===");
        System.out.print("Ingrese el precio mínimo: ");
        try {
            BigDecimal precio = new BigDecimal(scanner.nextLine());
            List<ProductoDTO> productos = logica.obtenerProductosPrecioMayorConCategoria(precio);
            mostrarProductos(productos);
        } catch (NumberFormatException e) {
            System.out.println("Error: El precio ingresado no es válido.");
        }
    }

    private static void buscarProductosPorStockYPrecio() {
        System.out.println("\n=== Buscar Productos por Stock y Precio ===");
        try {
            System.out.print("Ingrese el stock mínimo: ");
            int stockMinimo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Ingrese el precio máximo: ");
            BigDecimal precioMaximo = new BigDecimal(scanner.nextLine());
            
            List<ProductoDTO> productos = logica.obtenerProductosPorStockYPrecio(stockMinimo, precioMaximo);
            mostrarProductos(productos);
        } catch (NumberFormatException e) {
            System.out.println("Error: Los valores ingresados no son válidos.");
        }
    }

    private static void buscarProductosPorNombreYCategoria() {
        System.out.println("\n=== Buscar Productos por Nombre y Categoría ===");
        System.out.print("Ingrese el nombre del producto (o parte de él): ");
        String nombreProducto = scanner.nextLine();
        
        System.out.print("Ingrese el nombre de la categoría: ");
        String nombreCategoria = scanner.nextLine();
        
        List<ProductoDTO> productos = logica.obtenerProductosPorNombreYCategoria(nombreProducto, nombreCategoria);
        mostrarProductos(productos);
    }

    private static void mostrarProductos(List<ProductoDTO> productos) {
        System.out.flush();
        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
            return;
        }
        
        for (ProductoDTO producto : productos) {
            System.out.println("\nID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Precio: $" + producto.getPrecio());
            System.out.println("Stock: " + producto.getStock());
            System.out.println("Fecha de Ingreso: " + producto.getFechaIngreso());
            if (producto.getCategoria() != null) {
                System.out.println("Categoría: " + producto.getCategoria().getNombre());
            }
        }
    }
} 