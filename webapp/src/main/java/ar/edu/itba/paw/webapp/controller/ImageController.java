package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.dto.Book.BookModelDTO;
import ar.edu.itba.paw.webapp.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Component
@Path("images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Context
    private UriInfo uriInfo;

    /*
    @GetMapping(path="/images/{imageid:\\d+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("imageid") long id){
        return imageService.getImageById(id).getImage();
    }*/

    @GET
    @Path("/{imageid:\\d+}")
    @Produces(value = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response getImage(@PathVariable("imageid") long id){
        Image image = imageService.getImageById(id);
        return Response.ok(new GenericEntity<ImageDTO>(new ImageDTO().fromImageDTO(image)) {}).build();
    }

    @POST
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response saveImage(final MultipartFile image){
        Image imageObj = imageService.saveImage(image);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(imageObj.getImageId())).build()).build();
    }

}


