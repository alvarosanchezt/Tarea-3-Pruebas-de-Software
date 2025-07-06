package cl.usm.fidelidad.repositorio;

import cl.usm.fidelidad.modelo.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClienteRepositorio {
    private final Map<String, Cliente> clientes = new HashMap<>();

    public Cliente save(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    public Optional<Cliente> findById(String id) {
        return Optional.ofNullable(clientes.get(id));
    }

    public List<Cliente> findAll() {
        return new ArrayList<>(clientes.values());
    }

    public void deleteById(String id) {
        clientes.remove(id);
    }
    
    public Optional<Cliente> findByCorreo(String correo) {
        return clientes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }
}