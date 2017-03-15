package com.appzorro.driverappcabscout.model.AllAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.ReviewBeans;

import java.util.ArrayList;

/**
 * Created by vijay on 14/2/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<ReviewBeans> list;
    Context context;

    public ReviewAdapter(Context context,ArrayList<ReviewBeans> list) {
        this.list = list;
        this.context =context;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewadapterlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewBeans ratingBeans = list.get(position);
        holder.name.setText(ratingBeans.getName());
        holder.time.setText("2 day ago");
        holder.comment.setText(ratingBeans.getFeedback());
        holder.ratingtext.setText("Rating"+" "+ratingBeans.getRating());
        holder.customerRatings.setRating(Float.parseFloat(ratingBeans.getRating()));
    /*    Picasso.with(context)
                .load(ratingBeans.getProfilepic())
                .into(holder.imageView);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, time;
        private RatingBar customerRatings;
        //private ImageView imageView;
        private TextView comment,ratingtext;
        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.customerName);
            time = (TextView)itemView.findViewById(R.id.time);

            customerRatings = (RatingBar)itemView.findViewById(R.id.customerRatings);
          //  imageView = (ImageView) itemView.findViewById(R.id.customerImage);
            comment =(TextView)itemView.findViewById(R.id.txtcomment);
            ratingtext =(TextView)itemView.findViewById(R.id.txtrating);
            customerRatings=(RatingBar)itemView.findViewById(R.id.customerRatings);
        }
    }
}