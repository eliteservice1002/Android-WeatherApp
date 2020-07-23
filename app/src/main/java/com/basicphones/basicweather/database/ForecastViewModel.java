package com.basicphones.basicweather.database;

import android.app.Application;

import com.basicphones.basicweather.model.Forecast;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ForecastViewModel extends AndroidViewModel {
    private ForecastRepository mRepository;

    private LiveData<List<Forecast>> mAll;

    public ForecastViewModel (Application application) {
        super(application);

        mRepository = new ForecastRepository(application);
        mAll = mRepository.getAll();
    }

    public LiveData<List<Forecast>> getAll() { return mAll; }

    public void insert(Forecast forecast) { mRepository.insert(forecast); }

    public void delete(Forecast forecast) { mRepository.delete(forecast); }
}
