package com.iecsemanipal.holocaust.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.Result;
import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.models.Attendance;
import com.iecsemanipal.holocaust.models.AttendanceRequest;
import com.iecsemanipal.holocaust.models.ServerResponse;
import com.iecsemanipal.holocaust.network.HolocaustClient;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String API_KEY = "anuragtheboss";
    private ZXingScannerView sv;
    private static final int REQUEST_CAMERA = 316;
    private String eventID;
    private EditText memIDEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Toolbar toolbar = (Toolbar)findViewById(R.id.attendance_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.mark_attendance));
        }

        sv = (ZXingScannerView)findViewById(R.id.attendance_qr_scanner);
        sv.setResultHandler(this);

        memIDEditText = (EditText)findViewById(R.id.mem_id_edit_text);

        FloatingActionButton contButton = (FloatingActionButton)findViewById(R.id.cont_button);

        TextView eventName = (TextView)findViewById(R.id.event_name_text_view);
        TextView eventDate = (TextView)findViewById(R.id.event_date_text_view);

        eventName.setText(getIntent().getStringExtra("name"));
        eventDate.setText(getIntent().getStringExtra("date"));
        eventID = getIntent().getStringExtra("id");

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    hideSoftKeyboard(AttendanceActivity.this);
                    Integer id = Integer.parseInt(memIDEditText.getText().toString());
                    markAttendance(id.toString(), 0);
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void markAttendance(String memID, final int method){
        final ProgressDialog dialog = new ProgressDialog(AttendanceActivity.this);
        dialog.setMessage(getString(R.string.marking_attendance));
        dialog.setCancelable(false);
        dialog.show();

        AttendanceRequest request = new AttendanceRequest();
        request.setApiKey(API_KEY);

        Attendance attendance = new Attendance();
        attendance.setMemID(memID);
        attendance.setEventID(eventID);

        request.setAttendance(attendance);

        Call<ServerResponse> call = HolocaustClient.getHolocaustInterface().markAttendance(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (dialog.isShowing()) dialog.dismiss();
                ServerResponse resp;
                if (method == 1) {
                    sv.stopCamera();
                    sv.startCamera();
                    sv.setResultHandler(AttendanceActivity.this);
                }

                if (response != null && (resp = response.body()) != null) {
                    if (resp.getStatus() == 1) {
                        showAlert(getString(R.string.success), getString(R.string.attendance_marked), ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_success_alert));
                        memIDEditText.setText("");

                    }else if (resp.getStatus() == 7){
                        Log.d("Error", resp.getDescription());
                        showAlert(getString(R.string.error), getString(R.string.duplicate_attendance), ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_error_alert));
                    }else{
                        Log.d("Error", resp.getDescription());
                        showAlert(getString(R.string.error), getString(R.string.attendance_failed), ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_error_alert));
                    }
                }else{
                    showAlert(getString(R.string.error), getString(R.string.attendance_failed), ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_error_alert));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (dialog.isShowing()) dialog.dismiss();
                showAlert(getString(R.string.error), getString(R.string.check_connection), ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_error_alert));
                if (method == 1) {
                    sv.stopCamera();
                    sv.startCamera();
                    sv.setResultHandler(AttendanceActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.link_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onResume() {
        super.onResume();
        sv.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        sv.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        try{
            Integer id = Integer.parseInt(result.getText());
            markAttendance(id.toString(), 1);
        }catch(NumberFormatException e){
            e.printStackTrace();
            new AlertDialog.Builder(AttendanceActivity.this).setMessage(getString(R.string.invalid_qr)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    private void showAlert(String title, String message, Drawable icon){
        if (title != null && message != null && icon != null){
            new AlertDialog.Builder(this).setTitle(title).setMessage(message).setIcon(icon).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.link_flash:
                sv.toggleFlash();
                break;
            case R.id.link_reload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
                sv.stopCamera();
                sv.startCamera();
                sv.setResultHandler(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
