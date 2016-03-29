package com.vdzon.administratie.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleAnswer {

    private String result;

    public SingleAnswer() {
    }

    public SingleAnswer(String result) {
        this.result = result;
    }

    public String getAnswer() {
        return result;
    }
}
