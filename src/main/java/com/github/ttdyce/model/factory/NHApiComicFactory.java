package com.github.ttdyce.model.factory;

import com.github.ttdyce.model.api.ResponseCallback;

import java.io.IOException;

import com.github.ttdyce.model.api.NHAPI;

public class NHApiComicFactory implements ComicFactory {
    public static final int SORT_BY_DEFAULT = 0;
    public static final int SORT_BY_POPULAR = 1;

    private NHAPI nhapi;
    private String language;
    private String query;
    private int page;
    private boolean sortedPopular;
    private ResponseCallback callback;

    public NHApiComicFactory(NHAPI nhapi, String query, int page, boolean sortedPopular, ResponseCallback callback) {
        this.nhapi = nhapi;
        this.query = query;
        this.page = page;
        this.sortedPopular = sortedPopular;
        this.callback = callback;
    }

    public NHApiComicFactory(String language, String query, int page, boolean sortedPopular, ResponseCallback callback) {
        this.nhapi = new NHAPI();
        this.language = language;
        this.query = query;
        this.page = page;
        this.sortedPopular = sortedPopular;
        this.callback = callback;
    }

    @Override
    public void requestComicList() {
        try {
            nhapi.getComicList(language, query, page, sortedPopular, callback);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void setSortBy(int sortBy) {
        if(sortBy == SORT_BY_DEFAULT)
            sortedPopular = false;
        else if(sortBy == SORT_BY_POPULAR)
            sortedPopular = true;
    }
}
