package com.profitz.app;


public class GetReflink {
    String ref;
    private static final GetReflink ourInstance = new GetReflink();
    public static GetReflink getInstance() {
        return ourInstance;
    }
    public GetReflink() {
    }

    public void setReflink(String ref) {
        this.ref = ref;

    }
    public String getReflink() {
        return ref;
    }



}