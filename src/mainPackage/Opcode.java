package mainPackage;

import java.util.Hashtable;
//import java.util.Iterator;

public class Opcode {
	public static Hashtable<String,Integer> tbl = new Hashtable<String,Integer>();
	public void opcode()
	{
		
	tbl.put("add",24); 
	tbl.put("and",64);
	tbl.put("comp", 40);
	tbl.put("div", 36);
	tbl.put("j", 60);
	tbl.put("jeq", 48);
	tbl.put("jgt", 52);
	tbl.put("jlt", 56);
	tbl.put("jsub", 72);
	tbl.put("lda", 0);
	tbl.put("ldch", 80);
	tbl.put("ldl", 8);
	tbl.put("ldx", 4);
	tbl.put("mul", 32);
	tbl.put("or", 68);
	tbl.put("rd", 216);
	tbl.put("rsub", 76);
	tbl.put("sta", 12);
	tbl.put("stch", 84);
	tbl.put("stl", 20);
	tbl.put("stx", 16);
	tbl.put("sub", 28);
	tbl.put("td", 224);
	tbl.put("tix", 44);
	tbl.put("wd", 220);
	
	//for (String key : tbl.keySet()) {
		//System.out.println(key);
		
		
	//}
	}
	
}
