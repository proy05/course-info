package com.pluralsight.courseinfo.cli;

import com.pluralsight.courseinfo.cli.service.CourseRetrievalService;
import com.pluralsight.courseinfo.cli.service.CourseStorageService;
import com.pluralsight.courseinfo.cli.service.PluralsightCourse;
import com.pluralsight.courseinfo.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CourseRetriever {
    public static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String... args){
        LOG.info("CourseRetriever starting");
        if (args.length==0){
            LOG.warn("Please provide author id as first argument.");
            return;
        }
        try{
            retrieveCourses(args[0]);
        }
        catch(Exception e){
            LOG.error("Unexpected error", e);
            //e.printStackTrace();
        }
    }

    private static void retrieveCourses(String authorID) {
        LOG.info("Retrieving courses for author '{}'", authorID);
        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();//retrieves JSON and returns List<PluralSightCourse>
        CourseRepository  courseRepository = CourseRepository.openCourseRepository("./courses.db"); //call static factory method of CourseRepository abstraction to get object of CourseJdbcRepository;
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository); //converts each PluralSightCourse in List<PluralSightCourse> into a Course and saves each Course using courseRepository.saveCourse(Course).


        List<PluralsightCourse> coursesToStore =  courseRetrievalService.getCoursesFor(authorID)
                        .stream()
                        .filter(course -> !course.isRetired()) //filter to keep non-retired courses(intermed stream operation)
                        .collect(Collectors.toList()); //terminal stream operation
//        System.out.println("%s duration=%s".formatted(
//                coursesToStore.get(0),
//                coursesToStore.get(0).durationInMinutes()));
        LOG.info("Retrieved the following {} courses:\n{}",coursesToStore.size(),coursesToStore);
        courseStorageService.storePluralsightCourses(coursesToStore);
        LOG.info("Courses successfully stored");
    }
}
