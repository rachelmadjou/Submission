package com.bachelor.blockchainApplication.modell;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.swagger.annotations.ApiModelProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Data {
	
@ApiModelProperty(notes="send File ID", name ="id", required = true)
	private int id ;

@ApiModelProperty(notes="creator of the File", name ="creator", required = true)
	private String creator;

@ApiModelProperty(notes="date the file was created", name ="Date", required = true)
	private Date date;

@ApiModelProperty(notes="the send File", name ="ojectValue", required = true)
	private MultipartFile  objectValue;

@ApiModelProperty(notes="the format of the File", name ="formatFile", required = true)
	private String formatFile; 

	private static FileWriter file1;
	private String hash1;

	public String getHash1() {
		return this.hash1;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public void setFormatFile(String formatFile) {
		this.formatFile = formatFile;
	}

	public String getFormatFile() {
		return formatFile;
	}

	public int getId() {
		return id;
	}

	public String getCreator() {
		return creator;
	}


	public Date getDate() {
		return date;
	}

	public MultipartFile getObjectValue() {
		return this.objectValue;
	}

	public void setObjectValue(MultipartFile objectValue_) {
		this.objectValue = objectValue_;
	}

	
	HashMap<String, Object> obj= new HashMap<String, Object>();
	
	
	public Data(int id, String creator, Date date, 
			MultipartFile objectValue, String formatFile) {
		this.id = id;
		this.creator = creator;
		this.date = date;
		this.objectValue = objectValue;
		this.formatFile = formatFile;
		
	}

	//to store the data in Ipfs and to generate a hashIpfsValue

	public String storeData() { 	
		
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001"); 

		try {
			NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(new FileInputStream("D:\\Bachelorarbeit\\Blockchain\\Bachelorarbeit\\blockchainApplication\\files\\" 
					+this.getObjectValue().getOriginalFilename())); 

			MerkleNode response = ipfs.add(is).get(0);
			response.name.get();			
			this.hash1 = response.hash.toBase58(); 
		} catch (IOException ex) {
			throw new RuntimeException("Error whilst communicating with the IPFS node", ex); 
		} 

		return this.getHash1();  

	}

	//function to read the Json file, convert it in he Map File and make the difference between two File
	
	@SuppressWarnings({ "resource", "unchecked" })
	public JSONObject readFile(String path_first_file, String path_second_file) { 
	
		//read a file as Map instance
		ObjectMapper mapper = new ObjectMapper(); 
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		TypeReference<HashMap<String, Object>> type = 
		    new TypeReference<HashMap<String, Object>>() {};   
		    
		try  {
			
			FileReader reader;	
			FileReader readers;
			
				//MAP is formed here
			reader = new FileReader(path_first_file);
			readers = new FileReader(path_second_file);     
					Map<String, Object> leftMap = mapper.readValue(reader, type);
					Map<String, Object> rightMap = mapper.readValue(readers, type);
					
					//Objects on the maps are smoothed
					Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
					Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);    
					
					//compare mappen and print the difference
				MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);
				
				//Object to store the result
				JSONObject obj = new JSONObject();
				JSONObject obj1 = new JSONObject();
					
				System.out.println("gelöschten Elementen in Datensatz2\n--------------------------");			
				 difference.entriesOnlyOnLeft().forEach((key, value) -> {
					 obj1.put(key, "");
					// System.out.println(key + ": " + value);
				 });		 
				 
				 obj.put("geloeschte Element", obj1);
				 
				System.out.println("\n\nEingefügte Elementen in Datensatz 2\n--------------------------");	
				JSONObject obj2= new JSONObject();
				difference.entriesOnlyOnRight().forEach((key, value) ->{
					obj2.put(key, "");
					//System.out.println(key + ": " + value);
				});
				
				 obj.put("Eingefuegte Element", obj2);
				 
				System.out.println("\n\nGeänderte Elementen\n--------------------------");
				JSONObject obj3= new JSONObject();
				difference.entriesDiffering().forEach((key, value) ->{
					obj3.put(key, ""); 
					//System.out.println(key + ": " + value);
				});
				
				 obj.put("Geanderte Element", obj3);
				
				return obj; 
    						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null; 
		} catch (IOException e) {
			e.printStackTrace();
			return null; 
		}

	}    

}
