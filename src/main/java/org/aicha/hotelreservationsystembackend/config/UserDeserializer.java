package org.aicha.hotelreservationsystembackend.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.aicha.hotelreservationsystembackend.domain.User;
import org.aicha.hotelreservationsystembackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class UserDeserializer extends JsonDeserializer<User> {

    @Autowired
    private UserService userService;

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String userId = p.getText();
        return userService.getUserById(UUID.fromString(userId));
    }
}