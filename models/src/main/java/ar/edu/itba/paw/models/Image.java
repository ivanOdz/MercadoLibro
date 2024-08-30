package ar.edu.itba.paw.models;

public class Image {
    private final long imageId;
    private final byte[] image;

    public Image(long imageId, byte[] image) {
        this.imageId = imageId;
        this.image = image;
    }

    public long getImageId() {
        return imageId;
    }

    public byte[] getImage() {
        return image;
    }
}
