package com.ivan.procampo.modelos;

public class Sulfatos {

    //Atributos
    private String CodigoSulfato;
    private String FechaSulfato;
    private String TratamientoSulfato;
    private String CultivoSulfato;


    //Métodos


    public Sulfatos() {
    }

    public Sulfatos(String codigoSulfato, String fechaSulfato,String cultivoSulfato, String tratamientoSulfato) {
        CodigoSulfato = codigoSulfato;
        FechaSulfato = fechaSulfato;
        CultivoSulfato = cultivoSulfato;
        TratamientoSulfato = tratamientoSulfato;
    }



    public String getCodigoSulfato() {
        return CodigoSulfato;
    }

    public void setCodigoSulfato(String codigoSulfato) {
        CodigoSulfato = codigoSulfato;
    }

    public String getFechaSulfato() {
        return FechaSulfato;
    }

    public void setFechaSulfato(String fechaSulfato) {
        FechaSulfato = fechaSulfato;
    }

    public String getTratamientoSulfato() {
        return TratamientoSulfato;
    }

    public void setTratamientoSulfato(String tratamientoSulfato) {
        TratamientoSulfato = tratamientoSulfato;
    }

    public String getCultivoSulfato() {
        return CultivoSulfato;
    }

    public void setCultivoSulfato(String cultivoSulfato) {
        CultivoSulfato = cultivoSulfato;
    }
}
