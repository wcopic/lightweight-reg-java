package tramite;

import java.time.LocalDateTime;

public class Movimiento {
    private String dependencia;
    private LocalDateTime fechaHora;

    public Movimiento(String dependencia) {
        this.dependencia = dependencia;
        this.fechaHora = LocalDateTime.now();
    }

    public String getDependencia() {
        return dependencia;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}
