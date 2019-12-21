package com.github.ttdyce.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Request
 */
public class Request {

    public String get(String urlLink) throws IOException {
        URL url = new URL(urlLink.replace(" ", "%20"));//url seems cant read " "? browser is fine to replace it (2019/12/22)
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        int status = con.getResponseCode();
        if (status != 200)
            return String.format(" %d, %s", status, con.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
    
}