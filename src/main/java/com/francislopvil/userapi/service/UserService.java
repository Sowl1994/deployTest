package com.francislopvil.userapi.service;

import com.francislopvil.userapi.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    ResponseEntity<Object> getUserById(int id);

    ResponseEntity<Object> createUser(User user);

    ResponseEntity<String> deleteUserById(int id);

    ResponseEntity<Object> updateUsersById(User user, int id);

    ResponseEntity<Object> updatePartialUsersById(User user, int id);
}
