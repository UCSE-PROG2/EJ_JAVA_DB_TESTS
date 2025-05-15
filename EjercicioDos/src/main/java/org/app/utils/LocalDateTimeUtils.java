package org.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeUtils {
    /**
     * Genera un LocalDateTime a partir de una cadena de fecha en formato 'yyyy-MM-dd'
     * y la combina con la medianoche (00:00:00).
     *
     * @param fechaString La cadena de fecha en formato 'yyyy-MM-dd'.
     * @return Un LocalDateTime representando la fecha ingresada a medianoche,
     * o null si la cadena no puede ser parseada.
     */
    public static LocalDateTime crearLocalDateTimeDesdeFecha(String fechaString) {
        return crearLocalDateTimeDesdeFecha(fechaString, LocalTime.MIDNIGHT);
    }

    /**
     * Genera un LocalDateTime a partir de una cadena de fecha en formato 'yyyy-MM-dd'
     * y una hora LocalTime proporcionada.
     *
     * @param fechaString La cadena de fecha en formato 'yyyy-MM-dd'.
     * @param hora        El LocalTime a combinar con la fecha.
     * @return Un LocalDateTime representando la fecha y hora proporcionadas,
     * o null si la cadena de fecha no puede ser parseada.
     */
    public static LocalDateTime crearLocalDateTimeDesdeFecha(String fechaString, LocalTime hora) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fechaString, dateFormatter);
            return fechaLocalDate.atTime(hora);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha: " + e.getMessage());
            return null;
        }
    }

    /**
     * Genera un LocalDateTime a partir de cadenas de fecha (yyyy-MM-dd) y hora (HH:mm:ss).
     *
     * @param fechaString La cadena de fecha en formato 'yyyy-MM-dd'.
     * @param horaString  La cadena de hora en formato 'HH:mm:ss'.
     * @return Un LocalDateTime representando la fecha y hora proporcionadas,
     * o null si alguna de las cadenas no puede ser parseada.
     */
    public static LocalDateTime crearLocalDateTimeDesdeFechaYHora(String fechaString, String horaString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fechaString, dateFormatter);
            LocalTime horaLocalTime = LocalTime.parse(horaString, timeFormatter);
            return fechaLocalDate.atTime(horaLocalTime);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha u hora: " + e.getMessage());
            return null;
        }
    }
}
