package org.app.services;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.app.utils.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Join;
import org.hibernate.*;
import org.app.models.Producto;
import org.app.models.Categoria;
import org.app.dto.ProductoDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.util.stream.Collectors;

public class Logica {
    private static Logica instance;

    private Logica() {
    }

    public static Logica getInstance() {
        if (instance == null) {
            instance = new Logica();
        }
        return instance;
    }

    // 1. Join Básico entre Productos y Categorias
    public List<ProductoDTO> obtenerProductosConCategorias() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            Join<Producto, Categoria> categoria = producto.join("categoria");
            
            query.select(producto);
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 2. Left Join con Filtro
    public List<ProductoDTO> obtenerTodosProductosConCategorias() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            producto.join("categoria", jakarta.persistence.criteria.JoinType.LEFT);
            
            query.select(producto);
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 3. Inner Join con Filtrado por Precio
    public List<ProductoDTO> obtenerProductosPrecioMayorConCategoria(BigDecimal precio) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            Join<Producto, Categoria> categoria = producto.join("categoria");
            
            query.select(producto)
                 .where(cb.gt(producto.get("precio"), precio));
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 4. Right Join con Filtrado por Stock
    public List<ProductoDTO> obtenerCategoriasConProductos() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Categoria> categoria = query.from(Categoria.class);
            Join<Categoria, Producto> producto = categoria.join("productos", jakarta.persistence.criteria.JoinType.RIGHT);
            
            query.select(producto);
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 5. Consulta con Join y Ordenación
    public List<ProductoDTO> obtenerProductosOrdenadosPorCategoriaYPrecio() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            Join<Producto, Categoria> categoria = producto.join("categoria");
            
            query.select(producto)
                 .orderBy(
                     cb.asc(categoria.get("nombre")),
                     cb.desc(producto.get("precio"))
                 );
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 6. Consulta de Productos por Rango de Fechas
    public List<ProductoDTO> obtenerProductosPorRangoFechas(Date fechaInicio, Date fechaFin) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            
            query.select(producto)
                 .where(cb.between(producto.get("fechaIngreso"), fechaInicio, fechaFin));
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 7. Consulta de Stock y Precio
    public List<ProductoDTO> obtenerProductosPorStockYPrecio(int stockMinimo, BigDecimal precioMaximo) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            
            query.select(producto)
                 .where(
                     cb.and(
                         cb.gt(producto.get("stock"), stockMinimo),
                         cb.lt(producto.get("precio"), precioMaximo)
                     )
                 );
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 8. Consulta de Productos por Nombre y Categoría
    public List<ProductoDTO> obtenerProductosPorNombreYCategoria(String nombreProducto, String nombreCategoria) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            Join<Producto, Categoria> categoria = producto.join("categoria");
            
            query.select(producto)
                 .where(
                     cb.and(
                         cb.like(producto.get("nombre"), "%" + nombreProducto + "%"),
                         cb.equal(categoria.get("nombre"), nombreCategoria)
                     )
                 );
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 9. Consulta de Productos con Precio Mayor al Promedio
    public List<ProductoDTO> obtenerProductosPrecioMayorPromedio() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            
            // Primero calculamos el promedio
            CriteriaQuery<Double> avgQuery = cb.createQuery(Double.class);
            Root<Producto> avgRoot = avgQuery.from(Producto.class);
            avgQuery.select(cb.avg(avgRoot.get("precio")));
            Double precioPromedio = session.createQuery(avgQuery).getSingleResult();
            
            // Luego obtenemos los productos con precio mayor al promedio
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            
            query.select(producto)
                 .where(cb.gt(producto.get("precio"), precioPromedio));
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }

    // 10. Consulta de Productos con Stock Bajo por Categoría
    public List<ProductoDTO> obtenerProductosStockBajoPorCategoria(String nombreCategoria, int stockMinimo) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> producto = query.from(Producto.class);
            Join<Producto, Categoria> categoria = producto.join("categoria");
            
            query.select(producto)
                 .where(
                     cb.and(
                         cb.equal(categoria.get("nombre"), nombreCategoria),
                         cb.lt(producto.get("stock"), stockMinimo)
                     )
                 );
            
            List<Producto> productos = session.createQuery(query).getResultList();
            return productos.stream()
                          .map(ProductoDTO::fromEntity)
                          .collect(Collectors.toList());
        }
    }
} 