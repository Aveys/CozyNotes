package cpe.lesbarbus.cozynotes.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import cpe.lesbarbus.cozynotes.activities.LoginActivity;
import static cpe.lesbarbus.cozynotes.authenticator.AccountGeneral.*;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

/**
 * Created by arthurveys on 22/12/15.
 */
public class CozyAuthenticator extends AbstractAccountAuthenticator{

    private final Context appContext;

    public CozyAuthenticator(Context context) {
        super(context);
        this.appContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("CozyAuthenticator", "Add account");

        final Intent intent = new Intent(appContext, LoginActivity.class);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE,accountType);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE,authTokenType);
        intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT,true);
        intent.putExtra(LoginActivity.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT,intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final AccountManager am = AccountManager.get(appContext);
        String authToken = am.peekAuthToken(account,authTokenType);
        if(!TextUtils.isEmpty(authToken)){
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME,account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE,account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN,authToken);
            return result;
        }
        final Intent intent = new Intent(appContext,LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,response);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE,account.type);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE,authTokenType);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_NAME,account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT,intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return AUTHTOKEN_TYPE_FULL;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT,false);
        return result;
    }
}
