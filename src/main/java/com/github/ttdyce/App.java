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
            
            @Override
            public void onError(String error) {
                
                System.err.println("Error accessing comic list! API returned a \"" + error + "\"");
                
            }
        }; 
        //end of callback
        try {
            api.getComicList(comicListReturnCallback);//index page 1
            if (!list.isEmpty()) {
                for (Comic comic : list) {
                    System.out.println(comic.getTitle());
                }
                System.out.println("*********");
                System.out.println("");
            }
            api.getComicList("english", "", 1, true, comicListReturnCallback);//english comic page 1, sort by popularity
            if (!list.isEmpty()) {
                for (Comic comic : list) {
                    System.out.println(comic.getTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("---Next demo---");
        System.out.println("");
        
        if (list.isEmpty()) {
            
            System.out.println("Error getting comic list. Cannot try comic demo.");
            
        } else {
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
                @Override
                public void onError(String error) {
                    
                    System.err.println("Error accessing comic! API returned a \"" + error + "\"");
                    
                }
            };
            //end of callback
            try {
                api.getComic(id, comicReturnCallback);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
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
            
            @Override public void onError(String error) {
                
                System.err.println("Error demoing Comic Factory! API returned a \"" + error + "\"");
                
            }

        };
        //end of callback
        NHApiComicFactory factory = new NHApiComicFactory("english", " ", 1, true, onFactoryComicListReturn);
        factory.requestComicList();// trigger onFactoryComicListReturn.onResponse(response)
        
        if (list.isEmpty()) {
            
            System.out.println("There are no comics to demo.");
            
        } else {
            
            for (Comic comic : list) {
                System.out.println(comic.getTitle());
            }
            
        }


    }
}
