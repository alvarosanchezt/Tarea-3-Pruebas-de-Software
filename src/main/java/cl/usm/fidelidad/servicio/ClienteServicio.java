package cl.usm.fidelidad.servicio;

import cl.usm.fidelidad.modelo.Cliente;
import cl.usm.fidelidad.repositorio.ClienteRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class ClienteServicio {
    private final ClienteRepositorio clienteRepositorio;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    public Cliente agregarCliente(String nombre, String correo) {
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        if (clienteRepositorio.findByCorreo(correo).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        String id = UUID.randomUUID().toString();
        Cliente nuevoCliente = new Cliente(id, nombre, correo);
        return clienteRepositorio.save(nuevoCliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    public Optional<Cliente> buscarClientePorId(String id) {
        return clienteRepositorio.findById(id);
    }

    public Cliente actualizarCliente(String id, String nuevoNombre, String nuevoCorreo) {
        if (!EMAIL_PATTERN.matcher(nuevoCorreo).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        
        Optional<Cliente> clienteOpt = clienteRepositorio.findById(id);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }

        // Verifica si el nuevo correo ya está en uso por OTRO cliente
        Optional<Cliente> clientePorCorreoOpt = clienteRepositorio.findByCorreo(nuevoCorreo);
        if (clientePorCorreoOpt.isPresent() && !clientePorCorreoOpt.get().getId().equals(id)) {
            throw new IllegalArgumentException("El nuevo correo electrónico ya está en uso por otro cliente.");
        }

        Cliente cliente = clienteOpt.get();
        cliente.setNombre(nuevoNombre);
        cliente.setCorreo(nuevoCorreo);
        return clienteRepositorio.save(cliente);
    }

    public void eliminarCliente(String id) {
        if (clienteRepositorio.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
        clienteRepositorio.deleteById(id);
    }
}