package com.ivan.procampo.modelos;

public class Recolectas {

    //Atributos
        private String CodigoRecolecta;
        private String FechaRecolecta;
        private String KilosRecolecta;
        private String DATRecolecta;
        private String MatriculaRecolecta;
        private String CultivoRecolecta;

    //Métodos



    public Recolectas() {

    }

    public Recolectas(String cultivoRecolecta, String fechaRecolecta, String kilosRecolecta) {
        CultivoRecolecta = cultivoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;

    }

    public Recolectas(String codigoRecolecta, String fechaRecolecta, String kilosRecolecta, String cultivoRecolecta) {
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        CultivoRecolecta = cultivoRecolecta;
    }



    public Recolectas(String cultivoRecolecta,String codigoRecolecta ,String fechaRecolecta, String kilosRecolecta, String datrecolecta, String matriculaRecolecta){
        CultivoRecolecta = cultivoRecolecta;
        CodigoRecolecta = codigoRecolecta;
        FechaRecolecta = fechaRecolecta;
        KilosRecolecta = kilosRecolecta;
        DATRecolecta = datrecolecta;
        MatriculaRecolecta = matriculaRecolecta;


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

    public String getDATRecolecta() {
        return DATRecolecta;
    }

    public void setDATRecolecta(String DATRecolecta) {
        this.DATRecolecta = DATRecolecta;
    }

    public String getMatriculaRecolecta() {
        return MatriculaRecolecta;
    }

    public void setMatriculaRecolecta(String matriculaRecolecta) {
        MatriculaRecolecta = matriculaRecolecta;
    }

    public String getCultivoRecolecta() {
        return CultivoRecolecta;
    }

    public void setCultivoRecolecta(String cultivoRecolecta) {
        CultivoRecolecta = cultivoRecolecta;
    }

    //to string

    @Override
    public String toString() {
        return "Recolecta en " +CultivoRecolecta+ " de "+KilosRecolecta+ " kilos";
    }
}
