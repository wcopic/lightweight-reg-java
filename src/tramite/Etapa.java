package tramite;

public enum Etapa {
    RECIBIDO("Recibido", false),
    EN_REVISION("En revisión", false),
    EN_ESPERA("En espera de información", false),
    DERIVADO("Derivado a otra área", false),
    APROBADO("Finalizado · Aprobado", true),
    RECHAZADO("Finalizado · Rechazado", true);

    private final String etiqueta;
    private final boolean finaliza;

    Etapa(String etiqueta, boolean finaliza) {
        this.etiqueta = etiqueta;
        this.finaliza = finaliza;
    }

    public boolean finaliza() {
        return finaliza;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
