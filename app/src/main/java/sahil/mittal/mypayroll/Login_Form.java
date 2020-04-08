package sahil.mittal.mypayroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Form extends AppCompatActivity {
    private EditText textEmail,textPassword;
    private Button btnLogin;
    private FirebaseAuth fireAuth;
    private ProgressBar processBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);


        //Intialising Ids
        textEmail=findViewById(R.id.text_email);
        textPassword=findViewById(R.id.text_password);
        btnLogin=findViewById(R.id.btn_login);
        processBar=findViewById(R.id.process_bar);


        //Creating  instance of the firebase aunthetication.
        fireAuth=FirebaseAuth.getInstance();


        //click the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting the values from the fields
                final String email=textEmail.getText().toString().trim();
                final String password=textPassword.getText().toString().trim();


                //validating the user inputs
                if(TextUtils.isEmpty(email)){
                    textEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    textPassword.setError("Password is required.");
                    return;
                }


                //progress bar visisblity on
                processBar.setVisibility(View.VISIBLE);


                //login by registered email id
                fireAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        processBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(Login_Form.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),admin_panel.class));
                        }else{
                            Toast.makeText(Login_Form.this, "Login Failed, "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    public void sign_text(View view) {
        startActivity(new Intent(getApplicationContext(),Signup_Form.class));
    }
}
