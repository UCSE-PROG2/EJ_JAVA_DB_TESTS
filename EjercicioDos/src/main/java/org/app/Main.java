package org.app;

import org.app.dto.LibroDTO;
import org.app.services.Logica;
import org.app.utils.LocalDateTimeUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                case 1 -> crearLibro();
                case 2 -> mostrarLibros();
                case 3 -> actualizarLibro();
                case 4 -> eliminarLibro();
                case 5 -> continuar = false;
                default -> System.out.println("Opción no válida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Gestión de Libros ===");
        System.out.println("1. Crear libro");
        System.out.println("2. Mostrar libros");
        System.out.println("3. Actualizar libro");
        System.out.println("4. Eliminar libro");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void crearLibro() {
        System.out.println("\n=== Crear Libro ===");
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        
        System.out.print("Fecha de publicación (yyyy-MM-dd): ");
        LocalDateTime fechaPublicacion = LocalDateTimeUtils.crearLocalDateTimeDesdeFecha(scanner.nextLine());
        
        System.out.print("ID del género: ");
        Long generoId = Long.parseLong(scanner.nextLine());
        
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo(titulo);
        libroDTO.setAutor(autor);
        libroDTO.setGeneroId(generoId);
        
        try {
            libroDTO = logica.crearLibro(libroDTO);
            System.out.println("Libro creado exitosamente con ID: " + libroDTO.getId());
        } catch (Exception e) {
            System.out.println("Error al crear el libro: " + e.getMessage());
        }
    }

    private static void mostrarLibros() {
        System.out.println("\n=== Lista de Libros ===");
        List<LibroDTO> libros = logica.obtenerTodosLosLibros();
        
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        
        for (LibroDTO libro : libros) {
            System.out.println("\nID: " + libro.getId());
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor());
            System.out.println("Género: " + libro.getGeneroNombre());
        }
    }

    private static void actualizarLibro() {
        System.out.println("\n=== Actualizar Libro ===");
        
        System.out.print("ID del libro a actualizar: ");
        Long id = Long.parseLong(scanner.nextLine());
        
        LibroDTO libroDTO = logica.obtenerLibro(id);
        if (libroDTO == null) {
            System.out.println("Libro no encontrado.");
            return;
        }
        
        System.out.print("Nuevo título (actual: " + libroDTO.getTitulo() + "): ");
        String titulo = scanner.nextLine();
        if (!titulo.isEmpty()) {
            libroDTO.setTitulo(titulo);
        }
        
        System.out.print("Nuevo autor (actual: " + libroDTO.getAutor() + "): ");
        String autor = scanner.nextLine();
        if (!autor.isEmpty()) {
            libroDTO.setAutor(autor);
        }

        
        System.out.print("Nuevo ID del género (actual: " + libroDTO.getGeneroId() + "): ");
        String generoIdStr = scanner.nextLine();
        if (!generoIdStr.isEmpty()) {
            libroDTO.setGeneroId(Long.parseLong(generoIdStr));
        }
        
        try {
            libroDTO = logica.actualizarLibro(libroDTO);
            System.out.println("Libro actualizado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar el libro: " + e.getMessage());
        }
    }

    private static void eliminarLibro() {
        System.out.println("\n=== Eliminar Libro ===");
        
        System.out.print("ID del libro a eliminar: ");
        Long id = Long.parseLong(scanner.nextLine());
        
        try {
            logica.eliminarLibro(id);
            System.out.println("Libro eliminado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar el libro: " + e.getMessage());
        }
    }
} 