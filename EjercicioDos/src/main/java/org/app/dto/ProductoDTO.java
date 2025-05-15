package org.app.dto;

import org.app.models.Producto;
import org.app.models.Categoria;
import java.math.BigDecimal;
import java.util.Date;

public class ProductoDTO {
    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Date fechaIngreso;
    private CategoriaDTO categoria;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    // Static factory method to convert Entity to DTO
    public static ProductoDTO fromEntity(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setFechaIngreso(producto.getFechaIngreso());
        dto.setCategoria(CategoriaDTO.fromEntity(producto.getCategoria()));
        return dto;
    }
} 