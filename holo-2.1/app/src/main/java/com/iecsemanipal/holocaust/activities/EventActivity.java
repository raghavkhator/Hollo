package com.iecsemanipal.holocaust.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;

public class EventActivity extends AppCompatActivity {
    private static final int MARK_ATTENDANCE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar)findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton attendanceFAB = (FloatingActionButton)findViewById(R.id.add_attendance_fab);

        TextView date = (TextView)findViewById(R.id.date);
        TextView time = (TextView)findViewById(R.id.time);
        TextView venue = (TextView)findViewById(R.id.venue);
        TextView description = (TextView)findViewById(R.id.description);
        TextView category = (TextView)findViewById(R.id.category);
        TextView audience = (TextView)findViewById(R.id.audience_type);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        }

        Intent intent = getIntent();
        date.setText(intent.getStringExtra("date"));
        time.setText(intent.getStringExtra("time"));
        venue.setText(intent.getStringExtra("venue"));
        description.setText(intent.getStringExtra("description"));
        category.setText(intent.getStringExtra("category"));
        audience.setText(intent.getStringExtra("audience"));

        attendanceFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, AttendanceActivity.class);
                startActivityForResult(intent, MARK_ATTENDANCE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MARK_ATTENDANCE_CODE){
            if (resultCode == RESULT_OK && data != null){
                //Add network call to mark attendance
            }
        }
    }
}
