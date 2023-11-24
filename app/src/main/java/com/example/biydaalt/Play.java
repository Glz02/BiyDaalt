// Type1.java
package com.example.biydaalt;

import static java.lang.Character.toLowerCase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Play extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://biydaalt-8aa32-default-rtdb.firebaseio.com/");
    private ProgressDialog progressDialog;

    private Bitmap bitmap = null;
    String ScoreType;
    String upScore;
    Question selectedQuestion = (Question) getIntent().getSerializableExtra("selectedQuestion");
            ArrayList<Integer> scoreSave = (ArrayList) getIntent().getSerializableExtra("score");
    //menuger songosong dahin haruulah
    String listview=(String)getIntent().getSerializableExtra("listview");
    String Qname=(String)getIntent().getSerializableExtra("Qname");
    String phone=(String)getIntent().getSerializableExtra("phone");
    ArrayList<Question> QList=(ArrayList<Question>) getIntent().getSerializableExtra("QList");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        final Button enterBtn = findViewById(R.id.enterBtn);
        final Button backBtn = findViewById(R.id.backBtn);
        final Button tipBtn = findViewById(R.id.tipBtn);
        final EditText answer = findViewById(R.id.hariult);
        final TextView question=findViewById(R.id.asuult);
        question.setText(selectedQuestion.getQuestion());


        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String answerTxt = answer.getText().toString();
                if(answerTxt.isEmpty()){
                    Toast.makeText(Play.this, "Хариултаа оруулна уу", Toast.LENGTH_SHORT).show();
                }else{

                    if(selectedQuestion.getAnswer().toLowerCase().equals(answerTxt.toLowerCase())){
                        Toast.makeText(Play.this, "Зөв хариуллаа", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Play.this, List.class);
//                        intent.putExtra("score", selectedQuestion);
//                        intent.putExtra("scoreSave", scoreSave);
                        intent.putExtra("listview",listview);
                        intent.putExtra("Qname",Qname);
                        updateScore();
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(Play.this, "Хариулт буруу", Toast.LENGTH_SHORT).show();
                    }
//
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Play.this, List.class);
                intent.putExtra("listview",listview);
                intent.putExtra("Qname",Qname);

                startActivity(intent);
                finish();
            }
        });

        tipBtn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
//                    checkInternetConenction();
                    downloadImage(selectedQuestion.getTip());
                }
        });

    }
    private void updateScore(){
        int a = 1;
                        for (Question j : QList) {

                            if (j.getQuestion().equals(selectedQuestion.getQuestion())) {
                                upScore=String.valueOf(a);
                            }
                            a++;
                        }
         scoreType();
        databaseReference.child("users").child(phone).child("score").child(ScoreType).child(upScore).setValue(0);

    }
    private String scoreType(){


        if(Qname.equals("АЛДАРТАН")){
            ScoreType="score1";
        }else if(Qname.equals("БӨХ")){
            ScoreType="score2";
        }else if(Qname.equals("МОРЬ")){
            ScoreType="score3";
        }
        return ScoreType;
    }

//image
private void downloadImage(String urlStr) {

    final String url = urlStr;
    new Thread() {
        public void run() {
            InputStream in = null;
            Message msg = Message.obtain();
            msg.what = 1;
            try {
                in = openHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(in);
                Bundle b = new Bundle();
                b.putParcelable("bitmap", bitmap);
                msg.setData(b); in.close();
            }catch (IOException e1) {
                e1.printStackTrace();
            }
            messageHandler.sendMessage(msg);
        }
    }.start();
}
    private InputStream openHttpConnection (String urlStr){
        InputStream in = null;
        int resCode = -1; try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET"); httpConn.connect();
            resCode = httpConn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace(); } catch
        (IOException e) {
            e.printStackTrace(); }
        return in;
    }
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            View dialogView = (View)
                    View.inflate(Play.this, R.layout.dialog, null);
            AlertDialog.Builder dbl = new
                    AlertDialog.Builder(Play.this);
            ImageView ivPoster = (ImageView)
                    dialogView.findViewById(R.id.ivPoster);
            ivPoster.setImageBitmap(bitmap);
            dbl.setTitle("Миний зураг");
//            dbl.setIcon(R.drawable.bird2);
            dbl.setView(dialogView);
            dbl.setNegativeButton("Хаах", null);
            dbl.show();
        }
    };


//    private boolean checkInternetConenction () {
//// Connectivity Manager объектыг зарлах
//        ConnectivityManager connec = (ConnectivityManager)
//                getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
//// сүлжээний холболтыг шалгах
//        if (connec.getNetworkInfo(0).getState() ==
//                android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(0).getState() ==
//                android.net.NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState()
//                == android.net.NetworkInfo.State.CONNECTING ||
//                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
//            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
//            return true;
//        } else if (
//                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
//                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
//            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        return false;
//    }
}
