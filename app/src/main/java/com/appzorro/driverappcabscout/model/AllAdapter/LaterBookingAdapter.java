package com.appzorro.driverappcabscout.model.AllAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.LaterBookingList;

import java.util.ArrayList;

/**
 * Created by vijay on 11/2/17.
 */

public class LaterBookingAdapter extends RecyclerView.Adapter<LaterBookingAdapter.ViewHolder> {

   private ArrayList<LaterBookingList> list;
    Context context;

    public LaterBookingAdapter( Context context,ArrayList<LaterBookingList> list) {

        this.list = list;
        this.context= context;
    }

    @Override
    public LaterBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybookiingadapterlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LaterBookingAdapter.ViewHolder holder, int position) {
     LaterBookingList laterBookingList = list.get(position);
        Double sourcelat,sourcelng,destlat,destlng;
        sourcelat = Double.parseDouble(laterBookingList.getSourcelat());
        sourcelng = Double.parseDouble(laterBookingList.getSourcelng());
        destlat = Double.parseDouble(laterBookingList.getDestlat());
        destlng = Double.parseDouble(laterBookingList.getDestlng());
       /* holder.pickaddress.setText(""+ Utils.getCompleteAddressString(context,sourcelat,sourcelng));
        holder.dropaddress.setText(""+ Utils.getCompleteAddressString(context,destlat,destlng));*/
        holder.pickaddress.setText("Chandigarh");
        holder.dropaddress.setText("Mohali");
      //  holder.requestid.setText(laterBookingList.getRequestid());
        holder.dtae.setText(laterBookingList.getDate());
        holder.time.setText(laterBookingList.getTime());
        holder.starttrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.canceltrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestid ,dtae,time,pickaddress,dropaddress,starttrip, canceltrips;

        ViewHolder(View itemView) {
            super(itemView);

            //requestid = (TextView)itemView.findViewById(R.id.txtbookingnumber);
            dtae = (TextView)itemView.findViewById(R.id.txtdate);
            time = (TextView)itemView.findViewById(R.id.txttime);
            pickaddress = (TextView)itemView.findViewById(R.id.txtpick);
            dropaddress=(TextView)itemView.findViewById(R.id.txtdrop) ;
            starttrip = (TextView)itemView.findViewById(R.id.txtstarttrip);
            canceltrips = (TextView)itemView.findViewById(R.id.txtcancel);

        }
    }
}
