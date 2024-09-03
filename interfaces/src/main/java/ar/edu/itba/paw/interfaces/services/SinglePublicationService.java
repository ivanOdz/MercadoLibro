package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Publication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SinglePublicationService {

    Publication createPublication(String username, String mail, String isbn, String title, List<String> authors, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, String location);

}
