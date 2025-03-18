package com.kodat.of.halleyecommerce.common;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlugService {

    private final Slugify slugify;


    public String generateSlug(String name, String productCode) {
        String combinedString = name + "-" + productCode;
        combinedString = replaceTurkishCharacters(combinedString);
        return slugify.slugify(combinedString);
    }

    public String generateSlugWithNoCode(String name) {
        String combinedString = replaceTurkishCharacters(name);
        return slugify.slugify(combinedString);
    }


    private String replaceTurkishCharacters(String input) {
        return input
                .replace("ç", "c")
                .replace("ğ", "g")
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u")
                .replace("Ç", "C")
                .replace("Ğ", "G")
                .replace("İ", "I")
                .replace("Ö", "O")
                .replace("Ş", "S")
                .replace("Ü", "U");
    }

}
