package son.nt.en.login;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.HomeActivity;
import son.nt.en.R;
import son.nt.en.base.BaseActivity;
import son.nt.en.google_client_api.DaggerGoogleApiComponent;
import son.nt.en.google_client_api.GoogleApiClientModule;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity
        implements OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String REQUEST_START = "REQUEST_START";

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.sign_in_button)
    SignInButton mSignInButton;

    @BindView(R.id.login_welcome)
    TextView mTxtWelcome;
    @BindView(R.id.login_skip)
    TextView mTxtSkip;

    @Inject
    FirebaseAuth mFirebaseAuth;
    @Inject
    FirebaseUser mFirebaseUser;

    @Inject
    GoogleApiClient mGoogleApiClient;


    private boolean isNotStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        isNotStart = getIntent().getBooleanExtra("REQUEST_START", false);

        //inject
        GoogleApiClientModule googleApiClientModule = new GoogleApiClientModule(this, getString(R.string.default_web_client_id), this);
        DaggerGoogleApiComponent.builder().googleApiClientModule(googleApiClientModule).build().inject(this);

        // Set up the login form.
        mTxtWelcome.setText(getString(R.string.welcome_to, getString(R.string.app_name)));

        mSignInButton.setOnClickListener(this);
        mTxtSkip.setOnClickListener(this);
        if (mFirebaseUser != null) {
            moveToHome();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            }
            case R.id.login_skip: {
                moveToHome();

                break;
            }

        }
    }

    private void moveToHome() {
        Toast.makeText(this, "Hello " + mFirebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    if (isNotStart) {
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        moveToHome();
                        finish();
                    }

                }

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Do nothing

    }


}
