package com.user.authentication.authentication.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.tasks.Task;
import com.user.authentication.authentication.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by preetraj on 6/10/17.
 */
@Component
public class UserHelper {

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private DatabaseReference databaseReference;

    private DatabaseReference usersRef;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${firebase.users.url}")
    private String firebaseUsersUrl;

    @PostConstruct
    public void init() {
        usersRef = databaseReference.child("users");
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(UserHelper.class);

    public User createUser(User user) throws Exception {
        String displayName = user.getFirstName().concat(" ").concat(user.getLastName());
        UserRecord record = null;
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setEmailVerified(false)
                .setPassword(user.getPassword())
                .setDisplayName(displayName)
                .setDisabled(false);

        Task<UserRecord> task = firebaseAuth.createUser(request).addOnSuccessListener(userRecord -> {
            String id = userRecord.getUid();
            logger.info("Successfully created new user: {}", id);
            ResponseEntity.ok(firebaseAuth.getUser(id));
        })
                .addOnFailureListener(e -> {
                    logger.error("Error creating new user: ", e);
                });
        while (!task.isComplete()) {
            //logger.info("checking...");

        }
        record = task.getResult();
        logger.info("Successfully created user {}", user.getPhoneNumber());
        user.setPassword("******");
        user.setFirebaseUserId(record.getUid());
        usersRef.child(user.getPhoneNumber()).setValue(user);
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() throws Exception {
        List<User> users = new ArrayList<>();

        ResponseEntity<Map> entity = restTemplate.exchange(firebaseUsersUrl,
                HttpMethod.GET, null, Map.class);

        Map<String, Object> result = entity.getBody();

        result.forEach((x, y) -> users.add(mapper.convertValue(y, User.class)));

        return users;
    }


    public User updateUserDetails(User user) throws Exception {
        Map<String, Object> map = mapper.convertValue(user, new TypeReference<Map<String, Object>>() {
        });
        usersRef.child(user.getPhoneNumber()).updateChildren(map);

        return user;
    }
}
