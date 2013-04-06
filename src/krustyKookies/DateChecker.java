package krustyKookies;

import javax.swing.JTextField;

public class DateChecker {
	

public boolean checkDate(JTextField input) throws Exception{
	String date = null;
	try{date = input.getText();} catch (NullPointerException e){
		return false;
	}
	if(date.length() == 0){
		return false;
	}
	
	if(date.length() != 10){
		exceptionThrower();
		return false;
	}
		try{
		Integer.parseInt(date.substring(0,4));
		Integer.parseInt(date.substring(5,7));
		Integer.parseInt(date.substring(8,10));
		} catch (NumberFormatException e){
		exceptionThrower();
		}
		if(date.charAt(4) == '-' && date.charAt(7) == '-'){
			return true;
	}
		exceptionThrower();
		return false;
	
}

private void exceptionThrower() throws Exception{
	throw new Exception("Wrong Date Format (YYYY-MM-DD)");
}
}
