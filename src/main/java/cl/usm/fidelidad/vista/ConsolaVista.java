package cl.usm.fidelidad.vista;

import cl.usm.fidelidad.modelo.Cliente;
import cl.usm.fidelidad.modelo.Compra;
import cl.usm.fidelidad.servicio.ClienteServicio;
import cl.usm.fidelidad.servicio.CompraServicio;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsolaVista {
    private final ClienteServicio clienteServicio;
    private final CompraServicio compraServicio;
    private final Scanner scanner;

    public ConsolaVista(ClienteServicio clienteServicio, CompraServicio compraServicio) {
        this.clienteServicio = clienteServicio;
        this.compraServicio = compraServicio;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenuPrincipal() {
        int opcion = -1;
        while (opcion != 4) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Compras");
            System.out.println("3. Mostrar Puntos / Nivel de un Cliente");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> menuGestionClientes();
                    case 2 -> menuGestionCompras();
                    case 3 -> mostrarPuntosYnivel();
                    case 4 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número.");
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    private void menuGestionClientes() {
        int opcion = -1;
        while (opcion != 5) {
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Agregar nuevo cliente");
            System.out.println("2. Listar todos los clientes");
            System.out.println("3. Actualizar datos de un cliente");
            System.out.println("4. Eliminar un cliente");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> agregarCliente();
                    case 2 -> listarClientes();
                    case 3 -> actualizarCliente();
                    case 4 -> eliminarCliente();
                    case 5 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            } catch (Exception e) {
                System.err.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
    }

    private void agregarCliente() {
        try {
            System.out.print("Ingrese nombre del cliente: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese correo del cliente: ");
            String correo = scanner.nextLine();
            Cliente nuevoCliente = clienteServicio.agregarCliente(nombre, correo);
            System.out.println("Cliente agregado con éxito. ID: " + nuevoCliente.getId());
        } catch (IllegalArgumentException e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
        }
    }

    private void listarClientes() {
        List<Cliente> clientes = clienteServicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("\n--- Listado de Clientes ---");
            clientes.forEach(System.out::println);
            System.out.println("---------------------------");
        }
    }

    private void actualizarCliente() {
        try {
            System.out.print("Ingrese el ID del cliente a actualizar: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese el nuevo nombre: ");
            String nuevoNombre = scanner.nextLine();
            System.out.print("Ingrese el nuevo correo: ");
            String nuevoCorreo = scanner.nextLine();
            clienteServicio.actualizarCliente(id, nuevoNombre, nuevoCorreo);
            System.out.println("Cliente actualizado con éxito.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }
    
    private void eliminarCliente() {
        try {
            System.out.print("Ingrese el ID del cliente a eliminar: ");
            String id = scanner.nextLine();
            clienteServicio.eliminarCliente(id);
            System.out.println("Cliente eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }


    private void menuGestionCompras() {
        int opcion = -1;
        while (opcion != 3) {
            System.out.println("\n--- Gestión de Compras ---");
            System.out.println("1. Registrar nueva compra");
            System.out.println("2. Listar todas las compras");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> registrarCompra();
                    case 2 -> listarCompras();
                    case 3 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            } catch (Exception e) {
                System.err.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
    }

    private void registrarCompra() {
        try {
            System.out.print("Ingrese ID del cliente que realiza la compra: ");
            String idCliente = scanner.nextLine();
            System.out.print("Ingrese monto de la compra: ");
            double monto = Double.parseDouble(scanner.nextLine());

            compraServicio.registrarCompra(idCliente, monto);
            System.out.println("✅ Compra registrada con éxito.");

            Optional<Cliente> clienteOpt = clienteServicio.buscarClientePorId(idCliente);
            clienteOpt.ifPresent(cliente -> System.out.println("Estado actualizado -> Puntos: " + cliente.getPuntos() + ", Nivel: " + cliente.getNivel()));

        } catch (NumberFormatException e) {
            System.err.println("Error: El monto debe ser un número válido.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al registrar la compra: " + e.getMessage());
        }
    }
    
    private void listarCompras() {
        List<Compra> compras = compraServicio.listarCompras();
         if (compras.isEmpty()) {
            System.out.println("No hay compras registradas.");
        } else {
            System.out.println("\n--- Historial de Compras ---");
            compras.forEach(System.out::println);
            System.out.println("----------------------------");
        }
    }
    private void mostrarPuntosYnivel() {
        System.out.print("Ingrese el ID del cliente: ");
        String idCliente = scanner.nextLine();
        Optional<Cliente> clienteOpt = clienteServicio.buscarClientePorId(idCliente);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            System.out.println("------------------------------------");
            System.out.println("Detalles del Cliente:");
            System.out.println("  Nombre: " + cliente.getNombre());
            System.out.println("  Puntos Totales: " + cliente.getPuntos());
            System.out.println("  Nivel de Fidelidad: " + cliente.getNivel());
            System.out.println("------------------------------------");
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }
}