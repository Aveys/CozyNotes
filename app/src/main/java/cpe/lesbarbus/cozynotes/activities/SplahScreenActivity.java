package cpe.lesbarbus.cozynotes.activities;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;

public class SplahScreenActivity extends Activity {

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen);
        //Init of AccountManager
        mAccountManager = AccountManager.get(this);
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL);

    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     *
     * @param accountType the type of account (Cozynotes usually)
     * @param authTokenType the type of auth account (AUTH_FULL since there's only one type of auth)
     */
    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            Log.d("Auth", "GetTokenForAccount Bundle is " + bnd);
                            if (authtoken != null) {
                                Intent i = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }
                , null);
    }

    /**
     * Run on UI and show a Toaster
     * @param msg the message to show
     */
    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
