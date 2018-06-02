package app.mmguardian.com.location_tracking.adapter;


import android.arch.persistence.room.Ignore;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.mmguardian.com.location_tracking.R;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

public class LocationAdatper extends RecyclerView.Adapter<LocationAdatper.ViewHolder>{

    List<LocationRecord> alLocationRecord;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LocationAdatper( List<LocationRecord> alLocationRecord) {
        this.alLocationRecord = alLocationRecord;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        LocationRecord mLocationRecord = alLocationRecord.get(position);

        viewHolder.tvDate.setText(sdf.format(new Date(mLocationRecord.date)));
        //viewHolder.tvAddress.setText(String.valueOf(alLocationRecord.get(position).mAltitude));
    }

    @Override
    public int getItemCount() {
        return alLocationRecord.size();
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

    public void add(int position, LocationRecord mLocationRecord){
        alLocationRecord.add(position, mLocationRecord);
    }
}
