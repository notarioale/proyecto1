
package Objetos;

public class RegistroVehiculo {
    String cedula;
    String nombre;
    String apellido;
    String chapa;
    String marca;

    public RegistroVehiculo(String cedula, String nombre, String apellido, String chapa, String marca) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.chapa = chapa;
        this.marca = marca;
    }

    public String getCedula() {
        return cedula;
    }
    
    public String getNombre() {
        return nombre;
    }

    
    public String getApellido() {
        return apellido;
    }

    public String getChapa() {
        return chapa;
    }

    public String getMarca() {
        return marca;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setChapa(String chapa) {
        this.chapa = chapa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
   
}