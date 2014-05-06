package com.example.timemanagement.sqlite;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class SQLiteMethods extends SQLiteOpenHelper {
	
	// Database info

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "TimeManagement";
 
    // Constructor
    public SQLiteMethods(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Create "orders"-table
        String CREATE_ORDER_TABLE = 
        	"CREATE TABLE orders (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            "orderNumber TEXT, " +
            "orderName TEXT, " +
            "directWork INTEGER)";
        db.execSQL(CREATE_ORDER_TABLE);
        
        // Create "blocks"-table
        String CREATE_BLOCKS_TABLE =
        	"CREATE TABLE blocks(" +
        	"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        	"start INTEGER, " +
        	"stop INTEGER, " +
        	"orderID INTEGER, " +
        	"comment TEXT, "
        	+"checked INTEGER)";
        db.execSQL(CREATE_BLOCKS_TABLE);
    }
    
    // *********************************************** //
    // If a new database version is noticed
    // *********************************************** //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS blocks");
        this.onCreate(db);
    }
    
    // *********************************************** //
    // ORDERS
    // *********************************************** //
    
    // *********************************************** //
    // Define Orders table
    // *********************************************** //
    private static final String ORDERS_TABLE = "orders"; // Name
    private static final String ORDERS_TABLE_KEY_ID = "ID";
    private static final String ORDERS_TABLE_KEY_ORDERNUMBER = "orderNumber";
    private static final String ORDERS_TABLE_KEY_ORDERNAME = "orderName";
    private static final String ORDERS_TABLE_KEY_DIRECTWORK = "directWork";
    private static final String[] ORDERS_TABLE_COLUMNS = {ORDERS_TABLE_KEY_ID, 
    														ORDERS_TABLE_KEY_ORDERNUMBER, 
    														ORDERS_TABLE_KEY_ORDERNAME,
    														ORDERS_TABLE_KEY_DIRECTWORK};
    
    /**
     * INSERT: Create new order 
     * @param order
     */
    public void addOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ORDERS_TABLE_KEY_ORDERNUMBER, order.getOrderNumber());  
        values.put(ORDERS_TABLE_KEY_ORDERNAME, order.getOrderName()); 
        values.put(ORDERS_TABLE_KEY_DIRECTWORK, order.getOrderDirectWork());
 
        order.setID((int)db.insert(ORDERS_TABLE, null, values));
        db.close(); 
    }
    
    /**
     * GET: Return a order
     * 
     * @param ID
     * @return
     */
    public Order getOrder(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ORDERS_TABLE, ORDERS_TABLE_COLUMNS, 
        							" id = ?", 
        							new String[] { String.valueOf(ID) }, 
        							null, 
        							null, 
        							null, 
        							"1");
        
        if (cursor != null) {
        	// If there are any rows
        	if(!cursor.isAfterLast()) { // Return Order
        		cursor.moveToFirst();
        		
        		Order order = new Order();
	            order.setID(Integer.parseInt(cursor.getString(0)));
	            order.setOrderNumber(cursor.getString(1));
	            order.setOrderName(cursor.getString(2));
	            order.setOrderDirectWork(cursor.getInt(3));
	            return order;
        	}
        	else { // No rows
        		return null;
        	}
        } // No rows
        else {
        	return null;
        }
    }
    
    /**
     * GET: Return all orders
     * 
     * @return 
     */
    public List<Order> getAllOrders() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String query = "SELECT  * FROM " + ORDERS_TABLE;
    	Cursor cursor = db.rawQuery(query, null);
        
    	List<Order> orders = new LinkedList<Order>();
    	
       	Order order = null;
        if (cursor.moveToFirst()) {
            do {
                order = new Order();
                order.setID(Integer.parseInt(cursor.getString(0)));
                order.setOrderNumber(cursor.getString(1));
                order.setOrderName(cursor.getString(2));
                order.setOrderDirectWork(cursor.getInt(3));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        return orders;
    }
    
    /**
     * PUT: Update an order
     * 
     * @param order
     * @return
     */
    public int putOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put("orderNumber", order.getOrderNumber()); 
        values.put("orderName", order.getOrderName());
        values.put(ORDERS_TABLE_KEY_DIRECTWORK, order.getOrderDirectWork());
     
        int i = db.update(ORDERS_TABLE, 
        					values, 
        					ORDERS_TABLE_KEY_ID+" = ?", 
        					new String[] { String.valueOf(order.getID()) }); 
        
        db.close();
     
        return i;
    }

    /**
     * DELETE: Remove an order
     * 
     * @param order
     */
    public void deleteOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(ORDERS_TABLE, 
        			ORDERS_TABLE_KEY_ID + " = ?", 
        			new String[] { String.valueOf(order.getID()) });

        db.close();
    }
    
    // *********************************************** //
    // BLOCKS
    // *********************************************** //
    
    // *********************************************** //
    // Define Blocks table
    // *********************************************** //
    private static final String BLOCKS_TABLE = "blocks"; // Name
    private static final String BLOCKS_TABLE_KEY_ID = "ID";
    private static final String BLOCKS_TABLE_KEY_START = "start";
    private static final String BLOCKS_TABLE_KEY_STOP = "stop";
    private static final String BLOCKS_TABLE_KEY_ORDERID = "orderID";
    private static final String BLOCKS_TABLE_KEY_COMMENT = "comment";
    private static final String BLOCKS_TABLE_KEY_CHECKED = "checked";
    private static final String[] BLOCKS_TABLE_COLUMNS = {BLOCKS_TABLE_KEY_ID, 
    														BLOCKS_TABLE_KEY_START, 
    														BLOCKS_TABLE_KEY_STOP, 
    														BLOCKS_TABLE_KEY_ORDERID,
    														BLOCKS_TABLE_KEY_COMMENT,
    														BLOCKS_TABLE_KEY_CHECKED};
   
    /**
     * INSERT: Create new block
     * @param order
     */
    public void addBlock(Block block){
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(BLOCKS_TABLE_KEY_START, block.getStart());  
        values.put(BLOCKS_TABLE_KEY_STOP, block.getStop());
        values.put(BLOCKS_TABLE_KEY_ORDERID, block.getOrderID());
        values.put(BLOCKS_TABLE_KEY_COMMENT, block.getComment());
        values.put(BLOCKS_TABLE_KEY_CHECKED, block.getChecked());
 
        block.setID((int)db.insert(BLOCKS_TABLE, null, values));
        db.close(); 
    }
 
    /**
     * GET: Return a order
     * 
     * @param ID
     * @return
     */
    public Block getBlock(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BLOCKS_TABLE, BLOCKS_TABLE_COLUMNS, 
        							" id = ?", 
        							new String[] { String.valueOf(ID) }, 
        							null, 
        							null, 
        							null, 
        							"1");
        
        if (cursor != null) {
        	// If there are any rows
        	if(!cursor.isAfterLast()) { // Return Order
        		cursor.moveToFirst();
        		
        		Block block = new Block();
                block.setID(Integer.parseInt(cursor.getString(0)));
                block.setStart(cursor.getLong(1));
                block.setStop(cursor.getLong(2));
                block.setOrderID(cursor.getInt(3));
                block.setComment(cursor.getString(4));
                block.setChecked(cursor.getInt(5));

	            return block;
        	}
        	else { // No rows
        		return null;
        	}
        } // No rows
        else {
        	return null;
        }
    }
    
    /**
     * GET: Return all blocks for a certain order
     * 
     * @return 
     */
    public List<Block> getBlocks(int orderID) {
    	List<Block> blocks = new LinkedList<Block>();
    	
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BLOCKS_TABLE, 
        							BLOCKS_TABLE_COLUMNS, 
        							" orderID = ?", 
        							new String[] { String.valueOf(orderID) }, 
        							null, 
        							null, 
        							null, 
        							null);
        
       	Block block = null;
        if (cursor.moveToFirst()) {
            do {
                block = new Block();
                block.setID(Integer.parseInt(cursor.getString(0)));
                block.setStart(cursor.getLong(1));
                block.setStop(cursor.getLong(2));
                block.setOrderID(cursor.getInt(3));
                block.setComment(cursor.getString(4));
                block.setChecked(cursor.getInt(5));
                blocks.add(block);
            } while (cursor.moveToNext());
        }
        return blocks;
    }
    
   
    
    /**
     * GET: Return all blocks
     * 
     * @return 
     */
    public List<Block> getAllBlocks() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String query = "SELECT  * FROM " + BLOCKS_TABLE;
    	Cursor cursor = db.rawQuery(query, null);
        
    	List<Block> blocks = new LinkedList<Block>();
    	
       	Block block = null;
        if (cursor.moveToFirst()) {
            do {
                block = new Block();
                block.setID(Integer.parseInt(cursor.getString(0)));
                block.setStart(cursor.getLong(1));
                block.setStop(cursor.getLong(2));
                block.setOrderID(cursor.getInt(3));
                block.setComment(cursor.getString(4));
                block.setChecked(cursor.getInt(5));
                blocks.add(block);
            } while (cursor.moveToNext());
        }
        return blocks;
    }
    

    /**
     * Returns getBlocksBetweenDate() without specifying orderID
     * 
     * @param start The start of the interval
     * @param stop The end of the interval
     * @return The list with the matching blocks.
     */
    public List<Block> getBlocksBetweenDate(long start, long stop) {
    	return this.getBlocksBetweenDate(0,  start, stop);
    }
    
    /**
     * Returns all blocks in a specific time interval.
     * 
     * @param orderID a specified orderID. orderID = 0 may be chosen to eliminate this constraint
     * @param start The start of the interval
     * @param stop The end of the interval
     * @return The list with the matching blocks.
     */
    public List<Block> getBlocksBetweenDate(int orderID, long start, long stop) {
    	// Will contain found blocks
    	List<Block> blocks = new LinkedList<Block>();
    	// Reference to db
    	SQLiteDatabase db = this.getReadableDatabase();
    	// Commits SQL
    	Cursor cursor;
    	// Get all blocks
    	if(orderID == 0) {
    		cursor = db.rawQuery("SELECT * FROM blocks "
    							+ "WHERE start > ? AND stop < ?",
    	  						new String[] {String.valueOf(start), String.valueOf(stop)});
    	} // Get blocks of a certain orderID
    	else {
    		cursor = db.rawQuery("SELECT * FROM blocks "
						+ "WHERE start > ? AND stop < ? AND orderID = ?",
					new String[] {String.valueOf(start), String.valueOf(stop), String.valueOf(orderID)});
    	}

        if (cursor.moveToFirst()) {
            do {
                Block block = new Block();
                block.setID(Integer.parseInt(cursor.getString(0)));
                block.setStart(cursor.getLong(1));
                block.setStop(cursor.getLong(2));
                block.setOrderID(cursor.getInt(3));
                block.setComment(cursor.getString(4));
                block.setChecked(cursor.getInt(5));
                blocks.add(block);
            } while (cursor.moveToNext());
        }
        return blocks;
    }
    
    /**
     * PUT: Update a block
     * 
     * @param order
     * @return
     */
    public int putBlock(Block block) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(BLOCKS_TABLE_KEY_START, block.getStart()); 
        values.put(BLOCKS_TABLE_KEY_STOP, block.getStop());
        values.put(BLOCKS_TABLE_KEY_ORDERID, block.getOrderID());
        values.put(BLOCKS_TABLE_KEY_COMMENT, block.getComment());
        values.put(BLOCKS_TABLE_KEY_CHECKED, block.getChecked());
     
        int i = db.update(BLOCKS_TABLE, 
        					values, 
        					BLOCKS_TABLE_KEY_ID+" = ?", 
        					new String[] { String.valueOf(block.getID()) }); 
        
        db.close();
     
        return i;
    }
    
    /**
     * DELETE: Remove a block
     * 
     * @param order
     */
    public void deleteBlock(Block block) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(BLOCKS_TABLE, 
        			BLOCKS_TABLE_KEY_ID + " = ?", 
        			new String[] { String.valueOf(block.getID()) });

        db.close();
    }
    
    /**
     * Return database data as JSON string
     * 
     * @return
     */
    public String toJSONString() {
    	return this.toJSONString(0, 0);
    }
    
    public String toJSONString(long start, long stop) {
    	// Get all orders
        List<Order> orders = new ArrayList<Order>();
        orders = this.getAllOrders();
        Log.w("timemanagement", orders.size() + "");
        // Create root node
        JSONObject userData = new JSONObject();
        // Create order node
        JSONArray orderData = new JSONArray();
        // Define orders node
        for(int i = 0; i < orders.size(); i++) {
        	// Create order node
        	JSONObject theOrder = new JSONObject(); 
        	// Get blocks for this order
        	try {
        		// Get blocks
        		List<Block> blocks = new ArrayList<Block>(); 
        		if(start == 0 && stop == 0) {
        			blocks = this.getBlocks(orders.get(i).getID());
        		}
        		else {
        			blocks = this.getBlocksBetweenDate(orders.get(i).getID(), start, stop);
        		}
        		// Will contain the block data
        		JSONArray blockData = new JSONArray();
        		// Create block nodes
        		for(int j = 0; j < blocks.size(); j++) {
        			JSONObject theBlock = new JSONObject();
        			theBlock.put("start", blocks.get(j).getStart());
        			theBlock.put("stop", blocks.get(j).getStop());
        			theBlock.put("comment", blocks.get(j).getComment());
        			blockData.put(theBlock);
        		}
        		// Define order node
        		theOrder.put("orderNumber", orders.get(i).getOrderNumber());
        		theOrder.put("orderName", orders.get(i).getOrderName());
        		theOrder.put("blocks", blockData);
        		// Add this order to order-node
        		orderData.put(theOrder);
        	} catch(JSONException e) {
            	Log.w("timemanagement", "SQLiteMethods.toJSONString: JSONException: " + e.toString());
            }
        }
        try {
        	userData.put("orders", orderData);
        } catch(JSONException e) {
        	Log.w("timemanagement", "JSONException: " + e.toString());
        }
        String output = "[" + userData.toString() + "]";
        return output;
    }
    
    /**
     * Export json string file
     * 
     * @return
     */
    public boolean exportJSON() {
    	return exportJSON(0,0);
    }
    
    public boolean exportJSON(long start, long stop) {
    	String json;
    	if(start == 0 && stop == 0) {
    		json = this.toJSONString();
    	}
    	else {
    		json = this.toJSONString(start, stop);
    	}
    	// Where to store the file?
    	String path = Environment.getExternalStorageDirectory().toString();
    	String folderName = "Chronox";
    	String fileName = "ChronoxExport.json";    	
    	// Find folder, or create if it does not exist
        File folder = new File(path + "/" + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // Find file, or create if it does not exist
        File file = new File(path + "/" + folderName + "/" + fileName);
        if(!file.exists()) {
        	try {
        		file.createNewFile();
        	} catch (Exception e) {
        		Log.w("timemanagement", "SQLiteMethods.exportJSON(): trying to create new file, Exception: " + e.toString());
        		return false;
        	}
        }
        // Write to file
        try {
	    	Log.w("timemanagement", "SQLiteMethods.exportJSON(): writing file...");
	        FileWriter fw = new FileWriter(file.toString(), false);
	        // ************************************************** //
	        fw.write(json);
	        // ************************************************** //
	        fw.close();
	        return true;
        } catch(Exception e) {
        	Log.w("timemanagement", "SQLiteMethods.exportJSON(): trying to write to file, Exception: " + e.toString());
        	return false;
        }
    }
}
