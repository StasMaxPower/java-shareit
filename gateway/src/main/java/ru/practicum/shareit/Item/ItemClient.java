package ru.practicum.shareit.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.Item.dto.CommentDto;
import ru.practicum.shareit.Item.dto.ItemDto;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllToOwner(long owner, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", owner, parameters);
    }

    public ResponseEntity<Object> search(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size

        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }


    public ResponseEntity<Object> addItem(ItemDto itemDto, Long owner) {
        return post("", owner, itemDto);
    }

    public ResponseEntity<Object> addComment(CommentDto commentDto, Long owner, Long itemId) {
        return post("/" + itemId + "/comment", owner, commentDto);
    }

    public ResponseEntity<Object> getItemById(long id, Long owner) {
        return get("/" + id, owner);
    }

    public ResponseEntity<Object> updateById(ItemDto itemDto, Long id, Long owner) {
        return patch("/" + id, owner, itemDto);
    }


}
