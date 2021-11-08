package com.volvo.model;

public class Greetings {

    public Greetings(String greetings) {
        super();
        this.greetings = greetings;
    }

    private String greetings;

    public String getGreetings() {
        return greetings;
    }

    @Override
    public String toString() {
        return "Greetings " + greetings ;
    }

    
}
