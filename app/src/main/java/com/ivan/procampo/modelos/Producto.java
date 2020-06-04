package com.ivan.procampo.modelos;

import java.io.Serializable;

public class Producto implements Serializable {

    String idProducto;
    String image;
    String nomProducto;
   String precio;

    public Producto(String idProducto, String image, String nomProducto, String precio) {
        this.idProducto = idProducto;
        this.image = image;
        this.nomProducto = nomProducto;
        this.precio = precio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
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
