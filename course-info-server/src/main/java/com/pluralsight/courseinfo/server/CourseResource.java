package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.repository.RepositoryException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/courses") //base prefix for every endpoint
public class CourseResource {
    public static final Logger LOG = LoggerFactory.getLogger(CourseResource.class); //any variable that is not initialized
    // in constructor shud be static

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON) //to indicate to convert the List<Courses> to Json array of strings, each string representing a course object
    //But this is where an additional dependency of jersey-media-json-jackson is required for json serialization/deserialization
    public Stream<Course> getCourses(){
//        return courseRepository.
//                getAllCourses()
//                .stream()
//                .map(Course::toString)
//                .collect(Collectors.joining(", ")); //join all courses into a single string

        try {
            return courseRepository
                    .getAllCourses()
                    .stream()
                    .sorted(Comparator.comparing(Course::id)); //The left one uses method reference instead of a Lambda function below
                    // sorted((c1, c2) -> c1.id().compareTo(c2.id()) )

            //.collect(Collectors.toList()); //no need to return List<Course>, we can return Stream<Course>
        } catch (RepositoryException e) {
            LOG.error("Could not retrieve courses from the database");
            throw new NotFoundException(); // throw NotFoundException() from jax-rs that has 404 status code
            //though 404 is not the right status code. THis is just an example
            //jersey will turn this exception into 404 code
        }
    }

    @POST
    @Path("/{id}/notes")
    @Consumes(MediaType.TEXT_PLAIN) //post body needs to be plaintext
    public void addNotesToCourse(@PathParam("id") String id, String notes){
        try {
            courseRepository.addNotesToCourse(id, notes);
        } catch(RepositoryException e){
            LOG.error("Could not add notes to course id="+id);
            throw new RuntimeException("Adding notes to course id=%s failed".formatted(id), e);
        }
    }
}
