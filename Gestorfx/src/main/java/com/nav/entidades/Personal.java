package com.nav.entidades;

public class Personal {
    private String dni;
    private String nombre;
    private String sueldo;


    public Personal() {
    }

    public Personal(String dni, String nombre, String sueldo) {
        this.dni = dni;
        this.nombre = nombre;
        this.sueldo = sueldo;
    }

    // getters y setters
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSueldo() { return sueldo; }
    public void setSueldo(String sueldo) { this.sueldo = sueldo; }


    //corregir-1
    @Override
    public String toString() {
        return "Personal{" +
            "dni='" + dni + '\'' +
            ", nombre='" + nombre + '\'' +
            ", sueldo='" + sueldo + '\'' +
            '}';
    }

}
