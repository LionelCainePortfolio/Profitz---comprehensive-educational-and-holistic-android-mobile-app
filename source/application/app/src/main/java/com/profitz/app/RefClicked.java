package com.profitz.app;


public class RefClicked {
    String s;
    private ChangeListener listener;
    private static final RefClicked ourInstance = new RefClicked();
    public static RefClicked getInstance() {
        return ourInstance;
    }
    public RefClicked() {
    }
    public void setData(String s) {
        this.s = s;
        if (listener != null) listener.onChange();
    }
    public String getData() {
        return s;
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}