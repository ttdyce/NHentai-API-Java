package com.github.ttdyce.model.api;

import com.github.ttdyce.model.Request;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Locale;

public class NHAPI {
    private static final String TAG = "NHAPI";

    public NHAPI() {
    }

    public void getComicList(final ResponseCallback callback) throws IOException {
        getComicList("", 1, false, callback);
    }

    public void getComicList(String query, final ResponseCallback callback) throws IOException {
        getComicList(query, 1, false, callback);
    }

    public void getComicList(String query, int page, final ResponseCallback callback) throws IOException {
        getComicList(query, page, false, callback);
    }

    public void getComicList(String query, boolean sortedPopular, final ResponseCallback callback) throws IOException {
        getComicList(query, 1, sortedPopular, callback);
    }

    /*
     * Return a JsonArray string containing 25 Comic object, as [ {"id":
     * 284928,"media_id": "1483523",...}, ...]
     */

    public void getComicList(String query, int page, boolean sortedPopular, final ResponseCallback callback)
            throws IOException {
        // TODO: 2019/10/1 Function is limited if query == "" && language == all, by the API (2019/12/22)
        getComicList("", query, page, sortedPopular, callback);

    }

    public void getComicList(String language, String query, int page, boolean sortedPopular,
            final ResponseCallback callback) throws IOException {
        String languageQuery = "language:" + language;
        if (language.equals("") || language.equals("all"))
            languageQuery = "";

        String fullQuery = languageQuery + "" + query;
        String url = URLs.search(fullQuery, page, sortedPopular);
        if (languageQuery.equals("") && query.equals(""))
            url = URLs.getIndex(page);//forward to index if language, query == ""

        System.out.println("");
        System.out.println(TAG + " getComicList: loading from url " + url);
        System.out.println("");

        Request request = new Request();
        String response = request.get(url);
        JsonArray result = new JsonParser().parse(response).getAsJsonObject().get("result").getAsJsonArray();
        callback.onReponse(result.toString());

    }

    public void getComic(int id, final ResponseCallback callback) throws IOException {
        String url = URLs.getComic(id);

        System.out.println("");
        System.out.println(TAG + " getComic: loading from url " + url);
        System.out.println("");

        Request request = new Request();
        String response = request.get(url);
        callback.onReponse(response);
    }

    // https://nhentai.net/api/galleries/search?query=language:chinese&page=1&sort=popular
    // https://nhentai.net/api/gallery/284987
    public static class URLs {
        private static String searchPrefix = "https://nhentai.net/api/galleries/search?query=";
        private static String getComicPrefix = "https://nhentai.net/api/gallery/";
        private static String[] types = { "jpg", "png" };

        public static String search(String query, int page, boolean sortedPopular) {
            if (sortedPopular)
                return searchPrefix + query + "&page=" + page + "&sort=popular";
            else
                return searchPrefix + query + "&page=" + page;
        }

        public static String getComic(int id) {
            return getComicPrefix + id;
        }

        public static String getThumbnail(String mid, String type) {
            for (String t : types) {
                if (t.charAt(0) == type.charAt(0))
                    return String.format(Locale.ENGLISH, "https://t.nhentai.net/galleries/%s/thumb.%s", mid, t);
            }

            return "";// should be not needed
        }

        public static String getPage(String mid, int page, String type) {
            for (String t : types) {
                if (t.charAt(0) == type.charAt(0))
                    return String.format(Locale.ENGLISH, "https://i.nhentai.net/galleries/%s/%d.%s", mid, page, t);
            }

            return "";// should be not needed
        }

        public static String getIndex(int page) {
            return "https://nhentai.net/api/galleries/all?page=" + page;
        }
    }
}
