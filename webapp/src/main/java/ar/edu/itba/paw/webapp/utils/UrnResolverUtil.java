package ar.edu.itba.paw.webapp.utils;

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
        cutStart();  // removes '/'

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
        cutStart();  // removes '/'

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
        // path ends with {id}
        try {
            return Long.parseLong(this.urn);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    /**
     * Expects a path as "{base_path}/api/exchanges/{id}".
     * @return The expected exchange id. If the URN is not in the expected format, it will return null.
     */
    static public Long getExchangeId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(1);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/users/{id}/locations/{id}".
     * @return The expected location id. If the URN is not in the expected format, it will return null.
     */
     static public Long getLocationId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(3);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/publications/{id}".
     * @return The expected publication id. If the URN is not in the expected format, it will return null.
     */
     static public Long getPublicationId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(1);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/books/{id}".
     * @return The expected book id. If the URN is not in the expected format, it will return null.
     */
    static public Long getBookId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(1);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/users/{id}".
     * @return The expected user id. If the URN is not in the expected format, it will return null.
     */
     static public Long getUserId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(1);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/book_models/{id}".
     * @return The expected book model id. If the URN is not in the expected format, it will return null.
     */
     static public Long getBookModelId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(1);
        return ur.getId();
    }

    /**
     * Expects a path as "{base_path}/api/images/{id}".
     * @return The expected image id. If the URN is not in the expected format, it will return null.
     */
     static public Long getImageId(URI path) {
        UrnResolverUtil ur = new UrnResolverUtil(path.getPath());
        ur.cutBasePath();
        ur.cutUrn(2);
        return ur.getId();
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }


    private void cutBasePath(){
        String api = "/api";
        int index = this.urn.indexOf(api);
        if (index != -1) {
            this.urn = this.urn.substring(index + api.length());
        }
    }

    private void cutStart() {
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