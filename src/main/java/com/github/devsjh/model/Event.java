package com.github.devsjh.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.Instant;
import java.util.Set;

/**
 * @title  : Event 엔터티 클래스 (도메인 모델)
 * @author : jaeha-dev (Git)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private Instant date; // JDK 1.8 (Time API)
    private String title;
    private String description;

    @ManyToMany
    private Set<User> attendees;
}