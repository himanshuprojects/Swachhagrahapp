package com.example.swachhagrahmobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, cityname, email, password, adhaar;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        cityname = findViewById(R.id.cityname);
        adhaar=findViewById(R.id.adhaar);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        //For action performs when Register button clicked..
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String str_username = username.getText().toString();
                String str_cityname = cityname.getText().toString();
                String str_adhaar=adhaar.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();
                //check everything is fine or not.. its an validation part
                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_cityname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                }
                //when everything is fine call register function
                else {
                    register(str_username, str_cityname,str_adhaar, str_email, str_password);
                }
            }
        });

        //For action perform on pressing ""if already registered? Press login"" then goto login activity..
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public void register(final String username, final String cityname, final String adhaar, String email, String password)
    {
        //Firebase predefined authentication function will carry out tasks to register user..
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {//if signup is successfull get that user id
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            //Now we have to store his detail in firebase Database..under node USERS using hashing
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("username", username);
                            map.put("cityname", cityname);
                            map.put("adhaar",adhaar);
                            map.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/swachhagrahmobileapp.appspot.com/o/placeholder.png?alt=media&token=bff082fd-8914-4aa8-9fa1-88dba7041314");
                            map.put("bio", "");


                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        //now after successfully writing into database..pleasewait cycle will be stoped and mainactivity will launched..
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}