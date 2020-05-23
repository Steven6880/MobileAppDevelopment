package com.jiuwfung.comp6239.events.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.helper.EventAdapterItem;

import java.util.ArrayList;

public class EventChooseAdapter extends RecyclerView.Adapter<EventChooseAdapter.EventChooseViewHolder> {
    public ArrayList<EventChooseAdapterItem> eventChooseAdapterItems;

    @NonNull
    @Override
    public EventChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_choosestudents, parent , false);
        EventChooseViewHolder eventChooseViewHolder = new EventChooseViewHolder(v);
        return eventChooseViewHolder;
    }

    public EventChooseAdapter(ArrayList<EventChooseAdapterItem> eventChooseAdapterItems){
        this.eventChooseAdapterItems = eventChooseAdapterItems;
    }

    @Override
    public void onBindViewHolder(@NonNull EventChooseViewHolder holder, int position) {
        EventChooseAdapterItem currentItem = eventChooseAdapterItems.get(position);
        holder.mGroup.setText(currentItem.getGroup());
        holder.mName.setText(currentItem.getName());
        holder.mCheckBox.setChecked(currentItem.getCheck());
    }

    @Override
    public int getItemCount() {
        return eventChooseAdapterItems.size();
    }

    public static class EventChooseViewHolder extends RecyclerView.ViewHolder{
        public TextView mGroup;
        public TextView mName;
        public CheckBox mCheckBox;

        public EventChooseViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroup = itemView.findViewById(R.id.card_student_id);
            mName = itemView.findViewById(R.id.card_student_name);
            mCheckBox = itemView.findViewById(R.id.card_student_checkbox);
        }
    }
}
