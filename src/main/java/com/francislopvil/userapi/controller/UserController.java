package com.francislopvil.userapi.controller;

import com.francislopvil.userapi.entity.User;
import com.francislopvil.userapi.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserServiceImpl service;

    /**
     * Exception handler used when params have different type that required
     * @return ResponseEntity object with error code 400 and "Invalid user id" message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleException(){
        return new ResponseEntity<>("Invalid user id", HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler used when null values are sent to entity properties which can't be null
     * @return ResponseEntity object with error code 405 and "Invalid input" message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleExceptionInvalidInput(){
        return new ResponseEntity<>("Invalid input", HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Gets all users
     * @return All users listed
     */
    @GetMapping("/getUsers")
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    /**
     * Gets the user specified by the id parameter
     * @param id the user id
     * @return if id exists, user object is returned. Otherwise, a 404 error is sent.
     */
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id){
        return service.getUserById(id);
    }

    /**
     * Creates a new user
     * @param user the data of the user to be created
     * @return the new user created is returned
     */
    @PostMapping("/createUsers")
    public ResponseEntity<Object> createUser(@RequestBody User user){
        return service.createUser(user);
    }

    /**
     * Deletes the user specified by the id parameter
     * @param id the user id
     * @return if id exists, user is deleted and "OK" message is returned. Otherwise, a 404 error is sent and no user is removed.
     */
    @DeleteMapping("/deleteUsersById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        return service.deleteUserById(id);
    }

    /**
     * Updates the user specified by the id parameter with the data of the 'user' parameter
     * @param user data to be applied to the user in order to update it
     * @param id the user id
     * @return if id exists, user is updated and user object is returned. Otherwise, a 404 error is sent and no user is updated.
     */
    @PutMapping("/updateUsersById/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable int id){
        return service.updateUsersById(user,id);
    }

    /**
     * Updates the user specified by the id parameter with the data of the 'user' parameter, even when null values exists
     * @param user data to be applied to the user in order to update it
     * @param id the user id
     * @return if id exists, user is updated and user object is returned. Otherwise, a 404 error is sent and no user is updated.
     */
    @PatchMapping("/updateUsersById/{id}")
    public ResponseEntity<Object> updatePartialUser(@RequestBody User user, @PathVariable int id){
        return service.updatePartialUsersById(user,id);
    }
}
