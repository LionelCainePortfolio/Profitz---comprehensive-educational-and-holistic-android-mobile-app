package com.profitz.app.base;
public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);

}