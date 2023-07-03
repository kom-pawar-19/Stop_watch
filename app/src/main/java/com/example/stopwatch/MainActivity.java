package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button startButton, stopButton, holdButton, lapButton, resetButton;
    private TextView timerTextView;
    private LinearLayout lapLayout;

    private boolean running;
    private long startTime;
    private long elapsedTime;
    private Handler handler;
    private List<Long> lapTimes;
    private int lapCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        holdButton = findViewById(R.id.holdButton);
        lapButton = findViewById(R.id.lapButton);
        resetButton = findViewById(R.id.resetButton);
        lapLayout = findViewById(R.id.lapLayout);

        // Set initial state
        running = false;
        elapsedTime = 0;
        handler = new Handler();
        lapTimes = new ArrayList<>();
        lapCounter = 1;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    running = true;
                    startTime = SystemClock.elapsedRealtime() - elapsedTime;

                    // Start the stopwatch
                    handler.postDelayed(timerRunnable, 0);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    running = false;

                    // Stop the stopwatch
                    handler.removeCallbacks(timerRunnable);
                    elapsedTime = SystemClock.elapsedRealtime() - startTime;
                }
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    running = false;

                    // Pause the stopwatch
                    handler.removeCallbacks(timerRunnable);
                    elapsedTime = SystemClock.elapsedRealtime() - startTime;
                } else {
                    running = true;
                    startTime = SystemClock.elapsedRealtime() - elapsedTime;

                    // Resume the stopwatch
                    handler.postDelayed(timerRunnable, 0);
                }
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    long lapTime = SystemClock.elapsedRealtime() - startTime;
                    lapTimes.add(lapTime);
                    addLapView(lapTime);
                    lapCounter++;
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                handler.removeCallbacks(timerRunnable);
                elapsedTime = 0;
                startTime = 0;
                lapTimes.clear();
                lapCounter = 1;
                timerTextView.setText("00:00:00:000");
                lapLayout.removeAllViews();
            }
        });
    }

    private void addLapView(long lapTime) {
        TextView lapTextView = new TextView(this);
        lapTextView.setText("Lap " + lapCounter + ": " + formatTime(lapTime));
        lapLayout.addView(lapTextView);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = SystemClock.elapsedRealtime();
            elapsedTime = currentTime - startTime;
            timerTextView.setText(formatTime(elapsedTime));
            handler.postDelayed(this, 1);
        }
    };

    private String formatTime(long time) {
        int hours = (int) (time / 3600000);
        int minutes = (int) ((time / 60000) % 60);
        int seconds = (int) ((time / 1000) % 60);
        int milliseconds = (int) (time % 1000);

        return String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
    }
}




