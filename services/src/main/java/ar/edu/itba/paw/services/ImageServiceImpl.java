package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
	
    private final ImageDao imageDao;

    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public List<Image> saveImage(List<MultipartFile> image) {
    	
        List<Image> images = new ArrayList<>();

        for(MultipartFile file : image) {

            try {
                images.add(imageDao.createImage(file.getBytes()));

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        return images;
    }

    @Override
    public Optional<Image> getImageById(Long imageId) {
        return imageDao.getImageById(imageId);
    }

    @Override
    public Image getFirstImageByBookId(Long bookId) {
        return imageDao.getFirstImageByBookId(bookId);
    }

    @Override
    public List<Image> getImagesByBookImageList(List<BookImage> bookImages) {
    	
        List<Image> images = new ArrayList<>();
        
        if (bookImages.isEmpty()){
            return null;
        }
        for (BookImage bookImage : bookImages) {
            images.add(imageDao.getImageById(bookImage.getImageId()).orElse(null));
        }
        return images;
    }

}
