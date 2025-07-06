package cl.usm.fidelidad.modelo;

public enum Nivel {
    BRONCE(1.0),   // 0 - 499
    PLATA(1.2),    // 500 - 1499
    ORO(1.5),      // 1500 - 2999
    PLATINO(2.0);  // 3000+

    private final double multiplicador;

    Nivel(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}