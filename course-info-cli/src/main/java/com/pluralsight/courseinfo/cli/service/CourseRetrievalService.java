package com.pluralsight.courseinfo.cli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class CourseRetrievalService {
    private static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";
    private static final HttpClient CLIENT = HttpClient // HttpClient.newHttpClient(); //factory method returning sharable(static) client with default config
            .newBuilder() //builder method
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); //OBJECT_MAPPER to map the HTTP response to a Java Type


    public List<PluralsightCourse> getCoursesFor(String authorID){
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(authorID)))
                .GET()//Get HTTP method
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());//convert raw json bytes to simple string
            return switch(response.statusCode()){
                case 200 -> {
                    JavaType listPsCourses = OBJECT_MAPPER.getTypeFactory()
                            .constructCollectionType(List.class, PluralsightCourse.class);//create JavaType  for List<PluralSight>
                    yield OBJECT_MAPPER.readValue(response.body(), listPsCourses) ; //map response string to List<PluralSight> and return the same
                } // return string : response.body();
                case 404 -> List.of(); //return empty list
                default -> throw new RuntimeException("Pluralsight api call failed with status code" + response.statusCode());
            };

        } catch (IOException|InterruptedException e) {
            throw new RuntimeException("Could not call Pluralsight api", e); //throwing runtime exception is ok when u can do
            // nothing abt the catched checked exception
        }

    }
}
