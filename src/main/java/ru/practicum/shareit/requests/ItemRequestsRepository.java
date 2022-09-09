package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequestorOrderByCreatedDesc(int userId);

    Page<ItemRequest> findAll(Pageable pageable);
}
