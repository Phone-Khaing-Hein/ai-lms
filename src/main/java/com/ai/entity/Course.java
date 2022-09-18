package com.ai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Course Name is required!")
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private List<Module> modules;
    
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private List<Batch> batches;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Exam> exams;
}
