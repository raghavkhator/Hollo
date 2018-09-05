package com.iecsemanipal.holocaust.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.models.Pending;

import java.util.List;

/**
 * Created by anurag on 24/8/17.
 */
public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder>{
    private Context context;
    private List<Pending> pendingList;

    public PendingAdapter(Context context, List<Pending> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PendingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pending, parent, false));
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
        Pending pending = pendingList.get(position);
        holder.memID.setText(pending.getMemID());
        holder.name.setText(pending.getName());
        holder.regNo.setText(pending.getRegNo()+"");
        holder.phNo.setText(pending.getContactNo());
        holder.email.setText(pending.getEmail());
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    class PendingViewHolder extends RecyclerView.ViewHolder {
        TextView memID;
        TextView name;
        TextView regNo;
        TextView phNo;
        TextView email;

        public PendingViewHolder(View itemView)
        {
            super(itemView);
            memID = (TextView)itemView.findViewById(R.id.p_mem_id);
            name = (TextView)itemView.findViewById(R.id.p_name);
            regNo = (TextView)itemView.findViewById(R.id.p_reg_no);
            phNo = (TextView)itemView.findViewById(R.id.p_contact_number);
            email = (TextView)itemView.findViewById(R.id.p_email);
        }

    }
}
