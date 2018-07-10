package com.appzorro.driverappcabscout.model.AllAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;

import java.util.ArrayList;

/**
 * Created by vijay on 20/2/17.
 */


public class CompletedtrpsAdapter extends BaseAdapter {


    Context context;
    ArrayList<CompletedRideBean> list;
    private LayoutInflater inflater=null;

    public CompletedtrpsAdapter(Context context, ArrayList<CompletedRideBean> list) {

        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();



        final CompletedRideBean users = list.get(position);
        convertView = inflater.inflate(R.layout.ridehistoryadapter, null);

        holder.txtprice=(TextView) convertView.findViewById(R.id.txtprice);
        holder.txtdate=(TextView) convertView.findViewById(R.id.texttime);
        holder.txtprice.setText(""+users.getTotalamount());
        holder.txtdate.setText(""+users.getEdndtime());
        return convertView;
    }


    private  class Holder
    {

        TextView txtdate,txtprice;

    }

}
