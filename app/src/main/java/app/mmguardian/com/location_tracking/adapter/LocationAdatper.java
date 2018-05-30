package app.mmguardian.com.location_tracking.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.mmguardian.com.location_tracking.R;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

public class LocationAdatper extends RecyclerView.Adapter<LocationAdatper.ViewHolder>{

    ArrayList<LocationRecord> alLocationRecord;

    public LocationAdatper( ArrayList<LocationRecord> alLocationRecord) {
        this.alLocationRecord = alLocationRecord;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvDate.setText(alLocationRecord.get(position).getDate().toString());
        //viewHolder.tvAddress.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDate;
        public TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDate= itemView.findViewById(R.id.tvAddress);
        }
    }

    public void add(LocationRecord mLocationRecord){
        alLocationRecord.add(mLocationRecord);
    }
}
