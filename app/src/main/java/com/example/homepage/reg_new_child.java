package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class reg_new_child extends AppCompatActivity {

    String selectedGender;
    FirebaseFirestore db;
    Button back;
    EditText nameEditText;
    EditText emailEditText;
    EditText ageEditText;
    RadioGroup genderRadioGroup;
    RadioButton boyRadioButton;
    RadioButton girlRadioButton;
    Button datePickerButton;
    TextView dobTextView;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;
    String token;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_new_child);
        db = FirebaseFirestore.getInstance();
        // Initialize XML elements
        back = findViewById(R.id.back_button);
        nameEditText = findViewById(R.id.textbox_name);
        emailEditText = findViewById(R.id.textbox_email);
        ageEditText = findViewById(R.id.textbox_age);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        boyRadioButton = findViewById(R.id.radioButtonBoy);
        girlRadioButton = findViewById(R.id.radioButtonGirl);
        datePickerButton = findViewById(R.id.datePickerButton);
        dobTextView = findViewById(R.id.dobTextView);
        passwordEditText = findViewById(R.id.password_child);
        confirmPasswordEditText = findViewById(R.id.confirmpassword_child);
        registerButton = findViewById(R.id.loadDB_button);

        String parentId = SPUMaster.getParentId(getApplicationContext());
        emailEditText.setText(parentId);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d("token", ""+token);
                    }
                });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected RadioButton from the genderRadioGroup
                int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    selectedGender = selectedRadioButton.getText().toString();
                } else {
                    Toast.makeText(reg_new_child.this, "Choose Gender", Toast.LENGTH_SHORT).show();
                }

                // Rest of your registration logic
                // You can use the selectedGender variable here
                String childName = nameEditText.getText().toString();
//                String email = emailEditText.getText().toString();
                String age = ageEditText.getText().toString();
//                String dateOfBirth = dobTextView.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String parentId = SPUMaster.getParentId(getApplicationContext());
                int childCount = SPUChildSupport.getChildCount(getApplicationContext());
                if(childCount==1)
                {
                    Toast.makeText(reg_new_child.this, "1 Child limit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MCFChildRegister data=new MCFChildRegister(token,childName,parentId,age,selectedGender,password);
                    db.collection("child_details").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Child details uploaded", " ", task.getException());
                                Toast.makeText(reg_new_child.this, childName+" has been registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(reg_new_child.this, home.class);
                                startActivity(intent);
                            } else {
                                Log.d("Child upload problem", " ", task.getException());
                            }
                        }
                    });
                }
            }
        });

        back=findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reg_new_child.this, GeneralActivity.class);
                startActivity(intent);
            }
        });

    }
}