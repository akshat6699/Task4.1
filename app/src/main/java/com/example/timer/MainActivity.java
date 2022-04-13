package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;

    private TextView counter, head;
    private EditText subject;
    private ImageButton start, pause, stop;
    private boolean running;
    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        head = findViewById(R.id.head);
        counter = findViewById(R.id.text_view);
        start = findViewById(R.id.but_start);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.but_stop);
        subject = findViewById(R.id.subject);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        SharedPreferences preferences = getSharedPreferences("MySharedPref", Context.MODE_MULTI_PROCESS);
        int seconds1 = preferences.getInt("seconds", 0);
        String sub = preferences.getString("subject", ".....");
        int hours = seconds1 / 3600;
        int minutes = (seconds1 % 3600) / 60;
        int secs = seconds1 % 60;

        String time
                = String
                .format(Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs);
        head.setText("You spent " + time + " on " + sub + " last time");

        runTimer();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (subject.getText().toString().equalsIgnoreCase("")) {
                    subject.setError("Please enter a subject name");
                    Toast.makeText(getApplicationContext(), "You did not enter a subject.", Toast.LENGTH_SHORT).show();
                }
                else {
                    running = false;
                    SharedPreferences preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putInt("seconds", seconds);
                    edit.putString("subject", subject.getText().toString());
                    edit.apply();
                    seconds = 0;
                    subject.setText("");
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle saved) {
        super.onSaveInstanceState(saved);
        saved.putInt("seconds", seconds);
        saved.putBoolean("running", running);
        saved.putBoolean("wasRunning", wasRunning);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    private void runTimer()
    {

        final TextView timeView = (TextView)findViewById(R.id.text_view);

        final Handler handler = new Handler();


        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}