package com.iecsemanipal.holocaust.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.adapters.PendingAdapter;
import com.iecsemanipal.holocaust.models.Member;
import com.iecsemanipal.holocaust.models.Pending;
import com.iecsemanipal.holocaust.models.RegistrationRequest;
import com.iecsemanipal.holocaust.models.RegistrationResponse;
import com.iecsemanipal.holocaust.network.HolocaustClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {
    private RecyclerView pendingRecyclerView;
    private List<Pending> pendingList;
    private Realm realm;
    private PendingAdapter adapter;
    private TextView totalTextView;
    private static final String EVENT_NAME = "GBM2017";
    private volatile boolean masterFlag;
    private int respCount = 0;
    private TextView noPending;

    public PendingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        pendingRecyclerView = (RecyclerView) view.findViewById(R.id.pending_recycler_view);
        setHasOptionsMenu(true);

        realm = Realm.getDefaultInstance();
        pendingList = realm.copyFromRealm(realm.where(Pending.class).findAllSorted("memID"));
        adapter = new PendingAdapter(getActivity(), pendingList);
        pendingRecyclerView.setAdapter(adapter);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalTextView = (TextView) view.findViewById(R.id.pending_total);
        totalTextView.setText(pendingList.size() + "");

        noPending = (TextView)view.findViewById(R.id.no_pending_layout);
        if (pendingList.size() > 0){
            pendingRecyclerView.setVisibility(View.VISIBLE);
            noPending.setVisibility(View.GONE);
        }else{
            pendingRecyclerView.setVisibility(View.GONE);
            noPending.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pending_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pending_sync:
                if (pendingList.size() == 0){
                    new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.no_pending_2)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                    break;
                }
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_passkey_input, null);
                final EditText et = (EditText)v.findViewById(R.id.passkey_edit_text);
                et.setText("password");
                new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.enter_passkey)).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (et.getText().toString().isEmpty()){
                            new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.empty_passkey_msg)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                            return;
                        }

                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setMessage(getString(R.string.completing_pending));
                        dialog.setCancelable(false);
                        dialog.show();

                        final List<Pending> tempList = new ArrayList<>();
                        masterFlag = true;

                        realm.beginTransaction();
                        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);

                        for (final Pending p : pendingList) {
                            final Member m = new Member(p);
                            RegistrationRequest request = new RegistrationRequest(EVENT_NAME, et.getText().toString(), m);

                            Call<RegistrationResponse> call = HolocaustClient.getHolocaustInterface().createMember(request);
                            call.enqueue(new Callback<RegistrationResponse>() {
                                @Override
                                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                                    RegistrationResponse resp;
                                    if (response != null && (resp = response.body()) != null) {
                                        if (resp.getStatus()) {
                                            realm.where(Pending.class).equalTo("memID", m.getMemID()).findFirst().deleteFromRealm();
                                            m.setTimeStamp(sdf.format(new Date()));
                                            realm.copyToRealm(m);   /**shared preference*/
                                        } else {
                                            tempList.add(p);
                                            masterFlag = false;
                                        }
                                    } else {
                                        tempList.add(p);
                                        masterFlag = false;
                                    }
                                    respCount++;

                                    if (respCount == pendingList.size()) {
                                        realm.commitTransaction();
                                        pendingList.clear();
                                        pendingList.addAll(tempList);
                                        Collections.sort(pendingList, new Comparator<Pending>() {
                                            @Override
                                            public int compare(Pending p1, Pending p2) {
                                                return p1.getMemID().compareTo(p2.getMemID());
                                            }
                                        });

                                        adapter.notifyDataSetChanged();
                                        totalTextView.setText(pendingList.size() + "");

                                        if (pendingList.size() > 0) {
                                            pendingRecyclerView.setVisibility(View.VISIBLE);
                                            noPending.setVisibility(View.GONE);
                                        } else {
                                            pendingRecyclerView.setVisibility(View.GONE);
                                            noPending.setVisibility(View.VISIBLE);
                                        }

                                        if (dialog.isShowing()) dialog.dismiss();

                                        if (masterFlag) {
                                            showAlert(getString(R.string.reg_success), getString(R.string.check_reg), ContextCompat.getDrawable(getActivity(), R.drawable.ic_success_alert));
                                            ((PendingInterface)getActivity()).completePending();
                                        } else {
                                            showAlert(getString(R.string.reg_failure), getString(R.string.still_pending), ContextCompat.getDrawable(getActivity(), R.drawable.ic_error_alert));
                                        }
                                        respCount = 0;
                                    }
                                }

                                @Override
                                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                                    tempList.add(p);
                                    masterFlag = false;
                                    respCount++;

                                    if (respCount == pendingList.size()) {
                                        realm.commitTransaction();
                                        pendingList.clear();
                                        pendingList.addAll(tempList);
                                        Collections.sort(pendingList, new Comparator<Pending>() {
                                            @Override
                                            public int compare(Pending p1, Pending p2) {
                                                return p1.getMemID().compareTo(p2.getMemID());
                                            }
                                        });

                                        adapter.notifyDataSetChanged();
                                        totalTextView.setText(pendingList.size() + "");

                                        if (pendingList.size() > 0) {
                                            pendingRecyclerView.setVisibility(View.VISIBLE);
                                            noPending.setVisibility(View.GONE);
                                        } else {
                                            pendingRecyclerView.setVisibility(View.GONE);
                                            noPending.setVisibility(View.VISIBLE);
                                        }

                                        if (dialog.isShowing()) dialog.dismiss();

                                        showAlert(getString(R.string.reg_failure), getString(R.string.still_pending), ContextCompat.getDrawable(getActivity(), R.drawable.ic_error_alert));
                                        respCount = 0;
                                    }
                                }
                            });
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

                break;
            case R.id.pending_delete_all:
                new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.p_delete_confirmation)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realm.beginTransaction();
                        realm.where(Pending.class).findAll().deleteAllFromRealm();
                        realm.commitTransaction();
                        pendingList.clear();
                        adapter.notifyDataSetChanged();
                        totalTextView.setText("0");
                        pendingRecyclerView.setVisibility(View.GONE);
                        noPending.setVisibility(View.VISIBLE);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(String title, String message, Drawable icon){
        if (title != null && message != null && icon != null){
            new AlertDialog.Builder(getActivity()).setIcon(icon).setTitle(title).setMessage(message).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    public interface PendingInterface{
        void completePending();
    }
}
