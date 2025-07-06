package cl.usm.fidelidad.servicio;

import cl.usm.fidelidad.modelo.Cliente;
import cl.usm.fidelidad.modelo.Nivel;
import cl.usm.fidelidad.repositorio.ClienteRepositorio;
import cl.usm.fidelidad.repositorio.CompraRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompraServicioTest {
    
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
        
        // Cliente de prueba
        clienteBronce = clienteServicio.agregarCliente("Cliente Bronce", "bronce@test.com");
    }

    @Test
    void testCalculoPuntosBase() {
        compraServicio.registrarCompra(clienteBronce.getId(), 199); // $199 -> 1 punto
        assertEquals(1, clienteRepositorio.findById(clienteBronce.getId()).get().getPuntos());
    }

    @Test
    void testCalculoPuntosMultiplicadorPlata() {
        clienteBronce.setPuntos(500); // Promover a PLATA
        clienteBronce.setNivel(Nivel.PLATA);
        clienteRepositorio.save(clienteBronce);

        compraServicio.registrarCompra(clienteBronce.getId(), 1000); // $1000 -> 10 pts base * 1.2 = 12 pts
        
        Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
        assertEquals(512, clienteActualizado.getPuntos()); // 500 iniciales + 12 nuevos
        assertEquals(Nivel.PLATA, clienteActualizado.getNivel());
    }

    @Test
    void testPromocionANivelPlata() {
        clienteBronce.setPuntos(495);
        clienteRepositorio.save(clienteBronce);

        // Compra de $500 -> 5 puntos base * 1.0 (Bronce) = 5 puntos
        // Total: 495 + 5 = 500 -> Debería subir a PLATA
        compraServicio.registrarCompra(clienteBronce.getId(), 500);

        Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
        assertEquals(500, clienteActualizado.getPuntos());
        assertEquals(Nivel.PLATA, clienteActualizado.getNivel());
    }

    @Test
    void testBonoTresComprasMismoDia() {
        // Primera compra
        compraServicio.registrarCompra(clienteBronce.getId(), 200); // +2 puntos
        assertEquals(2, clienteRepositorio.findById(clienteBronce.getId()).get().getPuntos());

        // Segunda compra
        compraServicio.registrarCompra(clienteBronce.getId(), 200); // +2 puntos
        assertEquals(4, clienteRepositorio.findById(clienteBronce.getId()).get().getPuntos());

        // Tercera compra (con bono)
        compraServicio.registrarCompra(clienteBronce.getId(), 200); // +2 puntos base + 10 bono = 12 puntos
        assertEquals(16, clienteRepositorio.findById(clienteBronce.getId()).get().getPuntos());
    }

	@Test
	void testCalculoPuntosConMontoBajo100() {
	    // Objetivo: Verificar que montos menores a $100 no otorgan puntos.
	    
	    // Cuando
	    compraServicio.registrarCompra(clienteBronce.getId(), 99.9);
	
	    // Entonces
	    Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
	    assertEquals(0, clienteActualizado.getPuntos());
	}

	@Test
	void testPromocionANivelOro() {
	    // Objetivo: Verificar la promoción de Plata a Oro.
	    
	    // Dado
	    clienteBronce.setPuntos(1495); // A 5 puntos de ser Oro
	    clienteBronce.setNivel(Nivel.PLATA);
	    clienteRepositorio.save(clienteBronce);
	
	    // Cuando: se realiza una compra que otorga 5 puntos base (5 * 1.2 = 6 puntos)
	    compraServicio.registrarCompra(clienteBronce.getId(), 500);
	
	    // Entonces: el total es 1495 + 6 = 1501 puntos
	    Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
	    assertEquals(1501, clienteActualizado.getPuntos());
	    assertEquals(Nivel.ORO, clienteActualizado.getNivel());
	}

	@Test
	void testPromocionANivelPlatino() {
	    // Objetivo: Verificar la promoción de Oro a Platino.
	
	    // Dado
	    clienteBronce.setPuntos(2990); // A 10 puntos de ser Platino
	    clienteBronce.setNivel(Nivel.ORO);
	    clienteRepositorio.save(clienteBronce);
	
	    // Cuando: se realiza una compra que otorga 10 puntos base (10 * 1.5 = 15 puntos)
	    compraServicio.registrarCompra(clienteBronce.getId(), 1000);
	
	    // Entonces: el total es 2990 + 15 = 3005 puntos
	    Cliente clienteActualizado = clienteRepositorio.findById(clienteBronce.getId()).get();
	    assertEquals(3005, clienteActualizado.getPuntos());
	    assertEquals(Nivel.PLATINO, clienteActualizado.getNivel());
	}
}