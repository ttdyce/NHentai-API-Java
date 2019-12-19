package com.github.ttdyce;

import java.io.IOException;
import java.util.ArrayList;

import com.github.ttdyce.model.Comic;
import com.github.ttdyce.model.api.NHAPI;
import com.github.ttdyce.model.api.ResponseCallback;
import com.github.ttdyce.model.factory.NHApiComicFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {
    public static void main(String[] args) {
        NHAPI api = new NHAPI();
        /*
        * NHAPI usage demo, get comic list
        **/
        final ArrayList<Comic> list = new ArrayList<>();
        ResponseCallback comicListReturnCallback = new ResponseCallback() {
            @Override
            public void onReponse(String response) {
                JsonArray array = new JsonParser().parse(response).getAsJsonArray();
                Gson gson = new Gson();
                
                for (JsonElement jsonElement : array) {
                    Comic c = gson.fromJson(jsonElement, Comic.class);
                    list.add(c);
                }

            }
        }; 
        try {
            api.getComicList("chinese", "", 1, true, comicListReturnCallback);
            for (Comic comic : list) {
                System.out.println(comic.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("---Next demo---");
        System.out.println("");

        /*
        * NHAPI usage demo, get comic
        **/
        int id = list.get(0).getId();
        ResponseCallback comicReturnCallback = new ResponseCallback() {
            @Override
            public void onReponse(String response) {
                JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                Gson gson = new Gson();
                Comic comic = gson.fromJson(object, Comic.class);
                //here is what a comic looks like
                System.out.println(gson.toJson(comic));
            }
        };
        try {
            api.getComic(id, comicReturnCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("---Next demo---");
        System.out.println("");
        list.clear();
        
        /*
        * NHApiComicFactory usage demo
        **/
        ResponseCallback onFactoryComicListReturn = new ResponseCallback() {
            @Override
            public void onReponse(String response) {
                JsonArray array = new JsonParser().parse(response).getAsJsonArray();
                Gson gson = new Gson();
                Boolean hasNextPage = true;

                if (array.size() == 0)
                    hasNextPage = false;
                else if (array.size() != 25) {
                    hasNextPage = false;
                    for (JsonElement jsonElement : array) {
                        Comic c = gson.fromJson(jsonElement, Comic.class);
                        list.add(c);
                    }
                } else {
                    hasNextPage = true;
                    for (JsonElement jsonElement : array) {
                        Comic c = gson.fromJson(jsonElement, Comic.class);
                        list.add(c);
                    }

                }

            }

        };
        NHApiComicFactory factory = new NHApiComicFactory("chinese", "", 1, true, onFactoryComicListReturn);
        factory.requestComicList();// trigger onFactoryComicListReturn.onResponse(response)
        for (Comic comic : list) {
            System.out.println(comic.getTitle());
        }


    }
}