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
	

	/* --- insert own code here --- */

}
