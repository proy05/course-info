package com.pluralsight.courseinfo.domain;

import java.util.Optional;

public record Course(String id, String name, long length, String url, Optional<String> notes) {

    public Course {
        filled(id);
        filled(name);
        filled(url);
       notes.ifPresent(Course::filled);
       //above argument needs to be an instance of Consumer functional interface
        // that is, either a lambda function or a method reference
    }

    private static void filled(String s) {
        if(s == null || s.isBlank()) { //isBlank() checks for "" and whitespaces, null needs to be checked separately
            throw new IllegalArgumentException("No value present!");
        }
    }

}
