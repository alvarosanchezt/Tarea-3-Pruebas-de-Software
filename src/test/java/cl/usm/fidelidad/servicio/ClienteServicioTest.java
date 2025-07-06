package cl.usm.fidelidad.servicio;

import cl.usm.fidelidad.modelo.Compra;
import cl.usm.fidelidad.modelo.Cliente;
import cl.usm.fidelidad.repositorio.ClienteRepositorio;
import cl.usm.fidelidad.repositorio.CompraRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class ClienteServicioTest {

    private ClienteServicio clienteServicio;
    private CompraServicio compraServicio;
    private ClienteRepositorio clienteRepositorio;
    private CompraRepositorio compraRepositorio;
    
    private Cliente clienteBronce;

    @BeforeEach
    void setUp() {
        clienteRepositorio = new ClienteRepositorio();
        compraRepositorio = new CompraRepositorio();

        clienteServicio = new ClienteServicio(clienteRepositorio);
        compraServicio = new CompraServicio(clienteRepositorio, compraRepositorio); 

        clienteBronce = new Cliente("id-bronce", "Cliente Bronce", "bronce@test.com");
        clienteRepositorio.save(clienteBronce); 
    }

    @Test
    void testAgregarClienteExitosamente() {
        Cliente cliente = clienteServicio.agregarCliente("Test User", "test@user.com");
        assertNotNull(cliente.getId());
        assertEquals("Test User", cliente.getNombre());
        assertEquals(2, clienteServicio.listarClientes().size());
    }

    @Test
    void testAgregarClienteCorreoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.agregarCliente("Test User", "test-user.com");
        });
        assertEquals("El formato del correo electrónico es inválido.", exception.getMessage());
    }

    @Test
    void testAgregarClienteCorreoDuplicado() {
        clienteServicio.agregarCliente("User Uno", "duplicate@test.com");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.agregarCliente("User Dos", "duplicate@test.com");
        });
        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
    }
    
    @Test
    void testEliminarCliente() {
        Cliente cliente = clienteServicio.agregarCliente("Para Borrar", "delete@me.com");
        assertDoesNotThrow(() -> clienteServicio.eliminarCliente(cliente.getId()));
        assertTrue(clienteServicio.buscarClientePorId(cliente.getId()).isEmpty());
    }
    
    @Test
    void testActualizarClienteConCorreoYaExistente() {

        Cliente clienteA = clienteServicio.agregarCliente("Cliente A", "a@test.com");
        Cliente clienteB = clienteServicio.agregarCliente("Cliente B", "b@test.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.actualizarCliente(clienteB.getId(), "Nuevo Nombre B", "a@test.com");
        });
        
        assertEquals("El nuevo correo electrónico ya está en uso por otro cliente.", exception.getMessage());
    }

    @Test
    void testActualizarClienteConSuMismoCorreo() {
        Cliente clienteA = clienteServicio.agregarCliente("Cliente A", "a@test.com");

        assertDoesNotThrow(() -> {
            clienteServicio.actualizarCliente(clienteA.getId(), "Cliente A Actualizado", "a@test.com");
        });

        Cliente clienteActualizado = clienteServicio.buscarClientePorId(clienteA.getId()).get();
        assertEquals("Cliente A Actualizado", clienteActualizado.getNombre());
        assertEquals("a@test.com", clienteActualizado.getCorreo());
    }
    @Test
    void testReinicioDeStreakEnDiaDiferente() {

        LocalDate ayer = LocalDate.now().minusDays(1);
        compraRepositorio.save(new Compra("compra-ayer-1", clienteBronce.getId(), 200, ayer));
        compraRepositorio.save(new Compra("compra-ayer-2", clienteBronce.getId(), 200, ayer));

        clienteBronce.setPuntos(0);
        clienteRepositorio.save(clienteBronce);


        compraServicio.registrarCompra(clienteBronce.getId(), 1000); 

        Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
        assertEquals(10, clienteActualizado.getPuntos()); 
    }
}