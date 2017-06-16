package com.user.authentication.authentication.service;

import com.user.authentication.authentication.domain.User;
import com.user.authentication.authentication.helper.UserHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by preetraj on 6/8/17.
 */
@RestController
@RequestMapping("/user")
public class UserService {

    @Autowired
    private UserHelper userHelper;

    @ApiOperation(value = "createUser", nickname = "createUser")
    @RequestMapping(method = RequestMethod.POST, path = "/createUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userHelper.createUser(user));
    }

    @ApiOperation(value = "allUsers", nickname = "allUsers")
    @RequestMapping(method = RequestMethod.GET, path = "/allUsers")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> getUsers() throws Exception {
        return ResponseEntity.ok(userHelper.getAllUsers());
    }


    @ApiOperation(value = "updateUser", nickname = "updateUser")
    @RequestMapping(method = RequestMethod.POST, path = "/updateUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> updateUser(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userHelper.updateUserDetails(user));
    }

    @ApiOperation(value = "authenticateToken", nickname = "authenticateToken")
    @RequestMapping(method = RequestMethod.POST, path = "/authenticateToken")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> authenticateToken(@RequestParam("token") String token) throws Exception {
        return ResponseEntity.ok(userHelper.authenticateToken(token));
    }
}
