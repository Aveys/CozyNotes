package cpe.lesbarbus.cozynotes.authenticator;

/**
 * Created by arthurveys on 22/12/15.
 */
public interface ServerAuthenticate {
    /**
     * Execute a request to cozycloud instance to get the device password
     * @param pass the password of the instance
     * @param url the url of the instance
     * @return the auth token
     * @throws Exception
     */
    public String userSignIn(final String pass,final String url) throws Exception;
}
