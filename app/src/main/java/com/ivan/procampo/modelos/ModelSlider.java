package com.ivan.procampo.modelos;

public class ModelSlider {
    private int image;
    private int title;
    private int desc;

    public ModelSlider(int image, int title, int desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }



    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }
}
