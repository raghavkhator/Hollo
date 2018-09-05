package com.iecsemanipal.holocaust.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.models.Member;

import java.util.List;

/**
 * Created by anurag on 18/8/17.
 */
public class RegisteredAdapter extends RecyclerView.Adapter<RegisteredAdapter.RegisteredViewHolder> {

    private Context context;
    private List<Member> membersList;

    public RegisteredAdapter(Context context, List<Member> membersList) {
        this.context = context;
        this.membersList = membersList;
    }

    @Override
    public RegisteredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RegisteredViewHolder(LayoutInflater.from(context).inflate(R.layout.item_registered, parent, false));
    }

    @Override
    public void onBindViewHolder(RegisteredViewHolder holder, int position) {
        Member member = membersList.get(position);
        holder.memID.setText(member.getMemID());
        holder.memName.setText(member.getName());
        holder.regNo.setText(member.getRegNo()+"");
        holder.phNo.setText(member.getContactNo());
        holder.email.setText(member.getEmail());
        holder.timeStamp.setText(member.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    class RegisteredViewHolder extends RecyclerView.ViewHolder {
        TextView memID;
        TextView memName;
        TextView regNo;
        TextView phNo;
        TextView email;
        TextView timeStamp;

        public RegisteredViewHolder(View itemView)
        {
            super(itemView);
            memID = (TextView)itemView.findViewById(R.id.reg_mem_id);
            memName = (TextView)itemView.findViewById(R.id.reg_name);
            regNo = (TextView)itemView.findViewById(R.id.reg_no);
            phNo = (TextView)itemView.findViewById(R.id.reg_contact_number);
            email = (TextView)itemView.findViewById(R.id.reg_email);
            timeStamp = (TextView)itemView.findViewById(R.id.register_time_stamp);
        }

    }
}
