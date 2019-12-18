package com.github.ttdyce;

import java.io.IOException;
import java.util.ArrayList;

import com.github.ttdyce.model.Comic;
import com.github.ttdyce.model.Request;
import com.github.ttdyce.model.api.NHAPI;
import com.github.ttdyce.model.api.ResponseCallback;
import com.github.ttdyce.model.factory.NHApiComicFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        final ArrayList<Comic> list = new ArrayList<>();
        NHApiComicFactory factory = new NHApiComicFactory("chinese", "", 1, true, new ResponseCallback(){

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

        });
        factory.requestComicList();
        for (Comic comic : list) {
            System.out.println(comic.getTitle()); 
        }
        
        // Request request = new Request();

        // try {
        //     String response = request.get(NHAPI.URLs.getIndex(1));
        //     System.out.println(response);

        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

}
