package com.ippementa.ipem.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {

    public static Response get(URL urlToRequest) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) urlToRequest.openConnection();

        connection.setConnectTimeout(15000);

        connection.setRequestMethod("GET");

        connection.setDoInput(true);

        connection.connect();

        Response response = new Response();

        response.statusCode = connection.getResponseCode();

        InputStream payloadStream;

        if(response.statusCode < 400){
            payloadStream = connection.getInputStream();
        }else{
            payloadStream = connection.getErrorStream();
        }

        BufferedReader readerStream = new BufferedReader(new InputStreamReader(payloadStream));

        StringBuffer buffer = new StringBuffer();

        String readLine;

        while((readLine = readerStream.readLine()) != null){
            buffer.append(readLine);
        }

        response.payload = buffer.toString();

        readerStream.close();

        connection.disconnect();

        return response;
    }

    public static class Response{

        public int statusCode;

        public String payload;

    }

}
