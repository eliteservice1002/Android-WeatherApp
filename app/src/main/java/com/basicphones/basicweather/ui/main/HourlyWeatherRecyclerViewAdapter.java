package com.basicphones.basicweather.ui.main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basicphones.basicweather.model.HourConditions;
import com.basicphones.basicweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HourlyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<HourlyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<HourConditions> mValues;
    private Context mContext;

    public HourlyWeatherRecyclerViewAdapter(Context context, List<HourConditions> items) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hourlyweather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Typeface calibriFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/calibri.ttf");

        Date date = new java.util.Date(mValues.get(position).getTime()*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM.dd.yy HH:mm");
        String formattedDate = sdf.format(date);

        holder.mDateView.setText(formattedDate);
        holder.mWeatherView.setText(
                String.format("%s °F",
                        Math.round(mValues.get(position).getTemperature())));
        holder.mDateView.setTypeface(calibriFont);
        holder.mWeatherView.setTypeface(calibriFont);
        int resourceIdentifier = mContext.getResources().getIdentifier(mValues.get(position).getIconString().replace("-", "_"), "drawable", mContext.getPackageName());
        holder.mIcon.setImageResource(resourceIdentifier);

        double percentage = mValues.get(position).getPrecipProbability() * 100.0;
        holder.mPrecipProbabilityView.setText(String.format("%s %%", percentage));
        holder.mPrecipProbabilityView.setTypeface(calibriFont);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDateView;
        final TextView mWeatherView;
        final TextView mPrecipProbabilityView;
        final ImageView mIcon;
        HourConditions mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = view.findViewById(R.id.item_number);
            mWeatherView = view.findViewById(R.id.content);
            mIcon = view.findViewById(R.id.hourlyIconWeather);
            mPrecipProbabilityView = view.findViewById(R.id.hourlyPrecipProbability);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mWeatherView.getText() + "'";
        }
    }
}
