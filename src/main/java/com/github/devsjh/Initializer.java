package com.github.devsjh;

import com.github.devsjh.model.Event;
import com.github.devsjh.model.Group;
import com.github.devsjh.model.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @title  : Spring Boot with React CRUD App
 * @author : jaeha-dev (Git)
 * @refer  : https://developer.okta.com/blog/2018/07/19/simple-crud-react-and-spring-boot
 */
@Component
class Initializer implements CommandLineRunner {

    private final GroupRepository groupRepository;

    public Initializer(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of("Denver JUG", "Utah JUG", "Seattle JUG", "Richmond JUG")
              .forEach(name -> groupRepository.save(new Group(name))
        );

        Group denverJug = groupRepository.findByName("Denver JUG");

        Event e = Event.builder().title("Full Stack Reactive")
                                 .description("Reactive with Spring Boot + React")
                                 .date(Instant.parse("2020-06-06T22:00:00.000Z"))
                                 .build();

        denverJug.setEvents(Collections.singleton(e));
        groupRepository.save(denverJug);
        groupRepository.findAll().forEach(System.out::println);
    }
}