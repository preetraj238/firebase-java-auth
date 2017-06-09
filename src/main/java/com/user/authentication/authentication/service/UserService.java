package com.user.authentication.authentication.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.tasks.Task;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by preetraj on 6/8/17.
 */
@RestController
public class UserService {

    @Autowired
    private FirebaseAuth firebaseAuth;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @ApiOperation(value = "createUser", nickname = "createUser")
    @RequestMapping(method = RequestMethod.POST, path = "/createUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                                        @RequestParam("name") String namme) throws Exception {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisplayName(namme)
                .setDisabled(false);

        UserRecord record = null;
        Task<UserRecord> task = firebaseAuth.createUser(request).addOnSuccessListener(userRecord -> {
            String id = userRecord.getUid();
            logger.info("Successfully created new user: {}", id);
            ResponseEntity.ok(firebaseAuth.getUser(id));
        })
                .addOnFailureListener(e -> {
                    logger.error("Error creating new user: ", e);
                });
        while (!task.isComplete()) {
            logger.info("checking...");

        }
        record = task.getResult();
        return ResponseEntity.ok(record);
    }
}
