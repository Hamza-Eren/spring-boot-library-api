package com.hamzaeren.course.spring.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component // Sınıfı Java Bean'i yapması ve Spring'in yönetimine vermesi için
public class AppInfoContributor implements InfoContributor {

    @Override // Info nesnesine detay eklemek için
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of( // Değişmez bir Map oluşturur
                "name", "Kütüphane API",
                "description", "Kitap ödünç alma sistemi",
                "version", "1.0.0"));
    }
}