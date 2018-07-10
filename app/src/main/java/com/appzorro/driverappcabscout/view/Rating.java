package com.appzorro.driverappcabscout.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Rating extends AppCompatActivity {
    CustomerRequest customerRequest;
    ArrayList<CustomerRequest> list;
    EditText edtComment;
    TextView date_tv;
    ImageView back;
    AlertDialog dialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_rating_new);
        back=(ImageView)findViewById(R.id.back);
        linearLayout=(LinearLayout)findViewById(R.id.ll_sub);
        edtComment=(EditText)findViewById(R.id.help_desp);
        date_tv=(TextView) findViewById(R.id.lbl_rating_tym);
        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);

        date_tv.setText(Utils.getCurrentDate());
        TextView submit = (TextView)findViewById(R.id.btSubmit);
        final RatingBar ratingBar =(RatingBar)findViewById(R.id.rbUserrating);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rating.this,HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(ratingBar.getRating());
                String msg=edtComment.getText().toString();
                if(!rating.equalsIgnoreCase("0.0")&&!msg.equalsIgnoreCase(""))
                {
                    dialog = new SpotsDialog(Rating.this);
                    dialog.show();

                    ModelManager.getInstance().getRatingManager().RatingManager(Rating.this, Operations.sendRatingtoCustomer(Rating.this,
                            CSPreferences.readString(Rating.this,"customer_id"),customerRequest.getCutomerid(),rating,msg));

                }
                else
                {
                    Toast.makeText(Rating.this," Please fill Fields",Toast.LENGTH_LONG).show();
                }



            }
        });


    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.RATING:
                dialog.dismiss();
                Intent intent = new Intent(Rating.this,HomeScreenActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(Rating.this);

    }


    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(Rating.this);
    }

}

