package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class ResorceModel extends BaseObservable implements Serializable {
    String title;
    String seq;
    String cover;

    public String getSeq() {
        return seq;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }
}
