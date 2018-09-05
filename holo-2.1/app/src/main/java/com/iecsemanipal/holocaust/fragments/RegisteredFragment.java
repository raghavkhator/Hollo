package com.iecsemanipal.holocaust.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.adapters.RegisteredAdapter;
import com.iecsemanipal.holocaust.models.Member;

import java.util.List;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisteredFragment extends Fragment {
    private RecyclerView regRecyclerView;
    private List<Member> membersList;
    private Realm realm;
    private RegisteredAdapter adapter;
    private TextView totalTextView;
    private TextView noReg;

    public RegisteredFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered, container, false);
        regRecyclerView = (RecyclerView)view.findViewById(R.id.registered_recycler_view);
        setHasOptionsMenu(true);

        realm = Realm.getDefaultInstance();
        membersList = realm.copyFromRealm(realm.where(Member.class).findAll());
        adapter = new RegisteredAdapter(getActivity(), membersList);
        regRecyclerView.setAdapter(adapter);
        regRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalTextView = (TextView)view.findViewById(R.id.registered_total);
        totalTextView.setText(membersList.size()+"");

        noReg = (TextView)view.findViewById(R.id.no_reg_layout);
        if (membersList.size() > 0){
            regRecyclerView.setVisibility(View.VISIBLE);
            noReg.setVisibility(View.GONE);
        }else{
            regRecyclerView.setVisibility(View.GONE);
            noReg.setVisibility(View.VISIBLE);
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
        inflater.inflate(R.menu.registered_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.registered_delete_all:
                new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.delete_confirmation)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realm.beginTransaction();
                        realm.where(Member.class).findAll().deleteAllFromRealm();
                        realm.commitTransaction();
                        membersList.clear();
                        adapter.notifyDataSetChanged();
                        totalTextView.setText("0");
                        regRecyclerView.setVisibility(View.GONE);
                        noReg.setVisibility(View.VISIBLE);
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
}
