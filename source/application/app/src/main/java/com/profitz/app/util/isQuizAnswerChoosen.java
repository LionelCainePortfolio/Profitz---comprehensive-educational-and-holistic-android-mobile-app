package com.profitz.app.util;

public class isQuizAnswerChoosen {
    private boolean boo = false;
    private ChangeListener listener;

    public boolean isAnswerChoosen() {
        return boo;
    }

    public void setAnswerChoosen(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
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