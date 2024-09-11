package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public interface ImageService {
    Image saveImage(MultipartFile image);

    Optional<Image> getImageById(long imageId);

    Image getFirstImageByBookId (long bookId);

}
