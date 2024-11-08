package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ImageBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;


    @Override
    @Transactional
    public List<Image> saveImage(List<MultipartFile> image) {
        List<Image> images = new ArrayList<>();
        for(MultipartFile file : image) {
            try {
                images.add(imageDao.createImage(file.getBytes()));
            } catch (IOException e) {
                throw new ImageBadRequestException("Error saving image");
            }
        }

        return images;
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImageById(Long imageId) {
        Optional<Image> image = imageDao.getImageById(imageId);
        if (image.isEmpty()){
            throw new ImageNotFoundException("Image not found");
        }
        return image.get();
    }
}
