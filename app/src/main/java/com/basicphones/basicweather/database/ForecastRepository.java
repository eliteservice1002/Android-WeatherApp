package com.basicphones.basicweather.database;

import android.app.Application;
import android.os.AsyncTask;

import com.basicphones.basicweather.model.Forecast;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ForecastRepository {
    private ForecastDao mForecastDao;
    private LiveData<List<Forecast>> mAllForecasts;

    ForecastRepository(Application application) {
        ForeacastDatabase db = ForeacastDatabase.getDatabase(application);
        mForecastDao = db.forecastDao();
        mAllForecasts = mForecastDao.getAll();
    }

    LiveData<List<Forecast>> getAll() {
        return mAllForecasts;
    }


    public void insert (Forecast forecast) {
        new insertAsyncTask(mForecastDao).execute(forecast);
    }

    public void delete (Forecast forecast)  {
        new deleteAsyncTask(mForecastDao).execute(forecast);
    }

    private static class insertAsyncTask extends AsyncTask<Forecast, Void, Void> {

        private ForecastDao mAsyncTaskDao;

        insertAsyncTask(ForecastDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Forecast... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Forecast, Void, Void> {

        private ForecastDao mAsyncTaskDao;

        deleteAsyncTask(ForecastDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Forecast... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
