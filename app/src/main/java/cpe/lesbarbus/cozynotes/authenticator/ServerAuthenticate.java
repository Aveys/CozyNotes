package cpe.lesbarbus.cozynotes.authenticator;

/**
 * Created by arthurveys on 22/12/15.
 */
public interface ServerAuthenticate {
    public String userSignIn(final String email,final String pass,final String url) throws Exception;
}
