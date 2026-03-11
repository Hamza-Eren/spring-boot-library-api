package com.hamzaeren.course.spring.service;

import com.hamzaeren.course.spring.dto.BorrowRequest;
import com.hamzaeren.course.spring.dto.BorrowResponse;
import com.hamzaeren.course.spring.entity.Book;
import com.hamzaeren.course.spring.entity.Borrow;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.repository.BookRepository;
import com.hamzaeren.course.spring.repository.BorrowRepository;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kitap ödünç alma ve iade etme süreçlerini yöneten servis sınıfı.
 * Kullanıcıların kitap ödünç almasını, iade etmesini ve okuma geçmişlerini
 * izlemesini sağlar.
 *
 * @author Hamza Eren Sarpdağ
 * @version 1.0
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Kullanıcının sistemden bir kitabı ödünç almasını sağlar.
     * Kitabın şu anda başka bir kullanıcıda olup olmadığını kontrol eder.
     *
     * @param userId  Ödünç alma işlemini gerçekleştiren kullanıcının benzersiz
     *                ID'si
     * @param request Ödünç alınmak istenen kitabın bilgisi
     * @return Başarılı ödünç alma işleminin detaylarını içeren DTO
     * @throws RuntimeException Kitap zaten başkasındaysa veya kullanıcı/kitap
     *                          bulunamazsa
     */
    @Transactional
    public BorrowResponse borrowBook(Long userId, BorrowRequest request) {
        // Kitabın başka biri tarafından ödünç alınıp alınmadığını kontrol et
        borrowRepository.findByBookIdAndReturnDateIsNull(request.getBookId()).ifPresent(b -> {
            throw new RuntimeException("Bu kitap şu an ödünç alınmış durumda.");
        });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: ID " + userId));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: ID " + request.getBookId()));

        Borrow borrow = new Borrow(LocalDate.now());
        borrow.assignUser(user);
        borrow.assignBook(book);

        return mapToResponse(borrowRepository.save(borrow));
    }

    /**
     * Kullanıcının daha önce ödünç aldığı bir kitabı sisteme iade etmesini sağlar.
     * İşlemi yapan kişinin kitabı ödünç alan kişi olup olmadığını doğrular.
     *
     * @param borrowId İade edilecek ödünç işleminin işlem (kayıt) ID'si
     * @param userId   İşlemi yapan kullanıcının benzersiz ID'si
     * @return İade işleminin güncel durumu ve detayları
     * @throws RuntimeException Kayıt başkasına aitse veya daha önceden iade
     *                          edilmişse
     */
    @Transactional
    public BorrowResponse returnBook(Long borrowId, Long userId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı: ID " + borrowId));

        // Başkasının kaydını iade etmeye çalışıyor mu?
        if (!borrow.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu ödünç kaydı size ait değil.");
        }

        // Zaten iade edilmiş mi?
        if (borrow.getReturnDate() != null) {
            throw new RuntimeException("Bu kitap zaten iade edilmiş.");
        }

        borrow.markAsReturned(LocalDate.now());
        return mapToResponse(borrowRepository.save(borrow));
    }

    public List<BorrowResponse> getMyBorrows(Long userId) {
        return borrowRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BorrowResponse> getMyActiveBorrows(Long userId) {
        return borrowRepository.findByUserIdAndReturnDateIsNull(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BorrowResponse mapToResponse(Borrow borrow) {
        return new BorrowResponse(
                borrow.getId(),
                borrow.getBook().getId(),
                borrow.getBook().getTitle(),
                borrow.getBook().getIsbn(),
                borrow.getBorrowDate(),
                borrow.getReturnDate());
    }
}
