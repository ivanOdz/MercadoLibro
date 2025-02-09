package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Image;

public class ImageDTO {
    private byte[] image;

    public ImageDTO fromImageDTO(Image image){
        this.image = image.getImage();
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
