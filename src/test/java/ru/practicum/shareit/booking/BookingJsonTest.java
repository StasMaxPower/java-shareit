package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    UserDto userDto = UserDto.builder().name("test").email("test1@test1.ru").build();
    ItemDto itemDto = ItemDto.builder().description("test").description("desc")
            .build();
    BookingDto bookingDto = new BookingDto(1, null, null,
            itemDto, userDto, Status.WAITING);

    @Test
    public void testSerialization() throws IOException {
        var expectedJson = "{\"id\":1,\"start\":null,\"end\":null,\"item\":{\"id\":0," +
                "\"name\":null,\"description\":\"desc\",\"available\":null,\"requestId\":0," +
                "\"comments\":null,\"lastBooking\":null,\"nextBooking\":null},\"booker\":{\"id\":0," +
                "\"name\":\"test\",\"email\":\"test1@test1.ru\"},\"status\":\"WAITING\"}";
        assertThat(json.write(bookingDto)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserialization() throws IOException {
        var expectedJson = "{\"id\":1,\"start\":null,\"end\":null,\"item\":{\"id\":0,\"name\":" +
                "null,\"description\":\"desc\",\"available\":null,\"requestId\":0,\"comments\":null,\"" +
                "lastBooking\":null,\"nextBooking\":null},\"booker\":{\"id\":0,\"name\":\"test\",\"" +
                "email\":\"test1@test1.ru\"},\"status\":\"WAITING\"}";
        assertThat(json.parse(expectedJson)).isEqualTo(bookingDto);
    }
}
