package tramite;

public enum Prioridad {
    BAJA(1, "Baja"),
    MEDIA(2, "Media"),
    ALTA(3, "Alta"),
    MUY_ALTA(4, "Muy alta");

    private final int nivel;
    private final String etiqueta;

    Prioridad(int nivel, String etiqueta) {
        this.nivel = nivel;
        this.etiqueta = etiqueta;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
