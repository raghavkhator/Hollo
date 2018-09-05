package com.iecsemanipal.holocaust.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.activities.AttendanceActivity;
import com.iecsemanipal.holocaust.models.Event;

import java.util.List;

/**
 * Created by anurag on 20/10/17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder>{
    private List<Event> eventList;
    private Activity activity;

    public EventsAdapter(List<Event> eventList, Activity activity) {
        this.eventList = eventList;
        this.activity = activity;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getName());
        holder.date.setText(event.getDate());
        holder.venue.setText(event.getVenue());
        holder.time.setText(event.getTime());
        holder.category.setText(event.getCategory());
        holder.audience.setText(event.getAudienceType());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView date;
        TextView venue;
        TextView time;
        TextView category;
        TextView audience;

        public EventViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.event_name);
            date = (TextView)itemView.findViewById(R.id.event_date);
            venue = (TextView)itemView.findViewById(R.id.event_venue);
            time = (TextView)itemView.findViewById(R.id.event_time);
            category = (TextView)itemView.findViewById(R.id.event_category);
            audience = (TextView)itemView.findViewById(R.id.event_audience);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, AttendanceActivity.class);
            Event event = eventList.get(getLayoutPosition());
            intent.putExtra("id", event.getEventID());
            intent.putExtra("name", event.getName());
            intent.putExtra("date", event.getDate());
            intent.putExtra("venue", event.getVenue());
            intent.putExtra("time", event.getTime());
            intent.putExtra("category", event.getCategory());
            intent.putExtra("audience", event.getAudienceType());
            intent.putExtra("description", event.getDescription());
            activity.startActivity(intent);
        }
    }
}
