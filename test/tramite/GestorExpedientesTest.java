package tramite;

/** Pruebas de regresión sin librerías externas; ejecutar con java -ea. */
public final class GestorExpedientesTest {
    public static void main(String[] args) {
        GestorExpedientes gestor = new GestorExpedientes();
        Interesado interesado = new Interesado("12345678", "Renato", "", "", false);

        assert "EXP-001".equals(gestor.siguienteId());
        falla(() -> gestor.registrar(null, interesado, "Solicitud", ""), IllegalArgumentException.class);
        falla(() -> gestor.registrar(Prioridad.BAJA, interesado, "  ", ""), IllegalArgumentException.class);
        assert "EXP-001".equals(gestor.siguienteId());

        Expediente primero = gestor.registrar(Prioridad.BAJA, interesado, "Solicitud", "Ref-01");
        Expediente segundo = gestor.registrar(Prioridad.MUY_ALTA, interesado, "Revisión", "Ref-02");
        assert "EXP-001".equals(primero.getId());
        assert "EXP-002".equals(segundo.getId());
        assert gestor.buscar(" EXP-001 ") == primero;
        assert gestor.alertas().size() == 2;
        assert gestor.alertas().get(0) == segundo;
        assert gestor.listar().get(0) == segundo;

        gestor.mover(primero.getId(), Etapa.RECIBIDO, "");
        assert primero.getSeguimiento().size() == 1;
        falla(() -> gestor.mover(primero.getId(), Etapa.APROBADO, " "),
                IllegalArgumentException.class);
        assert primero.getSeguimiento().size() == 1;

        gestor.finalizar(primero.getId(), "Resolución 123");
        assert primero.getFechaFin() != null;
        assert "Resolución 123".equals(primero.getDocumentoResultado());
        assert primero.getSeguimiento().size() == 2;
        assert gestor.alertas().size() == 1 && gestor.alertas().get(0) == segundo;
        falla(() -> gestor.finalizar(primero.getId(), "Otra resolución"), IllegalStateException.class);
        falla(() -> gestor.mover(primero.getId(), Etapa.EN_REVISION, ""), IllegalStateException.class);

        gestor.mover(segundo.getId(), Etapa.RECHAZADO, "Oficio 456");
        assert gestor.alertas().isEmpty();
        assert segundo.getFechaFin() != null;
        assert segundo.getSeguimiento().size() == 1;
        falla(() -> gestor.mover("EXP-999", Etapa.RECIBIDO, ""), IllegalArgumentException.class);
        System.out.println("Pruebas de expedientes correctas.");
    }

    private static void falla(Runnable accion, Class<? extends Exception> tipo) {
        try {
            accion.run();
        } catch (Exception e) {
            if (tipo.isInstance(e)) {
                return;
            }
            throw new AssertionError("Excepción inesperada", e);
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
