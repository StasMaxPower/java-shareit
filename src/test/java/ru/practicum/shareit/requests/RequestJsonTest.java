package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.io.IOException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;
    ItemRequest.ShortItem item = new ItemRequest.ShortItem(0,
            "name", "desc", true, 1);
    Set<ItemRequest.ShortItem> items = Set.of(item);
    ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("test").items(items)
            .build();

    @Test
    public void testSerialization() throws IOException {

        var expectedJson = "{\"id\":0,\"description\":\"test\",\"created\":null,\"items\":" +
                "[{\"id\":0,\"name\":\"name\",\"description\":\"desc\",\"available\":true," +
                "\"requestId\":1}]}";
        assertThat(json.write(itemRequestDto)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserialization() throws IOException {
        var expectedJson = "{\"id\":0,\"description\":\"test\",\"created\":null,\"items\":" +
                "[{\"id\":0,\"name\":\"name\",\"description\":\"desc\",\"available\":true," +
                "\"requestId\":1}]}";
        assertThat(json.parse(expectedJson)).isEqualTo(itemRequestDto);
    }
}
