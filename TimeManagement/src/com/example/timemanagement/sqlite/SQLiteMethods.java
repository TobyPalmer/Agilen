package com.example.timemanagement.sqlite;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

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
import com.example.timemanagement.model.Notification;
import com.example.timemanagement.model.Order;
import com.example.timemanagement.model.UserDetails;

public class SQLiteMethods extends SQLiteOpenHelper {
	
	// Database info

    private static final int DATABASE_VERSION = 22;
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
        

        //Create "userdetails"-table
        String CREATE_USER_DETAILS_TABLE = 
        	"CREATE TABLE userdetails(" +
        	"username VARCHAR(50) PRIMARY KEY, " +
        	"workday INTEGER)";
        db.execSQL(CREATE_USER_DETAILS_TABLE);

     // Create "notifications"-table
        String CREATE_NOTIFICATIONS_TABLE =
        	"CREATE TABLE notifications(" +
        	"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        	"time LONG, " +
        	"everyDay INTEGER, " +
        	"everyWeek INTEGER, " +
        	"everyMonth INTEGER, " +
        	"spareTime INTEGER)";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

    }
    
    // *********************************************** //
    // If a new database version is noticed
    // *********************************************** //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS blocks");
        db.execSQL("DROP TABLE IF EXISTS userdetails");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        this.onCreate(db);
    }
    
    // *********************************************** //
    // USERDETAILS
    // *********************************************** //
    
    // *********************************************** //
    // Define userdetails table
    // *********************************************** //
    
    private static final String USER_DETAILS_TABLE = "userdetails";
    private static final String USER_DETAILS_TABLE_KEY_USERNAME = "username";
    private static final String USER_DETAILS_TABLE_KEY_WORKDAY = "workday";
    private static final String[] USER_DETAILS_TABLE_COLUMNS = {USER_DETAILS_TABLE_KEY_USERNAME,
    																USER_DETAILS_TABLE_KEY_WORKDAY};
    
    /**
     * INSERT: Create new user
     * @param Userdetails
     */
    public void addUser(UserDetails userDetails){
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(USER_DETAILS_TABLE_KEY_USERNAME, userDetails.getUsername());  
        values.put(USER_DETAILS_TABLE_KEY_WORKDAY, userDetails.getWorkday());        
 
        db.insert(USER_DETAILS_TABLE, null, values);
        db.close(); 
    }
    
    /**
     * Get user details from db
     * @param username
     */
    
    public UserDetails getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_DETAILS_TABLE, USER_DETAILS_TABLE_COLUMNS, 
        							USER_DETAILS_TABLE_KEY_USERNAME + " = ?", 
        							new String[] { username }, 
        							null, 
        							null, 
        							null, 
        							"1");
        
        if (cursor != null) {
        	// If there are any rows
        	if(!cursor.isAfterLast()) { // Return Order
        		cursor.moveToFirst();
        		
        		UserDetails userDetails = new UserDetails(cursor.getString(0), Long.parseLong(cursor.getString(1)));
	            
	            return userDetails;
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
     * PUT: Update user details
     * 
     * @param order
     * @return
     */
    public int putUser(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(USER_DETAILS_TABLE_KEY_USERNAME, userDetails.getUsername()); 
        values.put(USER_DETAILS_TABLE_KEY_WORKDAY, userDetails.getWorkday());       
     
        int i = db.update(USER_DETAILS_TABLE, 
        					values, 
        					USER_DETAILS_TABLE_KEY_USERNAME + " = ?", 
        					new String[] { userDetails.getUsername() }); 
        
        db.close();     
        return i;
    }
    
    /**
     * DELETE: Remove an order
     * 
     * @param order
     */
    public void deleteUser(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(USER_DETAILS_TABLE, 
        			USER_DETAILS_TABLE_KEY_USERNAME + " = ?", 
        			new String[] { userDetails.getUsername() });

        db.close();
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
    	
  	  	// Removing milliseconds.
    	start = start - (start % 1000);
  	  	stop = stop - (stop % 1000);
    	
    	// Get all blocks
    	if(orderID == 0) {
    		cursor = db.rawQuery("SELECT * FROM blocks "
    							+ "WHERE start >= ? AND stop <= ? ORDER BY start",
    	  						new String[] {String.valueOf(start), String.valueOf(stop)});
    	} // Get blocks of a certain orderID
    	else {
    		cursor = db.rawQuery("SELECT * FROM blocks "
						+ "WHERE start >= ? AND stop <= ? AND orderID = ? ORDER BY start",
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
    
    
    // *********************************************** //
    // NOTIFICATIONS
    // *********************************************** //
    
    // *********************************************** //
    // Define Notification table
    // *********************************************** //
    private static final String NOTIFICATIONS_TABLE = "notifications"; // Name
    private static final String NOTIFICATIONS_TABLE_KEY_ID = "ID";
    private static final String NOTIFICATIONS_TABLE_KEY_TIME = "time";
    private static final String NOTIFICATIONS_TABLE_KEY_EVERYDAY = "everyDay";
    private static final String NOTIFICATIONS_TABLE_KEY_EVERYWEEK = "everyWeek";
    private static final String NOTIFICATIONS_TABLE_KEY_EVERYMONTH = "everyMonth";
    private static final String NOTIFICATIONS_TABLE_KEY_SPARETIME = "spareTime";
    private static final String[] NOTIFICATIONS_TABLE_COLUMNS = {NOTIFICATIONS_TABLE_KEY_ID, 
														    	NOTIFICATIONS_TABLE_KEY_TIME, 
														    	NOTIFICATIONS_TABLE_KEY_EVERYDAY, 
														    	NOTIFICATIONS_TABLE_KEY_EVERYWEEK,
														    	NOTIFICATIONS_TABLE_KEY_EVERYMONTH,
														    	NOTIFICATIONS_TABLE_KEY_SPARETIME};
    
    
    /**
     * INSERT: Create new notification
     * @param notification
     */
    public void addNotification(Notification notification){
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        int d,w,m,s;
        d = (notification.everyDay())?1:0;
        w = (notification.everyWeek())?1:0;
        m = (notification.everyMonth())?1:0;
        s = (notification.spareTime())?1:0;
        
        values.put(NOTIFICATIONS_TABLE_KEY_TIME, notification.getTime());  
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYDAY, d); 
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYWEEK, w);
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYMONTH, m);
        values.put(NOTIFICATIONS_TABLE_KEY_SPARETIME, s);
 
        notification.setID((int)db.insert(NOTIFICATIONS_TABLE, null, values));
        
        db.close(); 
    }
    
    /**
     * GET: Return a notification
     * 
     * @param ID
     * @return
     */
    public Notification getNotification(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOTIFICATIONS_TABLE, NOTIFICATIONS_TABLE_COLUMNS, 
        							" id = ?", 
        							new String[] { String.valueOf(ID) }, 
        							null, 
        							null, 
        							null, 
        							"1");
        
        if (cursor != null) {
        	// If there are any rows
        	if(!cursor.isAfterLast()) { // Return Notification
        		cursor.moveToFirst();
        		
                boolean d,w,m,s;
                d = (cursor.getInt(2)==1)?true:false;
                w = (cursor.getInt(3)==1)?true:false;
                m = (cursor.getInt(4)==1)?true:false;
                s = (cursor.getInt(5)==1)?true:false;
                
            	Notification notification = new Notification();
        		notification.setID(cursor.getInt(0));
        		notification.setTime(cursor.getLong(1));
        		notification.setEveryDay(d);
        		notification.setEveryWeek(w);
        		notification.setEveryMonth(m);
        		notification.setSpareTime(s);
        		
	            return notification;
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
     * GET: Return all notifications
     * 
     * @return 
     */
	public List<Notification> getAllNotifications() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String query = "SELECT  * FROM " + NOTIFICATIONS_TABLE;
    	Cursor cursor = db.rawQuery(query, null);
        
    	List<Notification> notifications = new LinkedList<Notification>();
    	
        if (cursor.moveToFirst()) {
            do {
            	
                boolean d,w,m,s;
                d = (cursor.getInt(2)==1)?true:false;
                w = (cursor.getInt(3)==1)?true:false;
                m = (cursor.getInt(4)==1)?true:false;
                s = (cursor.getInt(5)==1)?true:false;
                
            	Notification notification = new Notification();
        		notification.setID(cursor.getInt(0));
        		notification.setTime(cursor.getLong(1));
        		notification.setEveryDay(d);
        		notification.setEveryWeek(w);
        		notification.setEveryMonth(m);
        		notification.setSpareTime(s);
        		notifications.add(notification);

            } while (cursor.moveToNext());
        }
        
        return notifications;
    }
    
    
    /**
     * PUT: Update a notification
     * 
     * @param notification
     * @return
     */
    public int putNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int d,w,m,s;
        d = (notification.everyDay())?1:0;
        w = (notification.everyWeek())?1:0;
        m = (notification.everyMonth())?1:0;
        s = (notification.spareTime())?1:0;
     
        ContentValues values = new ContentValues();
        values.put(NOTIFICATIONS_TABLE_KEY_TIME, notification.getTime()); 
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYDAY, d);
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYWEEK, w);
        values.put(NOTIFICATIONS_TABLE_KEY_EVERYMONTH, m);
        values.put(NOTIFICATIONS_TABLE_KEY_SPARETIME, s);
     
        int i = db.update(NOTIFICATIONS_TABLE, 
        					values, 
        					NOTIFICATIONS_TABLE_KEY_ID+" = ?", 
        					new String[] { String.valueOf(notification.getID()) }); 
        
        db.close();
     
        return i;
    }
    
    /**
     * DELETE: Remove a notification
     * 
     * @param notification
     */
    public void deleteNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(NOTIFICATIONS_TABLE, 
        			NOTIFICATIONS_TABLE_KEY_ID + " = ?", 
        			new String[] { String.valueOf(notification.getID()) });

        db.close();
    }
    
    
    
    // *********************************************** //
    // EXPORT METHODS
    // *********************************************** //

    /**
     * Return all data as JSON string
     * 
     */
    public String toJSONString() {
    	return this.toJSONString(0, 0);
    }
    
    /**
     * Return data subset as JSON string
     * 
     */
    public String toJSONString(long start, long stop) {
    	// Get all orders
        List<Order> orders = new ArrayList<Order>();
        orders = this.getAllOrders();
        // Create root node
        JSONObject userData = new JSONObject();
        // Create order node
        JSONArray orderData = new JSONArray();
        // Define orders node
        for(int i = -1; i < orders.size(); i++) { // Go from -1 in order to get blocks without orderNumber
        	// Create order node
        	JSONObject theOrder = new JSONObject();
        	// Define order
        	int orderID = 0, directWork;
    		String orderNumber, orderName;
    		// If "no" order
        	if(i == -1) {
        		orderNumber = "0";
        		orderName = "none";
        		directWork = 0;
        	}
        	else { // Get order data
        		orderID = orders.get(i).getID();
        		orderNumber = orders.get(i).getOrderNumber();
        		orderName = orders.get(i).getOrderName();
        		directWork = orders.get(i).getOrderDirectWork();
        	}
        	// Get blocks for this order
        	try {
        		// Get blocks
        		List<Block> blocks = new ArrayList<Block>(); 
        		if(start == 0 && stop == 0) {
        			blocks = this.getBlocks(orderID);
        		}
        		else {
        			blocks = this.getBlocksBetweenDate(orderID, start, stop);
        		}
        		// Will contain the block data
        		JSONArray blockData = new JSONArray();
        		// Create block nodes
        		for(int j = 0; j < blocks.size(); j++) {
        			JSONObject theBlock = new JSONObject();
        			theBlock.put("start", blocks.get(j).getStart());
        			theBlock.put("stop", blocks.get(j).getStop());
        			theBlock.put("comment", blocks.get(j).getComment());
        			theBlock.put("checked", blocks.get(j).getChecked());
        			blockData.put(theBlock);
        		}
        		// Define order node
        		theOrder.put("orderNumber", orderNumber);
        		theOrder.put("orderName", orderName);
        		theOrder.put("directWork", directWork);
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
     * Return all data as JSON string
     * 
     */
    public String toCSVString() {
    	return this.toCSVString(0, 0);
    }
    
    /**
     * Return data subset as JSON string
     * 
     */
    public String toCSVString(long start, long stop) {
    	// Contains CSV output...
    	String output = "Ordernummer,Start,Slut,Kommentar,Avcheckad\n";
    	// Define date format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z"); // the format of your date
		sdf.setTimeZone(TimeZone.getDefault());
    	// Get all blocks
        List<Block> blocks;
        if(start == 0 && stop == 0){
        	blocks = this.getAllBlocks();
        }
        else {
        	blocks = this.getBlocksBetweenDate(start, stop);
        }
        // For all blocks
        for(int i = 0; i < blocks.size(); i++) {
        	Order order = this.getOrder(blocks.get(i).getOrderID());
        	String orderNumber;
        	if(order == null) {
        		orderNumber = "0";
        	}
        	else {
        		orderNumber = order.getOrderNumber();
        	}
			output += orderNumber + ",";
			output += "\"" + sdf.format(new Date(blocks.get(i).getStart())) + "\",";
			output += sdf.format(new Date(blocks.get(i).getStop())) + ",";
			output += "\"" + blocks.get(i).getComment() + "\",";
			output += blocks.get(i).getChecked() + ",";
			output += "\n";
        }
        return output;
    }
    
    /**
     * Export all data as JSON file
     * 
     * @return
     */
    public String exportJSON() {
    	return exportJSON(0,0);
    }
    
    /**
     * Export data subset as JSON file
     * 
     * @return
     */
    public String exportJSON(long start, long stop) {
    	// For formatting dates
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		sdf.setTimeZone(TimeZone.getDefault());
    	// Where to store the file?
    	String path = Environment.getExternalStorageDirectory().toString();
    	String folderName = "Chronox";
    	String fileName = "";
    	fileName = "Backup (sparad " + sdf.format(new Date(System.currentTimeMillis())) + ").json";	
    	// Return text
    	String success = "Exporterade backup-data till telefonminne: " + folderName + "/" + fileName + "";
    	String error = "Misslyckades";

    	String json;
    	// Time constraint?
    	if(start == 0 && stop == 0) {
    		json = this.toJSONString();
    	}
    	else {
    		json = this.toJSONString(start, stop);
    	}
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
        		return error;
        	}
        }
        try { // Write to file
	    	Log.w("timemanagement", "SQLiteMethods.exportJSON(): Writing file...");
	        FileWriter fw = new FileWriter(file.toString(), false);
	        // ************************************************** //
	        fw.write(json);
	        // ************************************************** //
	        fw.close();
	        return success;
        } catch(Exception e) {
        	Log.w("timemanagement", "SQLiteMethods.exportJSON(): Trying to write to file, Exception: " + e.toString());
        	return error;
        }
    }
    
    /**
     * Export all data as CSV file
     * 
     * @return
     */
    public String exportCSV() {
    	return exportCSV(0,0);
    }
    
    /**
     * Export data subset as CSV file
     * 
     * @return
     */
    public String exportCSV(long start, long stop) {
    	// For formatting dates
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getDefault());
    	// Where to store the file?
    	String path = Environment.getExternalStorageDirectory().toString();
    	String fileName = "";
    	String folderName = "Chronox";
    	if(start == 0 && stop == 0) {
    		fileName = "Tidrapport (sparad " + sdf.format(new Date(System.currentTimeMillis())) + ").csv";
    	}
    	else {
    		fileName = "Tidrapport (" + sdf.format(new Date(start)) + " till " + sdf.format(new Date(stop)) + ").csv";
    	}
    	// Return text
    	String success = "Exporterade tidrapport till telefonminne: " + folderName + "/" + fileName + "";
    	String error = "Misslyckades";
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
        		Log.w("timemanagement", "SQLiteMethods.exportCSV(): trying to create new file, Exception: " + e.toString());
        		return error;
        	}
        }
        try { // Write to file
	    	Log.w("timemanagement", "SQLiteMethods.exportCSV(): Writing file... " + folderName + "/" + fileName + "");
	        FileWriter fw = new FileWriter(file.toString(), false);
	        // ************************************************** //
	        String line = "";
	        if(start == 0 && stop == 0) {
	        	line = this.toCSVString();
	        }
	        else {
	        	line = this.toCSVString(start, stop);
	        }
    		fw.write(line);
	        // ************************************************** //
	        fw.close();
	        return success;
        } catch(Exception e) {
        	Log.w("timemanagement", "SQLiteMethods.exportCSV(): Trying to write to file, Exception: " + e.toString());
        	return error;
        }
    }
}
