package com.appzorro.driverappcabscout.model.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by vijay on 30/8/18.
 */

@SuppressLint("AppCompatCustomView")
public class CustomSpinner extends Spinner {
    public CustomSpinner(Context context) {
        super(context);
    }
    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
        public interface OnSpinnerEventsListener {

            /**
             * Callback triggered when the spinner was opened.
             */
            void onSpinnerOpened(Spinner spinner);

            /**
             * Callback triggered when the spinner was closed.
             */
            void onSpinnerClosed(Spinner spinner);

        }

        private OnSpinnerEventsListener mListener;
        private boolean mOpenInitiated = false;

        // implement the Spinner constructors that you need

        @Override
        public boolean performClick() {
            // register that the Spinner was opened so we have a status
            // indicator for when the container holding this Spinner may lose focus
            mOpenInitiated = true;
            if (mListener != null) {
                mListener.onSpinnerOpened(this);
            }
            return super.performClick();
        }

        /**
         * Register the listener which will listen for events.
         */
        public void setSpinnerEventsListener(
                OnSpinnerEventsListener onSpinnerEventsListener) {
            mListener = onSpinnerEventsListener;
        }

        /**
         * Propagate the closed Spinner event to the listener from outside if needed.
         */
        public void performClosedEvent() {
            mOpenInitiated = false;
            if (mListener != null) {
                mListener.onSpinnerClosed(this);
            }
        }

        /**
         * A boolean flag indicating that the Spinner triggered an open event.
         *
         * @return true for opened Spinner
         */
        public boolean hasBeenOpened() {
            return mOpenInitiated;
        }

        public void onWindowFocusChanged (boolean hasFocus) {
            if (hasBeenOpened() && hasFocus) {
                performClosedEvent();
            }
        }

    }

