package ar.edu.itba.paw.utils;

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
        cutUrn();  // removes '/'

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
        cutUrn();  // removes '/'

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

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    private void cutUrn() {
        if(urn.startsWith("/")) {
            this.urn = this.urn.substring(1);
        }
    }
}