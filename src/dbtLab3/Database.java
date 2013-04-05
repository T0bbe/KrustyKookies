package dbtLab3;

import java.util.ArrayList;
import java.sql.*;

/**
 * Database is a class that specifies the interface to the krusty database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
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
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
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
	
	//calculates how the amount of cookies should be spread,
	private ArrayList<Integer> calc(int cookies){
		ArrayList<Integer> lefts = new ArrayList<Integer>();
		int nbrOfBags = 0;
		int nbrOfBoxes = 0;
		int nbrOfPallets = 0;
		while(cookies >= 15){
			nbrOfBags++;
			cookies = cookies - 15;
		}
		while(nbrOfBags >= 10){
			nbrOfBoxes++;
			nbrOfBags = nbrOfBags - 10;
		}
		while(nbrOfBoxes >= 36){
			nbrOfPallets++;
			nbrOfBoxes = nbrOfBoxes - 36;
		}
		lefts.add(cookies);
		lefts.add(nbrOfBags);
		lefts.add(nbrOfBoxes);
		lefts.add(nbrOfPallets);
		return lefts;
	}
	
	public void buildPallet(String cookieName, String cookies) {
		int nbrCookies = Integer.parseInt(cookies);
		ArrayList<Integer> nbrOfLeft = calc(nbrCookies);
		while(nbrOfLeft.get(3) >= 1){
			String sql = "insert into Pallet(null, '2013-03-20', false, 'InProduction', cookieName)";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			nbrOfLeft.add(3, nbrOfLeft.get(3) -1); 
		}
		}
	
	public Integer getPallet(String palletNbr) {
		String sql = "select * from Pallets where palletNbr = ?";
		int temp = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(palletNbr));
			ResultSet rs = ps.executeQuery();
			temp = rs.getInt("PalletNbr");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	public ArrayList<String> getBatch(String[] input) {
		PreparedStatement ps;
		String sql;
		try {
		if(input[1] == null && input[2] == null){
			sql = "select * from Pallets where cookieName = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
		} else if(input[1] != null && input[2] == null){
			sql = "select * from Pallets where cookieName = ? and date <= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[1]);
		} else if(input[1] == null && input[2] != null){
			sql = "select * from Pallets where cookieName = ? and date >= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[2]);
		} else {
			sql = "select * from Pallets where cookieName = ? and date >= ? and date <= ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, input[0]);
			ps.setString(2, input[1]);
			ps.setString(3, input[2]);
		}
		
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = ps.executeQuery();
		for(int i = 0; i < rs.getFetchSize(); i++)
		result.add(((Integer) rs.getInt("PalletNbr")).toString());
			rs.next();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> showPallet(String palletNbr){
		String sql = "select * from Pallets where palletNbr = ?";
		ArrayList<String> temp = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(palletNbr));
			ResultSet rs = ps.executeQuery();
			temp.add(palletNbr);
			temp.add(rs.getString("cookieName"));
			temp.add(rs.getString("dateOfProduction"));
			temp.add(rs.getString("currentLocation"));
			if(rs.getBoolean("isBlocked")){
			temp.add("Yes");
			} else {
				temp.add("No");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	

}
