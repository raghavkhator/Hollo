package com.iecsemanipal.holocaust.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.adapters.EventsAdapter;
import com.iecsemanipal.holocaust.models.Event;
import com.iecsemanipal.holocaust.models.EventCreationRequest;
import com.iecsemanipal.holocaust.models.GetEventsRequest;
import com.iecsemanipal.holocaust.models.GetEventsResponse;
import com.iecsemanipal.holocaust.models.ServerResponse;
import com.iecsemanipal.holocaust.network.HolocaustClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {
    private static final String API_KEY = "anuragtheboss";
    private List<Event> eventsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = (Toolbar)findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.events));
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.events_recycler_view);
        adapter = new EventsAdapter(eventsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addEvent = (FloatingActionButton)findViewById(R.id.add_event_fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.events_swipe_refresh);

        loadEvents();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadEvents();
            }
        });
    }

    private void loadEvents(){
        GetEventsRequest request = new GetEventsRequest();
        request.setApiKey(API_KEY);
        swipeRefreshLayout.setRefreshing(true);

        Call<GetEventsResponse> call = HolocaustClient.getHolocaustInterface().getEvents(request);
        call.enqueue(new Callback<GetEventsResponse>() {
            @Override
            public void onResponse(Call<GetEventsResponse> call, Response<GetEventsResponse> response) {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                GetEventsResponse resp;
                if (response != null && (resp = response.body()) != null){
                    if (resp.getStatus() == 1){
                        eventsList.clear();
                        eventsList.addAll(resp.getEventsList());
                        adapter.notifyDataSetChanged();
                    }else{
                        showAlert(getString(R.string.error), getString(R.string.events_fetch_failure), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
                    }
                }else{
                    showAlert(getString(R.string.error), getString(R.string.events_fetch_failure), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
                }
            }

            @Override
            public void onFailure(Call<GetEventsResponse> call, Throwable t) {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                showAlert(getString(R.string.error), getString(R.string.check_connection), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
            }
        });
    }

    private void createEvent(){
        View eventView = LayoutInflater.from(EventsActivity.this).inflate(R.layout.dialog_create_event, null);
        final TextInputEditText name = (TextInputEditText)eventView.findViewById(R.id.event_name_edit_text);
        final TextInputEditText date = (TextInputEditText)eventView.findViewById(R.id.event_date_edit_text);
        final TextInputEditText time = (TextInputEditText)eventView.findViewById(R.id.event_time_edit_text);
        final TextInputEditText venue = (TextInputEditText)eventView.findViewById(R.id.event_venue_edit_text);
        final TextInputEditText description = (TextInputEditText)eventView.findViewById(R.id.event_description_edit_text);
        final TextInputEditText category = (TextInputEditText)eventView.findViewById(R.id.event_category_edit_text);
        final TextInputEditText audienceType = (TextInputEditText)eventView.findViewById(R.id.event_audience_edit_text);

        new AlertDialog.Builder(EventsActivity.this).setView(eventView).setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (name.getText().toString().isEmpty() || date.getText().toString().isEmpty() || time.getText().toString().isEmpty()
                        || venue.getText().toString().isEmpty() || description.getText().toString().isEmpty() || category.getText().toString().isEmpty()
                        || audienceType.getText().toString().isEmpty()){
                    new AlertDialog.Builder(EventsActivity.this).setMessage(getString(R.string.enter_all_details)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }else{
                    final ProgressDialog dialog = new ProgressDialog(EventsActivity.this);
                    dialog.setMessage(getString(R.string.creating_event));
                    dialog.show();

                    Event event = new Event();
                    event.setName(name.getText().toString());
                    event.setDescription(description.getText().toString());
                    event.setDate(date.getText().toString());
                    event.setVenue(venue.getText().toString());
                    event.setTime(time.getText().toString());
                    event.setCategory(category.getText().toString());
                    event.setAudienceType(audienceType.getText().toString());
                    EventCreationRequest request = new EventCreationRequest();
                    request.setApiKey(API_KEY);
                    request.setEvent(event);

                    Call<ServerResponse> call = HolocaustClient.getHolocaustInterface().createEvent(request);
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                            if (dialog.isShowing()) dialog.dismiss();
                            ServerResponse resp;
                            if (response != null && (resp = response.body()) != null){
                                if (resp.getStatus() == 1){
                                    showAlert(getString(R.string.success), getString(R.string.event_creation_success), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_success));
                                    loadEvents();
                                }else{
                                    showAlert(getString(R.string.error), getString(R.string.event_create_failure), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
                                    Log.d("Error", resp.getDescription());
                                }
                            }else{
                                showAlert(getString(R.string.error), getString(R.string.event_create_failure), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            if (dialog.isShowing()) dialog.dismiss();
                            showAlert(getString(R.string.error), getString(R.string.check_connection), ContextCompat.getDrawable(EventsActivity.this, R.drawable.ic_error));
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
    }

    private void showAlert(String title, String message, Drawable icon){
        if (title != null && message != null && icon != null){
            new AlertDialog.Builder(this).setIcon(icon).setTitle(title).setMessage(message).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.events_refresh: loadEvents(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}
