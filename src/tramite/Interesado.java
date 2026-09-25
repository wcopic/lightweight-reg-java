package tramite;

public class Interesado { //
    private String dni, nombre, telefono, email;
    private boolean esInterno;

    public Interesado(String dni, String nombre, String telefono, String email, boolean esInterno) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.esInterno = esInterno;
    }

    public String getNombre() { 
        return nombre; 
    }
    public String getDni() { 
        return dni; 
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
