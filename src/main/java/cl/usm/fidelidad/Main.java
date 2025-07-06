package cl.usm.fidelidad;

import cl.usm.fidelidad.repositorio.ClienteRepositorio;
import cl.usm.fidelidad.repositorio.CompraRepositorio;
import cl.usm.fidelidad.servicio.ClienteServicio;
import cl.usm.fidelidad.servicio.CompraServicio;
import cl.usm.fidelidad.vista.ConsolaVista;

public class Main {
    public static void main(String[] args) {
        ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
        CompraRepositorio compraRepositorio = new CompraRepositorio();
        
        ClienteServicio clienteServicio = new ClienteServicio(clienteRepositorio);
        CompraServicio compraServicio = new CompraServicio(clienteRepositorio, compraRepositorio);
        
        cargarDatosIniciales(clienteServicio);
        
        ConsolaVista view = new ConsolaVista(clienteServicio, compraServicio);
        
        view.mostrarMenuPrincipal();
    }
    
    private static void cargarDatosIniciales(ClienteServicio clienteServicio) {
        try {
            System.out.println("Cargando datos iniciales de prueba...");
            clienteServicio.agregarCliente("Juan Perez", "juan.perez@example.com");
            clienteServicio.agregarCliente("Ana Lopez", "ana.lopez@example.com");
            System.out.println("Datos cargados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar datos iniciales: " + e.getMessage());
        }
    }
}