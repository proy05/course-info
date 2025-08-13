package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;

import java.util.List;

public interface CourseRepository {
    void saveCourse(Course course);

    List<Course> getAllCourses();

    void addNotesToCourse(String id, String notes);

    static CourseRepository openCourseRepository(String databaseFile){//static Factory method
        return new CourseJdbcRepository(databaseFile); //CourseJdbcRepository class (package protected) can be accessed here as CourseRepository interface is in the same package

    }

}
