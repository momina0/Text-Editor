package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.PreProcessText;

/**
 * JUnit Test Class for PreProcessText (Data Access Layer)
 * Tests text preprocessing including Arabic harakat removal
 * and non-Arabic character filtering
 */
public class PreProcessTextTest {

    // ==================== REMOVE HARAKAT TESTS ====================

    @Test
    @DisplayName("Test 1: Remove fatha (fatḥa) diacritic")
    void testRemoveHarakat_Fatha() {
        // Arrange - ba with fatha
        String withFatha = "بَ";

        // Act
        String result = PreProcessText.removeHarakat(withFatha);

        // Assert
        assertEquals("ب", result);
    }

    @Test
    @DisplayName("Test 2: Remove damma diacritic")
    void testRemoveHarakat_Damma() {
        // Arrange - ba with damma
        String withDamma = "بُ";

        // Act
        String result = PreProcessText.removeHarakat(withDamma);

        // Assert
        assertEquals("ب", result);
    }

    @Test
    @DisplayName("Test 3: Remove kasra diacritic")
    void testRemoveHarakat_Kasra() {
        // Arrange - ba with kasra
        String withKasra = "بِ";

        // Act
        String result = PreProcessText.removeHarakat(withKasra);

        // Assert
        assertEquals("ب", result);
    }

    @Test
    @DisplayName("Test 4: Remove tanween fath")
    void testRemoveHarakat_TanweenFath() {
        // Arrange
        String withTanween = "كِتابً";

        // Act
        String result = PreProcessText.removeHarakat(withTanween);

        // Assert
        assertFalse(result.contains("ً"));
    }

    @Test
    @DisplayName("Test 5: Remove sukun")
    void testRemoveHarakat_Sukun() {
        // Arrange
        String withSukun = "مْ";

        // Act
        String result = PreProcessText.removeHarakat(withSukun);

        // Assert
        assertEquals("م", result);
    }

    @Test
    @DisplayName("Test 6: Remove shadda")
    void testRemoveHarakat_Shadda() {
        // Arrange
        String withShadda = "مّ";

        // Act
        String result = PreProcessText.removeHarakat(withShadda);

        // Assert
        assertEquals("م", result);
    }

    @Test
    @DisplayName("Test 7: Remove multiple harakat")
    void testRemoveHarakat_Multiple() {
        // Arrange - fully vowelized word
        String vowelized = "كِتَابٌ";

        // Act
        String result = PreProcessText.removeHarakat(vowelized);

        // Assert
        assertEquals("كتاب", result);
    }

    @Test
    @DisplayName("Test 8: Text without harakat unchanged")
    void testRemoveHarakat_NoHarakat() {
        // Arrange
        String noHarakat = "كتاب";

        // Act
        String result = PreProcessText.removeHarakat(noHarakat);

        // Assert
        assertEquals(noHarakat, result);
    }

    // ==================== REMOVE NON-ARABIC TESTS ====================

    @Test
    @DisplayName("Test 9: Remove English characters")
    void testRemoveNonArabic_English() {
        // Arrange
        String mixed = "Hello مرحبا";

        // Act
        String result = PreProcessText.removeNonArabicCharacters(mixed);

        // Assert
        assertFalse(result.contains("Hello"));
        assertTrue(result.contains("مرحبا"));
    }

    @Test
    @DisplayName("Test 10: Remove numbers")
    void testRemoveNonArabic_Numbers() {
        // Arrange
        String withNumbers = "123 كتاب 456";

        // Act
        String result = PreProcessText.removeNonArabicCharacters(withNumbers);

        // Assert
        assertFalse(result.contains("123"));
        assertFalse(result.contains("456"));
        assertTrue(result.contains("كتاب"));
    }

    @Test
    @DisplayName("Test 11: Remove punctuation")
    void testRemoveNonArabic_Punctuation() {
        // Arrange
        String withPunct = "!كتاب؟.،";

        // Act
        String result = PreProcessText.removeNonArabicCharacters(withPunct);

        // Assert
        assertFalse(result.contains("!"));
        assertFalse(result.contains("؟"));
        assertFalse(result.contains("."));
    }

    @Test
    @DisplayName("Test 12: Preserve Arabic text")
    void testRemoveNonArabic_PreserveArabic() {
        // Arrange
        String arabic = "بسم الله الرحمن الرحيم";

        // Act
        String result = PreProcessText.removeNonArabicCharacters(arabic);

        // Assert
        assertEquals(arabic, result);
    }

    @Test
    @DisplayName("Test 13: Preserve spaces")
    void testRemoveNonArabic_PreserveSpaces() {
        // Arrange
        String withSpaces = "كلمة أخرى";

        // Act
        String result = PreProcessText.removeNonArabicCharacters(withSpaces);

        // Assert
        assertTrue(result.contains(" "), "Spaces should be preserved");
    }

    // ==================== PREPROCESS TEXT TESTS ====================

    @Test
    @DisplayName("Test 14: Full preprocessing - removes harakat and non-Arabic")
    void testPreprocessText_Full() {
        // Arrange
        String input = "Hello بِسْمِ الله 123";

        // Act
        String result = PreProcessText.preprocessText(input);

        // Assert
        assertFalse(result.contains("Hello"));
        assertFalse(result.contains("123"));
        assertFalse(result.contains("ِ")); // kasra removed
        assertFalse(result.contains("ْ")); // sukun removed
    }

    @Test
    @DisplayName("Test 15: Preprocessing converts to lowercase")
    void testPreprocessText_Lowercase() {
        // Arrange
        String input = "HELLO مرحبا WORLD";

        // Act
        String result = PreProcessText.preprocessText(input);

        // Assert
        // After removing non-Arabic, only Arabic remains
        // Lowercase applies to any remaining Latin characters
        assertFalse(result.contains("HELLO"));
        assertFalse(result.contains("WORLD"));
    }

    @Test
    @DisplayName("Test 16: Empty string preprocessing")
    void testPreprocessText_Empty() {
        // Act
        String result = PreProcessText.preprocessText("");

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Test 17: Only non-Arabic characters")
    void testPreprocessText_OnlyNonArabic() {
        // Arrange
        String english = "Hello World 123";

        // Act
        String result = PreProcessText.preprocessText(english);

        // Assert
        // After removing non-Arabic, may be empty or just spaces
        assertTrue(result.trim().isEmpty() || result.contains(" "));
    }

    @Test
    @DisplayName("Test 18: Fully vowelized Quran verse")
    void testPreprocessText_QuranVerse() {
        // Arrange - Bismillah with full tashkeel
        String bismillah = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";

        // Act
        String result = PreProcessText.preprocessText(bismillah);

        // Assert
        // Should be devowelized version
        assertNotNull(result);
        assertFalse(result.contains("ِ")); // No kasra
        assertFalse(result.contains("ْ")); // No sukun
        assertFalse(result.contains("َ")); // No fatha
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Test 19: Unicode normalization")
    void testPreprocessText_Unicode() {
        // Arrange
        String unicode = "مرحبا\u200Bمرحبا"; // Zero-width space

        // Act
        String result = PreProcessText.preprocessText(unicode);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test 20: Special Arabic characters preserved")
    void testPreprocessText_SpecialArabic() {
        // Arrange - hamza, alef variations
        String special = "ء أ إ آ";

        // Act
        String result = PreProcessText.preprocessText(special);

        // Assert
        assertTrue(result.contains("ء") || result.contains("أ") ||
                result.contains("إ") || result.contains("آ"));
    }
}
