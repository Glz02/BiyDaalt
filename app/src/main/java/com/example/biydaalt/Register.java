package com.example.biydaalt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Register extends AppCompatActivity {
    final int[] sc1 = {0,0,0};
    int sco1=0;
    int sco2=0;
    int sco3=0;
    DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReferenceFromUrl("https://biydaalt-8aa32-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText name = findViewById(R.id.name);
        final EditText email = findViewById(R.id.email);
        final EditText mobile = findViewById(R.id.mobile);
        final EditText password = findViewById(R.id.password);
        final EditText conpassword =findViewById(R.id.conpassword);
        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNow = findViewById(R.id.loginNow);





        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameTxt = name.getText().toString();
                final String emailTxt = email.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String passwordTxt= password.getText().toString();
                final String conpasswordTxt= conpassword.getText().toString();
                if(nameTxt.isEmpty() || emailTxt.isEmpty() ||
                        mobileTxt.isEmpty() || passwordTxt.isEmpty() || conpasswordTxt.isEmpty()){
                    Toast.makeText(Register.this, "Please fill all fields",
                            Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(conpasswordTxt)) {
                    Toast.makeText(Register.this, "Password are not matching",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    getSize();
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot
                                                         snapshot) {
                            if(snapshot.hasChild(mobileTxt)){
                                Toast.makeText(Register.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                databaseReference.child("users").child(mobileTxt).child("name").setValue(nameTxt);

                                databaseReference.child("users").child(mobileTxt).child("email").setValue(emailTxt);

                                databaseReference.child("users").child(mobileTxt).child("password").setValue(passwordTxt);
                                for(int i=1;i<=sco1;i++){

                                    databaseReference.child("users").child(mobileTxt).child("score").child("score1").child(String.valueOf(i)).setValue(0);
                                }

                                for(int i=1;i<=sco2;i++){
                                    databaseReference.child("users").child(mobileTxt).child("score").child("score2").child(String.valueOf(i)).setValue(0);
                                }
                                for(int i=1;i<=sco3;i++){

                                    databaseReference.child("users").child(mobileTxt).child("score").child("score3").child(String.valueOf(i)).setValue(0);
                                }

                                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this,
                                        Login.class));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
        });
        loginNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

    }
    private void getSize(){
        databaseReference.child("allquestion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot
                                             snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.child("type1").getChildren()) {
                    sc1[0]++;
                }
                for (DataSnapshot dataSnapshot : snapshot.child("type2").getChildren()) {
                    sc1[1]++;
                }
                for (DataSnapshot dataSnapshot : snapshot.child("type3").getChildren()) {
                    sc1[2]++;
                }
                sco1=sc1[0];
                sco2=sc1[1];
                sco3=sc1[2];

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }



}

