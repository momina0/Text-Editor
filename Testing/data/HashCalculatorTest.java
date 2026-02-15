package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.HashCalculator;

/**
 * JUnit Test Class for HashCalculator (Data Access Layer)
 * Tests the MD5 hash calculation functionality
 * 
 * MD5 produces a 128-bit hash value rendered as 32 hexadecimal characters
 * 
 * Verifies:
 * - Hash integrity: editing file changes hash
 * - Hash consistency: same input produces same hash
 * - Hash format: 32 uppercase hex characters
 */
public class HashCalculatorTest {

    // ==================== POSITIVE TEST CASES ====================

    @Test
    @DisplayName("Test 1: Calculate hash for simple text")
    void testCalculateHash_SimpleText() throws Exception {
        // Arrange
        String text = "Hello World";

        // Act
        String hash = HashCalculator.calculateHash(text);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters");
        // Known MD5 for "Hello World": B10A8DB164E0754105B7A99BE72E3FE5
        assertEquals("B10A8DB164E0754105B7A99BE72E3FE5", hash);
    }

    @Test
    @DisplayName("Test 2: Hash consistency - same input same output")
    void testCalculateHash_Consistency() throws Exception {
        // Arrange
        String text = "Consistent Text Content";

        // Act
        String hash1 = HashCalculator.calculateHash(text);
        String hash2 = HashCalculator.calculateHash(text);

        // Assert
        assertEquals(hash1, hash2, "Same input should produce same hash");
    }

    @Test
    @DisplayName("Test 3: Hash changes when content changes")
    void testCalculateHash_DifferentContent() throws Exception {
        // Arrange
        String original = "Original content";
        String modified = "Modified content";

        // Act
        String hashOriginal = HashCalculator.calculateHash(original);
        String hashModified = HashCalculator.calculateHash(modified);

        // Assert
        assertNotEquals(hashOriginal, hashModified,
                "Different content should produce different hashes");
    }

    @Test
    @DisplayName("Test 4: Hash is uppercase hexadecimal")
    void testCalculateHash_UppercaseHex() throws Exception {
        // Arrange
        String text = "Test for hex format";

        // Act
        String hash = HashCalculator.calculateHash(text);

        // Assert
        assertTrue(hash.matches("[0-9A-F]+"),
                "Hash should only contain uppercase hex characters");
    }

    @Test
    @DisplayName("Test 5: Hash Arabic text")
    void testCalculateHash_ArabicText() throws Exception {
        // Arrange
        String arabicText = "بسم الله الرحمن الرحيم";

        // Act
        String hash = HashCalculator.calculateHash(arabicText);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 6: Hash mixed Arabic and English")
    void testCalculateHash_MixedLanguages() throws Exception {
        // Arrange
        String mixed = "Hello مرحبا World عالم";

        // Act
        String hash = HashCalculator.calculateHash(mixed);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 7: Hash with special characters")
    void testCalculateHash_SpecialCharacters() throws Exception {
        // Arrange
        String special = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        String hash = HashCalculator.calculateHash(special);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 8: Hash with newlines and whitespace")
    void testCalculateHash_Whitespace() throws Exception {
        // Arrange
        String withWhitespace = "Line 1\nLine 2\tTab\r\nCRLF";

        // Act
        String hash = HashCalculator.calculateHash(withWhitespace);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 9: Empty string produces valid hash")
    void testCalculateHash_EmptyString() throws Exception {
        // Arrange
        String empty = "";

        // Act
        String hash = HashCalculator.calculateHash(empty);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // Known MD5 for empty string: D41D8CD98F00B204E9800998ECF8427E
        assertEquals("D41D8CD98F00B204E9800998ECF8427E", hash);
    }

    @Test
    @DisplayName("Test 10: Single character hash")
    void testCalculateHash_SingleCharacter() throws Exception {
        // Arrange
        String single = "a";

        // Act
        String hash = HashCalculator.calculateHash(single);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // Known MD5 for "a": 0CC175B9C0F1B6A831C399E269772661
        assertEquals("0CC175B9C0F1B6A831C399E269772661", hash);
    }

    // ==================== HASH INTEGRITY TESTS (for metadata) ====================

    @Test
    @DisplayName("Test 11: Editing content changes session hash")
    void testHashIntegrity_ContentEdit() throws Exception {
        // Arrange
        String originalContent = "This is the original document content";
        String importHash = HashCalculator.calculateHash(originalContent);

        // Simulate editing
        String editedContent = "This is the edited document content";
        String sessionHash = HashCalculator.calculateHash(editedContent);

        // Assert
        assertNotEquals(importHash, sessionHash,
                "Editing should change the current session hash");
    }

    @Test
    @DisplayName("Test 12: Minor change produces different hash")
    void testHashIntegrity_MinorChange() throws Exception {
        // Arrange
        String original = "The quick brown fox";
        String modified = "The quick brown Fox"; // Only case change

        // Act
        String hashOriginal = HashCalculator.calculateHash(original);
        String hashModified = HashCalculator.calculateHash(modified);

        // Assert
        assertNotEquals(hashOriginal, hashModified,
                "Even minor changes should produce different hash");
    }

    @Test
    @DisplayName("Test 13: Adding single space changes hash")
    void testHashIntegrity_AddSpace() throws Exception {
        // Arrange
        String original = "NoSpaces";
        String modified = "No Spaces";

        // Act
        String hashOriginal = HashCalculator.calculateHash(original);
        String hashModified = HashCalculator.calculateHash(modified);

        // Assert
        assertNotEquals(hashOriginal, hashModified);
    }

    @Test
    @DisplayName("Test 14: Import hash retained vs session hash")
    void testHashIntegrity_ImportVsSession() throws Exception {
        // Simulating import and edit scenario

        // Arrange - simulate file import
        String importedContent = "Original imported content from file";
        String importHash = HashCalculator.calculateHash(importedContent);

        // Store import hash (would be saved to database)
        String storedImportHash = importHash;

        // Simulate multiple edits
        String session1 = importedContent + " - edit 1";
        String session2 = session1 + " - edit 2";
        String finalSessionHash = HashCalculator.calculateHash(session2);

        // Assert
        // Import hash should be unchanged (stored value)
        assertEquals(importHash, storedImportHash, "Import hash should be retained");
        // Session hash should be different from import
        assertNotEquals(storedImportHash, finalSessionHash,
                "Session hash should differ from import hash after edits");
    }

    // ==================== BOUNDARY AND EDGE CASES ====================

    @Test
    @DisplayName("Test 15: Very long text hash")
    void testCalculateHash_LongText() throws Exception {
        // Arrange
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longText.append("This is a long piece of text. ");
        }

        // Act
        String hash = HashCalculator.calculateHash(longText.toString());

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 16: Unicode characters hash")
    void testCalculateHash_Unicode() throws Exception {
        // Arrange
        String unicode = "Unicode: \u00E9\u00E8\u00EA\u00EB\u4E2D\u6587\u65E5\u672C";

        // Act
        String hash = HashCalculator.calculateHash(unicode);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 17: Emoji characters hash")
    void testCalculateHash_Emoji() throws Exception {
        // Arrange
        String emoji = "Hello 😀 World 🌍 Test 🎉";

        // Act
        String hash = HashCalculator.calculateHash(emoji);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 18: Numeric only content")
    void testCalculateHash_NumericOnly() throws Exception {
        // Arrange
        String numbers = "1234567890";

        // Act
        String hash = HashCalculator.calculateHash(numbers);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 19: Hash with null bytes - should handle UTF-8")
    void testCalculateHash_UTF8Encoding() throws Exception {
        // Arrange - UTF-8 specific content
        String utf8 = "UTF-8: Ä Ö Ü ß";

        // Act
        String hash = HashCalculator.calculateHash(utf8);

        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("Test 20: Hash performance - quick execution")
    void testCalculateHash_Performance() throws Exception {
        // Arrange
        String text = "Performance test content";

        // Act & Assert
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            HashCalculator.calculateHash(text);
        }
        long endTime = System.currentTimeMillis();

        // Should complete 1000 hashes in under 1 second
        assertTrue((endTime - startTime) < 1000,
                "Hash calculation should be performant");
    }
}
