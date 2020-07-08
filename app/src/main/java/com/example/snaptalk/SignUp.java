package com.example.snaptalk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private Button cont1;
    private EditText Email, Pass , Name , Dob;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged( FirebaseAuth firebaseAuth)
            {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        };
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        cont1 = (Button) findViewById(R.id.Scont);
        Email = (EditText) findViewById(R.id.supEmail);
        Pass = (EditText) findViewById(R.id.supPass);
        Name = (EditText) findViewById(R.id.supName);
        Dob = (EditText) findViewById(R.id.supDob);


        cont1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                final String email = Email.getText().toString();
                final String password = Pass.getText().toString();
                final String name = Name.getText().toString();
                final RadioButton mRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                final String sex = mRadioButton.getText().toString();
                final String dob = Dob.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "sign up error", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("dob",dob );
                            userInfo.put("gender", sex);
                            currentUserDb.updateChildren(userInfo);

                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
