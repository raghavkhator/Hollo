package com.iecsemanipal.holocaust.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.activities.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler{
    private ZXingScannerView scannerView;
    private static final int REGISTER_CODE = 1;
    private static final int REQUEST_CAMERA = 316;

    public ScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        scannerView = (ZXingScannerView)view.findViewById(R.id.qr_scanner);
        scannerView.setResultHandler(this);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void handleResult(Result result) {
        try{
            JSONObject obj = new JSONObject(result.getText());
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            intent.putExtra("name", obj.getString("name"));
            intent.putExtra("regNo", obj.getString("regNo"));
            intent.putExtra("phNo", obj.getString("mobile"));
            intent.putExtra("email", obj.getString("email"));
            getActivity().startActivityForResult(intent, REGISTER_CODE);
        }catch(JSONException e) {
            showAlert("", getString(R.string.invalid_qr));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scan_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.scan_flash:
                scannerView.toggleFlash();
                break;
            case R.id.scan_reload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
                scannerView.stopCamera();
                scannerView.startCamera();
                scannerView.setResultHandler(this);
                break;
            case R.id.add_new:
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                getActivity().startActivityForResult(intent, REGISTER_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(String title, String message){
        if (title != null && message != null){
            new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    scannerView.stopCamera();
                    scannerView.startCamera();
                    scannerView.setResultHandler(ScanFragment.this);
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }
}
