package com.jiwei.base_llm.common.parser;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import java.io.IOException;
import java.io.InputStream;

public class DocumentParser {

	private final Tika tika = new Tika();

	public String parse(InputStream inputStream, String fileName) throws IOException {
		try {
			String content = tika.parseToString(inputStream);
			if (content == null || content.isBlank()) {
				throw new IOException("Unable to extract text from file: " + fileName);
			}
			return content.trim();
		}
		catch (TikaException ex) {
			throw new IOException("Failed to parse document: " + fileName, ex);
		}
	}

}
