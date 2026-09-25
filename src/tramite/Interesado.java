package tramite;

public class Interesado {
    private final String identificacion, nombre, telefono, email;
    private final boolean esInterno;

    public Interesado(String identificacion, String nombre, String telefono, String email, boolean esInterno) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.esInterno = esInterno;
    }

    public String getNombre() { 
        return nombre; 
    }
    public String getIdentificacion() {
        return identificacion;
    }
    public String getTelefono() { 
        return telefono; 
    }
    public String getEmail() { 
        return email; 
    }
    public boolean esInterno() { 
        return esInterno; 
    }
}
