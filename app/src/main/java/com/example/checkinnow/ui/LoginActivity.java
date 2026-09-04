package com.example.checkinnow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checkinnow.MainActivity;
import com.example.checkinnow.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent>
            googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getData() == null) {
                            return;
                        }

                        try {

                            com.google.android.gms.auth.api.signin
                                    .GoogleSignInAccount account =
                                    GoogleSignIn
                                            .getSignedInAccountFromIntent(
                                                    result.getData()
                                            )
                                            .getResult(
                                                    ApiException.class
                                            );

                            if (account == null) {
                                return;
                            }

                            AuthCredential credential =
                                    GoogleAuthProvider
                                            .getCredential(
                                                    account.getIdToken(),
                                                    null
                                            );

                            firebaseAuth.signInWithCredential(
                                    credential
                            ).addOnCompleteListener(
                                    this,
                                    task -> {

                                        if (task.isSuccessful()) {

                                            FirebaseUser user =
                                                    firebaseAuth
                                                            .getCurrentUser();

                                            if (user != null) {

                                                Toast.makeText(
                                                        this,
                                                        "Đăng nhập thành công",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                moMainActivity();
                                            }

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Đăng nhập thất bại",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                            );

                        } catch (ApiException e) {

                            Toast.makeText(
                                    this,
                                    "Không thể đăng nhập Google",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_login
        );

        firebaseAuth =
                FirebaseAuth.getInstance();

        cauHinhGoogle();

        Button btnDangNhapGoogle =
                findViewById(
                        R.id.btnDangNhapGoogle
                );

        btnDangNhapGoogle.setOnClickListener(
                v -> dangNhapGoogle()
        );
    }

    private void cauHinhGoogle() {

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        .requestIdToken(
                                getString(
                                        R.string.default_web_client_id
                                )
                        )
                        .requestEmail()
                        .build();

        googleSignInClient =
                GoogleSignIn.getClient(
                        this,
                        gso
                );
    }

    private void dangNhapGoogle() {

        Intent signInIntent =
                googleSignInClient
                        .getSignInIntent();

        googleSignInLauncher.launch(
                signInIntent
        );
    }

    private void moMainActivity() {

        Intent intent =
                new Intent(
                        this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}