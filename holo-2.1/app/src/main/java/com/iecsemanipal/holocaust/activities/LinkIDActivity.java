package com.iecsemanipal.holocaust.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.Result;
import com.iecsemanipal.holocaust.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LinkIDActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView sv;
    private static final int REQUEST_CAMERA = 316;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_id);

        Toolbar toolbar = (Toolbar)findViewById(R.id.link_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.link_membership_id));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        sv = (ZXingScannerView)findViewById(R.id.id_qr_scanner);
        sv.setResultHandler(this);
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
            String id = result.getText();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            setResult(RESULT_OK, intent);
            finish();
        }catch(NumberFormatException e){
            e.printStackTrace();
            showAlert("", getString(R.string.invalid_qr));
        }
    }

    private void showAlert(String title, String message){
        if (title != null && message != null){
            new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sv.stopCamera();
                    sv.startCamera();
                    sv.setResultHandler(LinkIDActivity.this);
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.link_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
}
