package com.hamzaeren.course.spring.service;

import com.hamzaeren.course.spring.dto.AuthorRequest;
import com.hamzaeren.course.spring.dto.AuthorResponse;
import com.hamzaeren.course.spring.dto.AuthorUpdate;
import com.hamzaeren.course.spring.entity.Author;
import com.hamzaeren.course.spring.entity.Role;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.mapper.AuthorMapper;
import com.hamzaeren.course.spring.repository.AuthorRepository;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




/**
 * Yazar (Author) verileriyle ilgili iş kurallarını işleten servis sınıfı.
 * Yazar ekleme, listeleme, silme ve güncelleme gibi CRUD işlemlerini yönetir.
 *
 * @author Hamza Eren Sarpdağ
 * @version 1.0
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final AuthorMapper authorMapper;

    /**
     * Mevcut bir kullanıcıyı YAZAR (AUTHOR) rolüne yükseltir.
     * Kullanıcıya bağlı otomatik bir yazar hesabı oluşturur.
     *
     * @param userId Yazar yapılacak kullanıcının ID'si
     * @return Yeni oluşturulan yazarın bilgileri
     */
    @Transactional
    public AuthorResponse assignAuthorRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: ID " + userId));

        if (user.getRole() == Role.AUTHOR || user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Kullanıcı zaten yazar veya üst düzey yetkiye sahip.");
        }

        user.setRole(Role.AUTHOR);
        userRepository.save(user);

        // İsim olarak varsa profil fullName, yoksa direkt username kullanıyoruz.
        String authorName = user.getProfile() != null && user.getProfile().getFullName() != null
                ? user.getProfile().getFullName()
                : user.getUsername();

        Author newAuthor = new Author(authorName, "Biyografi eklenmemiş.", user);
        return authorMapper.toResponse(authorRepository.save(newAuthor));
    }

    /**
     * Sisteme dışarıdan (tarihi veya sisteme giriş yapmayacak) yeni bir yazar
     * ekler.
     * Bu metodla eklenen yazarların sisteme login olma (User) yeteneği YOKTUR.
     * (Örn: Dostoyevski, Shakespeare gibi yazarların eklenmesi için kullanılır)
     * Aynı isimde başka bir yazarın olup olmadığını kontrol eder.
     *
     * @param request Eklenecek yazarın adı ve biyografi bilgilerini içeren DTO
     *                nesnesi
     * @return Sisteme kaydedilmiş olan yazarın bilgileri
     * @throws RuntimeException Eğer aynı isimde bir yazar sistemde zaten mevcutsa
     */
    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Bu isimde bir yazar zaten kayıtlı.");
        }
        Author author = new Author(request.getName(), request.getBio());
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    /**
     * Sistemdeki tüm yazarları sayfalı olarak getirir.
     *
     * @param pageable Sayfalama bilgisi (page, size, sort)
     * @return Yazar bilgilerini içeren sayfalı yapı
     */
    public org.springframework.data.domain.Page<AuthorResponse> getAllAuthors(org.springframework.data.domain.Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toResponse);
    }

    public AuthorResponse getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı: ID " + id));
        return authorMapper.toResponse(author);
    }

    @Transactional
    public AuthorResponse updateAuthor(Long id, AuthorUpdate request, Long callerId, boolean isAdmin) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı: ID " + id));

        // Sahiplik / Yetki Kontrolü
        if (!isAdmin) {
            Author currentAuthor = authorRepository.findByUserId(callerId)
                    .orElseThrow(() -> new RuntimeException("Size bağlı bir yazar hesabı bulunamadı."));
            if (!author.getId().equals(currentAuthor.getId())) {
                throw new RuntimeException("Sadece kendi yazar profilinizi güncelleyebilirsiniz.");
            }
        }

        // Eğer yeni bir isim geldiyse çakışma kontrolü yap
        if (request.getName() != null) {
            authorRepository.findByName(request.getName()).ifPresent(existingAuthor -> {
                if (!existingAuthor.getId().equals(id)) {
                    throw new RuntimeException("Bu isimde başka bir yazar zaten kayıtlı.");
                }
            });
            author.setName(request.getName());
        }

        if (request.getBio() != null) {
            author.setBio(request.getBio());
        }

        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Yazar bulunamadı: ID " + id);
        }
        authorRepository.deleteById(id);
    }


}
