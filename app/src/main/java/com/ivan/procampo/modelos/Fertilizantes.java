package com.ivan.procampo.modelos;

public class Fertilizantes {
    //Atributos
    public  String nombre, image;
    Double precio;


    //Métodos



    public Fertilizantes() {

    }

    public Fertilizantes(String nombre, Double precio, String image) {
        this.nombre = nombre;
        this.precio = precio;
        this.image = image;

    }

    public Fertilizantes(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
