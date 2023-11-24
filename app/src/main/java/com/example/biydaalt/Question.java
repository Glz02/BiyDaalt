package com.example.biydaalt;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String answer;
    private String tip;
    public Question(String s,String d,String g){
        setQuestion(s);
        setAnswer(d);
        setTip(g);
    }


    // GETTERS
    String getAnswer(){
        return answer;
    }

    String getQuestion(){
        return question;
    }
    String getTip(){
        return tip;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
