package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * // TODO .
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private  final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestDto addRequests(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                      @RequestHeader("X-Sharer-User-Id") int userId){
        return  itemRequestService.add(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") int userId){
        return  itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @PathVariable int requestId){
        return  itemRequestService.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestParam(defaultValue = "-1")  int from,
                                                     @RequestParam(defaultValue = "-1")  int size,
                                                     @RequestHeader("X-Sharer-User-Id") int userId){

        return  itemRequestService.getAllRequests(from, size, userId);
    }


}
