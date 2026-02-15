package data;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.AbstractDAOEditorFactory;
import dal.IDAOEditorFactory;
import dal.IEditorDBDAO;
import dal.MariaDBDAOFactory;

/**
 * JUnit Test Class for AbstractDAOEditorFactory (Data Access Layer)
 * Tests the Abstract Factory Pattern implementation
 * 
 * Abstract Factory Properties to verify:
 * 1. Factory method returns correct DAO type
 * 2. Singleton pattern for factory instance
 * 3. Configuration-based factory creation
 */
public class AbstractFactoryTest {

    /**
     * Reset factory instance between tests
     */
    @BeforeEach
    void setUp() {
        resetFactoryInstance();
    }

    @AfterEach
    void tearDown() {
        resetFactoryInstance();
    }

    /**
     * Helper method to reset factory singleton instance using reflection
     */
    private void resetFactoryInstance() {
        try {
            Field instance = AbstractDAOEditorFactory.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            // Ignore - may fail if field is final or doesn't exist
        }
    }

    // ==================== FACTORY PATTERN TESTS ====================

    @Test
    @DisplayName("Test 1: getInstance returns non-null factory")
    void testGetInstance_NotNull() {
        // Act
        IDAOEditorFactory factory = AbstractDAOEditorFactory.getInstance();

        // Assert
        assertNotNull(factory, "Factory instance should not be null");
    }

    @Test
    @DisplayName("Test 2: getInstance returns same instance (Singleton)")
    void testGetInstance_Singleton() {
        // Act
        IDAOEditorFactory factory1 = AbstractDAOEditorFactory.getInstance();
        IDAOEditorFactory factory2 = AbstractDAOEditorFactory.getInstance();

        // Assert
        assertSame(factory1, factory2, "Should return same factory instance");
    }

    @Test
    @DisplayName("Test 3: Factory creates MariaDB DAO")
    void testCreateEditorDAO_ReturnsDAO() {
        // Arrange
        IDAOEditorFactory factory = AbstractDAOEditorFactory.getInstance();

        // Act
        IEditorDBDAO dao = factory.createEditorDAO();

        // Assert
        assertNotNull(dao, "DAO should not be null");
    }

    @Test
    @DisplayName("Test 4: getInstance method is static")
    void testGetInstance_IsStatic() throws NoSuchMethodException {
        // Act
        Method method = AbstractDAOEditorFactory.class.getMethod("getInstance");
        int modifiers = method.getModifiers();

        // Assert
        assertTrue(Modifier.isStatic(modifiers), "getInstance should be static");
    }

    @Test
    @DisplayName("Test 5: getInstance method is public")
    void testGetInstance_IsPublic() throws NoSuchMethodException {
        // Act
        Method method = AbstractDAOEditorFactory.class.getMethod("getInstance");
        int modifiers = method.getModifiers();

        // Assert
        assertTrue(Modifier.isPublic(modifiers), "getInstance should be public");
    }

    // ==================== MARIADB FACTORY TESTS ====================

    @Test
    @DisplayName("Test 6: MariaDBDAOFactory extends AbstractDAOEditorFactory")
    void testMariaDBFactory_ExtendsAbstract() {
        // Assert
        assertTrue(AbstractDAOEditorFactory.class.isAssignableFrom(MariaDBDAOFactory.class),
                "MariaDBDAOFactory should extend AbstractDAOEditorFactory");
    }

    @Test
    @DisplayName("Test 7: MariaDBDAOFactory implements IDAOEditorFactory")
    void testMariaDBFactory_ImplementsInterface() {
        // Assert
        assertTrue(IDAOEditorFactory.class.isAssignableFrom(MariaDBDAOFactory.class),
                "MariaDBDAOFactory should implement IDAOEditorFactory");
    }

    @Test
    @DisplayName("Test 8: MariaDBDAOFactory creates valid DAO")
    void testMariaDBFactory_CreatesDAO() {
        // Arrange
        MariaDBDAOFactory factory = new MariaDBDAOFactory();

        // Act
        IEditorDBDAO dao = factory.createEditorDAO();

        // Assert
        assertNotNull(dao, "MariaDBDAOFactory should create non-null DAO");
    }

    // ==================== INTERFACE TESTS ====================

    @Test
    @DisplayName("Test 9: IDAOEditorFactory has createEditorDAO method")
    void testInterface_HasCreateMethod() throws NoSuchMethodException {
        // Act & Assert - should not throw NoSuchMethodException
        assertNotNull(IDAOEditorFactory.class.getMethod("createEditorDAO"));
    }

    @Test
    @DisplayName("Test 10: createEditorDAO returns IEditorDBDAO")
    void testCreateEditorDAO_ReturnType() throws NoSuchMethodException {
        // Act
        Class<?> returnType = IDAOEditorFactory.class.getMethod("createEditorDAO").getReturnType();

        // Assert
        assertEquals(IEditorDBDAO.class, returnType,
                "createEditorDAO should return IEditorDBDAO");
    }

    // ==================== THREAD SAFETY TESTS ====================

    @Test
    @DisplayName("Test 11: Concurrent getInstance calls return same instance")
    void testGetInstance_ThreadSafety() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final IDAOEditorFactory[] factories = new IDAOEditorFactory[threadCount];
        Thread[] threads = new Thread[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                factories[index] = AbstractDAOEditorFactory.getInstance();
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        IDAOEditorFactory first = factories[0];
        for (int i = 1; i < threadCount; i++) {
            assertSame(first, factories[i], "All threads should get same factory");
        }
    }

    // ==================== DESIGN PATTERN VERIFICATION ====================

    @Test
    @DisplayName("Test 12: AbstractDAOEditorFactory is abstract")
    void testAbstractFactory_IsAbstract() {
        // Act
        int modifiers = AbstractDAOEditorFactory.class.getModifiers();

        // Assert
        assertTrue(Modifier.isAbstract(modifiers),
                "AbstractDAOEditorFactory should be abstract");
    }

    @Test
    @DisplayName("Test 13: Factory has private instance field")
    void testFactory_HasInstanceField() throws NoSuchFieldException {
        // Act
        Field field = AbstractDAOEditorFactory.class.getDeclaredField("instance");
        int modifiers = field.getModifiers();

        // Assert
        assertTrue(Modifier.isPrivate(modifiers), "instance field should be private");
        assertTrue(Modifier.isStatic(modifiers), "instance field should be static");
    }

    @Test
    @DisplayName("Test 14: AbstractDAOEditorFactory implements IDAOEditorFactory")
    void testAbstractFactory_ImplementsInterface() {
        // Assert
        assertTrue(IDAOEditorFactory.class.isAssignableFrom(AbstractDAOEditorFactory.class),
                "AbstractDAOEditorFactory should implement IDAOEditorFactory");
    }

    @Test
    @DisplayName("Test 15: Factory instance field is of correct type")
    void testFactory_InstanceFieldType() throws NoSuchFieldException {
        // Act
        Field field = AbstractDAOEditorFactory.class.getDeclaredField("instance");

        // Assert
        assertEquals(IDAOEditorFactory.class, field.getType(),
                "instance field should be of type IDAOEditorFactory");
    }
}
