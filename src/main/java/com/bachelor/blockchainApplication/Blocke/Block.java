package com.bachelor.blockchainApplication.Blocke;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.simple.JSONObject;
import com.bachelor.blockchainApplication.modell.Data;



public class Block {

	private String previousHash ;
	private String blockHash ;
	private long timeStamp;
	private int blockId ; 
	Data dataFile1;
	Data dataFile2;
	Data creator;
	JSONObject jsonObject; 
	public static AtomicInteger genID = new AtomicInteger();

	public Data getCreator() {
		return creator;
	} 
	
	public String getPreviousHash() {
		return previousHash;
	} 

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public Data getDataFile1() {
		return dataFile1;
	}

	public void setDataFile1(Data dataFile1) {
		this.dataFile1 = dataFile1;
	}

	public Data getDataFile2() {
		return dataFile2;
	}

	public void setDataFile2(Data dataFile2) {
		this.dataFile2 = dataFile2;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
public Block() {
	
}
	
	public Block(String previousHash, Data dataFile1, Data dataFile2, JSONObject jsonObject, long timeStamp) {
		this.timeStamp = timeStamp;
		this.blockHash = calculateHash(); 
		this.previousHash = previousHash; 
		this.dataFile1 = dataFile1;
		this.dataFile2 = dataFile2;
		this.jsonObject = jsonObject;
		this.blockId = genID.getAndIncrement(); //for the auto generate a ID for a new block 

	}
	     //to calculate the Hashvalue of each block
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash + 
				Long.toString(timeStamp) +
				blockId +
				dataFile1 +
				dataFile2 + 
				jsonObject 
				) ; 
		return calculatedhash; 
	} 
	
//Method to return a block using an Id 
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject() {
		JSONObject myObject = new JSONObject();
		myObject.put("Block Id =", this.getBlockId()); 
		myObject.put("File 1 name =", this.getDataFile1().getObjectValue().getOriginalFilename());
		myObject.put("Hash File1 =", this.getDataFile1().getHash1());
		myObject.put("File 2 Name =", this.getDataFile2().getObjectValue().getOriginalFilename());
		myObject.put("Hash File 2 =" , this.getDataFile2().getHash1());
		myObject.put("Timestamp =", this.getTimeStamp());
		myObject.put("Hash Prev.Block =",this.getPreviousHash());
		myObject.put("hashBlock =", this.getBlockHash());
		myObject.put("Occured Change into the block = ", this.getJsonObject());
		
		return myObject;
 	}
	
}

