package com.example.timemanagement.sqlite;

import com.example.timemanagement.model.Order;
import com.example.timemanagement.model.Block;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.support.v4.util.LogWriter;
import android.util.Log;

public class SQLiteMethods extends SQLiteOpenHelper {
	
	// Database info
    private static final int DATABASE_VERSION = 7;
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
            "orderName TEXT)";
        db.execSQL(CREATE_ORDER_TABLE);
        
        // Create "blocks"-table
        String CREATE_BLOCKS_TABLE =
        	"CREATE TABLE blocks(" +
        	"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        	"start INTEGER, " +
        	"stop INTEGER, " +
        	"orderID INTEGER," +
        	"comment TEXT)";
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
    private static final String[] ORDERS_TABLE_COLUMNS = {ORDERS_TABLE_KEY_ID, 
    														ORDERS_TABLE_KEY_ORDERNUMBER, 
    														ORDERS_TABLE_KEY_ORDERNAME};
    
    /**
     * INSERT: Create new order 
     * @param order
     */
    public void addOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ORDERS_TABLE_KEY_ORDERNUMBER, order.getOrderNumber());  
        values.put(ORDERS_TABLE_KEY_ORDERNAME, order.getOrderName()); 
 
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
    private static final String[] BLOCKS_TABLE_COLUMNS = {BLOCKS_TABLE_KEY_ID, 
    														BLOCKS_TABLE_KEY_START, 
    														BLOCKS_TABLE_KEY_STOP, 
    														BLOCKS_TABLE_KEY_ORDERID,
    														BLOCKS_TABLE_KEY_COMMENT};
   
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
    public List<Block> getBlocksBetweenDate(long start, long stop) {
    	
    	  List<Block> blocks = new LinkedList<Block>();
    	
    	  SQLiteDatabase db = this.getReadableDatabase();
    	  
    	  Cursor cursor = db.rawQuery("SELECT * FROM blocks "
    	  							+ "WHERE start > ? AND stop < ?",
    			  new String[] {String.valueOf(start), String.valueOf(stop)});
    	  

         
          if (cursor.moveToFirst()) {
              do {
                  Block block = new Block();
                  block.setID(Integer.parseInt(cursor.getString(0)));
                  block.setStart(cursor.getLong(1));
                  block.setStop(cursor.getLong(2));
                  block.setOrderID(cursor.getInt(3));
                  block.setComment(cursor.getString(4));
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
}
