package com.bbc.rms.hooks;

import io.cucumber.java.Before;

import static com.bbc.rms.baseurl.RMS_BaseUrl.setUp;

public class Hooks {

    @Before ("@api")
    public void beforeApi(){
        setUp();
    }
}
