package com.ai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @Column(length = 10,name = "login_id")
    @NotBlank(message = "Student ID is required!")
    private String loginId;
    @Column(nullable = false)
    @NotBlank(message = "Student Name is required!")
    private String name;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Student Email is required!")
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(name = "is_active", nullable = false)
    public boolean isActive;
	@ManyToMany
    @JoinTable(name = "batch_has_user",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "batch_id"))
    @JsonIgnore
	private List<Batch> batches;

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Message> messages;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Comment> comments;

	@Column(name = "photo",columnDefinition = "VARCHAR(255) DEFAULT 'default.png'", nullable = true)
	private String photo;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "student", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)

	@Transient
    private Integer[] batchId;

    public enum Role{
        Admin,
        Teacher,
        Student
    }
}
