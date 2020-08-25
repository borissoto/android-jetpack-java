package com.gamla.festin.viewmodel;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gamla.festin.model.DogBreed;
import com.gamla.festin.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    private RetrieveSingleDogTask retrieveTask;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uid){
        retrieveTask = new RetrieveSingleDogTask();
        retrieveTask.execute(uid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(retrieveTask != null){
            retrieveTask.cancel(true);
            retrieveTask = null;
        }
    }

    private class RetrieveSingleDogTask extends AsyncTask<Integer, Void, DogBreed>{

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogBreedDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }
}
