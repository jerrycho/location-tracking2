package app.mmguardian.com.location_tracking.adapter;


import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.location.LocationResult;

import java.util.Date;
import java.util.List;
import io.reactivex.functions.Consumer;

import app.mmguardian.com.location_tracking.R;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

public class LocationAdatper extends RecyclerView.Adapter<LocationAdatper.ViewHolder>{

    public static final String TAG = "location_tracking";

    List<LocationRecord> alLocationRecord;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Consumer<LocationRecord> onMapClick;

    public LocationAdatper( List<LocationRecord> alLocationRecord) {
        this.alLocationRecord = alLocationRecord;
    }

    public void setOnMapClick(Consumer onMapClick){
        this.onMapClick = onMapClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final LocationRecord mLocationRecord = alLocationRecord.get(position);

        viewHolder.tvDate.setText("Time : " + sdf.format(new Date(mLocationRecord.date)));
        if (TextUtils.isEmpty(mLocationRecord.address)){
            viewHolder.tvAddress.setText("Address : ---");
        }
        else {
            viewHolder.tvAddress.setText("Address : " +mLocationRecord.address);
        }

        if (mLocationRecord.isNull){
            viewHolder.tvLotLong.setText("Cannot get location");
            viewHolder.btnMap.setOnClickListener(null);
        }
        else {
            viewHolder.tvLotLong.setText(
                    "Latitude : " + String.valueOf(mLocationRecord.latitude) + "\n"+
                    "Longitude : " + String.valueOf(mLocationRecord.longitude)
            );
            if (onMapClick!=null){
                viewHolder.btnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            onMapClick.accept(mLocationRecord);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return alLocationRecord.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDate;
        public TextView tvAddress;
        public TextView tvLotLong;
        public Button btnMap;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAddress= itemView.findViewById(R.id.tvAddress);
            tvLotLong = itemView.findViewById(R.id.tvLotLong);
            btnMap = itemView.findViewById(R.id.btnMap);
        }
    }

    public void add(LocationRecord mLocationRecord){
        alLocationRecord.add(mLocationRecord);
    }

    public void add(int position, LocationRecord mLocationRecord){
        alLocationRecord.add(position, mLocationRecord);
    }

    public void remove(int position){
        alLocationRecord.remove(position);
    }

    public LocationRecord getItemByPosition(int position){
        return alLocationRecord.get(position);
    }
}
