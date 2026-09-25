package tramite;

import java.time.LocalDateTime;

public class Expediente {
    private String id;
    private int prioridad;
    private Interesado interesado;
    private String asunto;
    private String docRef;
    private ListaSimple<Movimiento> seguimiento = new ListaSimple<>();

    private LocalDateTime fechaInicio = LocalDateTime.now(); 
    private LocalDateTime fechaFin;
    private String documentoResultado;

    public Expediente(String id, int prioridad, Interesado interesado, String asunto, String docRef) {
        this.id = id;
        this.prioridad = prioridad;
        this.interesado = interesado;
        this.asunto = asunto;
        this.docRef = docRef;
    }

    
    public String getId() { 
        return id; 
    }
    public int getPrioridad() { 
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
    public ListaSimple<Movimiento> getSeguimiento() { 
        return seguimiento; 
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

    
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDocumentoResultado(String documentoResultado) {
        this.documentoResultado = documentoResultado;
    }
}

