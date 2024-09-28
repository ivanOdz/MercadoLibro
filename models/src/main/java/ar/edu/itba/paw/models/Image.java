package ar.edu.itba.paw.models;

public class Image {
    private final Integer imageId;
    private final byte[] image;

    public Image(Integer imageId, byte[] image) {
        this.imageId = imageId;
        this.image = image;
    }

    public Integer getImageId() {
        return imageId;
    }

    public byte[] getImage() {
        return image;
    }
}
