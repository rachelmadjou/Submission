package com.bachelor.blockchainApplication.modell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

//a csv converter if we have a csv as Input file
public final class Converter {

	MultipartFile file;
	
public Converter(MultipartFile file) {

		this.file = file;
	}
	

public static FileWriter filewrite;
public static ArrayList<String> showCsv(String csv_path){
	ArrayList<String> lineList = new ArrayList<String>();
	BufferedReader csv_reader;
	try {
		csv_reader = new BufferedReader(new FileReader(csv_path));
		String line ="";
		while((line = csv_reader.readLine()) != null) {
			
			lineList.add(line);
			
		}
	} catch (FileNotFoundException e) {  
		
		System.out.println("File Read error "+e.toString());
	}catch(IOException io) {
		System.out.println("Path Error  "+io.toString());
		
	}
	

	return lineList;
}

@SuppressWarnings("unchecked")
public static void generateJson(String csv_path,String json_file_name) {
	ArrayList<String> listData = showCsv(csv_path);
	
	JSONObject my_object = new JSONObject();
		
	int index = 0;

	//write content
	
	String[] headerContent = listData.get(0).split(";");
	index = 1;
	JSONArray contents = new JSONArray();
	while(index < listData.size()) {
		String[] content = listData.get(index).split(";");
		JSONArray content_ = new JSONArray();
		int i = 0;
		JSONObject sub_object = new JSONObject();
		while(i < content.length) {
			sub_object.put(headerContent[i], content[i]);
					
			i++;
		}
		content_.add(sub_object);
		
		contents.add(content_);
				index++;
	}
	
	my_object.put("Contents", contents);
	
	try {
		filewrite = new FileWriter(json_file_name);
		filewrite.write(my_object.toJSONString());
		
	}catch(IOException io) {}
	finally {
		try {
			filewrite.flush();
			filewrite.close();
		}catch(IOException ex) {
			System.err.println("Error close file "+ex.toString());
		}
	}
	
}


}