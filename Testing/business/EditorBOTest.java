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

import bll.EditorBO;
import bll.IEditorBO;
import dal.IFacadeDAO;
import dto.Documents;
import dto.Pages;

/**
 * JUnit Test Class for EditorBO (Business Layer)
 * Tests the business logic operations with mocked DAO layer
 * 
 * Tests cover: File operations, search, transliteration, and text analysis
 */
@ExtendWith(MockitoExtension.class)
public class EditorBOTest {

    @Mock
    private IFacadeDAO mockFacadeDAO;

    private EditorBO editorBO;

    @BeforeEach
    void setUp() {
        editorBO = new EditorBO(mockFacadeDAO);
    }

    /**
     * Helper method to create test document
     */
    private Documents createTestDocument(int id, String name, String content) {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, id, 1, content));
        return new Documents(id, name, "testhash", "2024-01-01", "2024-01-01", pages);
    }

    // ==================== CREATE FILE TESTS ====================

    @Test
    @DisplayName("Test 1: Create file successfully")
    void testCreateFile_Success() {
        // Arrange
        when(mockFacadeDAO.createFileInDB("test.txt", "content")).thenReturn(true);

        // Act
        boolean result = editorBO.createFile("test.txt", "content");

        // Assert
        assertTrue(result);
        verify(mockFacadeDAO).createFileInDB("test.txt", "content");
    }

    @Test
    @DisplayName("Test 2: Create file fails")
    void testCreateFile_Failure() {
        // Arrange
        when(mockFacadeDAO.createFileInDB("test.txt", "content")).thenReturn(false);

        // Act
        boolean result = editorBO.createFile("test.txt", "content");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 3: Create file with exception handling")
    void testCreateFile_Exception() {
        // Arrange
        when(mockFacadeDAO.createFileInDB(anyString(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = editorBO.createFile("test.txt", "content");

        // Assert
        assertFalse(result, "Should return false on exception");
    }

    // ==================== UPDATE FILE TESTS ====================

    @Test
    @DisplayName("Test 4: Update file successfully")
    void testUpdateFile_Success() {
        // Arrange
        when(mockFacadeDAO.updateFileInDB(1, "updated.txt", 1, "new content")).thenReturn(true);

        // Act
        boolean result = editorBO.updateFile(1, "updated.txt", 1, "new content");

        // Assert
        assertTrue(result);
        verify(mockFacadeDAO).updateFileInDB(1, "updated.txt", 1, "new content");
    }

    @Test
    @DisplayName("Test 5: Update file fails")
    void testUpdateFile_Failure() {
        // Arrange
        when(mockFacadeDAO.updateFileInDB(anyInt(), anyString(), anyInt(), anyString())).thenReturn(false);

        // Act
        boolean result = editorBO.updateFile(999, "nonexistent.txt", 1, "content");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 6: Update file with exception")
    void testUpdateFile_Exception() {
        // Arrange
        when(mockFacadeDAO.updateFileInDB(anyInt(), anyString(), anyInt(), anyString()))
                .thenThrow(new RuntimeException("Update error"));

        // Act
        boolean result = editorBO.updateFile(1, "test.txt", 1, "content");

        // Assert
        assertFalse(result);
    }

    // ==================== DELETE FILE TESTS ====================

    @Test
    @DisplayName("Test 7: Delete file successfully")
    void testDeleteFile_Success() {
        // Arrange
        when(mockFacadeDAO.deleteFileInDB(1)).thenReturn(true);

        // Act
        boolean result = editorBO.deleteFile(1);

        // Assert
        assertTrue(result);
        verify(mockFacadeDAO).deleteFileInDB(1);
    }

    @Test
    @DisplayName("Test 8: Delete nonexistent file")
    void testDeleteFile_NotFound() {
        // Arrange
        when(mockFacadeDAO.deleteFileInDB(999)).thenReturn(false);

        // Act
        boolean result = editorBO.deleteFile(999);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 9: Delete file with exception")
    void testDeleteFile_Exception() {
        // Arrange
        when(mockFacadeDAO.deleteFileInDB(anyInt())).thenThrow(new RuntimeException("Delete error"));

        // Act
        boolean result = editorBO.deleteFile(1);

        // Assert
        assertFalse(result);
    }

    // ==================== GET FILE TESTS ====================

    @Test
    @DisplayName("Test 10: Get file by ID - found")
    void testGetFile_Found() {
        // Arrange
        Documents doc = createTestDocument(1, "test.txt", "content");
        List<Documents> docs = Arrays.asList(doc);
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(docs);

        // Act
        Documents result = editorBO.getFile(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test.txt", result.getName());
    }

    @Test
    @DisplayName("Test 11: Get file by ID - not found")
    void testGetFile_NotFound() {
        // Arrange
        Documents doc = createTestDocument(1, "test.txt", "content");
        List<Documents> docs = Arrays.asList(doc);
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(docs);

        // Act
        Documents result = editorBO.getFile(999);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Test 12: Get file from empty list")
    void testGetFile_EmptyList() {
        // Arrange
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(new ArrayList<>());

        // Act
        Documents result = editorBO.getFile(1);

        // Assert
        assertNull(result);
    }

    // ==================== GET ALL FILES TESTS ====================

    @Test
    @DisplayName("Test 13: Get all files - multiple documents")
    void testGetAllFiles_MultipleDocuments() {
        // Arrange
        List<Documents> docs = Arrays.asList(
                createTestDocument(1, "doc1.txt", "content1"),
                createTestDocument(2, "doc2.txt", "content2"));
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(docs);

        // Act
        List<Documents> result = editorBO.getAllFiles();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test 14: Get all files - empty list")
    void testGetAllFiles_Empty() {
        // Arrange
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(new ArrayList<>());

        // Act
        List<Documents> result = editorBO.getAllFiles();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ==================== FILE EXTENSION TESTS ====================

    @Test
    @DisplayName("Test 15: Get file extension - txt file")
    void testGetFileExtension_TxtFile() {
        // Act
        String extension = editorBO.getFileExtension("document.txt");

        // Assert
        assertEquals("txt", extension);
    }

    @Test
    @DisplayName("Test 16: Get file extension - md file")
    void testGetFileExtension_MdFile() {
        // Act
        String extension = editorBO.getFileExtension("readme.md");

        // Assert
        assertEquals("md", extension);
    }

    @Test
    @DisplayName("Test 17: Get file extension - no extension")
    void testGetFileExtension_NoExtension() {
        // Act
        String extension = editorBO.getFileExtension("filewithoutext");

        // Assert
        assertEquals("", extension);
    }

    @Test
    @DisplayName("Test 18: Get file extension - multiple dots")
    void testGetFileExtension_MultipleDots() {
        // Act
        String extension = editorBO.getFileExtension("file.name.with.dots.txt");

        // Assert
        assertEquals("txt", extension);
    }

    @Test
    @DisplayName("Test 19: Get file extension - dot at start")
    void testGetFileExtension_DotAtStart() {
        // Act
        String extension = editorBO.getFileExtension(".hidden");

        // Assert
        assertEquals("hidden", extension);
    }

    // ==================== TRANSLITERATE TESTS ====================

    @Test
    @DisplayName("Test 20: Transliterate Arabic text")
    void testTransliterate() {
        // Arrange
        when(mockFacadeDAO.transliterateInDB(1, "مرحبا")).thenReturn("Marhaba");

        // Act
        String result = editorBO.transliterate(1, "مرحبا");

        // Assert
        assertEquals("Marhaba", result);
        verify(mockFacadeDAO).transliterateInDB(1, "مرحبا");
    }

    // ==================== SEARCH KEYWORD TESTS ====================

    @Test
    @DisplayName("Test 21: Search keyword delegates to SearchWord class")
    void testSearchKeyword() {
        // Arrange
        Documents doc = createTestDocument(1, "test.txt", "This has keyword inside");
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(Arrays.asList(doc));

        // Act
        List<String> results = editorBO.searchKeyword("keyword");

        // Assert
        assertFalse(results.isEmpty());
    }

    @Test
    @DisplayName("Test 22: Search keyword - too short throws exception")
    void testSearchKeyword_TooShort() {
        // Arrange
        when(mockFacadeDAO.getFilesFromDB()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            editorBO.searchKeyword("ab");
        });
    }

    // ==================== TEXT ANALYSIS TESTS ====================

    @Test
    @DisplayName("Test 23: Lemmatize words delegates to DAO")
    void testLemmatizeWords() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("running", "run");
        when(mockFacadeDAO.lemmatizeWords("running")).thenReturn(expected);

        // Act
        Map<String, String> result = editorBO.lemmatizeWords("running");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 24: Extract POS tags")
    void testExtractPOS() {
        // Arrange
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("word", Arrays.asList("NOUN"));
        when(mockFacadeDAO.extractPOS("word")).thenReturn(expected);

        // Act
        Map<String, List<String>> result = editorBO.extractPOS("word");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 25: Extract roots")
    void testExtractRoots() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("كتابة", "كتب");
        when(mockFacadeDAO.extractRoots("كتابة")).thenReturn(expected);

        // Act
        Map<String, String> result = editorBO.extractRoots("كتابة");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 26: Perform TF-IDF calculation")
    void testPerformTFIDF() {
        // Arrange
        List<String> otherDocs = Arrays.asList("doc1 content", "doc2 content");
        when(mockFacadeDAO.performTFIDF(otherDocs, "selected content")).thenReturn(0.75);

        // Act
        double result = editorBO.performTFIDF(otherDocs, "selected content");

        // Assert
        assertEquals(0.75, result, 0.01);
    }

    @Test
    @DisplayName("Test 27: Perform PMI calculation")
    void testPerformPMI() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("word1", 0.5);
        when(mockFacadeDAO.performPMI("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = editorBO.performPMI("content");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 28: Perform PKL calculation")
    void testPerformPKL() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("word1", 1.5);
        when(mockFacadeDAO.performPKL("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = editorBO.performPKL("content");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 29: Stem words")
    void testStemWords() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("running", "run");
        when(mockFacadeDAO.stemWords("running")).thenReturn(expected);

        // Act
        Map<String, String> result = editorBO.stemWords("running");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test 30: Segment words")
    void testSegmentWords() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("والكتاب", "و ال كتاب");
        when(mockFacadeDAO.segmentWords("والكتاب")).thenReturn(expected);

        // Act
        Map<String, String> result = editorBO.segmentWords("والكتاب");

        // Assert
        assertEquals(expected, result);
    }
}
