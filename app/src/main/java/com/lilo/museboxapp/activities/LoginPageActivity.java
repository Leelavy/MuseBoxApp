package com.lilo.museboxapp.activities;

import com.google.firebase.auth.FirebaseAuth;
import com.lilo.museboxapp.R;
import com.lilo.museboxapp.Utils;
import com.lilo.museboxapp.model.ModelFirebase;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginPageActivity extends AppCompatActivity {

    ImageView backgroundImageView;
    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    Button registerBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();

        this.setTitle("Login");

        backgroundImageView = findViewById(R.id.login_activity_background_image_view);
        emailInput = findViewById(R.id.login_activity_email_edit_text);
        passwordInput = findViewById(R.id.login_activity_password_edit_text);

        registerBtn = findViewById(R.id.login_activity_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterPage();
            }
        });

        loginBtn = findViewById(R.id.login_activity_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelFirebase.loginUser(emailInput.getText().toString(), passwordInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(LoginPageActivity.this, HomeActivity.class));
                        LoginPageActivity.this.finish();
                    }
                    @Override
                    public void onFail() {
                    }
                });
            }
        });

        Utils.animateBackground(backgroundImageView, 30000);

    }

    private void toRegisterPage(){
        Intent intent = new Intent(this, RegisterPageActivity.class);
        startActivity(intent);
    }

}

