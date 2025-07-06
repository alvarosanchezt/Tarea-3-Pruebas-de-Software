package cl.usm.fidelidad.repositorio;

import cl.usm.fidelidad.modelo.Compra;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompraRepositorio {
    private final Map<String, Compra> compras = new HashMap<>();

    public Compra save(Compra compra) {
        compras.put(compra.getIdCompra(), compra);
        return compra;
    }
    
    public List<Compra> findAll() {
        return new ArrayList<>(compras.values());
    }

    public List<Compra> findByClienteIdAndFecha(String idCliente, LocalDate fecha) {
        return compras.values().stream()
                .filter(c -> c.getIdCliente().equals(idCliente) && c.getFecha().equals(fecha))
                .collect(Collectors.toList());
    }
}