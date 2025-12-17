package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByUser(User user);

    Page<Card> findAllByUser(User user, Pageable pageable);

    Page<Card> findAllByUserOrderByExpirationDate(User user, Pageable pageable);

    @Query(value = "SELECT * FROM Card as c WHERE c.user_id = :user_id AND c.serial_number LIKE CONCAT('%', :last4digits)", nativeQuery = true)
    Page<Card> findAllByUserAndLast4Digits(@Param("user_id") Long userId, @Param("last4digits") String last4digits, Pageable pageable);

    Optional<Card> findByUserAndId(User user, Long id);
}
