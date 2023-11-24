package com.example.biydaalt;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://biydaalt-8aa32-default-rtdb.firebaseio.com/");
    ListView listView;


    ArrayList<Question> QList;
    ArrayList<Integer> newScore = new ArrayList<>();
    ArrayList<String> client = new ArrayList<>();
    ArrayList<Integer> score = new ArrayList<>();
    ArrayList<Integer> scoreOne = new ArrayList<>();
    ArrayList<Integer> scoreTwo = new ArrayList<>();
    ArrayList<Integer> scoreThree = new ArrayList<>();
    LinearLayout lay;
    String phone;
    String a = "type1";
    String Qname = "Алдартан";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lay = (LinearLayout) findViewById(R.id.baseLayout);
        String phone1=getIntent().getStringExtra("phone");
        String listview=(String)getIntent().getSerializableExtra("listview");
        String qn=(String)getIntent().getSerializableExtra("Qname");
        Log.d("test",(String)getIntent().getSerializableExtra("phone"));

        phone=phone1;

        if(listview!=null) {
            a = listview;
        }
        if(qn!=null) {
            Qname = qn;
        }
        Log.d("aaaaaa",phone1);
        getData();


    }
    private ArrayList<Integer> getScore(String type){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            int cnt1=1;
            int cnt2=1;
            int cnt3=1;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("aaaaaa12",phone);
                if (phone != null) {
                    DataSnapshot phoneSnapshot = snapshot.child(phone).child("score").child("score1");
                    for (DataSnapshot dataSnapshot : phoneSnapshot.getChildren()) {
                        Integer score1 = dataSnapshot.child(String.valueOf(cnt1)).getValue(Integer.class);
                        cnt1++;
                        scoreOne.add(score1);
                    }
                    DataSnapshot phoneSnapshot1 = snapshot.child(phone).child("score").child("score2");
                    for (DataSnapshot dataSnapshot : phoneSnapshot1.getChildren()) {
                        Integer score2 = dataSnapshot.child(String.valueOf(cnt2)).getValue(Integer.class);
                        cnt2++;
                        scoreOne.add(score2);
                    }
                    DataSnapshot phoneSnapshot2 = snapshot.child(phone).child("score").child("score3");
                    for (DataSnapshot dataSnapshot : phoneSnapshot2.getChildren()) {
                        Integer score3 = dataSnapshot.child(String.valueOf(cnt3)).getValue(Integer.class);
                        cnt3++;
                        scoreOne.add(score3);
                    }


                    if (type.equals("АЛДАРТАН")) {
                        score = scoreOne;
                    } else if (type.equals("БӨХ")) {
                        score = scoreTwo;
                    } else if (type.equals("МОРЬ")) {
                        score = scoreThree;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return score;
    }

    private void getData() {
        databaseReference.child("allquestion").child(a).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QList = new ArrayList<>();

                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Question question = new Question(
                                dataSnapshot.child("question").getValue(String.class),
                                dataSnapshot.child("answer").getValue(String.class),
                                dataSnapshot.child("tip").getValue(String.class));
                        QList.add(question);


                    }
                    getScore(Qname);

                    for (int i = 1; i <= QList.size(); i++) {
                        client.add(Qname + " " + i + "     Авсан оноо:" + String.valueOf(score.get(i - 1)));

                    }


//                    Question result = (Question) getIntent().getSerializableExtra("score");
//                    ArrayList<Integer> scoreSave = (ArrayList) getIntent().getSerializableExtra("scoreSave");
//
//
//                    if (result != null) {
//                        int a = 1;
//                        newScore = scoreSave;
//                        for (Question j : QList) {
//
//                            if (j.getQuestion().equals(result.getQuestion())) {
//                                newScore.set(a - 1, 10);
//                            }
//                            client.set(a - 1, "Даалгавар" + " " + a + "     Авсан оноо:" + String.valueOf(newScore.get(a - 1)));
//                            a++;
//                        }
//                        score = newScore;
//
//
//                    }
                    listView = (ListView) findViewById(R.id.listView1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(List.this, android.R.layout.simple_list_item_1, client);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            Question selectedQuestion = QList.get(arg2);


                            // Create an Intent to start the Play activity
                            Intent intent = new Intent(List.this, Play.class);

                            // Pass the selected question to the Play activity using putExtra
                            intent.putExtra("selectedQuestion", selectedQuestion);
                            intent.putExtra("score", score);
                            intent.putExtra("listview", a);
                            intent.putExtra("Qname", Qname);
                            intent.putExtra("QList", QList);


                            // Start the Play activity
                            startActivity(intent);

                            // Finish the current activity if needed
                            finish();
                        }

                    });
                } catch (Exception e) {
                    Log.e("FirebaseData", "Error processing question: " + e.getMessage());
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(List.this, "Холболт амжилтгүй", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        client.clear();
        if (item.getItemId() == R.id.menu_aldartan) {
            a = "type1";
            Qname="Алдартан";
        } else if (item.getItemId() == R.id.menu_boh) {
            a = "type2";
            Qname="Бөх";
        } else if (item.getItemId() == R.id.menu_mori) {
            a = "type3";
            Qname="Морь";
        }
        getData();
        return false;
    }

}
