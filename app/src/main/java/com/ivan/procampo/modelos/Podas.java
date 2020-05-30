package com.ivan.procampo.modelos;

public class Podas {

    //Atributos
    private String CodigoPoda;
    private String FechaPoda;
    private String CultivoPoda;


    //Metodos

    public Podas() {
    }

    public Podas(String codigoPoda, String cultivoPoda, String fechaPoda) {
        CodigoPoda = codigoPoda;
        CultivoPoda = cultivoPoda;
        FechaPoda = fechaPoda;

    }



    public String getCodigoPoda() {
        return CodigoPoda;
    }

    public void setCodigoPoda(String codigoPoda) {
        CodigoPoda = codigoPoda;
    }

    public String getFechaPoda() {
        return FechaPoda;
    }

    public void setFechaPoda(String fechaPoda) {
        FechaPoda = fechaPoda;
    }

    public String getCultivoPoda() {
        return CultivoPoda;
    }

    public void setCultivoPoda(String cultivoPoda) {
        CultivoPoda = cultivoPoda;
    }
}
