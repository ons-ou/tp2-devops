package com.userManagement.controller;

import com.userManagement.dto.ResponseDto;
import com.userManagement.dto.UsersDto;
import com.userManagement.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/fetchUserByUsername/{username}")
    public ResponseEntity<?> fetchUserByUsernameEndpoint(@PathVariable String username) {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), usersService.fetchUserByUsername(username)));
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUserEndpoint(@RequestBody UsersDto usersDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.value(), usersService.addNewUser(usersDto)));
    }

    @GetMapping("/fetchAllUsers")
    public ResponseEntity<?> fetchAllUsersEndpoint() {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), usersService.fetchAllUsers()));
    }
}
