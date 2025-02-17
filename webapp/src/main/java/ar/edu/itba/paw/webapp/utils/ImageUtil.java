package ar.edu.itba.paw.webapp.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static MultipartFile convertToMultipartFile(InputStream imageStream) throws IOException {
        byte[] imageBytes = IOUtils.toByteArray(imageStream);
        return new MockMultipartFile("file", "uploaded_image.jpg", "image/jpeg", imageBytes);
    }
}
