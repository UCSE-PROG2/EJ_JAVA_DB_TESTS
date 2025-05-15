import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.math.BigDecimal;
import org.app.services.Logica;
import org.app.models.Producto;
import org.app.models.Categoria;
import org.app.dto.ProductoDTO;
import org.hibernate.Session;
import org.app.utils.HibernateUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogicaTest {
    
    private Logica logica;
    private Session session;
    private Categoria categoria1;
    private Categoria categoria2;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;

    @BeforeAll
    void setUp() {
        // Initialize Logica instance
        logica = Logica.getInstance();
        
        // Set up test data
        session = HibernateUtil.getSession();
        session.beginTransaction();

        // Create test categories
        categoria1 = new Categoria();
        categoria1.setNombre("Electrónicos");
        
        categoria2 = new Categoria();
        categoria2.setNombre("Ropa");

        session.persist(categoria1);
        session.persist(categoria2);

        // Create test products
        producto1 = new Producto();
        producto1.setNombre("Laptop");
        producto1.setPrecio(new BigDecimal("999.99"));
        producto1.setStock(10);
        producto1.setFechaIngreso(new Date());
        producto1.setCategoria(categoria1);

        producto2 = new Producto();
        producto2.setNombre("Smartphone");
        producto2.setPrecio(new BigDecimal("499.99"));
        producto2.setStock(15);
        producto2.setFechaIngreso(new Date());
        producto2.setCategoria(categoria1);

        producto3 = new Producto();
        producto3.setNombre("Camiseta");
        producto3.setPrecio(new BigDecimal("29.99"));
        producto3.setStock(50);
        producto3.setFechaIngreso(new Date());
        producto3.setCategoria(categoria2);

        session.persist(producto1);
        session.persist(producto2);
        session.persist(producto3);

        session.getTransaction().commit();
    }

    @AfterAll
    void tearDown() {
        if (session != null && session.isOpen()) {
            session.beginTransaction();
            session.createQuery("delete from Producto").executeUpdate();
            session.createQuery("delete from Categoria").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    void testObtenerProductosConCategorias() {
        List<ProductoDTO> productos = logica.obtenerProductosConCategorias();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(3, productos.size());
    }

    @Test
    void testObtenerTodosProductosConCategorias() {
        List<ProductoDTO> productos = logica.obtenerTodosProductosConCategorias();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(3, productos.size());
    }

    @Test
    void testObtenerProductosPrecioMayorConCategoria() {
        List<ProductoDTO> productos = logica.obtenerProductosPrecioMayorConCategoria(new BigDecimal("400.00"));
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(2, productos.size());
        assertTrue(productos.stream().allMatch(p -> p.getPrecio().compareTo(new BigDecimal("400.00")) > 0));
    }

    @Test
    void testObtenerCategoriasConProductos() {
        List<ProductoDTO> productos = logica.obtenerCategoriasConProductos();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(3, productos.size());
    }

    @Test
    void testObtenerProductosOrdenadosPorCategoriaYPrecio() {
        List<ProductoDTO> productos = logica.obtenerProductosOrdenadosPorCategoriaYPrecio();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(3, productos.size());
        
        // Verify ordering
        assertTrue(productos.get(0).getCategoria().getNombre().compareTo(
                  productos.get(1).getCategoria().getNombre()) <= 0);
        
        // Within same category, verify price ordering
        for (int i = 0; i < productos.size() - 1; i++) {
            if (productos.get(i).getCategoria().getNombre().equals(
                productos.get(i + 1).getCategoria().getNombre())) {
                assertTrue(productos.get(i).getPrecio().compareTo(
                         productos.get(i + 1).getPrecio()) >= 0);
            }
        }
    }

    @Test
    void testObtenerProductosPorRangoFechas() {
        Date fechaInicio = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        Date fechaFin = new Date(System.currentTimeMillis() + 86400000);    // Tomorrow
        
        List<ProductoDTO> productos = logica.obtenerProductosPorRangoFechas(fechaInicio, fechaFin);
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(3, productos.size());
    }

    @Test
    void testObtenerProductosPorStockYPrecio() {
        List<ProductoDTO> productos = logica.obtenerProductosPorStockYPrecio(5, new BigDecimal("1000.00"));
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertTrue(productos.stream().allMatch(p -> p.getStock() > 5 && 
                                                  p.getPrecio().compareTo(new BigDecimal("1000.00")) < 0));
    }

    @Test
    void testObtenerProductosPorNombreYCategoria() {
        List<ProductoDTO> productos = logica.obtenerProductosPorNombreYCategoria("Laptop", "Electrónicos");
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertEquals(1, productos.size());
        assertEquals("Laptop", productos.get(0).getNombre());
        assertEquals("Electrónicos", productos.get(0).getCategoria().getNombre());
    }

    @Test
    void testObtenerProductosPrecioMayorPromedio() {
        List<ProductoDTO> productos = logica.obtenerProductosPrecioMayorPromedio();
        assertNotNull(productos);
        
        // Calculate average manually to verify
        BigDecimal totalPrecio = new BigDecimal("0");
        totalPrecio = totalPrecio.add(producto1.getPrecio())
                                .add(producto2.getPrecio())
                                .add(producto3.getPrecio());
        BigDecimal promedio = totalPrecio.divide(new BigDecimal("3"), 2, BigDecimal.ROUND_HALF_UP);
        
        assertTrue(productos.stream().allMatch(p -> p.getPrecio().compareTo(promedio) > 0));
    }

    @Test
    void testObtenerProductosStockBajoPorCategoria() {
        List<ProductoDTO> productos = logica.obtenerProductosStockBajoPorCategoria("Electrónicos", 20);
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        assertTrue(productos.stream().allMatch(p -> p.getStock() < 20 && 
                                                  p.getCategoria().getNombre().equals("Electrónicos")));
    }
} 