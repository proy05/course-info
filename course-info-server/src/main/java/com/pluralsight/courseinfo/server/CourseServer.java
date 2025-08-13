package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class CourseServer {

    public static final Logger LOG = LoggerFactory.getLogger(CourseServer.class);
    //private static final String BASE_URI ; // Converted into a local variable for method after externalizing configurations using properties file -> ="http://localhost:8080/";

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install(); //Using the bridge library(jul-to-slf4j maven dependency), install hooks into JDK logging API (used by Jersey),
        // which redirects calls to it to SLF4J.

    } //static initializer block

    public static void main(String... args){
        String databaseFileName = loadDatabaseFileName();
        LOG.info("Starting HTTP server with database {}", databaseFileName);
        //call static factory method defined in the CourseRepository interface definition
        CourseRepository courseRepository = CourseRepository.openCourseRepository(databaseFileName);
        //dependency injection at work below bcoz the courseRepository dependency is injected into CourseResource
        //class through its constructor
        ResourceConfig config = new ResourceConfig().register(new CourseResource(courseRepository));
        String BASE_URI = loadBaseURI();
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config); //create Grizzly http2 server

    }

    private static String loadDatabaseFileName() {
        //properties file and anything in the resources folder will be available in the compiled jar file
        // which will be on the classpath and therefore, the properties file can be accessed
        try(InputStream propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")){
        Properties properties = new Properties();
        properties.load(propertiesStream);
        return properties.getProperty("course-info.database"); //pass in the key of the property to get back value
        }
        catch(IOException e){
            LOG.error("Could not load database filename");
            throw new IllegalStateException("Could not load database filename", e); //IllegalStateException is a Runtime Exception indicating we cant do anything abt it
        }
    }

    private static String loadBaseURI() {
        //properties file and anything in the resources folder will be available in the compiled jar file
        // which will be on the classpath and therefore, the properties file can be accessed
        try(InputStream propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")){
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("base-uri"); //pass in the key of the property to get back value
        }
        catch(IOException e){
            LOG.error("Could not load base-uri");
            throw new IllegalStateException("Could not load base-uri", e); //IllegalStateException is a Runtime Exception indicating we cant do anything abt it
        }
    }

}
