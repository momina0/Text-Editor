package data;

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

import dal.FacadeDAO;
import dal.IEditorDBDAO;
import dto.Documents;
import dto.Pages;

/**
 * JUnit Test Class for FacadeDAO (Data Access Layer - Facade Pattern)
 * Tests that the Facade correctly delegates to the underlying DAO
 * 
 * This verifies the Facade Pattern implementation in the data layer
 */
@ExtendWith(MockitoExtension.class)
public class FacadeDAOTest {

    @Mock
    private IEditorDBDAO mockDAO;

    private FacadeDAO facadeDAO;

    @BeforeEach
    void setUp() {
        facadeDAO = new FacadeDAO(mockDAO);
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
    @DisplayName("Test 1: FacadeDAO delegates createFileInDB")
    void testCreateFileInDB_Delegation() {
        // Arrange
        when(mockDAO.createFileInDB("test.txt", "content")).thenReturn(true);

        // Act
        boolean result = facadeDAO.createFileInDB("test.txt", "content");

        // Assert
        assertTrue(result);
        verify(mockDAO).createFileInDB("test.txt", "content");
    }

    @Test
    @DisplayName("Test 2: FacadeDAO delegates updateFileInDB")
    void testUpdateFileInDB_Delegation() {
        // Arrange
        when(mockDAO.updateFileInDB(1, "test.txt", 1, "new content")).thenReturn(true);

        // Act
        boolean result = facadeDAO.updateFileInDB(1, "test.txt", 1, "new content");

        // Assert
        assertTrue(result);
        verify(mockDAO).updateFileInDB(1, "test.txt", 1, "new content");
    }

    @Test
    @DisplayName("Test 3: FacadeDAO delegates deleteFileInDB")
    void testDeleteFileInDB_Delegation() {
        // Arrange
        when(mockDAO.deleteFileInDB(1)).thenReturn(true);

        // Act
        boolean result = facadeDAO.deleteFileInDB(1);

        // Assert
        assertTrue(result);
        verify(mockDAO).deleteFileInDB(1);
    }

    @Test
    @DisplayName("Test 4: FacadeDAO delegates getFilesFromDB")
    void testGetFilesFromDB_Delegation() {
        // Arrange
        List<Documents> docs = Arrays.asList(
                createTestDocument(1, "doc1.txt", "content1"),
                createTestDocument(2, "doc2.txt", "content2"));
        when(mockDAO.getFilesFromDB()).thenReturn(docs);

        // Act
        List<Documents> result = facadeDAO.getFilesFromDB();

        // Assert
        assertEquals(2, result.size());
        verify(mockDAO).getFilesFromDB();
    }

    @Test
    @DisplayName("Test 5: FacadeDAO delegates transliterateInDB")
    void testTransliterateInDB_Delegation() {
        // Arrange
        when(mockDAO.transliterateInDB(1, "مرحبا")).thenReturn("Marhaba");

        // Act
        String result = facadeDAO.transliterateInDB(1, "مرحبا");

        // Assert
        assertEquals("Marhaba", result);
        verify(mockDAO).transliterateInDB(1, "مرحبا");
    }

    @Test
    @DisplayName("Test 6: FacadeDAO delegates lemmatizeWords")
    void testLemmatizeWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("كاتب", "كتب");
        when(mockDAO.lemmatizeWords("كاتب")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeDAO.lemmatizeWords("كاتب");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).lemmatizeWords("كاتب");
    }

    @Test
    @DisplayName("Test 7: FacadeDAO delegates extractPOS")
    void testExtractPOS_Delegation() {
        // Arrange
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("كتاب", Arrays.asList("NOUN"));
        when(mockDAO.extractPOS("كتاب")).thenReturn(expected);

        // Act
        Map<String, List<String>> result = facadeDAO.extractPOS("كتاب");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).extractPOS("كتاب");
    }

    @Test
    @DisplayName("Test 8: FacadeDAO delegates extractRoots")
    void testExtractRoots_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("مكتوب", "كتب");
        when(mockDAO.extractRoots("مكتوب")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeDAO.extractRoots("مكتوب");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).extractRoots("مكتوب");
    }

    @Test
    @DisplayName("Test 9: FacadeDAO delegates performTFIDF")
    void testPerformTFIDF_Delegation() {
        // Arrange
        List<String> unselected = Arrays.asList("doc1", "doc2");
        String selected = "selected doc";
        when(mockDAO.performTFIDF(unselected, selected)).thenReturn(0.85);

        // Act
        double result = facadeDAO.performTFIDF(unselected, selected);

        // Assert
        assertEquals(0.85, result, 0.01);
        verify(mockDAO).performTFIDF(unselected, selected);
    }

    @Test
    @DisplayName("Test 10: FacadeDAO delegates performPMI")
    void testPerformPMI_Delegation() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("pair1", 0.7);
        when(mockDAO.performPMI("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = facadeDAO.performPMI("content");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).performPMI("content");
    }

    @Test
    @DisplayName("Test 11: FacadeDAO delegates performPKL")
    void testPerformPKL_Delegation() {
        // Arrange
        Map<String, Double> expected = new HashMap<>();
        expected.put("word", 1.2);
        when(mockDAO.performPKL("content")).thenReturn(expected);

        // Act
        Map<String, Double> result = facadeDAO.performPKL("content");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).performPKL("content");
    }

    @Test
    @DisplayName("Test 12: FacadeDAO delegates stemWords")
    void testStemWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("كاتبون", "كاتب");
        when(mockDAO.stemWords("كاتبون")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeDAO.stemWords("كاتبون");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).stemWords("كاتبون");
    }

    @Test
    @DisplayName("Test 13: FacadeDAO delegates segmentWords")
    void testSegmentWords_Delegation() {
        // Arrange
        Map<String, String> expected = new HashMap<>();
        expected.put("والكتاب", "و ال كتاب");
        when(mockDAO.segmentWords("والكتاب")).thenReturn(expected);

        // Act
        Map<String, String> result = facadeDAO.segmentWords("والكتاب");

        // Assert
        assertEquals(expected, result);
        verify(mockDAO).segmentWords("والكتاب");
    }

    // ==================== ERROR PROPAGATION TESTS ====================

    @Test
    @DisplayName("Test 14: FacadeDAO propagates false from DAO")
    void testCreateFile_PropagatesFailure() {
        // Arrange
        when(mockDAO.createFileInDB(anyString(), anyString())).thenReturn(false);

        // Act
        boolean result = facadeDAO.createFileInDB("fail.txt", "content");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 15: FacadeDAO returns empty list from DAO")
    void testGetFiles_EmptyList() {
        // Arrange
        when(mockDAO.getFilesFromDB()).thenReturn(new ArrayList<>());

        // Act
        List<Documents> result = facadeDAO.getFilesFromDB();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test 16: FacadeDAO handles null return from DAO")
    void testTransliterate_NullReturn() {
        // Arrange
        when(mockDAO.transliterateInDB(anyInt(), anyString())).thenReturn(null);

        // Act
        String result = facadeDAO.transliterateInDB(1, "test");

        // Assert
        assertNull(result);
    }

    // ==================== INTERFACE IMPLEMENTATION TESTS ====================

    @Test
    @DisplayName("Test 17: FacadeDAO implements IFacadeDAO")
    void testFacadeDAO_ImplementsInterface() {
        // Assert
        assertTrue(facadeDAO instanceof dal.IFacadeDAO);
    }

    @Test
    @DisplayName("Test 18: FacadeDAO implements IEditorDBDAO (through IFacadeDAO)")
    void testFacadeDAO_ImplementsEditorDBDAO() {
        // Assert
        assertTrue(facadeDAO instanceof IEditorDBDAO);
    }

    @Test
    @DisplayName("Test 19: Multiple calls use same mock DAO")
    void testMultipleCalls_SameDAO() {
        // Arrange
        when(mockDAO.createFileInDB(anyString(), anyString())).thenReturn(true);

        // Act
        facadeDAO.createFileInDB("file1.txt", "content1");
        facadeDAO.createFileInDB("file2.txt", "content2");
        facadeDAO.createFileInDB("file3.txt", "content3");

        // Assert
        verify(mockDAO, times(3)).createFileInDB(anyString(), anyString());
    }

    @Test
    @DisplayName("Test 20: Facade correctly passes all parameters")
    void testParameterPassing() {
        // Arrange
        when(mockDAO.updateFileInDB(eq(42), eq("specific.txt"), eq(7), eq("exact content")))
                .thenReturn(true);

        // Act
        boolean result = facadeDAO.updateFileInDB(42, "specific.txt", 7, "exact content");

        // Assert
        assertTrue(result);
        verify(mockDAO).updateFileInDB(42, "specific.txt", 7, "exact content");
    }
}
