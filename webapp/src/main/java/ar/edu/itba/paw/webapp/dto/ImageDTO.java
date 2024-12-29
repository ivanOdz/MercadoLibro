package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Image;
import org.springframework.web.multipart.MultipartFile;

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
