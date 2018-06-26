package org.bitm.pencilbox.firebaseauthpb5;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this,DatabaseActivity.class));
        }
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        /*startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                123);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            if(resultCode == RESULT_OK){
                user = FirebaseAuth.getInstance().getCurrentUser();
                ((TextView)findViewById(R.id.infoTV)).setText(user.getDisplayName());
                startActivity(new Intent(MainActivity.this,DatabaseActivity.class));
            }
        }
    }

    public void createNewUser(View view) {
        String email = ((EditText)findViewById(R.id.emailET)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passET)).getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user = firebaseAuth.getCurrentUser();
                        String mail  = user.getEmail();
                        Log.e(TAG, "signed in as : "+mail);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    public void loginUser(View view) {
        String email = ((EditText)findViewById(R.id.emailET)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passET)).getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user = firebaseAuth.getCurrentUser();
                        String mail  = user.getEmail();
                        Log.e(TAG, "signed in as : "+mail);
                        startActivity(new Intent(MainActivity.this,DatabaseActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    public void logoutUser(View view) {
        if(user != null){
            firebaseAuth.signOut();
        }
    }

    public void updateUser(View view) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Donald Trump")
                .setPhotoUri(Uri.parse("https://www.telegraph.co.uk/content/dam/news/2017/11/22/TELEMMGLPICT000147365976_trans_NvBQzQNjv4Bq3XmyF3YIL3K1caQxZsZv2Ssm-UOV8_Q90I8_c5Af0yY.jpeg"))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
                        updateUI();
                    }
                });
    }

    private void updateUI() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            ((TextView)findViewById(R.id.infoTV)).setText(user.getDisplayName());
            ImageView iv = findViewById(R.id.imageView);
            Picasso.get().load(user.getPhotoUrl()).into(iv);
        }
    }
}
