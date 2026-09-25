package tramite;

import java.time.LocalDateTime;
import java.util.List;

public class Expediente {
    private final String id;
    private final Prioridad prioridad;
    private final Interesado interesado;
    private final String asunto;
    private final String docRef;
    private final ListaSimple<Movimiento> seguimiento = new ListaSimple<>();

    private final LocalDateTime fechaInicio = LocalDateTime.now();
    private LocalDateTime fechaFin;
    private String documentoResultado;

    public Expediente(String id, Prioridad prioridad, Interesado interesado, String asunto, String docRef) {
        this.id = id;
        this.prioridad = prioridad;
        this.interesado = interesado;
        this.asunto = asunto;
        this.docRef = docRef;
    }

    
    public String getId() { 
        return id; 
    }
    public Prioridad getPrioridad() {
        return prioridad; 
    }
    public Interesado getInteresado() { 
        return interesado; 
    }
    public String getAsunto() { 
        return asunto; 
    }
    public String getDocRef() { 
        return docRef; 
    }
    public List<Movimiento> getSeguimiento() {
        return seguimiento.toList();
    }
    void agregarMovimiento(Movimiento movimiento) {
        if (fechaFin != null) {
            throw new IllegalStateException("El expediente ya está finalizado.");
        }
        seguimiento.add(movimiento);
    }
    public LocalDateTime getFechaInicio() { 
        return fechaInicio; 
    }
    public LocalDateTime getFechaFin() { 
        return fechaFin; 
    }
    public String getDocumentoResultado() { 
        return documentoResultado; 
    }

    
    void finalizar(String documentoResultado) {
        if (fechaFin != null) {
            throw new IllegalStateException("El expediente ya está finalizado.");
        }
        this.documentoResultado = documentoResultado;
        this.fechaFin = LocalDateTime.now();
    }
}
