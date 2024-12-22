package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface ImageService {
    Image saveImage(MultipartFile image);
    Image getImageById(Long imageId);
}
