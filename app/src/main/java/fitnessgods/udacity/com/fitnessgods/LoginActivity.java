package fitnessgods.udacity.com.fitnessgods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "FireBaseLog" ;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserInformation;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @BindView(R.id.txt_email) private EditText mEmail;
    @BindView(R.id.txt_password) private EditText mPassword;
    @BindView(R.id.btn_login) private Button mSignIn;
    @BindView(R.id.btn_google) private ImageButton mGoogleSignIn;
    @BindView(R.id.btn_facebook) private ImageButton mFacebookSignIn;
    @BindView(R.id.txt_newmember) private TextView signUp;

    CallbackManager callbackManager;
    private ProgressDialog mProgressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn)
            startActivity(new Intent(LoginActivity.this , MainActivity.class).putExtra("Login","Facebook"));

        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(LoginActivity.this , MainActivity.class).putExtra("Login","Facebook"));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mFacebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating..");

        mUserInformation = mFirebaseDatabase.getReference().child(String.valueOf(R.string.user_info));

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //User is signed in
                    startActivity(new Intent(LoginActivity.this , MainActivity.class).putExtra("Login","Google"));
                }
                else{
                    //User is signed out
                    // startActivity(new Intent(LoginActivity.this , RegistrationActivity.class));
                }
            }
        };

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignup();
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

    }

    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Used for facebook signIn
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //Used for Google signIn
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            mProgressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            mProgressDialog.hide();
                        }
                        else
                        {
                            mProgressDialog.hide();
                            startActivity(new Intent(LoginActivity.this , MainActivity.class).putExtra("Login","Google"));
                        }
                    }
                });
    }

    private void startSignup()
    {
        startActivity(new Intent(LoginActivity.this , RegistrationActivity.class));
    }

    private void startSignIn()
    {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Fields are empty.",Toast.LENGTH_LONG).show();
        }
        else
        {

            mProgressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        mProgressDialog.hide();
                        Toast.makeText(LoginActivity.this,"Problem With Login.",Toast.LENGTH_LONG).show();
                    }
                    else {
                        mProgressDialog.hide();
                        startActivity(new Intent(LoginActivity.this , MainActivity.class).putExtra("Login","FitnessGods"));
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
