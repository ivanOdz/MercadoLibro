package ar.edu.itba.paw.models;

import java.util.List;

public class Publications {

    private final List<Publication> publications;

    public Publications (List<Publication> pb) {
        this.publications = pb;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    // TODO
    // Ordernar los elementos de las lista
    // Filtrar elemetos de la lista
}
