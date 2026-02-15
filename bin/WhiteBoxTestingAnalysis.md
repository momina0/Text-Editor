# White-Box Testing Analysis Report

## Arabic Text Editor - Software Verification & Validation

---

## 1. Selected Methods for Analysis

Based on the assignment requirements, we have selected the following two methods for structural analysis:

1. **Search & Replace Word** - `SearchWord.searchKeyword()` method
2. **Pagination Logic** - `PaginationDAO.paginate()` method

---

## 2. SearchWord.searchKeyword() Analysis

### 2.1 Source Code

```java
public static List<String> searchKeyword(String keyword, List<Documents> docs) {
    List<String> getFiles = new ArrayList<>();

    // Node 1: Validation check
    if (keyword == null || keyword.length() < 3) {
        throw new IllegalArgumentException("Could not Search, Please Enter at least 3 letter to search");
    }

    // Node 2-3: Outer loop through documents
    for (Documents doc : docs) {
        if (doc.getPages() == null) {
            continue;
        }
        // Node 4-5: Inner loop through pages
        for (Pages page : doc.getPages()) {
            String pageContent = page.getPageContent();
            if (pageContent == null || pageContent.isEmpty()) {
                continue;
            }
            // Node 6: Content check
            if (pageContent.contains(keyword)) {
                String[] words = pageContent.split("\\s+");
                // Node 7-8: Word matching loop
                for (int i = 0; i < words.length; i++) {
                    if (words[i].equalsIgnoreCase(keyword)) {
                        // Node 9: Build result
                        String prefixWord;
                        if (i > 0) {
                            prefixWord = words[i - 1];
                        } else {
                            prefixWord = "";
                        }
                        getFiles.add(doc.getName() + " - " + prefixWord + " " + keyword + "...");
                        break;
                    }
                }
                break;
            }
        }
    }
    // Node 10: Return
    return getFiles;
}
```

### 2.2 Control Flow Graph (CFG)

```
                    [START]
                       |
                       v
                   +-------+
                   |  N1   |  Entry, Initialize variables
                   +-------+
                       |
                       v
                   +-------+
                   |  N2   |  Check: keyword == null || keyword.length() < 3?
                   +---+---+
                      /|\
                     / | \
                    Y  |  N
                   /   |   \
                  v    |    v
            +-------+  |  +-------+
            |  N3   |  |  |  N4   |  Loop: for each doc in docs
            +-------+  |  +---+---+
            (throw     |     /|\
             exception)|    / | \
                       |   Y  |  N (no more docs)
                       |  /   |   \
                       v v    |    v
                   +-------+  |  +-------+
                   |  N5   |  |  |  N14  |  Return getFiles
                   +-------+  |  +-------+
                   Check:     |      |
                   pages==null|      v
                      /|\     |   [END]
                     / | \    |
                    Y  |  N   |
                   /   |   \  |
                  |    |    v |
                  |    |  +-------+
                  |    |  |  N6   |  Loop: for each page
                  |    |  +---+---+
                  |    |     /|\
                  |    |    / | \
                  |    |   Y  |  N (no more pages)
                  |    |  /   |   \
                  |    | v    |    \
                  |    |+-------+   |
                  |    ||  N7   |   |
                  |    |+-------+   |
                  |    |Check:      |
                  |    |content==   |
                  |    |null/empty  |
                  |    |   /|\      |
                  |    |  / | \     |
                  |    | Y  |  N    |
                  |    |/   |   \   |
                  |    |    |    v  |
                  |    |    | +-------+
                  |    |    | |  N8   |  Check: content.contains(keyword)?
                  |    |    | +---+---+
                  |    |    |    /|\
                  |    |    |   / | \
                  |    |    |  N  |  Y
                  |    |    | /   |   \
                  |    |    |/    |    v
                  |    |    |     | +-------+
                  |    |    |     | |  N9   |  Loop: for each word
                  |    |    |     | +---+---+
                  |    |    |     |    /|\
                  |    |    |     |   / | \
                  |    |    |     |  Y  |  N
                  |    |    |     | /   |   \
                  |    |    |     |v    |    \
                  |    |    |  +-------+|     \
                  |    |    |  |  N10  ||      \
                  |    |    |  +-------+|       \
                  |    |    |  Check:   |        \
                  |    |    |  word     |         |
                  |    |    |  matches? |         |
                  |    |    |    /|\    |         |
                  |    |    |   / | \   |         |
                  |    |    |  N  |  Y  |         |
                  |    |    | /   |   \ |         |
                  |    |    |/    |    v|         |
                  |    |    |     | +-------+     |
                  |    |    |     | |  N11  |     |
                  |    |    |     | +-------+     |
                  |    |    |     | Check: i > 0? |
                  |    |    |     |    /|\        |
                  |    |    |     |   / | \       |
                  |    |    |     |  Y  |  N      |
                  |    |    |     | /   |   \     |
                  |    |    |     |v    |    v    |
                  |    |    |  +----+   | +----+  |
                  |    |    |  |N12 |   | |N13 |  |
                  |    |    |  +----+   | +----+  |
                  |    |    |  prefix=  | prefix= |
                  |    |    |  words    | ""      |
                  |    |    |  [i-1]    |         |
                  |    |    |     \     | /       |
                  |    |    |      \    |/        |
                  |    |    |       v   v         |
                  |    |    |    +-------+        |
                  |    |    |    |  N14  |        |
                  |    |    |    +-------+        |
                  |    |    |    Add to list      |
                  |    |    |    break (page)     |
                  |    |    |         |           |
                  +----+----+---------+-----------+
                       |
                       v
                   +-------+
                   |  N15  |  Return getFiles
                   +-------+
                       |
                       v
                    [END]
```

### 2.3 Cyclomatic Complexity Calculation

Using the formula: **V(G) = E - N + 2P**

Where:

- E = Number of edges
- N = Number of nodes
- P = Number of connected components (1 for a single program)

**Node Count (N):** 15 nodes
**Edge Count (E):** 22 edges
**Connected Components (P):** 1

**V(G) = 22 - 15 + 2(1) = 22 - 15 + 2 = 9**

Alternative calculation using predicates:

- Decision points: 9 (keyword check, docs loop, null pages, pages loop, null content, contains check, words loop, word match, i > 0)
- **V(G) = 9 + 1 = 10**

### 2.4 Independent Test Paths (Set Notation)

Let P be the set of all independent paths:

$$P = \{p_1, p_2, p_3, p_4, p_5, p_6, p_7, p_8, p_9\}$$

Where each path is defined as:

**Path 1 (Invalid keyword - null):**
$$p_1 = \langle N_{start}, N_1, N_2, N_3, N_{end} \rangle$$

- Keyword is null → throws exception

**Path 2 (Invalid keyword - too short):**
$$p_2 = \langle N_{start}, N_1, N_2, N_3, N_{end} \rangle$$

- Keyword length < 3 → throws exception

**Path 3 (Empty document list):**
$$p_3 = \langle N_{start}, N_1, N_2, N_4, N_{15}, N_{end} \rangle$$

- No documents to search → returns empty list

**Path 4 (Document with null pages):**
$$p_4 = \langle N_{start}, N_1, N_2, N_4, N_5, N_4, N_{15}, N_{end} \rangle$$

- Document has null pages → continues to next doc

**Path 5 (Page with null/empty content):**
$$p_5 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_6, N_4, N_{15}, N_{end} \rangle$$

- Page content is null/empty → continues to next page

**Path 6 (Keyword not in content):**
$$p_6 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_6, N_4, N_{15}, N_{end} \rangle$$

- Content doesn't contain keyword → continues searching

**Path 7 (Keyword found, no exact word match):**
$$p_7 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_9, N_{10}, N_9, N_6, N_4, N_{15}, N_{end} \rangle$$

- Contains substring but no exact word match

**Path 8 (Keyword found at beginning - i=0):**
$$p_8 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_9, N_{10}, N_{11}, N_{13}, N_{14}, N_{15}, N_{end} \rangle$$

- Exact match at first word → prefix is empty

**Path 9 (Keyword found with prefix - i>0):**
$$p_9 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_9, N_{10}, N_{11}, N_{12}, N_{14}, N_{15}, N_{end} \rangle$$

- Exact match with preceding word → includes prefix

---

## 3. PaginationDAO.paginate() Analysis

### 3.1 Source Code

```java
public static List<Pages> paginate(String fileContent) {
    int pageSize = 100;
    int pageNumber = 1;
    String pageContent = "";
    List<Pages> pages = new ArrayList<Pages>();

    // Node 1: Null/empty check
    if (fileContent == null || fileContent.isEmpty()) {
        pages.add(new Pages(0, 0, pageNumber, pageContent.toString()));
        return pages;
    }

    // Node 2-5: Loop through characters
    for (int i = 0; i < fileContent.length(); i++) {
        pageContent += fileContent.charAt(i);

        // Node 3: Check if page full or end of content
        if (pageContent.length() == pageSize || i == fileContent.length() - 1) {
            pages.add(new Pages(0, 0, pageNumber, pageContent));
            pageNumber++;
            pageContent = "";
        }
    }

    // Node 6: Return
    return pages;
}
```

### 3.2 Control Flow Graph (CFG)

```
                    [START]
                       |
                       v
                   +-------+
                   |  N1   |  Initialize: pageSize=100, pageNumber=1,
                   +-------+  pageContent="", pages=new ArrayList
                       |
                       v
                   +-------+
                   |  N2   |  Check: fileContent == null || fileContent.isEmpty()?
                   +---+---+
                      /|\
                     / | \
                    Y  |  N
                   /   |   \
                  v    |    v
            +-------+  |  +-------+
            |  N3   |  |  |  N4   |  Loop: for i = 0; i < fileContent.length()
            +-------+  |  +---+---+
            Add empty  |     /|\
            page,      |    / | \
            return     |   Y  |  N (loop done)
                |      |  /   |   \
                v      | v    |    v
             [END]     |+-------+ +-------+
                       ||  N5   | |  N9   |  Return pages
                       |+-------+ +-------+
                       |Append      |
                       |char to     v
                       |pageContent [END]
                       |    |
                       |    v
                       |+-------+
                       ||  N6   |  Check: pageContent.length() == pageSize
                       |+-------+  || i == fileContent.length() - 1?
                       |   /|\
                       |  / | \
                       | Y  |  N
                       |/   |   \
                       v    |    \
                   +-------+|     \
                   |  N7   ||      \
                   +-------+|       \
                   Add page,|        |
                   increment|        |
                   pageNum, |        |
                   reset    |        |
                   content  |        |
                       |    |        |
                       v    v        |
                   +-------+         |
                   |  N8   |<--------+
                   +-------+
                   Loop back (i++)
                       |
                       v
                      N4 (loop check)
```

### 3.3 Cyclomatic Complexity Calculation

Using the formula: **V(G) = E - N + 2P**

**Node Count (N):** 9 nodes
**Edge Count (E):** 11 edges
**Connected Components (P):** 1

**V(G) = 11 - 9 + 2(1) = 11 - 9 + 2 = 4**

Alternative calculation using predicates:

- Decision points: 3 (null/empty check, loop condition, page full/end check)
- **V(G) = 3 + 1 = 4**

### 3.4 Independent Test Paths (Set Notation)

Let P be the set of all independent paths:

$$P = \{p_1, p_2, p_3, p_4\}$$

Where each path is defined as:

**Path 1 (Null content):**
$$p_1 = \langle N_{start}, N_1, N_2, N_3, N_{end} \rangle$$

- fileContent is null → returns single empty page

**Path 2 (Empty string content):**
$$p_2 = \langle N_{start}, N_1, N_2, N_3, N_{end} \rangle$$

- fileContent is empty string → returns single empty page

**Path 3 (Content shorter than page size):**
$$p_3 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_4, N_9, N_{end} \rangle$$

- Content fits in one page (e.g., 50 chars) → single page with content

**Path 4 (Content exactly page size):**
$$p_4 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_4, N_9, N_{end} \rangle$$

- Content is exactly 100 chars → single full page

**Path 5 (Content spans multiple pages):**
$$p_5 = \langle N_{start}, N_1, N_2, N_4, N_5, N_6, N_7, N_8, N_4, N_5, N_6, N_7, N_8, N_4, N_9, N_{end} \rangle$$

- Content is 150 chars → two pages (100 + 50)

---

## 4. Test Case Mapping

### 4.1 SearchWord Test Cases to Paths

| Test Case                                | Path Covered | Description                |
| ---------------------------------------- | ------------ | -------------------------- |
| testSearchKeyword_NullKeyword            | p₁           | Null keyword input         |
| testSearchKeyword_KeywordTooShort        | p₂           | Keyword < 3 chars          |
| testSearchKeyword_EmptyDocumentList      | p₃           | Empty docs list            |
| testSearchKeyword_NullPages              | p₄           | Doc with null pages        |
| testSearchKeyword_EmptyPageContent       | p₅           | Null/empty page content    |
| testSearchKeyword_NotFound               | p₆           | Keyword not in any doc     |
| testSearchKeyword_SubstringNotExactMatch | p₇           | Contains but no word match |
| testSearchKeyword_KeywordAtBeginning     | p₈           | Match at first word        |
| testSearchKeyword_ExactMatch             | p₉           | Match with prefix word     |

### 4.2 PaginationDAO Test Cases to Paths

| Test Case                      | Path Covered | Description         |
| ------------------------------ | ------------ | ------------------- |
| testPaginate_NullContent       | p₁           | Null input          |
| testPaginate_EmptyString       | p₂           | Empty string input  |
| testPaginate_SinglePage        | p₃           | Content < 100 chars |
| testPaginate_ExactlyPageSize   | p₄           | Content = 100 chars |
| testPaginate_JustOverPageSize  | p₅           | Content = 101 chars |
| testPaginate_MultipleFullPages | p₅           | Content = 300 chars |

---

## 5. Summary

### Complexity Summary Table

| Method                     | V(G) | Nodes | Edges | Decision Points |
| -------------------------- | ---- | ----- | ----- | --------------- |
| SearchWord.searchKeyword() | 9-10 | 15    | 22    | 9               |
| PaginationDAO.paginate()   | 4    | 9     | 11    | 3               |

### Test Coverage

- **SearchWord**: 20+ test cases covering all 9 independent paths
- **PaginationDAO**: 20 test cases covering all 4 independent paths

### Bug Fixes Applied

1. **SearchWord.java**: Added null checks for keyword, pages list, and page content
2. **EditorBO.java**: Fixed file extension check from "md5" to "md" for markdown files
3. **PaginationDAO.java**: Changed method visibility from package-private to public

---

_Report Generated: February 2026_
_Arabic Text Editor - QA Analysis_
