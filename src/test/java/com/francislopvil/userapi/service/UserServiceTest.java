package com.francislopvil.userapi.service;

import com.francislopvil.userapi.entity.Address;
import com.francislopvil.userapi.entity.User;
import com.francislopvil.userapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userMock;

    @InjectMocks
    UserServiceImpl userImpl;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        Address a1 = new Address(1,"Test Street","TestLand","New Test City","United States of Tests","TEST");
        User u1 =  new User(1,"Test","Test", LocalDateTime.now(),a1);
        User u2 =  new User(2,"Test2","Test2", LocalDateTime.now(),a1);
        User u3 =  new User(3,"Test3","Test3", LocalDateTime.now(),a1);

        users.add(u1);
        users.add(u2);
        users.add(u3);
    }

    @DisplayName("Get all users")
    @Test
    void testGetAllUsers(){
        BDDMockito.given(userMock.findAll()).willReturn(users);
        List<User> userList = userImpl.getAllUsers();

        org.assertj.core.api.Assertions.assertThat(userList).isNotNull();
        Assertions.assertEquals(users,userList);
    }

    @DisplayName("Get User By Id")
    @Test
    void testGetUserById(){
        final int id = 1;
        final int notFoundId = 2;
        final User user = users.get(0);

        BDDMockito.given(userMock.findById(id)).willReturn(Optional.of(user));

        ResponseEntity<?> OKresponse = userImpl.getUserById(id);
        ResponseEntity<?> NFresponse = userImpl.getUserById(notFoundId);

        //200 Response
        Assertions.assertEquals(ResponseEntity.ok(user),OKresponse);

        //404 Response
        Assertions.assertEquals(new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND),NFresponse);

    }

    @DisplayName("User creation test")
    @Test
    void testCreateUser(){
        final Address newAddress = new Address(1,"Test Street","TestLand","New Test City","United States of Tests","TEST");
        final User newUser = new User(4,"New User","new@user.io", LocalDateTime.now(),newAddress);

        BDDMockito.given(userMock.save(newUser)).willReturn(newUser);

        User userCreated = (User)userImpl.createUser(newUser).getBody();

        Assertions.assertEquals(newUser, userCreated);
        org.assertj.core.api.Assertions.assertThat(userCreated).isNotNull();
    }

    @DisplayName("Invalid user deletion test")
    @Test
    void testDeleteInvalidUser(){
        final int id = 99999;
        BDDMockito.given(userMock.existsById(id)).willReturn(false);

        ResponseEntity<String> responseEntity = userImpl.deleteUserById(id);

        Mockito.verify(userMock, Mockito.times(1)).existsById(id);
        Mockito.verify(userMock, Mockito.times(0)).deleteById(id);
        Assertions.assertEquals("User not found", responseEntity.getBody());
    }

    @DisplayName("User deletion test")
    @Test
    void testDeleteValidUser(){
        final int id = 1;
        BDDMockito.given(userMock.existsById(id)).willReturn(true);

        ResponseEntity<String> responseEntity = userImpl.deleteUserById(id);

        Mockito.verify(userMock, Mockito.times(1)).existsById(id);
        Mockito.verify(userMock, Mockito.times(1)).deleteById(id);
        Assertions.assertEquals("OK", responseEntity.getBody());
    }

    @DisplayName("Invalid user update test")
    @Test
    void testUpdateInvalidUsersById(){
        final Address newAddress = new Address(1,"Test Street","TestLand","New Test City","United States of Tests","TEST");
        final User newUser = new User(4,"New User","new@user.io", LocalDateTime.now(),newAddress);
        final int id = newUser.getId();

        BDDMockito.given(userMock.existsById(newUser.getId())).willReturn(false);
        newUser.setName("Updated User");

        userImpl.updateUsersById(newUser, id).getBody();

        Mockito.verify(userMock, Mockito.times(1)).existsById(id);
        Mockito.verify(userMock, Mockito.times(0)).save(newUser);

        org.assertj.core.api.Assertions.assertThat(newUser).isNotNull();
    }

    @DisplayName("User update test")
    @Test
    void testUpdateValidUsersById(){
        final Address newAddress = new Address(1,"Test Street","TestLand","New Test City","United States of Tests","TEST");
        final User newUser = new User(4,"New User","new@user.io", LocalDateTime.now(),newAddress);
        final int id = newUser.getId();

        BDDMockito.given(userMock.existsById(id)).willReturn(true);

        newUser.setName("Updated User");

        userImpl.updateUsersById(newUser, id).getBody();

        Mockito.verify(userMock, Mockito.times(1)).existsById(id);
        Mockito.verify(userMock, Mockito.times(1)).save(newUser);
        org.assertj.core.api.Assertions.assertThat(newUser).isNotNull();
    }

    @DisplayName("User nulls values removed test")
    @Test
    void testRemoveNullsValues() throws Exception {
        final User userToBeUpdated = users.get(0);
        final int id = userToBeUpdated.getId();
        final User newData = new User(id,"User updated",null, null,null);

        BDDMockito.given(userMock.findById(id)).willReturn(Optional.of(userToBeUpdated));
        try {
            User updatedUser = userImpl.removeNulls(newData,id);
            Assertions.assertNotEquals(userToBeUpdated.getName(),updatedUser.getName());
            Assertions.assertEquals(userToBeUpdated.getEmail(),updatedUser.getEmail());
        }catch (Exception e){
            throw new Exception();
        }

    }

}
