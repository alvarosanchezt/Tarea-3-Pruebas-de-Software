package cl.usm.fidelidad.modelo;

import java.time.LocalDate;

public class Compra {
    private String idCompra;
    private String idCliente;
    private double monto;
    private LocalDate fecha;

    public Compra(String idCompra, String idCliente, double monto, LocalDate fecha) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters
    public String getIdCompra() { return idCompra; }
    public String getIdCliente() { return idCliente; }
    public double getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Compra {" +
                "idCompra='" + idCompra + '\'' +
                ", idCliente='" + idCliente + '\'' +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }
}