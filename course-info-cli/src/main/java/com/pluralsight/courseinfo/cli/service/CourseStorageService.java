package com.pluralsight.courseinfo.cli.service;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

public class CourseStorageService {
    private final CourseRepository courseRepository; //reference type is of Repository interafce to maintain abstraction
    private static final String PS_BASE_URL = "https://app.pluralsight.com";

    public CourseStorageService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void storePluralsightCourses(List<PluralsightCourse> psCourses){
        //Do translation of psCourse object into Course object
        for (PluralsightCourse psCourse : psCourses){
            //convert PluralsightCourse psCourse into Course (domain) object
            Course course = new Course(psCourse.id(), psCourse.title(), psCourse.durationInMinutes(),
                    PS_BASE_URL+psCourse.contentUrl(), Optional.empty());
            courseRepository.saveCourse(course);

        }
    }
}
