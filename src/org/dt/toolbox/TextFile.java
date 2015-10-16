package org.dt.toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class TextFile {
	
	//---------- INIT

	protected ArrayList<String> lines = new ArrayList<>();
	private int i_line = 0;
	private int i_char = 0;
	private File file;
	
	public TextFile(File textfile) {
		this.file = new File(textfile.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
	       try {
	           InputStream str = Files.newInputStream(textfile.toPath());
	           BufferedReader br = new BufferedReader(new InputStreamReader(str));
	           String line;
	           while ((line = br.readLine()) != null) {
	               lines.add(line);
	           }
	           br.close();
	           str.close();
	       }
	       catch (Exception ex) {
	           System.err.println("Couldn't get a read/write/readwrite permission for input file: " + textfile.toPath() + "\n" + ex.toString());
	       }
	}
	
	public TextFile(String path) {
		this(new File(path));
	}
	
	//---------- READING
	
	/**
	 * This function does not interfere with the text pointer position
	 * @return the whole file content as unicode text
	 */
	public String getAllText() {
		String result = "";
		for (String el : lines) {
			result += "\n" + el;
		}
		return result;
	}
	
	/**
	 * This function does not interfere with the text pointer position
	 * @return an ArrayList<String> containing the all lines
	 */
	public ArrayList<String> getAllLines() {
		return (ArrayList<String>)lines.clone();
	}
	
	/**
	 * This function does not interfere with the text pointer position
	 * @return a String containing the specified line
	 */
	public String getLine(int index) {
		return lines.get(index);
	}
	
	/**
	 * reads the current line and increments the text pointer
	 * @return the whole current line
	 */
	public String readLine() {
		i_char = 0;
		return lines.get(i_line++);
	}
	
	/**
	 * reads the current char and increments the text pointer
	 * @return char at the current position
	 */
	public char read() {
		char result = lines.get(i_line).charAt(i_char);
		if (i_char >= lines.get(i_line).length()) {
			i_char = 0;
			++i_line;
		}
		return result;
	}
	
	//---------- WRITING
	
	public void save() {
		FileWriter fw;
		try {
			file.createNewFile();
			fw = new FileWriter(file);
			for (String line : lines) {
				fw.write(line + System.getProperty("line.separator"));
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
