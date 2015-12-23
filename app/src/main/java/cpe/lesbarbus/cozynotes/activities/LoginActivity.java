package cpe.lesbarbus.cozynotes.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;

import static cpe.lesbarbus.cozynotes.authenticator.AccountGeneral.sServerAuth;

public class LoginActivity extends AccountAuthenticatorActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final int REQUEST_SIGNUP = 1;

    public final static String ARG_ACCOUNT_TYPE = "cpe.lesbarbus.cozynotes.auth";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT";

    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String KEY_ACCOUNT_AUTHENTICATOR_RESPONSE = null;

    public final static String PARAM_USER_PASS = "USER_PASS";
    private String mAuthTokenType;

    private AccountManager mAccountManager;

    @Bind(R.id.input_url)
    EditText _urlText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAccountManager = AccountManager.get(getBaseContext());

        mAuthTokenType = "COZYCLOUD TOKEN";

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        //TODO : reactivate validation for prod and validate URL field
        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/
        if(!isOnline()){
            Toast.makeText(this,R.string.no_connection,Toast.LENGTH_LONG).show();
        }
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        final String password = _passwordText.getText().toString();
        final String url = _urlText.getText().toString();

        final String accountType = this.mAuthTokenType;

        // TODO: Implement your own authentication logic here.

        new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {
                Log.d("cozynotes", TAG + " > auth launched");
                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    authtoken = sServerAuth.userSignIn( password, url);
                    Log.d("LoginActivity", "AuthToken retrieved :" + authtoken);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, "Cozynotes");
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, password);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                    e.printStackTrace();
                }
                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                } else
                    onLoginSuccess(intent);
            }
        }.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(Intent intent) {

        Log.d("Cozynotes", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        Log.d("Cozynotes", TAG + "> finishLogin > addAccountExplicitly");
        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        String authtokenType = mAuthTokenType;

        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        mAccountManager.addAccountExplicitly(account, accountPassword, null);
        mAccountManager.setAuthToken(account, authtokenType, authtoken);


        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }

    public boolean validate() {
        boolean valid = true;

        String password = _passwordText.getText().toString();
        //exemple with built-in pattern
        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo !=null && netInfo.isConnected();
    }
}
