package com.ai.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @Column(length = 10,name = "login_id")
    @NotBlank(message = "User Login Id is required!")
    private String loginId;
    @Column(nullable = false)
    @NotBlank(message = "Username is required!")
    private String name;
    @Column(nullable = false)
    @NotBlank(message = "User email is required!")
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_active", nullable = false)
    public boolean isActive;
	@ManyToOne
	private Batch batch;

    public enum Role{
        Admin,
        Teacher,
        Student
    }
}
