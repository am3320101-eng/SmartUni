package com.example.demo.service;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

@Service
public class AcademicService {
    private final CourseRepository courseRepository;
    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    public AcademicService(CourseRepository courseRepository, LocationRepository locationRepository, SessionRepository sessionRepository) {
        this.courseRepository = courseRepository;
        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    public Course saveCourse(Course course) { return courseRepository.save(course); }
    public Location saveLocation(Location location) { return locationRepository.save(location); }
    public Session saveSession(Session session) { return sessionRepository.save(session); }
}