package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;
    UserDto userDto = UserDto.builder().name("test").email("test1@test1.ru").build();

    @Test
    public void testSerialization() throws IOException {
        var expectedJson = "{\"name\":\"test\",\"email\":\"test1@test1.ru\"}";
        assertThat(json.write(userDto)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserialization() throws IOException {
        var expectedJson = "{\"name\":\"test\",\"email\":\"test1@test1.ru\"}";
        assertThat(json.parse(expectedJson)).isEqualTo(userDto);
    }
}
