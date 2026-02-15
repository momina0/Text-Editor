package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.Transliteration;

/**
 * JUnit Test Class for Transliteration (Data Access Layer)
 * Tests the Arabic to Roman transliteration functionality
 * 
 * Tests the TransliterateCommand pattern execution indirectly
 * through the transliterate static method
 */
public class TransliterationTest {

    // ==================== BASIC TRANSLITERATION TESTS ====================

    @Test
    @DisplayName("Test 1: Transliterate single Arabic letter - ب (ba)")
    void testTransliterate_SingleLetter_Ba() {
        // Act
        String result = Transliteration.transliterate("ب");

        // Assert
        assertEquals("B", result, "ب should transliterate to 'B'");
    }

    @Test
    @DisplayName("Test 2: Transliterate Arabic word - مرحبا (marhaba)")
    void testTransliterate_SimpleWord() {
        // Arrange
        String arabic = "مرحبا"; // marhaba - hello

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Expected: Mrhba (without vowels) or similar
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Test 3: Transliterate Arabic sentence")
    void testTransliterate_Sentence() {
        // Arrange
        String arabic = "بسم الله";

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(" "), "Should preserve word boundaries");
    }

    @Test
    @DisplayName("Test 4: Transliteration capitalizes first letter of each word")
    void testTransliterate_Capitalization() {
        // Arrange
        String arabic = "كتاب"; // kitab - book

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertTrue(Character.isUpperCase(result.charAt(0)),
                "First letter should be capitalized");
    }

    @Test
    @DisplayName("Test 5: Multiple words each capitalized")
    void testTransliterate_MultipleWordsCapitalized() {
        // Arrange
        String arabic = "السلام عليكم"; // assalamu alaikum

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        String[] words = result.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                assertTrue(Character.isUpperCase(word.charAt(0)),
                        "Each word should be capitalized: " + word);
            }
        }
    }

    // ==================== INDIVIDUAL CHARACTER TESTS ====================

    @Test
    @DisplayName("Test 6: Transliterate alef - ا")
    void testTransliterate_Alef() {
        String result = Transliteration.transliterate("ا");
        assertEquals("A", result);
    }

    @Test
    @DisplayName("Test 7: Transliterate ta - ت")
    void testTransliterate_Ta() {
        String result = Transliteration.transliterate("ت");
        assertEquals("T", result);
    }

    @Test
    @DisplayName("Test 8: Transliterate shin - ش")
    void testTransliterate_Shin() {
        String result = Transliteration.transliterate("ش");
        assertEquals("Sh", result);
    }

    @Test
    @DisplayName("Test 9: Transliterate ain - ع")
    void testTransliterate_Ain() {
        String result = Transliteration.transliterate("ع");
        assertEquals("3", result, "Ain should be represented as '3'");
    }

    @Test
    @DisplayName("Test 10: Transliterate kha - خ")
    void testTransliterate_Kha() {
        String result = Transliteration.transliterate("خ");
        assertEquals("Kh", result);
    }

    // ==================== DIACRITICAL MARKS (HARAKAT) TESTS ====================

    @Test
    @DisplayName("Test 11: Transliterate with fatha - بَ")
    void testTransliterate_WithFatha() {
        // Arrange
        String arabic = "بَ"; // ba with fatha

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("b"));
    }

    @Test
    @DisplayName("Test 12: Transliterate with damma - بُ")
    void testTransliterate_WithDamma() {
        // Arrange
        String arabic = "بُ"; // ba with damma

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test 13: Transliterate with kasra - بِ")
    void testTransliterate_WithKasra() {
        // Arrange
        String arabic = "بِ"; // ba with kasra

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertNotNull(result);
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 14: Transliterate empty string")
    void testTransliterate_EmptyString() {
        // Act
        String result = Transliteration.transliterate("");

        // Assert
        assertEquals("", result, "Empty input should produce empty output");
    }

    @Test
    @DisplayName("Test 15: Transliterate non-Arabic characters - should skip")
    void testTransliterate_NonArabic() {
        // Arrange
        String english = "Hello World";

        // Act
        String result = Transliteration.transliterate(english);

        // Assert
        // Non-Arabic characters are skipped
        assertEquals("", result.trim(), "Non-Arabic characters should be skipped");
    }

    @Test
    @DisplayName("Test 16: Transliterate mixed Arabic and English")
    void testTransliterate_Mixed() {
        // Arrange
        String mixed = "Hello مرحبا World";

        // Act
        String result = Transliteration.transliterate(mixed);

        // Assert
        // Only Arabic parts should be transliterated
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test 17: Transliterate numbers - should skip")
    void testTransliterate_Numbers() {
        // Arrange
        String withNumbers = "١٢٣ بسم";

        // Act
        String result = Transliteration.transliterate(withNumbers);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test 18: Multiple spaces are collapsed")
    void testTransliterate_MultipleSpaces() {
        // Arrange
        String arabic = "كلمة    كلمة"; // Multiple spaces

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertFalse(result.contains("  "), "Multiple spaces should be collapsed");
    }

    @Test
    @DisplayName("Test 19: Leading/trailing spaces are trimmed")
    void testTransliterate_TrimSpaces() {
        // Arrange
        String arabic = "   مرحبا   ";

        // Act
        String result = Transliteration.transliterate(arabic);

        // Assert
        assertEquals(result.trim(), result, "Result should be trimmed");
    }

    @Test
    @DisplayName("Test 20: Transliterate full Quran verse")
    void testTransliterate_QuranVerse() {
        // Arrange
        String bismillah = "بسم الله الرحمن الرحيم";

        // Act
        String result = Transliteration.transliterate(bismillah);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Should have multiple capitalized words
        String[] words = result.split(" ");
        assertTrue(words.length >= 2, "Should produce multiple words");
    }

    // ==================== SPECIAL ARABIC CHARACTERS ====================

    @Test
    @DisplayName("Test 21: Transliterate alef with madda - آ")
    void testTransliterate_AlefMadda() {
        String result = Transliteration.transliterate("آ");
        assertEquals("Aa", result);
    }

    @Test
    @DisplayName("Test 22: Transliterate hamza - ء")
    void testTransliterate_Hamza() {
        String result = Transliteration.transliterate("ء");
        assertEquals("'", result);
    }

    @Test
    @DisplayName("Test 23: Transliterate emphatic letters")
    void testTransliterate_EmphaticLetters() {
        // Emphatic letters have uppercase representation
        assertEquals("S", Transliteration.transliterate("ص").trim());
        assertEquals("D", Transliteration.transliterate("ض").trim());
        assertEquals("T", Transliteration.transliterate("ط").trim());
    }

    @Test
    @DisplayName("Test 24: Transliterate waw - و")
    void testTransliterate_Waw() {
        String result = Transliteration.transliterate("و");
        assertEquals("W", result);
    }

    @Test
    @DisplayName("Test 25: Transliterate ya - ي")
    void testTransliterate_Ya() {
        String result = Transliteration.transliterate("ي");
        assertEquals("Y", result);
    }
}
