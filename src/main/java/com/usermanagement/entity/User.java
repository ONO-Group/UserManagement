package com.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private int status;

    @CreationTimestamp
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

    @Column(name = "last_login")
    private Timestamp lastLogin;


    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)//self relation with user
    @JoinColumn(name = "created_by")
    private User createdBy;



    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;


}
