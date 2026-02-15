package bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.Documents;
import dto.Pages;
import pl.EditorPO;

public class SearchWord {
	public static List<String> searchKeyword(String keyword, List<Documents> docs) {
		final Logger LOGGER = LogManager.getLogger(EditorPO.class);
		List<String> getFiles = new ArrayList<>();

		// BUG FIX: Added null check for keyword
		if (keyword == null || keyword.length() < 3) {
			throw new IllegalArgumentException("Could not Search, Please Enter at least 3 letter to search");
		}

		for (Documents doc : docs) {
			// BUG FIX: Added null check for pages list
			if (doc.getPages() == null) {
				continue;
			}
			for (Pages page : doc.getPages()) {
				String pageContent = page.getPageContent();
				// BUG FIX: Added null check for page content
				if (pageContent == null || pageContent.isEmpty()) {
					continue;
				}
				if (pageContent.contains(keyword)) {

					String[] words = pageContent.split("\\s+");

					for (int i = 0; i < words.length; i++) {
						if (words[i].equalsIgnoreCase(keyword)) {

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
		return getFiles;
	}

}
