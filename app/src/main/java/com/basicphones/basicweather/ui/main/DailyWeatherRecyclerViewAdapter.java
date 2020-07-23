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

import com.basicphones.basicweather.model.DayConditions;
import com.basicphones.basicweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<DailyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<DayConditions> mValues;
    private Context mContext;

    public DailyWeatherRecyclerViewAdapter(Context context, List<DayConditions> items) {
        mValues = items;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dailyweather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Typeface calibriFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/calibri.ttf");

        // convert seconds to milliseconds and format the date to month.day.year
        Date date = new java.util.Date(mValues.get(position).getTime()*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM.dd.yy");
        String formattedDate = sdf.format(date);
        holder.mDateView.setText(formattedDate);
        holder.mDateView.setTypeface(calibriFont);
        holder.mMaxTempView.
                setText(String.format("%s °F",
                        String.valueOf(Math.round(mValues.get(position).getTemperatureMax()))));
        holder.mMinTempView.
                setText(String.format("%s °F",
                        String.valueOf(Math.round(mValues.get(position).getTemperatureMin()))));
        holder.mMaxTempView.setTypeface(calibriFont);
        holder.mMinTempView.setTypeface(calibriFont);
        int resourceIdentifier = mContext.getResources().getIdentifier(mValues.get(position).getIconString().replace("-", "_"), "drawable", mContext.getPackageName());
        holder.mIcon.setImageResource(resourceIdentifier);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDateView;
        final TextView mMaxTempView;
        final TextView mMinTempView;
        final ImageView mIcon;
        DayConditions mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = view.findViewById(R.id.date);
            mMaxTempView = view.findViewById(R.id.dailyMaxTempForDay);
            mMinTempView = view.findViewById(R.id.dailyMinTempForDay);
            mIcon = view.findViewById(R.id.dailyIconWeather);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mMaxTempView.getText() + " " + mMinTempView.getText() + "'";
        }
    }
}
