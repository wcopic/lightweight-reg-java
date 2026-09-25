package tramite;

import java.util.List;

/** Operaciones y reglas de negocio de los expedientes durante esta sesión. */
public final class GestorExpedientes {
    private final ListaDoble<Expediente> expedientes = new ListaDoble<>();
    private final ListaCircular<Expediente> alertas = new ListaCircular<>();
    private int siguienteNumero = 1;

    public String siguienteId() {
        return String.format("EXP-%03d", siguienteNumero);
    }

    public Expediente registrar(int prioridad, Interesado interesado, String asunto, String docRef) {
        if (prioridad <= 0) {
            throw new IllegalArgumentException("La prioridad debe ser un número positivo.");
        }
        if (interesado == null || interesado.getDni() == null || interesado.getDni().isBlank()
                || interesado.getNombre() == null || interesado.getNombre().isBlank()) {
            throw new IllegalArgumentException("Ingresa el DNI y el nombre del interesado.");
        }
        if (asunto == null || asunto.isBlank()) {
            throw new IllegalArgumentException("Ingresa el asunto del expediente.");
        }

        Expediente expediente = new Expediente(siguienteId(), prioridad, interesado,
                asunto.trim(), docRef == null ? "" : docRef.trim());
        expedientes.add(expediente);
        alertas.add(expediente);
        siguienteNumero++;
        return expediente;
    }

    public Expediente buscar(String id) {
        if (id == null) {
            return null;
        }
        for (Expediente expediente : expedientes.toList()) {
            if (expediente.getId().equals(id.trim())) {
                return expediente;
            }
        }
        return null;
    }

    public void mover(String id, String etapa, String documentoResultado) {
        Expediente expediente = exigirAbierto(id);
        if (etapa == null || etapa.isBlank()) {
            throw new IllegalArgumentException("Selecciona una etapa.");
        }
        boolean finaliza = etapa.contains("Finalizado");
        if (finaliza && (documentoResultado == null || documentoResultado.isBlank())) {
            throw new IllegalArgumentException("Ingresa el documento de resultado para finalizar.");
        }
        expediente.agregarMovimiento(new Movimiento(etapa.trim()));
        if (finaliza) {
            cerrar(expediente, documentoResultado);
        }
    }

    public void finalizar(String id, String documentoResultado) {
        Expediente expediente = exigirAbierto(id);
        if (documentoResultado == null || documentoResultado.isBlank()) {
            throw new IllegalArgumentException("Ingresa el documento de resultado.");
        }
        expediente.agregarMovimiento(new Movimiento("Trámite finalizado"));
        cerrar(expediente, documentoResultado);
    }

    public List<Expediente> alertas() {
        return alertas.toList();
    }

    private Expediente exigirAbierto(String id) {
        Expediente expediente = buscar(id);
        if (expediente == null) {
            throw new IllegalArgumentException("Expediente no encontrado.");
        }
        if (expediente.getFechaFin() != null) {
            throw new IllegalStateException("El expediente ya está finalizado.");
        }
        return expediente;
    }

    private void cerrar(Expediente expediente, String documentoResultado) {
        expediente.finalizar(documentoResultado.trim());
        alertas.remove(expediente);
    }
}
