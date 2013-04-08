package krustyKookies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.*;

/**
 * Database is a class that specifies the interface to the krusty database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	
	private int palletsToRegister = 0;
	
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * An SQL statement object.
	 */
	private Statement stmt;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
		openConnection("db05", "dicksteele");
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ userName, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/** 
	 *  Retrieves all cookies
	 *  
	 * @return all cookie names
	 */
	public ArrayList<String> allCookies() {
		String sql = "select * from Recipes";
		ArrayList<String> cookies = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				cookies.add(rs.getString("cookieName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cookies;
	}
	
	public ArrayList<String> allProductionPallets() {
		String sql = "select * from Pallet where dateOfProduction is null";
		ArrayList<String> pallets = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				pallets.add(rs.getString("PalletNbr"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallets;
	}

	/** 
	 *  Calculate how an amount of cookies should be distributed
	 *  
	 *  @param int cookies; the amount/number of cookies the user has inputed in an int
	 * @return the arrayList of the spread
	 */
	private ArrayList<Integer> calc(int cookies) {
		ArrayList<Integer> lefts = new ArrayList<Integer>();
		int nbrOfBags = 0;
		int nbrOfBoxes = 0;
		int nbrOfPallets = 0;
		while (cookies >= 15) {
			nbrOfBags++;
			cookies = cookies - 15;
		}
		while (nbrOfBags >= 10) {
			nbrOfBoxes++;
			nbrOfBags = nbrOfBags - 10;
		}
		while (nbrOfBoxes >= 36) {
			nbrOfPallets++;
			nbrOfBoxes = nbrOfBoxes - 36;
		}
		lefts.add(0, cookies);
		lefts.add(1, nbrOfBags);
		lefts.add(2, nbrOfBoxes);
		lefts.add(3, nbrOfPallets);
		return lefts;
	}
	
	
	/** 
	 *  Inserts new pallets into the database 
	 *  
	 * @param String cookieName
	 * @param String cookies; the amount/number of cookies the user has inputed in a String 
	 * @return the arrayList of the spread, see above method
	 */
	public ArrayList<Integer> buildPallet(String cookieName, String cookies) {
		int nbrCookies = 0;
		try{
			nbrCookies = Integer.parseInt(cookies);
		}catch(NumberFormatException nuff){
			return null;
		}
		ArrayList<Integer> nbrOfLeft = calc(nbrCookies);
		int pallets = nbrOfLeft.get(3);
		palletsToRegister = pallets;
		while (pallets >= 1) {
			String sql = "insert into Pallet(palletNbr, dateOfProduction, isBlocked, currentLocation, cookieName) values(null, null, false, 'InProduction', ?)";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, cookieName);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pallets--;
		}
		return nbrOfLeft;
	}
	
	
	/** 
	 *  Updates built pallets which has yet not have
	 *  their barcode read at the deep freeze storage.
	 *  
	 */
	public void readPalletCode(int palletNbr) {
			String timeStamp = getCurrentTimeStamp();
			System.out.print(timeStamp);
			String sql = "update Pallet set currentLocation = 'DeepFreeze' where palletNbr = ?";
			String sql2 = "update Pallet set dateOfProduction = ? where palletNbr = ?";
			String sql3 = "update Pallet set timeOfProduction = ? where palletNbr = ?";
			try {
				PreparedStatement loc = conn.prepareStatement(sql);
				loc.setInt(1, palletNbr);
				PreparedStatement date = conn.prepareStatement(sql2);
				date.setString(1, new String(timeStamp.subSequence(0,4) + "-" + timeStamp.subSequence(4, 6) + "-" + timeStamp.subSequence(6, 8)));
				date.setInt(2, palletNbr);
				PreparedStatement time = conn.prepareStatement(sql3);
				time.setString(1, new String(timeStamp.subSequence(9, 11) + ":" + timeStamp.subSequence(11,13) + ":" + timeStamp.subSequence(13,15)));
				time.setInt(2, palletNbr);
				loc.executeUpdate();
				date.executeUpdate();
				time.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	/** 
	 *  Get the current time stamp from the running computer
	 *  
	 *  @return the current time stamp in the pre-defined format; example: "20130404_135711"
	 */
	private static String getCurrentTimeStamp() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String date = sdfDate.format(cal.getTime());
		return date;
	}

	public Integer getPallet(int palletNbr) {
		String sql = "select * from Pallets where palletNbr = ?";
		int temp = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, palletNbr);
			ResultSet rs = ps.executeQuery();
			temp = rs.getInt("PalletNbr");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public ArrayList<String> getBatch(String date) {
		String sql = "select * from Pallets where date = ?";
		ArrayList<String> temp = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ResultSet rs = ps.executeQuery();
			temp.add(((Integer) rs.getInt("PalletNbr")).toString());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

/*	public ArrayList<String> showPallet(Integer palletNbr) {
		String sql = "select * from Pallets where palletNbr = ?";
		ArrayList<String> temp = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, palletNbr);
			ResultSet rs = ps.executeQuery();
			temp.add(palletNbr.toString());
			temp.add(rs.getString("cookieName"));
			temp.add(rs.getString("dateOfProduction"));
			temp.add(rs.getString("timeOfProduction"));
			temp.add(rs.getString("currentLocation"));
			if (rs.getBoolean("isBlocked")) {
				temp.add("Yes");
			} else {
				temp.add("No");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	*/
	
	/** 
	 *  Calculates and updates the new amount value for
	 *  each ingredients for a cookieName
	 *  
	 *  @param String cookieName
	 */
	public void updateRaws(String cookieName) {
		ArrayList<String> materials = getIng(cookieName);
		for (int i = 0; i < materials.size(); i++) {
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			String materialType = materials.get(i);
			int amountInStock = 0;
			int amountNeeded = 0;
			String sql1 = "select amountNeeded from RecipesHasIngridients where materialType = ?";
			try {
				PreparedStatement ps = conn.prepareStatement(sql1);
				ps.setString(1, materialType);
				rs1 = ps.executeQuery();
				if(rs1.next()){
					amountNeeded = rs1.getInt("amountNeeded");
				};
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String sql2 = "select amountInStock from RawMaterials where materialType = ?";
			try {
				PreparedStatement ps = conn.prepareStatement(sql2);
				ps.setString(1, materialType);
				rs2 = ps.executeQuery();
				if(rs2.next()){
					amountInStock = rs2.getInt("amountInStock");
				};
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int howManyPallets = PalletsProduced(cookieName);
			int calc = amountInStock - howManyPallets * 54 * amountNeeded;
			String sql3 = "update RawMaterials set amountInStock = ? where materialType = ?";
			try {
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				ps3.setInt(1, calc);
				ps3.setString(2, materialType);
				ps3.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 *  Calculates the number of pallets
	 *  which have been moved to the deep
	 *  freeze storage and thus have had its
	 *  barcode read. They have been produced.
	 *  
	 *  @param String cookieName
	 *  @return the number of pallets
	 */
	private int PalletsProduced(String cookieName) {
		int howManyPallets = 0;
		String sql = "select count(*) as pallets from Pallet where cookieName = ? and currentLocation = 'DeepFreeze'";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				howManyPallets = rs.getInt("pallets");
			};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return howManyPallets;
	}
	
	/** 
	 * Stores every ingredients for a specific cookie
	 *  
	 *  @param String cookieName
	 *  @return an arrayList of all ingredients for a cookie
	 */
	private ArrayList<String> getIng(String cookieName) {
		String sqlIng = "select materialType from RecipesHasIngridients where cookieName = ?";
		ArrayList<String> materials = new ArrayList<String>();
		ResultSet rs = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sqlIng);
			ps.setString(1, cookieName);
			rs = ps.executeQuery();
			while (rs.next()) {
				materials.add(rs.getString("materialType"));
			}
			;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return materials;
	}
	
	/** 
	 *  Fetches the palletNbr of the last built pallet
	 * 
	 *  @return the palletNbr for the last built pallet
	 */
	private int getLastBuiltPallet(){
		int palletNbr = 0;
		String sql = "select palletNbr from Pallet order by palletNbr";
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.last();
			return rs.getInt("palletNbr");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return palletNbr;
	}
	
	public Integer getPallet(String palletNbr) throws Exception {
		String sql = "select * from Pallet where palletNbr = ?";
		int temp = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(palletNbr));
			ResultSet rs = ps.executeQuery();
			rs.next();
			temp = rs.getInt("palletNbr");
			
		} catch (SQLException e) {
			throw new Exception("No such PalletNbr");
		}
		return temp;
	}
	
	public ArrayList<String> getBatch(String[] input) {
		PreparedStatement ps;
		String sql;
		if(input[0] == "All"){
			input[0] = "%";
		}
		try {
		if(input[1] == null && input[2] == null){
			sql = "select * from Pallet where cookieName like ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
		} else if(input[1] != null && input[2] == null){
			sql = "select * from Pallet where cookieName like ? and dateOfProduction >= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[1]);
		} else if(input[1] == null && input[2] != null){
			sql = "select * from Pallet where cookieName like ? and dateOfProduction <= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[2]);
		} else {
			sql = "select * from Pallet where cookieName like ? and dateOfProduction >= ? and dateOfProduction <= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[1]);
			ps.setString(3, input[2]);
		}
		
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = ps.executeQuery();
		
		if(input[3] == "Yes"){
		while(rs.next()){
		if(rs.getBoolean("isBlocked") == true){
		result.add(((Integer) rs.getInt("PalletNbr")).toString());}
		}
		return result;
		}   else if(input[3] == "No"){
			while(rs.next()){
			if(rs.getBoolean("isBlocked") == false){
			result.add(((Integer) rs.getInt("PalletNbr")).toString());}
			}
			return result;
			} else {
				while(rs.next()){
					result.add(((Integer) rs.getInt("PalletNbr")).toString());}
			}
					return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> showPallet(String palletNbr) throws Exception{
		String sql = "select * from Pallet where palletNbr = ?";
		ArrayList<String> temp = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(palletNbr));
			ResultSet rs = ps.executeQuery();
			rs.next();
			temp.add(palletNbr);
			temp.add(rs.getString("cookieName"));
			temp.add(rs.getString("dateOfProduction"));
			temp.add(rs.getString("timeOfProduction"));
			temp.add(rs.getString("currentLocation"));
			if(rs.getBoolean("isBlocked")){
			temp.add("Yes");
			} else {
				temp.add("No");
			}
			
		} catch (Exception e) {
			throw new Exception("No Pallet Selected");
		}
		return temp;
	}
	
	public void blockPallet(String input) throws Exception{
		String sql = "select * from Pallet where palletNbr = ?";
		try{
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, Integer.valueOf(input));
		ResultSet rs = ps.executeQuery();
		sql = "update Pallet set isBlocked = ? where palletNbr = ?";
		ps = conn.prepareStatement(sql);
		ps.setInt(2, Integer.valueOf(input));
		rs.next();
		if(rs.getBoolean("isBlocked")){
			ps.setBoolean(1, false);
		} else {ps.setBoolean(1, true);}
		ps.executeUpdate();}
		catch(Exception e){
		throw new Exception("No Pallet Selected");
		}
	}
	

}
