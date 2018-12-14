package com.appzorro.driverappcabscout.model.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.TripDetail;

import java.util.ArrayList;

/**
 * Created by vijay on 19/6/18.
 */

public class CompleteAdapter_new extends RecyclerView.Adapter<CompleteAdapter_new.ViewHolder> {

    private Context context;
    ArrayList<CompletedRideBean> list;
    String status = "1";

    public CompleteAdapter_new(Context context, ArrayList<CompletedRideBean> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_history_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CompletedRideBean users = list.get(position);
        if (users.getTotalamount().equalsIgnoreCase("null")) {
            holder.txtprice.setText("N/A");

        } else {
            holder.txtprice.setText( Utils.currencyConverter(Double.parseDouble(users.getTotalamount())));


        }
        Log.d("time", users.getStart_tym() + " start tym in holder");
        holder.txtdate.setText(users.getStartdate());
        holder.txt_tym.setText(users.getEdndtym());

        holder.lltripD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TripDetail.class);
                intent.putExtra("ListData", users);
                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void customNotify(Context context, ArrayList<CompletedRideBean> trips) {
        this.context = context;
        this.list = trips;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtprice, txtdate, txt_tym;
        ImageView showimage;
        RelativeLayout baselayout;
        LinearLayout lltripD;

        public ViewHolder(View itemView) {

            super(itemView);
            lltripD = itemView.findViewById(R.id.rl_address);

            txtprice = (TextView) itemView.findViewById(R.id.txt_price);
            txtdate = (TextView) itemView.findViewById(R.id.txt_date);
            txt_tym = (TextView) itemView.findViewById(R.id.ride_tym);


        }
    }
}
