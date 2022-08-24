package ru.practicum.shareit.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "ALTER TABLE users AUTO_INCREMENT=0")
    void setAutoincrement();
}
