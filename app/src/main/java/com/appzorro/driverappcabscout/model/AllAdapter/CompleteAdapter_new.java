package com.appzorro.driverappcabscout.model.AllAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;

import java.util.ArrayList;

/**
 * Created by vijay on 19/6/18.
 */

public class CompleteAdapter_new extends RecyclerView.Adapter<CompleteAdapter_new.ViewHolder> {

    private Context context;
    ArrayList<CompletedRideBean> list;
    String status="1";

    public CompleteAdapter_new(Context context, ArrayList<CompletedRideBean> list){
   this.list=list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ridehistoryadapter,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CompletedRideBean users = list.get(position);

        holder.txtprice.setText(""+users.getName());
        holder.txtdate.setText(""+users.getEdndtime());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void customNotify(Context context, ArrayList<CompletedRideBean> trips) {
        this.context=context;
        this.list=trips;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtprice,txtdate;
        ImageView showimage;
        RelativeLayout baselayout;

        public ViewHolder(View itemView) {

            super(itemView);

            txtprice=(TextView)itemView.findViewById(R.id.txtprice);
            txtdate=(TextView)itemView.findViewById(R.id.texttime);

        }
    }
}
