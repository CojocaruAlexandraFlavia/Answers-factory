package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.UserDto;
import org.jetbrains.annotations.NotNull;

public class UserDtoMock {

    public static @NotNull UserDto userDto() {
        UserDto dto = new UserDto();
        dto.setEmail("email@email.com");
        dto.setFirstName("firstName");
        dto.setLastName("lastName");
        return dto;
    }

}
