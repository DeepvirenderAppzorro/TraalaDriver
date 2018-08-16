package com.appzorro.driverappcabscout.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;

import com.appzorro.driverappcabscout.R;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_new);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rbUserrating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(Main5Activity.this, R.color.blue));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(Main5Activity.this, R.color.white));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(Main5Activity.this, R.color.white));
    }
    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
