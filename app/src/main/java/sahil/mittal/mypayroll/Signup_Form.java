package sahil.mittal.mypayroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Form extends AppCompatActivity {
    // Declaring vriables.
    private EditText textName,textEmail,textPassword,textConfirm,textcontact;
    private Button btnRegister;
    private ProgressBar processBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);


        // Intialising ids.
        textName=findViewById(R.id.text_name);
        textEmail=findViewById(R.id.text_email);
        textPassword=findViewById(R.id.text_password);
        textConfirm=findViewById(R.id.text_confirm);
        textcontact=findViewById(R.id.text_contact);
        btnRegister=findViewById(R.id.btn_register);
        processBar=findViewById(R.id.process_bar);


        //creating the instance of class FirebaseAuth.
        firebaseAuth=FirebaseAuth.getInstance();
        databaseRefrence= FirebaseDatabase.getInstance().getReference("Admins");


        //aleready login or already have an account
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),admin_panel.class));
            finish();
        }

        //Getting the values from the input fields.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String name=textName.getText().toString().trim();
                final  String email=textEmail.getText().toString().trim();
                final  String password=textPassword.getText().toString().trim();
                String confirm=textConfirm.getText().toString().trim();
                final String contact=textcontact.getText().toString().trim();


                //Validating the input fields by textUtils class.
                if(TextUtils.isEmpty(name)){
                    textName.setError("Please enter name.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    textEmail.setError("Please enter email.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    textPassword.setError("Please choose password");
                    return;
                }
                if(TextUtils.isEmpty(confirm)){
                    textConfirm.setError("Please confirm password.");
                    return;
                }
                if(TextUtils.isEmpty(contact)){
                    textcontact.setError("Please enter contact no..");
                    return;
                }
                if(password.length()<6){
                    textPassword.setError("Password too short (min 6).");
                    return;
                }


                // Progress bar will start .
                processBar.setVisibility(View.VISIBLE);


                //Finally doing Aunthentication with email and password.
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        processBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Admins_info info=new Admins_info(
                                  name,
                                  email,
                                  contact
                            );


                            //Storing the data after passing the values to the constructer object.
                            FirebaseDatabase.getInstance().getReference("Admins")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Signup_Form.this, "User register sucessfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login_Form.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(Signup_Form.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void login_text(View view) {
        startActivity(new Intent(getApplicationContext(),Login_Form.class));
    }
}
