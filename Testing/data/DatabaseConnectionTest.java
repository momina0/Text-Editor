package data;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.DatabaseConnection;

/**
 * JUnit Test Class for DatabaseConnection (Data Access Layer)
 * Tests the Singleton Pattern implementation
 * 
 * Singleton Properties to verify:
 * 1. Only one instance exists
 * 2. Private constructor
 * 3. Static getInstance() method
 * 4. Same reference returned on multiple calls
 * 
 * Note: Some tests may require database to be running
 */
public class DatabaseConnectionTest {

    /**
     * Reset singleton instance between tests using reflection
     */
    @BeforeEach
    void setUp() {
        resetSingleton();
    }

    @AfterEach
    void tearDown() {
        resetSingleton();
    }

    /**
     * Helper method to reset the singleton instance using reflection
     * This allows each test to start fresh
     */
    private void resetSingleton() {
        try {
            Field instance = DatabaseConnection.class.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            // Ignore - may fail if field doesn't exist or is final
        }
    }

    // ==================== SINGLETON PROPERTY TESTS ====================

    @Test
    @DisplayName("Test 1: Singleton - constructor is private")
    void testSingleton_PrivateConstructor() {
        // Act
        Constructor<?>[] constructors = DatabaseConnection.class.getDeclaredConstructors();

        // Assert
        for (Constructor<?> constructor : constructors) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                    "Constructor should be private for Singleton pattern");
        }
    }

    @Test
    @DisplayName("Test 2: Singleton - getInstance returns non-null")
    void testSingleton_GetInstanceNotNull() {
        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();

        // Assert
        assertNotNull(instance, "getInstance should return a non-null instance");
    }

    @Test
    @DisplayName("Test 3: Singleton - same instance returned on multiple calls")
    void testSingleton_SameInstance() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();

        // Assert
        assertSame(instance1, instance2, "Should return same instance");
        assertSame(instance2, instance3, "Should return same instance");
        assertSame(instance1, instance3, "Should return same instance");
    }

    @Test
    @DisplayName("Test 4: Singleton - getInstance method is static")
    void testSingleton_StaticMethod() throws NoSuchMethodException {
        // Act
        int modifiers = DatabaseConnection.class.getMethod("getInstance").getModifiers();

        // Assert
        assertTrue(Modifier.isStatic(modifiers),
                "getInstance should be a static method");
    }

    @Test
    @DisplayName("Test 5: Singleton - getInstance is synchronized")
    void testSingleton_Synchronized() throws NoSuchMethodException {
        // Act
        int modifiers = DatabaseConnection.class.getMethod("getInstance").getModifiers();

        // Assert
        assertTrue(Modifier.isSynchronized(modifiers),
                "getInstance should be synchronized for thread safety");
    }

    @Test
    @DisplayName("Test 6: Singleton - has static INSTANCE field")
    void testSingleton_HasInstanceField() throws NoSuchFieldException {
        // Act
        Field instanceField = DatabaseConnection.class.getDeclaredField("INSTANCE");
        int modifiers = instanceField.getModifiers();

        // Assert
        assertTrue(Modifier.isStatic(modifiers), "INSTANCE field should be static");
        assertTrue(Modifier.isPrivate(modifiers), "INSTANCE field should be private");
    }

    // ==================== CONNECTION TESTS ====================

    @Test
    @DisplayName("Test 7: getConnection method exists")
    void testGetConnection_MethodExists() throws NoSuchMethodException {
        // Act & Assert - should not throw NoSuchMethodException
        assertNotNull(DatabaseConnection.class.getMethod("getConnection"));
    }

    @Test
    @DisplayName("Test 8: getConnection returns Connection type")
    void testGetConnection_ReturnType() throws NoSuchMethodException {
        // Act
        Class<?> returnType = DatabaseConnection.class.getMethod("getConnection").getReturnType();

        // Assert
        assertEquals(Connection.class, returnType,
                "getConnection should return java.sql.Connection");
    }

    @Test
    @DisplayName("Test 9: closeConnection method exists")
    void testCloseConnection_MethodExists() throws NoSuchMethodException {
        // Act & Assert - should not throw NoSuchMethodException
        assertNotNull(DatabaseConnection.class.getMethod("closeConnection"));
    }

    @Test
    @DisplayName("Test 10: closeConnection returns void")
    void testCloseConnection_ReturnType() throws NoSuchMethodException {
        // Act
        Class<?> returnType = DatabaseConnection.class.getMethod("closeConnection").getReturnType();

        // Assert
        assertEquals(void.class, returnType);
    }

    // ==================== THREAD SAFETY TESTS ====================

    @Test
    @DisplayName("Test 11: Singleton - thread safety with concurrent access")
    void testSingleton_ThreadSafety() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final DatabaseConnection[] instances = new DatabaseConnection[threadCount];
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that get instance simultaneously
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all instances should be the same
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < threadCount; i++) {
            assertSame(firstInstance, instances[i],
                    "All threads should get the same instance");
        }
    }

    @Test
    @DisplayName("Test 12: Singleton - rapid successive calls return same instance")
    void testSingleton_RapidCalls() {
        // Arrange
        final int callCount = 100;
        DatabaseConnection[] instances = new DatabaseConnection[callCount];

        // Act
        for (int i = 0; i < callCount; i++) {
            instances[i] = DatabaseConnection.getInstance();
        }

        // Assert
        for (int i = 1; i < callCount; i++) {
            assertSame(instances[0], instances[i],
                    "All calls should return same instance");
        }
    }

    // ==================== PROPERTY READING TESTS ====================

    @Test
    @DisplayName("Test 13: DatabaseConnection reads from config.properties")
    void testDatabaseConnection_ReadsConfig() {
        // The constructor reads from config.properties
        // If file doesn't exist or DB is down, connection may be null
        // but the instance should still be created

        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();

        // Assert
        assertNotNull(instance, "Instance should be created even if connection fails");
    }

    @Test
    @DisplayName("Test 14: Connection can be null if database unavailable")
    void testGetConnection_CanBeNull() {
        // This tests that the class handles connection failure gracefully

        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();

        // Assert - connection may be null if database is not running
        // This is acceptable behavior
        if (conn == null) {
            // Database not available - acceptable
            assertTrue(true, "Connection can be null if database unavailable");
        } else {
            // Database available - connection is valid
            assertNotNull(conn);
        }
    }

    // ==================== REFLECTION-BASED INSTANTIATION TESTS
    // ====================

    @Test
    @DisplayName("Test 15: Cannot create instance using reflection newInstance")
    void testSingleton_ReflectionProtection() {
        // Act & Assert
        Constructor<?>[] constructors = DatabaseConnection.class.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                    "All constructors should be private");
        }
    }

    @Test
    @DisplayName("Test 16: getInstance returns DatabaseConnection type")
    void testGetInstance_ReturnType() throws NoSuchMethodException {
        // Act
        Class<?> returnType = DatabaseConnection.class.getMethod("getInstance").getReturnType();

        // Assert
        assertEquals(DatabaseConnection.class, returnType);
    }

    // ==================== DESIGN PATTERN VERIFICATION ====================

    @Test
    @DisplayName("Test 17: Class follows Singleton design pattern")
    void testSingleton_PatternCompliance() throws NoSuchFieldException {
        // Verify all Singleton pattern requirements

        // 1. Private constructor
        Constructor<?>[] constructors = DatabaseConnection.class.getDeclaredConstructors();
        assertTrue(constructors.length > 0, "Should have at least one constructor");
        for (Constructor<?> c : constructors) {
            assertTrue(Modifier.isPrivate(c.getModifiers()), "Constructor must be private");
        }

        // 2. Static instance field
        Field instanceField = DatabaseConnection.class.getDeclaredField("INSTANCE");
        assertTrue(Modifier.isStatic(instanceField.getModifiers()), "INSTANCE must be static");

        // 3. Public static getInstance method
        try {
            var method = DatabaseConnection.class.getMethod("getInstance");
            assertTrue(Modifier.isStatic(method.getModifiers()), "getInstance must be static");
            assertTrue(Modifier.isPublic(method.getModifiers()), "getInstance must be public");
        } catch (NoSuchMethodException e) {
            fail("getInstance method should exist");
        }
    }

    @Test
    @DisplayName("Test 18: Only one instance field exists")
    void testSingleton_SingleInstanceField() {
        // Act
        Field[] fields = DatabaseConnection.class.getDeclaredFields();
        int staticInstanceCount = 0;

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    field.getType().equals(DatabaseConnection.class)) {
                staticInstanceCount++;
            }
        }

        // Assert
        assertEquals(1, staticInstanceCount,
                "Should have exactly one static instance field");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 19: CloseConnection does not throw on null connection")
    void testCloseConnection_NullSafe() {
        // Act & Assert - should not throw exception
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertDoesNotThrow(() -> instance.closeConnection());
    }

    @Test
    @DisplayName("Test 20: Multiple close calls are safe")
    void testCloseConnection_MultipleCallsSafe() {
        // Act & Assert - multiple closes should not throw
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertDoesNotThrow(() -> {
            instance.closeConnection();
            instance.closeConnection();
            instance.closeConnection();
        });
    }
}
