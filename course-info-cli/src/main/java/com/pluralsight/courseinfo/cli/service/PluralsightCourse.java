package com.pluralsight.courseinfo.cli.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true) //While data-binding, ignore fields from json response not found in below record definition
public record PluralsightCourse(String id, String title, String duration, String contentUrl, boolean isRetired) {
    //duration = "00:05:31"
    public long durationInMinutes(){
        return Duration.between(
                LocalTime.MIN, //OO:00
                LocalTime.parse(duration)
                ).toMinutes();
    }
}
