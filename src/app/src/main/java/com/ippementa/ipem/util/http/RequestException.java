package com.ippementa.ipem.util.http;

public class RequestException extends IllegalStateException {

    public Client.Response response;

    public RequestException(Client.Response response){
        this.response = response;
    }
}
