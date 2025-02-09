package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.dto.output.ImageDTO;
import ar.edu.itba.paw.webapp.utils.CacheResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;

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
    @Path("/{id}")
    @Produces({"image/png"})
    public Response getImage(@PathParam("id") long id) {
        Image image = imageService.getImageById(id);

        if (image.getIsImageNull()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return CacheResponseUtil
                .unconditionalCacheResponse(Response.ok(image.getImage()));
    }


    @POST
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response saveImage(final MultipartFile image){ // /images
        Image imageObj = imageService.saveImage(image);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(imageObj.getImageId())).build()).build();
    }

}


