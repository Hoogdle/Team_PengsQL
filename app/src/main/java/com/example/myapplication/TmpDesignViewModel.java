package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class TmpDesignViewModel extends ViewModel {
    private final MutableLiveData<List<TmpDesignData>> fileDataLiveData;

    public TmpDesignViewModel() {
        // 초기 더미 데이터 추가
        List<TmpDesignData> sampleFiles = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            sampleFiles.add(new TmpDesignData("file" + i, 123.3F));
        }
        fileDataLiveData = new MutableLiveData<>(sampleFiles);
    }

    public LiveData<List<TmpDesignData>> getFileDataLiveData() {
        return fileDataLiveData;
    }

    public void addFile(String name, float size) {
        List<TmpDesignData> currentList = new ArrayList<>(fileDataLiveData.getValue());
        currentList.add(new TmpDesignData(name, size));
        fileDataLiveData.setValue(currentList);
    }
}
