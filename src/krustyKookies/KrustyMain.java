package krustyKookies;

public class KrustyMain {

	
//main for the program. Initiates db and starts the GUI
	public static void main(String[] args) {
		Database db = new Database();
		new KrustyGUI(db);

	}

}
