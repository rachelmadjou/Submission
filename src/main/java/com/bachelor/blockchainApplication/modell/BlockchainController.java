package com.bachelor.blockchainApplication.modell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bachelor.blockchainApplication.Blocke.Block;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiOperation;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RestController 
public class BlockchainController {  
	
	
	
	static String  path = "D:\\Bachelorarbeit\\Blockchain\\Bachelorarbeit\\blockchainApplication\\files\\";
	public static Path localPath = Paths.get("files");
	public static ArrayList<String> listPath = new ArrayList<String>();
	JSONObject jsonObject = new JSONObject();
	
	//public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static LinkedList<Block> blockchain = new LinkedList<Block>();
	
	//for metadata
	public static ArrayList<Data> listHash = new ArrayList<Data>();
	public static ArrayList<Data> test = new ArrayList<Data>();
	
	//to store a file in a systeme
	public static void moveFile(MultipartFile myFile) {
	    try {
	    		if(Files.exists(localPath)){
	    	        try {
	    	            Files.copy(myFile.getInputStream(), localPath.resolve(myFile.getOriginalFilename()));
	    	            
	    	            
	    	          } catch (Exception e) {
	    	            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    	          }
	    		}else {
	    			Files.createDirectory(localPath);
	    			 try {
		    	            Files.copy(myFile.getInputStream(), localPath.resolve(myFile.getOriginalFilename()));
		    	           
		    	          } catch (Exception e) {
		    	            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		    	          }
	    		}
	   

	      } catch (IOException e) {
	        throw new RuntimeException("Could not initialize folder for upload!");
	      }
	}
	
	//POST methode to send the first version of a file
@ApiOperation(value= "to send the first File version into the System", response = Data.class)
	@RequestMapping(value= "/sendInitFile", method = RequestMethod.POST, consumes = {"multipart/form-data"}) 
	public void sendInitFile(@ModelAttribute Data data, @RequestParam("objectValue") MultipartFile file_) throws IllegalStateException, IOException {
		
		 data.setObjectValue(file_); 
		 String[] objectValue_fileName = file_.getOriginalFilename().split("\\."); 
		 
		 //prüft ob Json 		 
		 if(objectValue_fileName[objectValue_fileName.length-1].equalsIgnoreCase("json")) {
			
			 	 moveFile(file_);
				 data.storeData();  
				 listHash.add(data);
				 listPath.add(path+file_.getOriginalFilename());
			
 	            
		 }else {
			 if(objectValue_fileName[objectValue_fileName.length-1].equalsIgnoreCase("csv") ) {
				 //Datei ist eine CSV
				 
					  			moveFile(file_);
								 //file_.transferTo(fileUpload);  	
					  			Path path_ = Paths.get(path+file_.getOriginalFilename());
								Converter.generateJson(path+file_.getOriginalFilename(),path+"Modified"+file_.getName()+".json");      
								data.storeData();  
								listHash.add(data);
								listPath.add(path+"Modified"+file_.getName()+".json");
								
			 }
		 }	
		 System.out.println(listHash.get(0).getHash1()); 
		 
	}
	
	
	//POST methode to send the version 1 of the file. or to send the latest version of a file
@ApiOperation(value= "to send the second File version into the System", response = Data.class)
	@RequestMapping(value= "/sendCurrentFile", method = RequestMethod.POST, consumes = {"multipart/form-data"}) 
	
	public JSONObject sendCurrentFile(@ModelAttribute Data data, @RequestParam("objectValue") MultipartFile file_) 
			throws IllegalStateException, IOException { 

		 data.setObjectValue(file_); 
		 String[] objectValue_fileName = file_.getOriginalFilename().split("\\."); 
		 
		 //ckecks if Json 		 
		 if(objectValue_fileName[objectValue_fileName.length-1].equalsIgnoreCase("json")) {
			 
			 	moveFile(file_);
				 data.storeData();  	
				 listHash.add(data);
				 listPath.add(path+file_.getOriginalFilename());
		 }else {
			 if(objectValue_fileName[objectValue_fileName.length-1].equalsIgnoreCase("csv") ) {
				 //file is a  CSV
				 
					  			moveFile(file_);
								 //file_.transferTo(fileUpload);  				
								Converter.generateJson(path+file_.getOriginalFilename(),path+"ModifiedCurrent"+file_.getName()+".json");      
								data.storeData();  
								listHash.add(data);
								listPath.add(path+"ModifiedCurrent"+file_.getName()+".json");
								
			 }
			 System.out.println(listHash.get(0).getHash1()); 
			 
		 }	
		
		 //test whether the file has been modified, if so, the changes are reproduced
		 //if 0 that means zero changes
		 if(listHash.size() >= 2)
		 {
				if(listHash.get(listHash.size() -2).getHash1().compareTo(listHash.get(listHash.size() -1).getHash1()) == 0)
				{
					System.out.println("Die Beide sind gleich. Sien enthalten keine neue Modificationen");
				}else {
					
					 Path first_file = Paths.get(listPath.get(listPath.size() -2));
					 Path second_file = Paths.get(listPath.get(listPath.size() -1));
					 System.out.println("First path : "+first_file+"\n Second Path : "+second_file+"\n");
					 
					 if(Files.exists(first_file))
						 if(Files.exists(second_file)) {
							 System.out.println(" All Paths exist");
							 if(listPath.size() >=0)
								jsonObject= data.readFile(listPath.get(listPath.size() -2),listPath.get(listPath.size() -1));
							 
							 //creation of Blockchain 
							
							 Block block; 
							 if(blockchain.size() == 0) {
								  block = new Block(""+ 0, listHash.get(listHash.size() - 2), listHash.get(listHash.size() - 1), jsonObject,System.currentTimeMillis());
							 }else {
								  block = new Block( blockchain.get(blockchain.size() -1).getBlockHash(),  listHash.get(listHash.size() - 2), listHash.get(listHash.size() - 1), jsonObject, System.currentTimeMillis())	;
							 }
							
							 blockchain.add(block);
						
							//displayBlockchain();	
						 }
							
				 }
		 }else {
			 System.out.println("List empty ");
		 }
		 	 
		 return jsonObject;
	}
	
	
	//get all Block
		@SuppressWarnings("unchecked")
		
@ApiOperation(value= "to get the Blockchain with all changes and Metadata", response = Data.class)
@RequestMapping(method = RequestMethod.GET , path = "/blockchain")
		public ArrayList<JSONObject> getAllBloecken() {	
			
			ArrayList<JSONObject> result = new ArrayList<JSONObject>();
			
		if(blockchain.size()==0) {
				JSONObject object = new JSONObject();
				object.put("IsEmpty", blockchain.size());
			}
		else {
			for(Block block : blockchain) {
				JSONObject object = new JSONObject();
				object.put("Block Id =", block.getBlockId()); 
				object.put("File 1 name =", block.getDataFile1().getObjectValue().getOriginalFilename());
				object.put("Hash File1 =", block.getDataFile1().getHash1());
				object.put("File 2 Name =", block.getDataFile2().getObjectValue().getOriginalFilename());
				object.put("Hash File 2 =" , block.getDataFile2().getHash1());
				object.put("Timestamp =", block.getTimeStamp());
				object.put("Hash Prev.Block =",block.getPreviousHash());
				object.put("hashBlock =", block.getBlockHash());
				object.put("Occured Change into the block = ", block.getJsonObject());
				object.put("IsValid", validate(blockchain));
				
				result.add(object);
			}	 	
				 
			}
			
			return  result;		   	 	      
		}
			

		//this methode verify the integroty of the blockchain
		
		private static boolean validate(List<Block> blockChain) {
	        
	        boolean result = true;
	        Block lastBlock = null;
	        for (int i = blockChain.size() - 1; i >= 0; i--) {
	            if (lastBlock == null) {
	                lastBlock = blockChain.get(i);
	            } else {
	                Block current = blockChain.get(i);
	                if (lastBlock.getPreviousHash() != current.getBlockHash()) {
	                    result = false;
	                    break;
	                }
	                lastBlock = current;
	            }
	        }
	        return result;
	    }	
		
	
//return a block using an Id
@SuppressWarnings("unchecked")
@ApiOperation(value= "to get a block using an ID", response = Data.class)
@RequestMapping(method = RequestMethod.GET , path = "/blockchain/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getBlock(@PathVariable("id") String id) {
		
		JSONObject object = new JSONObject();
		object.put("IsEmpty", "Block with this Id doesn't exist");
		
		for (int i=0; i<blockchain.size(); i++) {
			if(blockchain.get(i).getBlockId() == Integer.parseInt(id)){ 
			
				return blockchain.get(i).toJsonObject();
			}	
		}
		return object;   
		 
	}

	 
	
}
