package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ImageBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    /*@Override
    @Transactional
    public List<Image> saveImage(List<MultipartFile> image) {
        LOGGER.info("Starting to save images, total images: {}", image.size());

        List<Image> images = new ArrayList<>();
        for(MultipartFile file : image) {
            try {
                LOGGER.info("Saving image with original filename: {}", file.getOriginalFilename());
                images.add(imageDao.createImage(file.getBytes()));
                LOGGER.info("Image with filename {} saved successfully", file.getOriginalFilename());
            } catch (IOException e) {
                LOGGER.warn("Error saving image with filename: {}", file.getOriginalFilename());
                throw new ImageBadRequestException("Error saving image");
            }
        }

        LOGGER.info("Finished saving images, total saved: {}", images.size());
        return images;
    }*/

    @Override
    @Transactional
    public Image saveImage(MultipartFile file) {
        Image image;

        try {
            LOGGER.info("Saving image with original filename: {}", file.getOriginalFilename());
            image = imageDao.createImage(file.getBytes());
            LOGGER.info("Image with filename {} saved successfully", file.getOriginalFilename());
        } catch (IOException e) {
            LOGGER.warn("Error saving image with filename: {}", file.getOriginalFilename());
            throw new ImageBadRequestException("Error saving image");
        }

        LOGGER.info("Image saved");
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImageById(Long imageId) {
        LOGGER.info("Fetching image with ID: {}", imageId);

        Optional<Image> image = imageDao.getImageById(imageId);
        if (image.isEmpty()){
            LOGGER.warn("Image with ID {} not found", imageId);
            throw new ImageNotFoundException("Image not found");
        }

        LOGGER.info("Image with ID {} found", imageId);
        return image.get();
    }
}
