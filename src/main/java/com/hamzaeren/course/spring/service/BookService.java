package com.hamzaeren.course.spring.service;


import com.hamzaeren.course.spring.dto.BookRequest;
import com.hamzaeren.course.spring.dto.BookResponse;
import com.hamzaeren.course.spring.dto.BookUpdate;
import com.hamzaeren.course.spring.entity.Author;
import com.hamzaeren.course.spring.entity.Book;
import com.hamzaeren.course.spring.mapper.BookMapper;
import com.hamzaeren.course.spring.repository.BookRepository;
import com.hamzaeren.course.spring.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kitaplarla ilgili iş mantığının (Business Logic) yürütüldüğü servis sınıfı.
 * Veritabanı işlemleri için repository'leri kullanır ve DTO dönüşümlerini
 * sağlar.
 *
 * @author Hamza Eren Sarpdağ
 * @version 1.0
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    /**
     * Sisteme yeni bir kitap ekler.
     * Gönderilen ISBN numarasının benzersiz olup olmadığını kontrol eder.
     *
     * @param request Eklenecek kitabın detaylarını içeren DTO nesnesi
     * @return Kaydedilen kitabın veritabanı ID'si ve detaylarını içeren yanıt
     *         nesnesi
     * @throws RuntimeException Eğer ISBN numarası sistemde zaten mevcutsa
     */
    @Transactional
    public BookResponse createBook(BookRequest request, Long callerId, boolean isAdmin) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new RuntimeException("Bu ISBN'de başka bir kitap zaten kayıtlı.");
        }

        Author author;
        // Eğer istek atan kişi ADMIN ise ve body'de authorId verildiyse o yazara ait
        // ekler.
        if (isAdmin && request.getAuthorId() != null) {
            author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Yazar bulunamadı: " + request.getAuthorId()));
        } else {
            // Eğer istek atan AUTHOR ise, kendi adına ekler.
            author = authorRepository.findByUserId(callerId)
                    .orElseThrow(() -> new RuntimeException("Size bağlı bir yazar hesabı bulunamadı."));
        }

        Book book = new Book(request.getTitle(), request.getIsbn(), request.getPublishedDate(), author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    /**
     * Sistemdeki kitapları sayfalı olarak listeler.
     * Eğer yazar ID'si gönderilirse sadece o yazara ait kitapları filtreler.
     *
     * @param authorId Filtreleme yapılmak istenen yazarın ID'si (Opsiyonel)
     * @param pageable Sayfalama bilgisi
     * @return Kitapların sayfalı listesi
     */
    public org.springframework.data.domain.Page<BookResponse> getAllBooks(Long authorId, org.springframework.data.domain.Pageable pageable) {
        if (authorId != null) {
            return bookRepository.findByAuthorId(authorId, pageable)
                    .map(bookMapper::toResponse);
        }
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponse);
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: ID " + id));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdate request, Long callerId, boolean isAdmin) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: ID " + id));

        // Sahiplik / Yetki Kontrolü
        if (!isAdmin) {
            Author currentAuthor = authorRepository.findByUserId(callerId)
                    .orElseThrow(() -> new RuntimeException("Size bağlı bir yazar hesabı bulunamadı."));
            if (!book.getAuthor().getId().equals(currentAuthor.getId())) {
                throw new RuntimeException("Bu kitabı güncellemek için yetkiniz yok.");
            }
        }

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }

        if (request.getIsbn() != null) {
            bookRepository.findByIsbn(request.getIsbn()).ifPresent(existingBook -> {
                if (!existingBook.getId().equals(id)) {
                    throw new RuntimeException("Bu ISBN'de başka bir kitap zaten kayıtlı.");
                }
            });
            book.setIsbn(request.getIsbn());
        }

        if (request.getPublishedDate() != null) {
            book.setPublishedDate(request.getPublishedDate());
        }

        if (request.getAuthorId() != null) {
            if (!isAdmin) {
                throw new RuntimeException("Kitabın yazarını değiştirme yetkisi sadece ADMIN'e aittir.");
            }
            book.setAuthor(authorRepository.findById(request.getAuthorId()).orElseThrow());
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id, Long callerId, boolean isAdmin) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: ID " + id));

        // Sahiplik / Yetki Kontrolü
        if (!isAdmin) {
            Author currentAuthor = authorRepository.findByUserId(callerId)
                    .orElseThrow(() -> new RuntimeException("Size bağlı bir yazar hesabı bulunamadı."));
            if (!book.getAuthor().getId().equals(currentAuthor.getId())) {
                throw new RuntimeException("Bu kitabı silmek için yetkiniz yok.");
            }
        }

        bookRepository.deleteById(id);
    }



}
