package mainPackage;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainClass {

	public static void main(String[] args) throws ScriptException {
		//Opcode o = new Opcode();
		//o.opcode();
//				String text = "aa*bb-cc+dd/ee";
//
//				String [] result = text.split("[-+*/]");
//				String [] resultOp = text.split("(?<=[-+*/])|(?=[-+*/])");
//				for (int i = 0; i < result.length; i++) {
//					System.out.println(result[i]);
//				}
//				for (int i = 0; i < resultOp.length; i++) {
//					System.out.println("op  "+resultOp[i]);
//				}
				
//		if (text.contains("-") && text.matches(".*\\d+.*"))
//		{
//		    System.out.println("It contains the -");
//		}
//		else if (text.contains("+") && text.matches(".*\\d+.*"))
//		{
//			System.out.println("It contains the +");
//		}
//		else if (text.contains("*") && text.matches(".*\\d+.*"))
//		{
//			System.out.println("It contains the *");
//		}
//		else if (text.contains("/") && text.matches(".*\\d+.*"))
//		{
//			System.out.println("It contains the /");
//		}
//		else 
//	    {
//		    System.out.println("It doesn't contain the operations ");
//	    }
		
		Opcode o=new Opcode();
		o.opcode();
		Pass1 p = new Pass1();
		Pass2 p2= new Pass2();
		p.read();
//		for (int key : Pass1.blockAdd.values())
//			{
//				System.out.println("littabValue values  : "+key);
//				
//			}
//			for (String key : Pass1.blockAdd.keySet())
//			{
//				System.out.println("littabValue names : "+key);
//			}
		p2.read();
		
		//String s = "123456789";
		//String newString = s.substring(0, 4) + "o";
		//System.out.println(newString);
		
		//String t="      ";
		//String r=t.trim();
		//System.out.println(t);
		
		//System.out.println(o.tbl.size());
		 //String Str = new String("   Welcome to Tutorialspoint.com   ");

	      //System.out.print("Return Value :" );
	      //System.out.println(Str.trim() );
//for (String key : Pass1.littabValue.values())
//{
//	System.out.println("littabValue values  : "+key);
//	
//}
//for (String key : Pass1.littabValue.keySet())
//{
//	System.out.println("littabValue names : "+key);
////}
//		ScriptEngineManager mgr = new ScriptEngineManager();
//	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
//	    String foo = "-";
//	    double x =   (int) engine.eval(foo);
//	    System.out.println(x);
//	   System.out.println((int)Math.floor(x));


	}

}
