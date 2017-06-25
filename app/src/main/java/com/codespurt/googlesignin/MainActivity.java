package com.codespurt.googlesignin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private final int RC_SIGN_IN = 1001;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signIn;
    private GoogleSignInAccount acct;

    private ImageView userImage;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (SignInButton) findViewById(R.id.btn_google_sign_in);
        userImage = (ImageView) findViewById(R.id.img_profile);
        userEmail = (TextView) findViewById(R.id.tv_email);

        signIn.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        signIn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        signIn.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_google_sign_in:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_SIGN_IN:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                    break;
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI
            acct = result.getSignInAccount();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if (b) {
            String personId = acct.getId();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
        } else {
            Toast.makeText(this, "Unable to sign in", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}