package ps.borsuquesquad.pl.allinone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Facebook extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager callbackManager;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fb_load_page);
        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.button2);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Facebook.this, "Log in failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Facebook.this, "Failed to LogIn", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Facebook.this, UserPage.class));
                            Toast.makeText(Facebook.this, "Wellcome "+user.getDisplayName(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Facebook.this, "Failed to log in", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}