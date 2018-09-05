package com.iecsemanipal.holocaust.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.models.Member;
import com.iecsemanipal.holocaust.models.Pending;
import com.iecsemanipal.holocaust.models.RegistrationRequest;
import com.iecsemanipal.holocaust.models.RegistrationResponse;
import com.iecsemanipal.holocaust.network.HolocaustClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private static final int ID_LINK_CODE = 0;
    private static final String EVENT_NAME = "GBM2018";
    private static final int RESULT_NONE = 2;
    private Realm realm;
    private TextInputEditText idEditText;
    private TextInputEditText name;
    private TextInputEditText regNo;
    private TextInputEditText contact;
    private TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        realm = Realm.getDefaultInstance();

        final Toolbar toolbar = (Toolbar)findViewById(R.id.reg_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.register_member));
        }

        idEditText = (TextInputEditText)findViewById(R.id.id_edit_text);
        name = (TextInputEditText)findViewById(R.id.name_edit_text);
        regNo = (TextInputEditText)findViewById(R.id.reg_edit_text);
        contact = (TextInputEditText)findViewById(R.id.contact_edit_text);
        email = (TextInputEditText)findViewById(R.id.email_edit_text);

        Button registerButton = (Button)findViewById(R.id.register_button);

        ImageView scanQR = (ImageView)findViewById(R.id.id_scan_qr);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        regNo.setText(intent.getStringExtra("regNo"));
        contact.setText(intent.getStringExtra("phNo"));
        email.setText(intent.getStringExtra("email"));

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LinkIDActivity.class);
                startActivityForResult(intent, ID_LINK_CODE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            register();
            }
        });
    }

    private void register(){
        if (name.getText().toString().isEmpty() || regNo.getText().toString().isEmpty() ||
                contact.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || idEditText.getText().toString().isEmpty()){
            new AlertDialog.Builder(RegistrationActivity.this).setMessage(getString(R.string.enter_all_details)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

        }else{
            View v = LayoutInflater.from(RegistrationActivity.this).inflate(R.layout.fragment_passkey_input, null);
            final EditText et = (EditText)v.findViewById(R.id.passkey_edit_text);

            new AlertDialog.Builder(RegistrationActivity.this).setTitle(getString(R.string.enter_passkey)).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (et.getText().toString().isEmpty()){
                        new AlertDialog.Builder(RegistrationActivity.this).setMessage(getString(R.string.empty_passkey_msg)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                        return;
                    }

                    final ProgressDialog dialog = new ProgressDialog(RegistrationActivity.this);
                    dialog.setMessage(getString(R.string.registering_member));
                    dialog.setCancelable(false);
                    dialog.show();

                    final Member member = new Member();
                    member.setName(name.getText().toString());
                    member.setMemID(idEditText.getText().toString());
                    member.setContactNo(contact.getText().toString());
                    member.setEmail(email.getText().toString());
                    member.setRegNo(regNo.getText().toString());

                    RegistrationRequest request = new RegistrationRequest(EVENT_NAME, et.getText().toString(), member);

                    Call<RegistrationResponse> call = HolocaustClient.getHolocaustInterface().createMember(request);
                    call.enqueue(new Callback<RegistrationResponse>(){
                        @Override
                        public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                            if (dialog.isShowing()) dialog.dismiss();
                            RegistrationResponse resp;
                            if (response != null && (resp = response.body()) != null){
                                if (resp.getStatus()){
                                    realm.beginTransaction();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
                                    member.setTimeStamp(sdf.format(new Date()));
                                    realm.copyToRealm(member);

                                    try {
                                        realm.where(Pending.class).equalTo("memID", member.getMemID()).findFirst().deleteFromRealm();
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }
                                    realm.commitTransaction();

                                    setResult(RESULT_OK);
                                    showAlert(getString(R.string.reg_success), getString(R.string.check_reg), ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.ic_success_alert), true);
                                }else{
                                    showAlert(getString(R.string.reg_failure), getString(R.string.error_occured), ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.ic_error_alert), false);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            if (dialog.isShowing()) dialog.dismiss();
                            realm.beginTransaction();
                            Pending pending = new Pending(member);
                            realm.where(Pending.class).equalTo("memID", pending.getMemID()).findAll().deleteAllFromRealm();
                            realm.copyToRealm(pending);
                            realm.commitTransaction();
                            setResult(RESULT_CANCELED);
                            showAlert(getString(R.string.registration_pending), getString(R.string.check_connection), ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.ic_pending_alert), true);
                        }
                    });
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void showAlert(String title, String message, Drawable icon, final boolean fin){
        if (title != null && message != null && icon != null){
            new AlertDialog.Builder(this).setIcon(icon).setTitle(title).setMessage(message).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (fin) finish();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (fin) finish();
                }
            }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && requestCode == ID_LINK_CODE){
            String id = data.getStringExtra("id");
            idEditText.setText(id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_NONE);
        super.onBackPressed();
    }
}
