package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    // Bir kullanıcının tüm ödünç kayıtları (geçmiş dahil)
    List<Borrow> findByUserId(Long userId);

    // Bir kullanıcının iade edilmemiş kitapları (returnDate null olanlar)
    List<Borrow> findByUserIdAndReturnDateIsNull(Long userId);

    // Bir kitabın şu an ödünç verilip verilmediğini kontrol etmek için
    Optional<Borrow> findByBookIdAndReturnDateIsNull(Long bookId);
}
