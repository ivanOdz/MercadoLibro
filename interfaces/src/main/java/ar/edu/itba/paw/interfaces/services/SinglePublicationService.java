package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SinglePublicationService {

    public Publication createPublication(int bookModelId, int ownerId, BookState bookState, int exchangesQty, int rating);

}
