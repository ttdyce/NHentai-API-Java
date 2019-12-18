package com.github.ttdyce.model.factory;

public interface ComicFactory {
    void requestComicList();

    void setPage(int page);
    void setSortBy(int sortBy);
}
