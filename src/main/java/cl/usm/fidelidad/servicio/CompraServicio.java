package cl.usm.fidelidad.servicio;

import cl.usm.fidelidad.modelo.Cliente;
import cl.usm.fidelidad.modelo.Compra;
import cl.usm.fidelidad.modelo.Nivel;
import cl.usm.fidelidad.repositorio.ClienteRepositorio;
import cl.usm.fidelidad.repositorio.CompraRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CompraServicio {
    private final ClienteRepositorio clienteRepositorio;
    private final CompraRepositorio compraRepositorio;
    private LocalDate ultimoDiaProcesado = null; // Para controlar el reinicio del streak

    public CompraServicio(ClienteRepositorio clienteRepositorio, CompraRepositorio compraRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.compraRepositorio = compraRepositorio;
    }

    public Compra registrarCompra(String idCliente, double monto) {
        LocalDate hoy = LocalDate.now();
        
        // Simula el paso del tiempo para reiniciar streaks (importante para testing y la app real)
        if (ultimoDiaProcesado != null && !ultimoDiaProcesado.equals(hoy)) {
            reiniciarStreaksDiarios();
        }
        this.ultimoDiaProcesado = hoy;

        Cliente cliente = clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + idCliente));

        // 1. Calcular puntos base
        int puntosBase = (int) (monto / 100);

        // 2. Aplicar multiplicador por nivel
        double multiplicador = cliente.getNivel().getMultiplicador();
        int puntosObtenidos = (int) (puntosBase * multiplicador);

        // 3. Verificar bono por 3 compras en el día
        List<Compra> comprasDelDia = compraRepositorio.findByClienteIdAndFecha(idCliente, hoy);
        int bono = 0;
        if ((comprasDelDia.size() + 1) % 3 == 0) {
            bono = 10;
        }
        puntosObtenidos += bono;
        
        // 4. Actualizar puntos del cliente
        cliente.setPuntos(cliente.getPuntos() + puntosObtenidos);

        // 5. Recalcular nivel
        recalcularNivel(cliente);

        // 6. Guardar la compra
        String idCompra = UUID.randomUUID().toString();
        Compra nuevaCompra = new Compra(idCompra, idCliente, monto, hoy);
        compraRepositorio.save(nuevaCompra);
        clienteRepositorio.save(cliente); // Guardar cambios en el cliente (puntos y nivel)

        return nuevaCompra;
    }
    
    private void recalcularNivel(Cliente cliente) {
        int puntos = cliente.getPuntos();
        Nivel nuevoNivel = cliente.getNivel();

        if (puntos >= 3000) {
            nuevoNivel = Nivel.PLATINO;
        } else if (puntos >= 1500) {
            nuevoNivel = Nivel.ORO;
        } else if (puntos >= 500) {
            nuevoNivel = Nivel.PLATA;
        } else {
            nuevoNivel = Nivel.BRONCE;
        }
        cliente.setNivel(nuevoNivel);
    }
    
    private void reiniciarStreaksDiarios() {
        clienteRepositorio.findAll().forEach(cliente -> cliente.setStreakDias(0));
    }
    
    public List<Compra> listarCompras() {
        return compraRepositorio.findAll();
    }
}