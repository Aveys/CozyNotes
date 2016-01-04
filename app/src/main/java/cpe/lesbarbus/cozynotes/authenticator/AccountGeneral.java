package cpe.lesbarbus.cozynotes.authenticator;

/**
 * Created by arthurveys on 22/12/15.
 */
public class AccountGeneral {

    // General Strings used for Authenticator
    public static final String ACCOUNT_TYPE="cpe.lesbarbus.cozynotes.auth";

    public static final String ACCOUNT_NAME="Cozynotes";

    public static final String AUTHTOKEN_TYPE_FULL="Full_Access";

    public static final ServerAuthenticate sServerAuth = new CozyServerAuthenticate();
    //DEV : Modify this for having multiple device on one instance of cozycloud
    //TODO : add this in advance parameter at first login or another system
    public static final String DEVICE_NAME = "cozynote-polo";
}
