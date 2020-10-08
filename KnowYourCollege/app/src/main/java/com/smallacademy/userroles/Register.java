package com.smallacademy.userroles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName,email,password,phone,code;
    Button registerBtn,goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isTeacherBox;
    CheckBox isStudentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        isTeacherBox=findViewById(R.id.isTeacher);
        isStudentBox=findViewById(R.id.isStudent);

        code=findViewById(R.id.teachercode);


        //Checkboxes Logics

        isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    isTeacherBox.setChecked(false);
                    code.setVisibility(View.INVISIBLE);
                }
            }
        });
        isTeacherBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    isStudentBox.setChecked(false);
                    code.setVisibility(View.VISIBLE);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                //checkbox Validation

                if(!(isTeacherBox.isChecked()||isStudentBox.isChecked()))
                {
                    Toast.makeText(Register.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isTeacherBox.isChecked())
                {
                    if((code.getText().toString()).length()==0)
                    {
                        Toast.makeText(Register.this, "Enter Code!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if((code.getText().toString()).equals("123"))
                    {

                        if (valid) {
                            //start the user registerartion Process
                            fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("FullName", fullName.getText().toString());
                                    userInfo.put("UserEmail", email.getText().toString());
                                    userInfo.put("PhoneNUmber", phone.getText().toString());

                                    //Specify the access level

                                    if(isTeacherBox.isChecked())
                                    {
                                        userInfo.put("isTeacher", "1");
                                    }

                                    if(isStudentBox.isChecked())
                                    {
                                        userInfo.put("isStudent", "1");
                                    }

                                    df.set(userInfo);


                                    if(isTeacherBox.isChecked())
                                    {
                                        startActivity(new Intent(getApplicationContext(), Admin.class));
                                        finish();
                                    }

                                    if(isStudentBox.isChecked())
                                    {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed To Create Account", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }



                    }
                    else
                    {
                        Toast.makeText(Register.this, "Incorrect Code!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                {
                    if (valid) {
                        //start the user registerartion Process
                        fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                DocumentReference df = fStore.collection("Users").document(user.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", fullName.getText().toString());
                                userInfo.put("UserEmail", email.getText().toString());
                                userInfo.put("PhoneNUmber", phone.getText().toString());

                                //Specify the access level

                                if(isTeacherBox.isChecked())
                                {
                                    if((code.getText().toString()).length()==0)
                                    {
                                        Toast.makeText(Register.this, "Enter Code!!", Toast.LENGTH_SHORT).show();
                                    }
                                    if((code.getText().toString()).equals("123"))
                                    {
                                        userInfo.put("isTeacher", "1");
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "Incorrect Code!!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }

                                if(isStudentBox.isChecked())
                                {
                                    userInfo.put("isStudent", "1");
                                }

                                df.set(userInfo);


                                if(isTeacherBox.isChecked())
                                {
                                    startActivity(new Intent(getApplicationContext(), Admin.class));
                                    finish();
                                }

                                if(isStudentBox.isChecked())
                                {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "Failed To Create Account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }




            }

        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}