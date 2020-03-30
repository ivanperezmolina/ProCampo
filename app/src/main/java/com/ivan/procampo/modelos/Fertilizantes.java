package com.ivan.procampo.modelos;

public class Fertilizantes {
    //Atributos
    public  String nombre, precio, image;


    //Métodos



    public Fertilizantes() {

    }

    public Fertilizantes(String nombre, String precio, String image) {
        this.nombre = nombre;
        this.precio = precio;
        this.image = image;

    }

    public Fertilizantes(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
