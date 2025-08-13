package com.pluralsight.courseinfo.cli.service;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseStorageServiceTest {

    @Test
    void storePluralsightCourses() {
        CourseRepository repository = new InMemoryCourseRepository(); //= new CourseJdbcRepository(); Cant access CourseJdbcRepository as intended in another package since we want to hide the DB implementation class
        CourseStorageService courseStorageService = new CourseStorageService(repository);

        //create test data
        PluralsightCourse ps1 = new PluralsightCourse("1", "Title1",
                "01:40:00", "/url", false);

        courseStorageService.storePluralsightCourses(List.of(ps1));
        Course expected = new Course("1", "Title1", 100,
                "https://app.pluralsight.com/url", Optional.empty());
        assertEquals(List.of(expected), repository.getAllCourses());
    }

    static class InMemoryCourseRepository implements CourseRepository{ //static inner class is a better choice as the inner class doesn't refer to the outer class's instance variables
        //hence implement a new repository class that saves in memory
        private final List<Course> courses = new ArrayList<>();

        @Override
        public void saveCourse(Course course) {
            courses.add(course);

        }

        @Override
        public List<Course> getAllCourses() {
            return courses;
        }

        @Override
        public void addNotesToCourse(String id, String notes) {
            throw new UnsupportedOperationException();//Since this test does not rely on this functionality of adding notes

        }
    }
}