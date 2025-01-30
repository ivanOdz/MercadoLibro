package ar.edu.itba.paw.utils;

import java.net.URI;

public class UrnResolverUtil {
    private String urn;

    public UrnResolverUtil(String urn) {
        this.urn = urn;
    }


    /**
     * Returns the next path in the URN. If "/publication/1" is the URN, it will return "/1".
     * @return the class itself if there is a next path, null otherwise.
     */
    public UrnResolverUtil nextPath() {
        cutUrnStart();  // removes '/'

        int i = this.urn.indexOf('/');
        if (i != -1) {
            this.urn = this.urn.substring(i);
        } else {
            return null;
        }
        return this;
    }


    /**
     * Expects a URN like "/1" and returns the expected id. If the URN is not in the expected format, it will return null.
     * @return the id if the URN is in the expected format, null otherwise.
     */
    public Long getId() {
        cutUrnStart();  // removes '/'

        int i = this.urn.indexOf('/');
        if (i != -1) {
            String id = this.urn.substring(0, i);
            this.urn = this.urn.substring(i);
            try {
                return Long.parseLong(id);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Expects a path as "/api/exchanges/{id}".
     * @return The expected exchange id. If the URN is not in the expected format, it will return null.
     */
    public static Long getExchangeId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/users/{id}/locations/{id}".
     * @return The expected location id. If the URN is not in the expected format, it will return null.
     */
    public static Long getLocationId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(4);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/publications/{id}".
     * @return The expected publication id. If the URN is not in the expected format, it will return null.
     */
    public static Long getPublicationId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/books/{id}".
     * @return The expected book id. If the URN is not in the expected format, it will return null.
     */
    public static Long getBookId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/users/{id}".
     * @return The expected user id. If the URN is not in the expected format, it will return null.
     */
    public static Long getUserId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/book_models/{id}".
     * @return The expected book model id. If the URN is not in the expected format, it will return null.
     */
    public static Long getBookModelId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    /**
     * Expects a path as "/api/images/{id}".
     * @return The expected image id. If the URN is not in the expected format, it will return null.
     */
    public static Long getImageId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutUrn(2);
        return ur.getId();
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    private void cutUrnStart() {
        if(urn.startsWith("/")) {
            this.urn = this.urn.substring(1);
        }
    }

    private void cutUrn(int n) {
        for (int i = 0; i < n; i++) {
            nextPath();
        }
    }
}