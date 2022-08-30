package ru.practicum.shareit.item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;
    CommentDto commentDto = CommentDto.builder().text("text").authorName("test").build();
    private List<CommentDto> list = List.of(commentDto);
    ItemDto itemDto = ItemDto.builder().description("test").comments(list).description("desc")
            .build();

    @Test
    public void testSerialization() throws IOException {
        var expectedJson = "{\"id\":0,\"name\":null,\"description\":\"desc\",\"available\":null," +
                "\"requestId\":0,\"comments\":[{\"id\":0,\"text\":\"text\",\"item\":0,\"authorName\":" +
                "\"test\",\"created\":null}],\"lastBooking\":null,\"nextBooking\":null}";
        assertThat(json.write(itemDto)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserialization() throws IOException {
        var expectedJson = "{\"id\":0,\"name\":null,\"description\":\"desc\",\"available\":null," +
                "\"requestId\":0,\"comments\":[{\"id\":0,\"text\":\"text\",\"item\":0,\"authorName\":" +
                "\"test\",\"created\":null}],\"lastBooking\":null,\"nextBooking\":null}";
        assertThat(json.parse(expectedJson)).isEqualTo(itemDto);
    }
}
