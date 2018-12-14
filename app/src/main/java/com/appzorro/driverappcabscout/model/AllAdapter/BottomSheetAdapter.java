package com.appzorro.driverappcabscout.model.AllAdapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.FaredetailBeans;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vijay on 27/2/17.
 */

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {

    private Context context;
    ArrayList<FaredetailBeans> list;
    List<CustomerListResponse.Datum> namelist = new ArrayList<>();
    int selectedPosition = 0;
    int state = 0;
    public ClickListener clickListener;

    public BottomSheetAdapter(Context context, List<CustomerListResponse.Datum> namelist, ClickListener clickListener) {
        this.context = context;
        this.namelist = namelist;
        this.clickListener = clickListener;
    }


    public void customNotify(List<CustomerListResponse.Datum> namelist, int selectedPosition) {
        this.namelist = namelist;
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bootom_sheet_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (selectedPosition == position)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);
        holder.txt_riderName.setText(namelist.get(position).getName());

        Picasso.with(context).load(Config.baserurl_image + namelist.get(position).getProfilePic()).placeholder(R.drawable.ic_icon_pic).into(holder.circleImageView);

        holder.txt_riderName.setText(namelist.get(position).getName());
        paymentType(namelist.get(position).getPaymentType(), holder.txt_paymentType);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    holder.checkBox.setChecked(true);
                    state = 1;
                } else if (state == 1) {
                    holder.checkBox.setChecked(false);
                    state = 0;
                }
                clickListener.onClick(position);
            }
        });
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + namelist.get(position).getMobile()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_riderName, txt_paymentType;
        CheckBox checkBox;
        ImageView callButton;
        private CircleImageView circleImageView;
        private LinearLayout parentLayout;

        public ViewHolder(View itemView) {

            super(itemView);

            txt_riderName = itemView.findViewById(R.id.riderName);
            txt_paymentType = itemView.findViewById(R.id.txtpayment);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setEnabled(false);
            callButton = itemView.findViewById(R.id.callButton);
            circleImageView = itemView.findViewById(R.id.customer_image);
            parentLayout = itemView.findViewById(R.id.parentLayout);


        }
    }

    public interface ClickListener {

        public void onClick(int position);

    }

    private void paymentType(String s, TextView textView) {
        switch (s) {
            case "0":
                textView.setText("Cash");
                break;

            case "1":
                textView.setText("Credit Card");
                break;

            case "2":
                textView.setText("Corp. Account");
                break;
        }
    }
}
