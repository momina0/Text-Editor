package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.PaginationDAO;
import dto.Pages;

/**
 * JUnit Test Class for PaginationDAO (Data Access Layer)
 * Tests the pagination logic that splits content into pages
 * 
 * White-Box Testing: Tests cover all branches of the paginate method
 * 
 * Control Flow Analysis:
 * - Node 1: Method entry, initialize variables
 * - Node 2: Check if fileContent is null or empty
 * - Node 3: Add empty page and return (if null/empty)
 * - Node 4: Loop through characters
 * - Node 5: Check if page is full or at end of content
 * - Node 6: Add page to list, increment page number, reset content
 * - Node 7: Return pages list
 */
public class PaginationDAOTest {

    // Page size is 100 characters as per implementation
    private static final int PAGE_SIZE = 100;

    // ==================== POSITIVE TEST CASES ====================

    @Test
    @DisplayName("Test 1: Paginate short content - single page")
    void testPaginate_SinglePage() {
        // Arrange
        String content = "This is short content";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(1, result.size(), "Short content should result in single page");
        assertEquals(1, result.get(0).getPageNumber());
        assertEquals(content, result.get(0).getPageContent());
    }

    @Test
    @DisplayName("Test 2: Paginate content exactly 100 characters - single page")
    void testPaginate_ExactlyPageSize() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE; i++) {
            sb.append("x");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(1, result.size(), "Exactly 100 chars should be single page");
        assertEquals(PAGE_SIZE, result.get(0).getPageContent().length());
    }

    @Test
    @DisplayName("Test 3: Paginate content 101 characters - two pages")
    void testPaginate_JustOverPageSize() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            sb.append("y");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(2, result.size(), "101 chars should be two pages");
        assertEquals(PAGE_SIZE, result.get(0).getPageContent().length());
        assertEquals(1, result.get(1).getPageContent().length());
    }

    @Test
    @DisplayName("Test 4: Paginate multiple full pages")
    void testPaginate_MultipleFullPages() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE * 3; i++) {
            sb.append("z");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(3, result.size(), "300 chars should be three pages");
        for (Pages page : result) {
            assertEquals(PAGE_SIZE, page.getPageContent().length());
        }
    }

    @Test
    @DisplayName("Test 5: Page numbers are sequential")
    void testPaginate_SequentialPageNumbers() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE * 5; i++) {
            sb.append("a");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        for (int i = 0; i < result.size(); i++) {
            assertEquals(i + 1, result.get(i).getPageNumber(),
                    "Page number should be " + (i + 1));
        }
    }

    @Test
    @DisplayName("Test 6: Content with newlines")
    void testPaginate_WithNewlines() {
        // Arrange
        String content = "Line 1\nLine 2\nLine 3\nLine 4";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPageContent().contains("\n"),
                "Newlines should be preserved");
    }

    @Test
    @DisplayName("Test 7: Arabic text pagination")
    void testPaginate_ArabicText() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("مرحبا "); // Arabic "Hello"
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPageContent().contains("مرحبا"));
    }

    @Test
    @DisplayName("Test 8: Mixed content pagination")
    void testPaginate_MixedContent() {
        // Arrange
        String content = "English text مع نص عربي mixed together 123 !@#";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(content, result.get(0).getPageContent());
    }

    // ==================== NEGATIVE TEST CASES ====================

    @Test
    @DisplayName("Test 9: Paginate null content")
    void testPaginate_NullContent() {
        // Act
        List<Pages> result = PaginationDAO.paginate(null);

        // Assert
        assertEquals(1, result.size(), "Null content should return single empty page");
        assertEquals("", result.get(0).getPageContent());
        assertEquals(1, result.get(0).getPageNumber());
    }

    @Test
    @DisplayName("Test 10: Paginate empty string")
    void testPaginate_EmptyString() {
        // Act
        List<Pages> result = PaginationDAO.paginate("");

        // Assert
        assertEquals(1, result.size(), "Empty string should return single page");
        assertEquals("", result.get(0).getPageContent());
    }

    // ==================== BOUNDARY TEST CASES ====================

    @Test
    @DisplayName("Test 11: Single character content")
    void testPaginate_SingleCharacter() {
        // Arrange
        String content = "X";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(1, result.size());
        assertEquals("X", result.get(0).getPageContent());
    }

    @Test
    @DisplayName("Test 12: 99 characters - just under page size")
    void testPaginate_JustUnderPageSize() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE - 1; i++) {
            sb.append("a");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(1, result.size());
        assertEquals(PAGE_SIZE - 1, result.get(0).getPageContent().length());
    }

    @Test
    @DisplayName("Test 13: 200 characters - exactly two pages")
    void testPaginate_ExactlyTwoPages() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE * 2; i++) {
            sb.append("b");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(2, result.size());
        assertEquals(PAGE_SIZE, result.get(0).getPageContent().length());
        assertEquals(PAGE_SIZE, result.get(1).getPageContent().length());
    }

    @Test
    @DisplayName("Test 14: 199 characters - just under two pages")
    void testPaginate_JustUnderTwoPages() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAGE_SIZE * 2 - 1; i++) {
            sb.append("c");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(2, result.size());
        assertEquals(PAGE_SIZE, result.get(0).getPageContent().length());
        assertEquals(PAGE_SIZE - 1, result.get(1).getPageContent().length());
    }

    @Test
    @DisplayName("Test 15: Large content - many pages")
    void testPaginate_LargeContent() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("content ");
        }
        String content = sb.toString();

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertTrue(result.size() > 1, "Large content should produce multiple pages");
        // Verify all pages except possibly the last are full
        for (int i = 0; i < result.size() - 1; i++) {
            assertEquals(PAGE_SIZE, result.get(i).getPageContent().length());
        }
    }

    // ==================== SPECIAL CHARACTER TESTS ====================

    @Test
    @DisplayName("Test 16: Content with only spaces")
    void testPaginate_OnlySpaces() {
        // Arrange
        String content = "          "; // 10 spaces

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getPageContent().length());
    }

    @Test
    @DisplayName("Test 17: Content with tabs")
    void testPaginate_WithTabs() {
        // Arrange
        String content = "Col1\tCol2\tCol3";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertTrue(result.get(0).getPageContent().contains("\t"));
    }

    @Test
    @DisplayName("Test 18: Content with special Unicode characters")
    void testPaginate_UnicodeCharacters() {
        // Arrange
        String content = "Unicode: \u00E9\u00E8\u00EA\u00EB emoji: 😀🎉";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPageContent().contains("emoji"));
    }

    // ==================== PAGE ID AND FILE ID TESTS ====================

    @Test
    @DisplayName("Test 19: Verify page ID is initialized to 0")
    void testPaginate_PageIdInitialization() {
        // Arrange
        String content = "Test content";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(0, result.get(0).getPageId(),
                "Page ID should be 0 (unset) from pagination");
    }

    @Test
    @DisplayName("Test 20: Verify file ID is initialized to 0")
    void testPaginate_FileIdInitialization() {
        // Arrange
        String content = "Test content";

        // Act
        List<Pages> result = PaginationDAO.paginate(content);

        // Assert
        assertEquals(0, result.get(0).getFileId(),
                "File ID should be 0 (unset) from pagination");
    }
}
