package cl.usm.fidelidad.modelo;

public class Cliente {
    private String id;
    private String nombre;
    private String correo;
    private int puntos;
    private Nivel nivel;
    private int streakDias;

    public Cliente(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
        this.streakDias = 0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }
    public int getStreakDias() { return streakDias; }
    public void setStreakDias(int streakDias) { this.streakDias = streakDias; }

    @Override
    public String toString() {
        return "Cliente {" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", puntos=" + puntos +
                ", nivel=" + nivel +
                '}';
    }
}