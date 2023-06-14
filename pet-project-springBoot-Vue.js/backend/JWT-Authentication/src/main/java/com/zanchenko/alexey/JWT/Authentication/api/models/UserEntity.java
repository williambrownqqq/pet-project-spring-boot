package com.zanchenko.alexey.JWT.Authentication.api.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Eager - when we load this we always want out relationship to be shown whenever you load a user you always want your roles to be shown
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))// is going to create JOIN Table that we was talkign about
    // join columns represent the actual columns, the user ID and the role ID
    List<Role> roles = new ArrayList<>();

}
