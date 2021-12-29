package com.github.ttdyce;

import java.util.ArrayList;

import com.github.ttdyce.model.Comic;
import com.github.ttdyce.model.api.PopularType;
import com.github.ttdyce.model.api.ResponseCallback;
import com.github.ttdyce.model.factory.NHApiComicFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void shouldShowPopularityOption()
    {
        // System.out.println(NHAPI.URLs.search("chinese", 1, true, PopularType.week));
        // System.out.println(NHAPI.URLs.search("chinese", 1, true, PopularType.today));

        final ArrayList<Comic> list = new ArrayList<>();
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
        //end of callback
        NHApiComicFactory factory = new NHApiComicFactory("chinese", " ", 1, PopularType.today, onFactoryComicListReturn);
        factory.requestComicList();// trigger onFactoryComicListReturn.onResponse(response)
        for (Comic comic : list) {
            System.out.println(comic.getTitle());
        }
    }
}
