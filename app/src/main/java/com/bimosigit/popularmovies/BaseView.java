package com.bimosigit.popularmovies;

/**
 * Created by sigitbn on 6/19/17.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    void setLoadingIndicator(boolean active);
}
