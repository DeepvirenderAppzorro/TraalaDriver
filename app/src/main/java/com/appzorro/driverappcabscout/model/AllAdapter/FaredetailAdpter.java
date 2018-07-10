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
import com.appzorro.driverappcabscout.model.Beans.FaredetailBeans;

import java.util.ArrayList;

/**
 * Created by vijay on 27/2/17.
 */

public class FaredetailAdpter extends RecyclerView.Adapter<FaredetailAdpter.ViewHolder> {

    private Context context;
    ArrayList<FaredetailBeans>list;
    ArrayList<String>namelist;
    String status="1";

    public FaredetailAdpter(Context context,ArrayList<String>namelist){

        this.context = context;
        this.namelist =namelist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faredeatladapter,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.txtname.setText(namelist.get(position));
        holder.txtbasefare.setText("50");
        holder.txttimefare.setText("2.00");
        holder.txtdistnacefare.setText("0.05");
        holder.showimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status=="1"){
                   holder.baselayout.setVisibility(View.VISIBLE);
                    holder.showimage.setImageResource(R.mipmap.doubleup);
                    status="0";


                }
                else {

                    holder.baselayout.setVisibility(View.GONE);
                    holder.showimage.setImageResource(R.mipmap.doubledown);
                    status ="1";
                }



            }
        });


    }
    @Override
    public int getItemCount() {
        return namelist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtname,txtbasefare,txttimefare,txtdistnacefare;
        ImageView showimage;
        RelativeLayout baselayout;

        public ViewHolder(View itemView) {

            super(itemView);

            txtbasefare =(TextView)itemView.findViewById(R.id.txtbasepricevalue);
            txttimefare =(TextView)itemView.findViewById(R.id.txttimepricevalue);
            txtdistnacefare =(TextView)itemView.findViewById(R.id.txtkmvalue);
            txtname=(TextView)itemView.findViewById(R.id.txtcomapnyname);
            showimage =(ImageView)itemView.findViewById(R.id.updownimage);
            baselayout =(RelativeLayout)itemView.findViewById(R.id.baselayout);


        }
    }
}
