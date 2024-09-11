package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageDao imageDao;

    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Image saveImage(MultipartFile image) {
        Image i = null;
        try {
            i = imageDao.createImage(image.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la imagen", e);
        }
        return i;
    }

    @Override
    public Optional<Image> getImageById(long imageId) {
        return imageDao.getImageById(imageId);
    }

    @Override
    public Image getFirstImageByBookId(long bookId) {
        return imageDao.getFirstImageByBookId(bookId);
    }

}
