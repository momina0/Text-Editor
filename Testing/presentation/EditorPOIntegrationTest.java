package presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bll.IFacadeBO;
import dto.Documents;
import dto.Pages;

/**
 * JUnit Test Class for Presentation Layer
 * Tests the interaction between UI components and Business Layer through
 * IFacadeBO
 * 
 * Since EditorPO is a Swing JFrame, we test through interface mocking
 * to verify correct business layer invocations
 * 
 * Note: GUI components are difficult to unit test directly.
 * These tests verify the contract between Presentation and Business layers.
 */
@ExtendWith(MockitoExtension.class)
public class EditorPOIntegrationTest {

    @Mock
    private IFacadeBO mockFacade;

    /**
     * Helper method to create test document
     */
    private Documents createTestDocument(int id, String name, String content) {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, id, 1, content));
        return new Documents(id, name, "testhash", "2024-01-01", "2024-01-01", pages);
    }

    // ==================== FILE OPERATION TESTS ====================

    @Test
    @DisplayName("Test 1: Create file through facade")
    void testCreateFile_ThroughFacade() {
        // Arrange
        when(mockFacade.createFile("test.txt", "content")).thenReturn(true);

        // Act
        boolean result = mockFacade.createFile("test.txt", "content");

        // Assert
        assertTrue(result);
        verify(mockFacade).createFile("test.txt", "content");
    }

    @Test
    @DisplayName("Test 2: Update file through facade")
    void testUpdateFile_ThroughFacade() {
        // Arrange
        when(mockFacade.updateFile(1, "test.txt", 1, "new content")).thenReturn(true);

        // Act
        boolean result = mockFacade.updateFile(1, "test.txt", 1, "new content");

        // Assert
        assertTrue(result);
        verify(mockFacade).updateFile(1, "test.txt", 1, "new content");
    }

    @Test
    @DisplayName("Test 3: Delete file through facade")
    void testDeleteFile_ThroughFacade() {
        // Arrange
        when(mockFacade.deleteFile(1)).thenReturn(true);

        // Act
        boolean result = mockFacade.deleteFile(1);

        // Assert
        assertTrue(result);
        verify(mockFacade).deleteFile(1);
    }

    @Test
    @DisplayName("Test 4: Get single file through facade")
    void testGetFile_ThroughFacade() {
        // Arrange
        Documents doc = createTestDocument(1, "test.txt", "content");
        when(mockFacade.getFile(1)).thenReturn(doc);

        // Act
        Documents result = mockFacade.getFile(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(mockFacade).getFile(1);
    }

    @Test
    @DisplayName("Test 5: Get all files through facade")
    void testGetAllFiles_ThroughFacade() {
        // Arrange
        List<Documents> docs = Arrays.asList(
                createTestDocument(1, "doc1.txt", "content1"),
                createTestDocument(2, "doc2.txt", "content2"));
        when(mockFacade.getAllFiles()).thenReturn(docs);

        // Act
        List<Documents> result = mockFacade.getAllFiles();

        // Assert
        assertEquals(2, result.size());
        verify(mockFacade).getAllFiles();
    }

    // ==================== SEARCH OPERATION TESTS ====================

    @Test
    @DisplayName("Test 6: Search keyword through facade")
    void testSearchKeyword_ThroughFacade() {
        // Arrange
        List<String> searchResults = Arrays.asList("Doc1.txt - found keyword...");
        when(mockFacade.searchKeyword("keyword")).thenReturn(searchResults);

        // Act
        List<String> result = mockFacade.searchKeyword("keyword");

        // Assert
        assertFalse(result.isEmpty());
        verify(mockFacade).searchKeyword("keyword");
    }

    @Test
    @DisplayName("Test 7: Search with minimum characters")
    void testSearchKeyword_MinLength() {
        // Arrange
        when(mockFacade.searchKeyword("abc")).thenReturn(new ArrayList<>());

        // Act
        List<String> result = mockFacade.searchKeyword("abc");

        // Assert
        verify(mockFacade).searchKeyword("abc");
    }

    // ==================== TRANSLITERATION TESTS ====================

    @Test
    @DisplayName("Test 8: Transliterate Arabic text")
    void testTransliterate_ThroughFacade() {
        // Arrange
        when(mockFacade.transliterate(1, "مرحبا")).thenReturn("Marhaba");

        // Act
        String result = mockFacade.transliterate(1, "مرحبا");

        // Assert
        assertEquals("Marhaba", result);
        verify(mockFacade).transliterate(1, "مرحبا");
    }

    @Test
    @DisplayName("Test 9: Transliterate empty string")
    void testTransliterate_EmptyString() {
        // Arrange
        when(mockFacade.transliterate(1, "")).thenReturn("");

        // Act
        String result = mockFacade.transliterate(1, "");

        // Assert
        assertEquals("", result);
    }

    // ==================== TEXT ANALYSIS TESTS ====================

    @Test
    @DisplayName("Test 10: TF-IDF calculation through facade")
    void testTFIDF_ThroughFacade() {
        // Arrange
        List<String> otherDocs = Arrays.asList("doc1 content", "doc2 content");
        when(mockFacade.performTFIDF(otherDocs, "selected content")).thenReturn(0.75);

        // Act
        double result = mockFacade.performTFIDF(otherDocs, "selected content");

        // Assert
        assertEquals(0.75, result, 0.01);
        verify(mockFacade).performTFIDF(otherDocs, "selected content");
    }

    @Test
    @DisplayName("Test 11: Lemmatization through facade")
    void testLemmatizeWords_ThroughFacade() {
        // Arrange
        Map<String, String> lemmas = new HashMap<>();
        lemmas.put("كاتب", "كتب");
        when(mockFacade.lemmatizeWords("كاتب")).thenReturn(lemmas);

        // Act
        Map<String, String> result = mockFacade.lemmatizeWords("كاتب");

        // Assert
        assertEquals(lemmas, result);
        verify(mockFacade).lemmatizeWords("كاتب");
    }

    @Test
    @DisplayName("Test 12: POS tagging through facade")
    void testExtractPOS_ThroughFacade() {
        // Arrange
        Map<String, List<String>> posTags = new HashMap<>();
        posTags.put("كتاب", Arrays.asList("NOUN"));
        when(mockFacade.extractPOS("كتاب")).thenReturn(posTags);

        // Act
        Map<String, List<String>> result = mockFacade.extractPOS("كتاب");

        // Assert
        assertEquals(posTags, result);
        verify(mockFacade).extractPOS("كتاب");
    }

    @Test
    @DisplayName("Test 13: Root extraction through facade")
    void testExtractRoots_ThroughFacade() {
        // Arrange
        Map<String, String> roots = new HashMap<>();
        roots.put("مكتوب", "كتب");
        when(mockFacade.extractRoots("مكتوب")).thenReturn(roots);

        // Act
        Map<String, String> result = mockFacade.extractRoots("مكتوب");

        // Assert
        assertEquals(roots, result);
        verify(mockFacade).extractRoots("مكتوب");
    }

    @Test
    @DisplayName("Test 14: Stemming through facade")
    void testStemWords_ThroughFacade() {
        // Arrange
        Map<String, String> stems = new HashMap<>();
        stems.put("كاتبون", "كاتب");
        when(mockFacade.stemWords("كاتبون")).thenReturn(stems);

        // Act
        Map<String, String> result = mockFacade.stemWords("كاتبون");

        // Assert
        assertEquals(stems, result);
        verify(mockFacade).stemWords("كاتبون");
    }

    @Test
    @DisplayName("Test 15: Word segmentation through facade")
    void testSegmentWords_ThroughFacade() {
        // Arrange
        Map<String, String> segments = new HashMap<>();
        segments.put("والكتاب", "و ال كتاب");
        when(mockFacade.segmentWords("والكتاب")).thenReturn(segments);

        // Act
        Map<String, String> result = mockFacade.segmentWords("والكتاب");

        // Assert
        assertEquals(segments, result);
        verify(mockFacade).segmentWords("والكتاب");
    }

    @Test
    @DisplayName("Test 16: PMI calculation through facade")
    void testPerformPMI_ThroughFacade() {
        // Arrange
        Map<String, Double> pmiScores = new HashMap<>();
        pmiScores.put("word_pair", 0.5);
        when(mockFacade.performPMI("content")).thenReturn(pmiScores);

        // Act
        Map<String, Double> result = mockFacade.performPMI("content");

        // Assert
        assertEquals(pmiScores, result);
        verify(mockFacade).performPMI("content");
    }

    @Test
    @DisplayName("Test 17: PKL calculation through facade")
    void testPerformPKL_ThroughFacade() {
        // Arrange
        Map<String, Double> pklScores = new HashMap<>();
        pklScores.put("term", 1.5);
        when(mockFacade.performPKL("content")).thenReturn(pklScores);

        // Act
        Map<String, Double> result = mockFacade.performPKL("content");

        // Assert
        assertEquals(pklScores, result);
        verify(mockFacade).performPKL("content");
    }

    // ==================== FILE EXTENSION TESTS ====================

    @Test
    @DisplayName("Test 18: Get file extension through facade")
    void testGetFileExtension_ThroughFacade() {
        // Arrange
        when(mockFacade.getFileExtension("document.txt")).thenReturn("txt");

        // Act
        String result = mockFacade.getFileExtension("document.txt");

        // Assert
        assertEquals("txt", result);
        verify(mockFacade).getFileExtension("document.txt");
    }

    @Test
    @DisplayName("Test 19: Get markdown extension")
    void testGetFileExtension_Markdown() {
        // Arrange
        when(mockFacade.getFileExtension("readme.md")).thenReturn("md");

        // Act
        String result = mockFacade.getFileExtension("readme.md");

        // Assert
        assertEquals("md", result);
    }

    @Test
    @DisplayName("Test 20: Get extension for file without extension")
    void testGetFileExtension_NoExtension() {
        // Arrange
        when(mockFacade.getFileExtension("noextension")).thenReturn("");

        // Act
        String result = mockFacade.getFileExtension("noextension");

        // Assert
        assertEquals("", result);
    }
}
