package com.ivan.procampo.modelos;

public class Recolectas {

    //Atributos
        private String CodigoRecolecta;
        private String FechaRecolecta;
        private String KilosRecolecta;
        private String CultivoRecolecta;
        private String fotoValeRecolecta;
        private String fotoDATRecolecta;



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

    public Recolectas(String codigoRecolecta, String fechaRecolecta, String kilosRecolecta, String cultivoRecolecta, String fotoValeRecolecta) {
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;
        fotoValeRecolecta = fotoValeRecolecta;
    }

    public Recolectas(String codigoRecolecta, String fechaRecolecta, String kilosRecolecta, String cultivoRecolecta, String fotoValeRecolecta, String fotoDATRecolecta) {
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;
        this.fotoValeRecolecta = fotoValeRecolecta;
        this.fotoDATRecolecta = fotoDATRecolecta;
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

    public String getFotoValeRecolecta() {
        return fotoValeRecolecta;
    }

    public void setFotoValeRecolecta(String fotoValeRecolecta) {
        this.fotoValeRecolecta = fotoValeRecolecta;
    }

    public String getFotoDATRecolecta() {
        return fotoDATRecolecta;
    }

    public void setFotoDATRecolecta(String fotoDATRecolecta) {
        this.fotoDATRecolecta = fotoDATRecolecta;
    }

    //to string

    @Override
    public String toString() {
        return "Recolecta en " +CultivoRecolecta+ " de "+KilosRecolecta+ " kilos";
    }
}
