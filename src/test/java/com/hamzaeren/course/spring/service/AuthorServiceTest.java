package com.hamzaeren.course.spring.service;

import com.hamzaeren.course.spring.dto.AuthorRequest;
import com.hamzaeren.course.spring.dto.AuthorResponse;
import com.hamzaeren.course.spring.entity.Author;
import com.hamzaeren.course.spring.repository.AuthorRepository;
import com.hamzaeren.course.spring.entity.Role;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// Junit 5 Test İfadeleri:
import static org.junit.jupiter.api.Assertions.*;

// Mockito İfadeleri:
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith(MockitoExtension.class): Junit 5'e "Bu test sınıfını çalıştırırken
 * Mockito güçlerini (Yani sahte nesne yaratma yeteneğini) aktif et" diyoruz.
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    /**
     * @Mock: Veritabanına gerçekten bağlanmak İSTEMİYORUZ.
     * Bu yüzden AuthorRepository'nin birebir kopyasını, yani bir Kuklasını (Mock)
     * yaratıyoruz. Bu sahte repository hafızada (RAM) yaşıyor ve sadece bizim 
     * söylediğimiz şeyleri taklit ediyor.
     */
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserRepository userRepository;

    /**
     * @InjectMocks: Bizim GERÇEKTEN test etmek istediğimiz asıl sınıf budur.
     * Mockito'ya diyoruz ki: "Gideceksin gerçek bir AuthorService nesnesi yaratacaksın,
     * ama içine bağımlılık (dependency) olarak yukarıda ürettiğin @Mock (sahte)
     * authorRepository'i gizlice enjekte edeceksin."
     */
    @InjectMocks
    private AuthorService authorService;

    private AuthorRequest sampleRequest;

    /**
     * @BeforeEach: Bu sınıfın içindeki her bir @Test metodu çalışmadan HEMEN ÖNCE
     * çalıştırılan hazırlık (Setup) metodudur. Her test temiz bir veriyle başlasın 
     * diye nesnelerimizi burada oluştururuz. (GWT - Given aşamasının ön hazırlığı)
     */
    @BeforeEach
    void setUp() {
        sampleRequest = new AuthorRequest();
        sampleRequest.setName("George Orwell");
        sampleRequest.setBio("1984 Kitabının Yazarı");
    }

    /**
     * @Test: Bu metodun çalıştırılabilir bağımsız bir Birim Testi olduğunu belirtir.
     * Metod isimlendirmeleri (naming conventions) testin ne yaptığını Düz İngilizce gibi
     * uzun uzun net bir şekilde anlatmalıdır (Örn: shouldReturnResponse_WhenValidRequestIsGiven).
     */
    @Test
    void createAuthor_HappyPath_ReturnsAuthorResponse() {
        // [1] GIVEN (VERİLEN - HAZIRLIK AŞAMASI)
        
        // Mockito'ya kuralları öğretiyoruz. Veritabanına isim sorulduğunda "Yok, bulamadım" demesi lazım:
        // "Eğer authorRepository 'findByName' metodu çağırılırsa ve içine 'George Orwell' yazılırsa,
        // geriye boş (Optional.empty()) bir sonuç dön (Yani sistemde böyle biri yok de)."
        when(authorRepository.findByName("George Orwell")).thenReturn(Optional.empty());

        // Veritabanına kaydet (save) denilirse ne yapacağını öğretiyoruz:
        // "Eğer save metodu, içine herhangi bir Author nesnesi (any(Author.class)) alarak çağrılırsa,
        // geriye ID'si 1 olan, sanki gerçekten DB'ye kaydedilmiş gibi bir Author döndür."
        Author savedAuthor = new Author("George Orwell", "1984 Kitabının Yazarı");
        // Reflection ile private id'yi set eder gibi davranamayız çünkü Id getter'ı yok ama Testler için önemli ise setter açabiliriz 
        // veya direk DTO'nun dönüş değerlerindeki isim/biyografi üzerinden Assertion yapabiliriz.
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        
        // [2] WHEN (AKSİYON / ÇALIŞMASINI İSTEDİĞİMİZ AN)
        
        // Artık asıl test etmek istediğimiz metodu, kendi hazırladığımız request ile çağırıyoruz.
        AuthorResponse response = authorService.createAuthor(sampleRequest);

        
        // [3] THEN (O ZAMAN / DOĞRULAMA - ASSERTION AŞAMASI)
        
        // assertNotNull: "response nesnesi sakın ha null olmasın!" (JUnit metodu)
        assertNotNull(response);
        
        // assertEquals: "response'un getName'i ile George Orwell birebir AYNI MI kontrol et." (JUnit metodu)
        assertEquals("George Orwell", response.getName());
        assertEquals("1984 Kitabının Yazarı", response.getBio());

        // verify: Acaba yazdığımız kod, kurallara uygun şekilde bağımlılıkları çağırdı mı testi?
        // "Mockladığımız authorRepository, acaba SAHİDEN 'save' fonksiyonunu TAM 1 KERE çağırdı mı?" (Mockito metodu)
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorRepository, times(1)).findByName("George Orwell");
    }

    @Test
    void createAuthor_WhenAuthorExists_ThrowsException() {
        // [1] GIVEN
        
        // Bu sefer sahte veritabanımıza DİYORUZ Kİ: 
        // "Biri findByName metodunu çağırırsa, ona dolu (içinde mevcut yazar olan) bir Optional dön"
        Author existingAuthor = new Author("George Orwell", "Ölüm yılı 1950");
        when(authorRepository.findByName("George Orwell")).thenReturn(Optional.of(existingAuthor));

        // [2] WHEN & [3] THEN Birlikte
        
        // assertThrows: Biz metodumuzu çalıştırdığımızda bir "RuntimeException" PATLAMASINI BEKLİYORUZ.
        // Eğer patlamazsa veya başka hata verirse, TEST BAŞARISIZ OLUR (FAIL).
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.createAuthor(sampleRequest); // Aksiyonumuz lambda fonksiyonu içinde
        });

        // Fırlatılan hatanın içindeki mesaj gerçekten "Bu isimde bir yazar zaten kayıtlı." mı?
        assertEquals("Bu isimde bir yazar zaten kayıtlı.", exception.getMessage());

        // verify: Hata fırlattığı için sistemin ASLA save fonksiyonuna uğramadığını kontrol ediyoruz.
        // "authorRepository üzerinden save() metodu 'hiçbir zaman' (never) çağrılmamalı!"
        verify(authorRepository, never()).save(any(Author.class));
    }

    // --- ASSIGN AUTHOR ROLE TESTLERİ ---

    @Test
    void assignAuthorRole_HappyPath_CreatesAuthor() {
        // GIVEN
        User user = new User("testuser", "test@test.com", "123");
        org.springframework.test.util.ReflectionTestUtils.setField(user, "id", 1L);
        user.setRole(Role.USER); // Sadece düz USER rolündekiler yazar yapılabilir.

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
            Author author = invocation.getArgument(0); // Save içine gönderilen author nesnesini yakala
            return new Author(author.getName(), author.getBio(), user); // Kaydetmiş gibi yap
        });

        // WHEN
        AuthorResponse response = authorService.assignAuthorRole(1L);

        // THEN
        assertNotNull(response);
        assertEquals("testuser", response.getName()); // Profil null olduğu için username kullanılmalı
        assertEquals(Role.AUTHOR, user.getRole()); // User yetkisi AUTHOR'a çekilmiş mi?
        
        verify(userRepository, times(1)).save(user);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void assignAuthorRole_WhenUserIsAlreadyAuthor_ThrowsException() {
        // GIVEN
        User user = new User("testuser", "test@test.com", "123");
        org.springframework.test.util.ReflectionTestUtils.setField(user, "id", 1L);
        user.setRole(Role.AUTHOR); // Zaten yazar!

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.assignAuthorRole(1L);
        });

        assertEquals("Kullanıcı zaten yazar veya üst düzey yetkiye sahip.", exception.getMessage());
        verify(authorRepository, never()).save(any());
    }

    // --- GET ALL AUTHORS TESTİ ---

    @Test
    void getAllAuthors_ReturnsPage() {
        // GIVEN
        Author author1 = new Author("Yazar 1", "Bio 1");
        Author author2 = new Author("Yazar 2", "Bio 2");
        java.util.List<Author> authorList = java.util.List.of(author1, author2);
        org.springframework.data.domain.Page<Author> authorPage = new org.springframework.data.domain.PageImpl<>(authorList);
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(authorRepository.findAll(pageable)).thenReturn(authorPage);

        // WHEN
        org.springframework.data.domain.Page<AuthorResponse> responsePage = authorService.getAllAuthors(pageable);

        // THEN
        assertEquals(2, responsePage.getTotalElements());
        assertEquals("Yazar 1", responsePage.getContent().get(0).getName());
        verify(authorRepository, times(1)).findAll(pageable);
    }

    // --- UPDATE AUTHOR TESTLERİ ---

    @Test
    void updateAuthor_ByAdmin_Success() {
        // GIVEN
        Author existingAuthor = new Author("Eski İsim", "Eski Bio");
        // Reflection yardımıyla ID ataması (Çünkü setter'ı yok ve logic ID karşılaştırması yapıyor)
        org.springframework.test.util.ReflectionTestUtils.setField(existingAuthor, "id", 5L);

        com.hamzaeren.course.spring.dto.AuthorUpdate updateRequest = new com.hamzaeren.course.spring.dto.AuthorUpdate();
        updateRequest.setName("Yeni İsim");
        updateRequest.setBio("Yeni Bio");

        when(authorRepository.findById(5L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.findByName("Yeni İsim")).thenReturn(Optional.empty()); // İsim çakışması yok
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // WHEN (isAdmin = true, callerId önemsiz)
        AuthorResponse response = authorService.updateAuthor(5L, updateRequest, 99L, true);

        // THEN
        assertEquals("Yeni İsim", response.getName());
        assertEquals("Yeni Bio", response.getBio());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void updateAuthor_ByAuthorThemselves_Success() {
        // GIVEN
        Author existingAuthor = new Author("Benim Adım", "Benim Biom");
        org.springframework.test.util.ReflectionTestUtils.setField(existingAuthor, "id", 10L);

        // Sahiplik kontrolü için callerId'ye denk gelen Yazar hesabı
        Author callerAuthorAccount = new Author();
        org.springframework.test.util.ReflectionTestUtils.setField(callerAuthorAccount, "id", 10L); // ID'ler eşleşmeli!

        com.hamzaeren.course.spring.dto.AuthorUpdate updateRequest = new com.hamzaeren.course.spring.dto.AuthorUpdate();
        updateRequest.setBio("Sadece biomu güncelledim");

        when(authorRepository.findById(10L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.findByUserId(777L)).thenReturn(Optional.of(callerAuthorAccount)); // Caller sorgusu
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // WHEN (isAdmin = false, callerId = 777L)
        AuthorResponse response = authorService.updateAuthor(10L, updateRequest, 777L, false);

        // THEN
        assertEquals("Sadece biomu güncelledim", response.getBio());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void updateAuthor_ByAnotherAuthor_ThrowsException() {
        // GIVEN: Güncellenmek istenen yazar (ID: 10)
        Author targetAuthor = new Author("Zavallı Yazar", "Bio");
        org.springframework.test.util.ReflectionTestUtils.setField(targetAuthor, "id", 10L);

        // GIVEN: İsteği atan kötü niyetli (veya yetkisiz) yazar (ID: 99)
        Author attackerAuthor = new Author("Kötü Yazar", "Bio");
        org.springframework.test.util.ReflectionTestUtils.setField(attackerAuthor, "id", 99L); // ID'ler eşleşMİYOR!

        when(authorRepository.findById(10L)).thenReturn(Optional.of(targetAuthor));
        when(authorRepository.findByUserId(666L)).thenReturn(Optional.of(attackerAuthor)); // Caller'ın Author hesabı 99

        com.hamzaeren.course.spring.dto.AuthorUpdate updateRequest = new com.hamzaeren.course.spring.dto.AuthorUpdate();
        updateRequest.setBio("Hacked!");

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.updateAuthor(10L, updateRequest, 666L, false); // isAdmin = false
        });

        assertEquals("Sadece kendi yazar profilinizi güncelleyebilirsiniz.", exception.getMessage());
        verify(authorRepository, never()).save(any());
    }

    // --- DELETE AUTHOR TESTİ ---

    @Test
    void deleteAuthor_WhenExists_Deletes() {
        when(authorRepository.existsById(1L)).thenReturn(true);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }
}
