package com.francislopvil.userapi.service;

import com.francislopvil.userapi.entity.User;
import com.francislopvil.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository repository;

    private static final String NOTFOUNDMSG = "User not found";

    @Override
    public List<User> getAllUsers(){
        return repository.findAll();
    }

    @Override
    public ResponseEntity<Object> getUserById(int id) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isPresent()) return ResponseEntity.ok(userOpt.get());
        return new ResponseEntity<>(NOTFOUNDMSG,HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> createUser(User user) {
        return new ResponseEntity<>(repository.save(user),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteUserById(int id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.ok("OK");
        }
        return new ResponseEntity<>(NOTFOUNDMSG, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> updateUsersById(User user, int id) {
        if (repository.existsById(id)){
            user.setId(id);
            repository.save(user);
            return ResponseEntity.ok(user);
        }
        return new ResponseEntity<>(NOTFOUNDMSG, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> updatePartialUsersById(User user, int id) {
        if (repository.existsById(id)){
            try {
                User updatedUser = removeNulls(user,id);
                repository.save(updatedUser);
                return ResponseEntity.ok(user);
            }catch (Exception e){
                return new ResponseEntity<>(NOTFOUNDMSG, HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        return new ResponseEntity<>(NOTFOUNDMSG, HttpStatus.NOT_FOUND);
    }


    /**
     * Remove nulls values to user entity
     * @param user user object to be updated
     * @param id user id used to collect the current data
     * @return user updated with all null values replaced with storaged values
     */
    public User removeNulls(User user, int id) throws Exception {
        Optional<User> userOpt =repository.findById(id);
        if (userOpt.isPresent()){
            User oldValues = userOpt.get();
            user.setId(id);

            if (user.getName()==null) user.setName(oldValues.getName());
            if (user.getEmail()==null) user.setEmail(oldValues.getEmail());
            if (user.getBirthDate()==null) user.setBirthDate(oldValues.getBirthDate());
            if (user.getAddress()==null) user.setAddress(oldValues.getAddress());

            return user;
        }
        throw new Exception();
    }

}
