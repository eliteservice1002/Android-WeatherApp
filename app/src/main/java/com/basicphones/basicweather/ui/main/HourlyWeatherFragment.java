package com.basicphones.basicweather.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basicphones.basicweather.Utility;
import com.basicphones.basicweather.database.ForecastViewModel;
import com.basicphones.basicweather.model.Forecast;
import com.basicphones.basicweather.R;

import java.util.List;

public class HourlyWeatherFragment extends Fragment {

    private static String TAG;

    private RecyclerView mRecyclerView;
    private int mSelectedItem = 0;
    private HourlyWeatherRecyclerViewAdapter mHourlyWeatherRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HourlyWeatherFragment() {
    }

    public static HourlyWeatherFragment newInstance() {
        Log.d(TAG, "new instance");

        return new HourlyWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        Log.d(TAG, "On Create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourlyweather_list, container, false);

        Log.d(TAG, "On create view");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;

            mRecyclerView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();

                    // Return false if scrolled to the bounds and allow focus to move off the list
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if (Utility.isConfirmButton(keyEvent)) {
                            if ((keyEvent.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) {
                                mRecyclerView.findViewHolderForAdapterPosition(mSelectedItem).itemView.performLongClick();
                            } else {
                                keyEvent.startTracking();
                            }
                            return true;
                        } else {
                            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                return tryMoveSelection(lm, 1);
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                return tryMoveSelection(lm, -1);
                            }
                        }
                    } else if (keyEvent.getAction() == KeyEvent.ACTION_UP && Utility.isConfirmButton(keyEvent)
                            && ((keyEvent.getFlags() & KeyEvent.FLAG_LONG_PRESS) != KeyEvent.FLAG_LONG_PRESS)) {
                        mRecyclerView.findViewHolderForAdapterPosition(mSelectedItem).itemView.performClick();
                        return true;
                    }
                    return false;
                }
            });

            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            ForecastViewModel forecastViewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
            forecastViewModel.getAll().observe(this, new Observer<List<Forecast>>() {
                @Override
                public void onChanged(@Nullable final List<Forecast> forecastList) {
                    Log.d(TAG, "Forecast list: " + forecastList);
                    if (!forecastList.isEmpty() && forecastList.size() > 0) {
                        mHourlyWeatherRecyclerViewAdapter =
                                new HourlyWeatherRecyclerViewAdapter(getContext(),
                                        forecastList.
                                                get(forecastList.size() - 1).
                                                getHourlyForecast().getHourlyConditionsList());
                        mRecyclerView.setAdapter(mHourlyWeatherRecyclerViewAdapter);
                    } else {
                        Log.e(TAG, getString(R.string.no_cache_data));
                        Toast.makeText(getContext(), getString(R.string.no_cache_data), Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            });
        }
        return view;
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int nextSelectItem = mSelectedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (nextSelectItem >= 0 && nextSelectItem < mHourlyWeatherRecyclerViewAdapter.getItemCount()) {
            mHourlyWeatherRecyclerViewAdapter.notifyItemChanged(mSelectedItem);
            mSelectedItem = nextSelectItem;
            mHourlyWeatherRecyclerViewAdapter.notifyItemChanged(mSelectedItem);
            //lm.scrollToPosition(mSelectedItem);
            mRecyclerView.smoothScrollToPosition(mSelectedItem);
            return true;
        }

        return false;
    }

}
