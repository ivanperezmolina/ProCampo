package com.ivan.procampo.modelos;

public class Recolectas {

    //Atributos
        private String CodigoRecolecta;
        private String FechaRecolecta;
        private String KilosRecolecta;
        private String CultivoRecolecta;
        private String urlFotoVale;



    //Métodos



    public Recolectas() {

    }

    public Recolectas( String fechaRecolecta, String kilosRecolecta,String cultivoRecolecta) {
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;

    }

    public Recolectas(String codigoRecolecta, String fechaRecolecta, String kilosRecolecta, String cultivoRecolecta) {
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;
    }

    public Recolectas(String codigoRecolecta, String fechaRecolecta, String kilosRecolecta, String cultivoRecolecta, String urlFotoVale) {
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;
        urlFotoVale = urlFotoVale;
    }



    public String getCodigoRecolecta() {
        return CodigoRecolecta;
    }

    public void setCodigoRecolecta(String codigoRecolecta) {
        CodigoRecolecta = codigoRecolecta;
    }

    public String getFechaRecolecta() {
        return FechaRecolecta;
    }

    public void setFechaRecolecta(String fechaRecolecta) {
        FechaRecolecta = fechaRecolecta;
    }

    public String getKilosRecolecta() {
        return KilosRecolecta;
    }

    public void setKilosRecolecta(String kilosRecolecta) {
        KilosRecolecta = kilosRecolecta;
    }

    public String getCultivoRecolecta() {
        return CultivoRecolecta;
    }

    public void setCultivoRecolecta(String cultivoRecolecta) {
        CultivoRecolecta = cultivoRecolecta;
    }

    public String getUrlFotoVale() {
        return urlFotoVale;
    }

    public void setUrlFotoVale(String urlFotoVale) {
        this.urlFotoVale = urlFotoVale;
    }

    //to string

    @Override
    public String toString() {
        return "Recolecta en " +CultivoRecolecta+ " de "+KilosRecolecta+ " kilos";
    }
}
