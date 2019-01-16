package com.example.tongan.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tongan.myapplication.R;

public class AddPostActivity extends AppCompatActivity {
    private String sad_AT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        sad_AT = "sad AT is going to sleep but AT wants to see Kenny and doesn't" +
                "want to sleep alone, cry cry. AT missed Kenny so so so so much t_t" +
                "q_q Q-Q QwQ p_q ;_; T-T TAT ToT QoQ cry cry";

    }
}
