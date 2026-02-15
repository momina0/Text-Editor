# Testing Package - Arabic Text Editor

## Overview
This package contains comprehensive JUnit 5 test cases for the Arabic Text Editor application, following the 3-layer architecture pattern.

## Directory Structure

```
Testing/
├── business/                    # Business Layer Tests
│   ├── EditorBOTest.java        # EditorBO tests (30 tests)
│   ├── FacadeBOTest.java        # FacadeBO tests - Facade Pattern (20 tests)
│   └── SearchWordTest.java      # SearchWord tests (21 tests)
├── data/                        # Data Access Layer Tests
│   ├── AbstractFactoryTest.java # Factory Pattern tests (15 tests)
│   ├── DatabaseConnectionTest.java # Singleton Pattern tests (20 tests)
│   ├── FacadeDAOTest.java       # FacadeDAO tests (20 tests)
│   ├── HashCalculatorTest.java  # MD5 Hash tests (20 tests)
│   ├── PaginationDAOTest.java   # Pagination logic tests (20 tests)
│   ├── PreProcessTextTest.java  # Text preprocessing tests (20 tests)
│   ├── TFIDFCalculatorTest.java # TF-IDF algorithm tests (20 tests)
│   └── TransliterationTest.java # Arabic transliteration tests (25 tests)
├── presentation/                # Presentation Layer Tests
│   └── EditorPOIntegrationTest.java # Integration tests (20 tests)
└── WhiteBoxTestingAnalysis.md   # CFG, Cyclomatic Complexity, Test Paths
```

## Test Summary

| Layer | Test Class | Test Count | Coverage Focus |
|-------|-----------|------------|----------------|
| **Business** | SearchWordTest | 21 | Search algorithm, validation |
| **Business** | EditorBOTest | 30 | CRUD operations, analysis methods |
| **Business** | FacadeBOTest | 20 | Facade Pattern delegation |
| **Data** | PaginationDAOTest | 20 | Pagination logic |
| **Data** | TFIDFCalculatorTest | 20 | TF-IDF algorithm accuracy |
| **Data** | HashCalculatorTest | 20 | MD5 hash integrity |
| **Data** | DatabaseConnectionTest | 20 | Singleton Pattern |
| **Data** | TransliterationTest | 25 | Arabic transliteration |
| **Data** | PreProcessTextTest | 20 | Text preprocessing |
| **Data** | AbstractFactoryTest | 15 | Abstract Factory Pattern |
| **Data** | FacadeDAOTest | 20 | Facade Pattern in DAL |
| **Presentation** | EditorPOIntegrationTest | 20 | UI-Business integration |

**Total: ~250 test cases**

## Running Tests

### Using Eclipse
1. Right-click on the Testing folder
2. Select "Run As" → "JUnit Test"

### Using Maven (if configured)
```bash
mvn test
```

### Using Gradle (if configured)
```bash
gradle test
```

## Design Patterns Tested

### 1. Singleton Pattern (DatabaseConnection)
- Private constructor verification
- Same instance guarantee
- Thread safety validation

### 2. Facade Pattern (FacadeBO, FacadeDAO)
- Delegation verification
- Error propagation testing
- Interface compliance

### 3. Abstract Factory Pattern (AbstractDAOEditorFactory)
- Factory instantiation
- DAO creation verification
- Configuration-based loading

## White-Box Testing Analysis

The `WhiteBoxTestingAnalysis.md` file contains:

1. **Control Flow Graphs (CFG)** for:
   - `SearchWord.searchKeyword()`
   - `PaginationDAO.paginate()`

2. **Cyclomatic Complexity** calculations:
   - SearchWord: V(G) = 9-10
   - PaginationDAO: V(G) = 4

3. **Independent Test Paths** in set notation:
   - P = {p₁, p₂, ..., pₙ}

## Test Categories

### Positive Tests
- Valid inputs producing expected outputs
- Normal operation flows

### Negative Tests
- Invalid inputs (null, empty, too short)
- Exception handling verification

### Boundary Tests
- Edge cases (min/max values)
- Exactly-at-limit scenarios

### Integration Tests
- Cross-layer communication
- Mock-based testing

## Dependencies

Tests require:
- JUnit 5 (Jupiter)
- Mockito (for mocking)
- Log4j2 (logging)

## Bug Fixes Applied

1. **SearchWord.java**: Added null checks for keyword, pages list, and page content
2. **EditorBO.java**: Fixed file extension check from "md5" to "md" for markdown files  
3. **PaginationDAO.java**: Changed method visibility from package-private to public

---

*Software Verification & Validation - QA Team*
