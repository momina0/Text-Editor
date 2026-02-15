package business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
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

import bll.FacadeBO;
import bll.IEditorBO;
import dto.Documents;
import dto.Pages;

/**
 * JUnit Test Class for FacadeBO (Business Layer - Facade Pattern)
 * Tests that the Facade correctly delegates to the underlying EditorBO
 * 
 * This tests the Facade Pattern implementation in the business layer
 */
@ExtendWith(MockitoExtension.class)
public class FacadeBOTest {

    @Mock
    private IEditorBO mockEditorBO;

    private FacadeBO facadeBO;

    @BeforeEach
    void setUp() {
        facadeBO = new FacadeBO(mockEditorBO);
    }

    /**
     * Helper method to create test document
     */
    private Documents createTestDocument(int id, String name, String content) {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, id, 1, content));
        return new Documents(id, name, "testhash", "2024-01-01", "2024-01-01", pages);
    }

    // ==================== FACADE DELEGATION TESTS ====================

    @Test
    @DisplayName("Test 1: FacadeBO delegates createFile to EditorBO")
    void testCreateFile_Delegation() {
        // Arrange
        when(mockEditorBO.createFile("test.txt", "content")).thenReturn(true);

        // Act
        boolean result = facadeBO.createFile("test.txt", "content");

        // Assert
        assertTrue(result);
        verify(mockEditorBO).createFile("test.txt", "content");
    }

    @Test
    @DisplayName("Test 2: FacadeBO delegates updateFile to EditorBO")
    void testUpdateFile_Delegation() {
        // Arrange
        when(mockEditorBO.updateFile(1, "test.txt", 1, "new content")).thenReturn(true);

        // Act
        boolean result = facadeBO.updateFile(1, "test.txt", 1, "new content");

        // Assert
        assertTrue(result);
        verify(mockEditorBO).updateFile(1, "test.txt", 1, "new content");
    }

    @Test
    @DisplayName("Test 3: FacadeBO delegates deleteFile to EditorBO")
    void testDeleteFile_Delegation() {
        // Arrange
        when(mockEditorBO.deleteFile(1)).thenReturn(true);

        // Act
        boolean result = facadeBO.deleteFile(1);

        // Assert
        assertTrue(result);
        verify(mockEditorBO).deleteFile(1);
    }

    @Test
    @DisplayName("Test 4: FacadeBO delegates importTextFiles to EditorBO")
    void testImportTextFiles_Delegation() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockEditorBO.importTextFiles(mockFile, "imported.txt")).thenReturn(true);

        // Act
        boolean result = facadeBO.importTextFiles(mockFile, "imported.txt");

        // Assert
        assertTrue(result);
        verify(mockEditorBO).importTextFiles(mockFile, "imported.txt");
    }

    @Test
    @DisplayName("Test 5: FacadeBO delegates getFile to EditorBO")
    void testGetFile_Delegation() {
        // Arrange
        Documents expectedDoc = createTestDocument(1, "test.txt", "content");
        when(mockEditorBO.getFile(1)).thenReturn(expectedDoc);

        // Act
        Documents result = facadeBO.getFile(1);

        // Assert
        assertEquals(expectedDoc, result);
        verify(mockEditorBO).getFile(1);
    }

    @Test
    @DisplayName("Test 6: FacadeBO delegates getAllFiles to EditorBO")
    void testGetAllFiles_Delegation() {
        // Arrange
        List<Documents> expectedDocs = Arrays.asList(
                createTestDocument(1, "doc1.txt", "content1"),
                createTestDocument(2, "doc2.txt", "content2"));
        when(mockEditorBO.getAllFiles()).thenReturn(expectedDocs);

        // Act
        List<Documents> result = facadeBO.getAllFiles();

        // Assert
        assertEquals(2, result.size());
        verify(mockEditorBO).getAllFiles();
    }

    @Test
    @DisplayName("Test 7: FacadeBO delegates getFileExtension to EditorBO")
    void testGetFileExtension_Delegation() {
        // Arrange
        when(mockEditorBO.getFileExtension("test.txt")).thenReturn("txt");

        // Act
        String result = facadeBO.getFileExtension("test.txt");

        // Assert
        assertEquals("txt", result);
        verify(mockEditorBO).getFileExtension("test.txt");
    }

    @Test
    @DisplayName("Test 8: FacadeBO delegates transliterate to EditorBO")
    void testTransliterate_Delegation() {
        // Arrange
        when(mockEditorBO.transliterate(1, "مرحبا")).thenReturn("Marhaba");

        // Act
        String result = facadeBO.transliterate(1, "مرحبا");

        // Assert
        assertEquals("Marhaba", result);
        verify(mockEditorBO).transliterate(1, "مرحبا");
    }

    @Test
    @DisplayName("Test 9: FacadeBO delegates searchKeyword to EditorBO")
    void testSearchKeyword_Delegation() {
        // Arrange
        List<String> expectedResults = Arrays.asList("Doc1.txt - prefix keyword...");
        when(mockEditorBO.searchKeyword("keyword")).thenReturn(expectedResults);

        // Act
        List<String> result = facadeBO.searchKeyword("keyword");

        // Assert
        assertEquals(expectedResults, result);
        verify(mockEditorBO).searchKeyword("keyword");
    }

    @Test
    @DisplayName("Test 10: FacadeBO delegates lemmatizeWords to EditorBO")
    void testLemmatizeWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("writing", "write");
        when(mockEditorBO.lemmatizeWords("writing")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeBO.lemmatizeWords("writing");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).lemmatizeWords("writing");
    }

    @Test
    @DisplayName("Test 11: FacadeBO delegates extractPOS to EditorBO")
    void testExtractPOS_Delegation() {
        // Arrange
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("word", Arrays.asList("NOUN", "VERB"));
        when(mockEditorBO.extractPOS("word")).thenReturn(expected);

        // Act
        Map<String, List<String>> result = facadeBO.extractPOS("word");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).extractPOS("word");
    }

    @Test
    @DisplayName("Test 12: FacadeBO delegates extractRoots to EditorBO")
    void testExtractRoots_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("مكتوب", "كتب");
        when(mockEditorBO.extractRoots("مكتوب")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeBO.extractRoots("مكتوب");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).extractRoots("مكتوب");
    }

    @Test
    @DisplayName("Test 13: FacadeBO delegates performTFIDF to EditorBO")
    void testPerformTFIDF_Delegation() {
        // Arrange
        List<String> unselected = Arrays.asList("doc1", "doc2");
        String selected = "selected doc";
        when(mockEditorBO.performTFIDF(unselected, selected)).thenReturn(0.85);

        // Act
        double result = facadeBO.performTFIDF(unselected, selected);

        // Assert
        assertEquals(0.85, result, 0.01);
        verify(mockEditorBO).performTFIDF(unselected, selected);
    }

    @Test
    @DisplayName("Test 14: FacadeBO delegates performPMI to EditorBO")
    void testPerformPMI_Delegation() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("pair1", 0.7);
        when(mockEditorBO.performPMI("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = facadeBO.performPMI("content");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).performPMI("content");
    }

    @Test
    @DisplayName("Test 15: FacadeBO delegates performPKL to EditorBO")
    void testPerformPKL_Delegation() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("word", 1.2);
        when(mockEditorBO.performPKL("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = facadeBO.performPKL("content");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).performPKL("content");
    }

    @Test
    @DisplayName("Test 16: FacadeBO delegates stemWords to EditorBO")
    void testStemWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("running", "run");
        when(mockEditorBO.stemWords("running")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeBO.stemWords("running");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).stemWords("running");
    }

    @Test
    @DisplayName("Test 17: FacadeBO delegates segmentWords to EditorBO")
    void testSegmentWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("الكتاب", "ال كتاب");
        when(mockEditorBO.segmentWords("الكتاب")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeBO.segmentWords("الكتاب");

        // Assert
        assertEquals(expected, result);
        verify(mockEditorBO).segmentWords("الكتاب");
    }

    // ==================== ERROR PROPAGATION TESTS ====================

    @Test
    @DisplayName("Test 18: FacadeBO propagates errors from EditorBO")
    void testCreateFile_ErrorPropagation() {
        // Arrange
        when(mockEditorBO.createFile(anyString(), anyString())).thenReturn(false);

        // Act
        boolean result = facadeBO.createFile("fail.txt", "content");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 19: FacadeBO handles null return from EditorBO")
    void testGetFile_NullReturn() {
        // Arrange
        when(mockEditorBO.getFile(999)).thenReturn(null);

        // Act
        Documents result = facadeBO.getFile(999);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Test 20: FacadeBO handles empty list from EditorBO")
    void testGetAllFiles_EmptyList() {
        // Arrange
        when(mockEditorBO.getAllFiles()).thenReturn(new ArrayList<>());

        // Act
        List<Documents> result = facadeBO.getAllFiles();

        // Assert
        assertTrue(result.isEmpty());
    }
}
