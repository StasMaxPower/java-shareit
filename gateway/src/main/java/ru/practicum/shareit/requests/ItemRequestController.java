package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequests(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Add request, userId={}", userId);
        return itemRequestClient.addRequests(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Get request to owner, userId={}", userId);
        return itemRequestClient.getOwnRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable int requestId) {
        log.info("Get request by Id, Id={}, userId={}", requestId, userId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all request, userId={}", userId);
        return itemRequestClient.getAllRequests(from, size, userId);
    }


}
