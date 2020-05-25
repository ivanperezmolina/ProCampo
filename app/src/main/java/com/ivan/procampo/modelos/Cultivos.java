package com.ivan.procampo.modelos;

public class Cultivos {
    //Atributos
        private String codigoCultivo;
        private String nombreCultivo;
        private String localizacionCultivo;
        private String hectareasCultivo;
        private String tipoDeAceituna;

    //Métodos


    public Cultivos() {
    }

    public Cultivos(String codigoCultivo, String nombreCultivo) {
        this.codigoCultivo = codigoCultivo;
        this.nombreCultivo = nombreCultivo;
    }

    public Cultivos(String codigoCultivo, String nombreCultivo, String localizacionCultivo, String hectareasCultivo, String tipoDeAceituna) {
        this.codigoCultivo = codigoCultivo;
        this.nombreCultivo = nombreCultivo;
        this.localizacionCultivo = localizacionCultivo;
        this.hectareasCultivo = hectareasCultivo;
        this.tipoDeAceituna = tipoDeAceituna;
    }

    public String getNombreCultivo() {
        return nombreCultivo;
    }

    public void setNombreCultivo(String nombreCultivo) {
        this.nombreCultivo = nombreCultivo;
    }

    public String getLocalizacionCultivo() {
        return localizacionCultivo;
    }

    public void setLocalizacionCultivo(String localizacionCultivo) {
        this.localizacionCultivo = localizacionCultivo;
    }

    public String getHectareasCultivo() {
        return hectareasCultivo;
    }

    public void setHectareasCultivo(String hectareasCultivo) {
        this.hectareasCultivo = hectareasCultivo;
    }

    public String getTipoDeAceituna() {
        return tipoDeAceituna;
    }

    public void setTipoDeAceituna(String tipoDeAceituna) {
        this.tipoDeAceituna = tipoDeAceituna;
    }

    public String getCodigoCultivo() {
        return codigoCultivo;
    }

    public void setCodigoCultivo(String codigoCultivo) {
        this.codigoCultivo = codigoCultivo;
    }

    @Override
    public String toString() {
        return nombreCultivo;
    }
}

