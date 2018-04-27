package mainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Pass1 {

	static String s;
	static int StartingAddress = 0;
	static int flag = 0, ProgramLength = 0;
	static Hashtable<String, Integer> symtab = new Hashtable<String, Integer>();
	static Hashtable<String, String> littabValue = new Hashtable<String, String>();
	static Hashtable<String, Integer> littabAddress = new Hashtable<String, Integer>();
	static Hashtable<String, Integer> littabAsterisk = new Hashtable<String, Integer>();
	static Hashtable<String ,Integer> expressionTab = new Hashtable <String , Integer>();
	static int generalCount = 0;
	static ArrayList<String> general = new ArrayList<String>();
	static boolean flagltorg = false;
	static boolean flagOrg = false;
	static String OpAddress = "";
	static String blockName ="default";
	static int counterno = 0;
	static LinkedHashMap<String, Integer> blockloc = new LinkedHashMap<String, Integer>();
	static Hashtable<String, Integer> blockno = new Hashtable<String, Integer>();
	static LinkedHashMap<Integer, String> blockno1 = new LinkedHashMap<Integer, String>();
	static Hashtable<String, Integer> blockAdd = new Hashtable<String, Integer>();
	FileInputStream in;
	InputStreamReader reader;
	BufferedReader br;

	FileOutputStream out = null;
	OutputStreamWriter osw = null;
	BufferedWriter bw = null;
	static boolean flaguse;

	public void read() {

		try {
			in = new FileInputStream("/Users/karim/Desktop/System.txt");
			reader = new InputStreamReader(in);
			br = new BufferedReader(reader);
			iterate();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				reader.close();
				in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public void iterate() {
		blockno.put("default", 0);
		blockno1.put(0,"default");
		s = "";
		String label = null;
		String opcode = null;
		String operand = null;
		String comment = null;
		int locctr = 0;
		int locctrOrg = 0;

		// Opcode o = new Opcode();
		try {
			out = new FileOutputStream("/Users/karim/Desktop/SystemOut.txt");
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			loop1:
			while ((s = br.readLine()) != null) {

				if (s.equals("")) {
					bw.write("$$$$ Line With no Instruction $$$$");
					bw.newLine();
					continue;
				}

				else if (s.substring(0, 1).equals(".")) {
					bw.write(s);
					bw.newLine();
					continue;

				} else if (s.length() < 17) {
					label = s.substring(0, 8).trim();
					opcode = s.substring(9, s.length()).trim();
					
				
					if (!opcode.equalsIgnoreCase("rsub")&& !opcode.equalsIgnoreCase("ltorg") && (opcode.equalsIgnoreCase("org")||opcode.equalsIgnoreCase("use")))
					{
						if (opcode.equalsIgnoreCase("org")||opcode.equalsIgnoreCase("use")) 
						{
							operand = "";
						} 
						else 
						{
							bw.write("$$$$ Unavailable operand $$$$");
							bw.newLine();
						}

					}
				} else if (s.length() <= 35) {
					label = s.substring(0, 8).trim();
					opcode = s.substring(9, 15).trim();
					operand = s.substring(17, s.length()).trim();
				} else if (s.length() > 35) {
					label = s.substring(0, 8).trim();
					opcode = s.substring(9, 15).trim();
					operand = s.substring(17, 35).trim();
					comment = s.substring(35, s.length()).trim();
				}

				if (opcode.equalsIgnoreCase("start")) {
					locctr = Integer.parseInt(operand, 16);
					StartingAddress = Integer.parseInt(operand, 16);
					//System.out.println(blockName);
					blockAdd.put(blockName, StartingAddress);
					blockno.put(blockName, counterno);
					blockno1.put(counterno, blockName);
					
					symtab.put(label, locctr);
					
					bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
					bw.newLine();
					

				} else if ((opcode.equalsIgnoreCase("end"))) {
					// literal
					bw.write(s + "                                " + locctr);
					bw.newLine();
					if (!general.isEmpty() || flagltorg == false) {

						for (int i = 0; i < general.size(); i++) {
							// System.out.println("Ltorg false");
							String temp = "";
							temp = general.get(i);

							if (!temp.substring(0, 1).equals("*")
									&& (temp.substring(1, 2).equals("c")
											|| temp.substring(1, 2).equals("x")
											|| temp.substring(1, 2).equals("0")
											|| temp.substring(1, 2).equals("1")
											|| temp.substring(1, 2).equals("2")
											|| temp.substring(1, 2).equals("3")
											|| temp.substring(1, 2).equals("4")
											|| temp.substring(1, 2).equals("5")
											|| temp.substring(1, 2).equals("6")
											|| temp.substring(1, 2).equals("7")
											|| temp.substring(1, 2).equals("8") 
											|| temp.substring(1, 2).equals("9"))) {

								// System.out.println("End C or X or Constant");
								littabAddress.put(temp, locctr);
								bw.write("*        " + temp
										+ "                                "
										+blockno.get(blockName)+ " "+locctr);
								bw.newLine();
								blockloc.put(blockName, locctr);
								if (temp.substring(1, 2).equals("c")) {
									int z = temp.length() - 4;
									locctr = locctr + z;
								} else if (temp.substring(1, 2).equals("x")) {
									int z = temp.length() - 4;
									locctr = locctr + (z / 2);
								} else {
									locctr += 3;
								}
							} else if (temp.substring(0, 1).equals("*")) {

								// System.out.println("End *");

								bw.write("*        " + "*"
										+ "                                "
										+blockno.get(blockName)+ " "+locctr);
								bw.newLine();
								
								locctr += 3;
							}

						}
						// littabAddress.clear();
						littabAsterisk.clear();
						// littabValue.clear();
						general.clear();
						// generalCount = 0;
					}
					
					
					//blockloc.put(blockName, locctr);
					  for(int s:blockno1.keySet())
                    	System.out.println("%%%%"+s);
                    for(String x2:blockno1.values())
                    	System.out.println("####"+x2);
                  int counter=  blockloc.size();
                 
                    for(String s:blockloc.keySet())
                    {  
                    	int x = blockno.get(s);
                    	
                    	if (x==0)
                    	{
                    		continue;
                    	}
                    	else if(x==1)
                    	{
                    		System.out.println("x= "+x);
                    	//System.out.println(blockno1.get(1));
                    	blockAdd.put(s, blockloc.get(blockno1.get(x-1))+blockAdd.get(blockno1.get(x-1)));
                    	}
                    	else if(x==2)
                    	{
                    		System.out.println("x= "+x);
                    	//System.out.println(blockno1.get(1));
                    	blockAdd.put(s, blockloc.get(blockno1.get(x-1))+blockAdd.get(blockno1.get(x-1)));
                    	}
                    }
                  
                    
                    
					System.out.println("END Pass 1");
					ProgramLength = locctr - StartingAddress;
					break;
				} else if (!(s.substring(0, 1).equals("."))) {
					if (symtab.containsKey(label) && !label.equals("")) {
						System.out.println("ERROR DUPLICATE");
						bw.write("$$$$ ERROR LABEL DUPLICATION $$$$");
						bw.newLine();
						continue;
					} else if (!label.equals("") && !opcode.equalsIgnoreCase("equ")) {
						
						//System.out.println("label : "+label);
						//System.out.println("block no : "+ blockno.get(blockName));
						
						symtab.put(label, locctr);
						

					}

					else if (Opcode.tbl.containsKey(opcode)
							&& !opcode.equals("rsub") && operand.equals("")
							&& !opcode.equalsIgnoreCase("ltorg"))
						continue;

					if (Opcode.tbl.containsKey(opcode)) {
//						if (flagOrg)// org
//						{
//							bw.write(s + "                                "
//									+ locctrOrg);
//							bw.newLine();
//							locctrOrg += 3;
//						} else {
//							bw.write(s + "                                "
//									+ locctr);
//							bw.newLine();
//							locctr += 3;
//						}
						// Literals pass 1
						// System.out.println(operand);
						if (!operand.equals("") && operand.substring(0, 1).equals("=")) {
							flagltorg = false;
							String value = "";
							if (operand.substring(1, 2).equals("c") || operand.substring(1, 2).equals("x")) {
								if (operand.substring(1, 2).equals("c")) {
									String sub;
									sub = operand.substring(3,
											operand.length() - 1);
									String hex = "";

									for (int i = 0; i < sub.length(); i++) {
										hex = hex
												+ Integer.toHexString(sub
														.charAt(i));
									}
									value = hex;
								} else if (operand.substring(1, 2).equals("x")) {
									value = operand.substring(3,
											operand.length() - 1);
									value = Integer.toHexString(Integer
											.parseInt(value, 16));
								}
								if (!littabValue.containsValue(value)) {
									littabValue.put(operand, value);
									littabAddress.put(operand, 0);
									general.add(generalCount, operand);
									// System.out.println("arraylist components : "+general.get(generalCount));
									generalCount++;
								} else
									continue;
							} else if (operand.substring(1, 2).equals("*")) {
								littabAsterisk.put(
										"*" + Integer.toString(locctr - 3),
										locctr - 3);
								general.add(generalCount,
										"*" + Integer.toString(locctr - 3));
								// System.out.println("arraylist components : "+general.get(generalCount));
								generalCount++;
							} else {
								value = operand.substring(1, operand.length());
								if (!littabValue.containsValue(value)) {
									littabValue.put(operand, value);
									littabAddress.put(operand, 0);
									general.add(generalCount, operand);
									// System.out.println("arraylist components : "+general.get(generalCount));
									generalCount++;
								} else
									continue;
							}
						}
						// else if operand has absolute constants
						else if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))) {
							if(operand.matches(".*\\d+.*"))
							{
							int x = 0;
							//System.out.println(operand);
							ScriptEngineManager mgr = new ScriptEngineManager();
						    ScriptEngine engine = mgr.getEngineByName("JavaScript");
						    String foo = operand;
						    if(engine.eval(foo) instanceof Integer){
						    int y = (int) engine.eval(foo);
						    //System.out.println(y);
						     x = (int) Math.floor(y);
						     }
						    else if(engine.eval(foo) instanceof Double){
						    	Double y=(Double)engine.eval(foo);
						    	 //System.out.println(y);
							     x = (int) Math.floor(y);
						    }
						   
								if (x < 0) {
									System.out.println(x);
									System.out.println("invaild Addressing");
									bw.write("$$$$ Invalid Addressing $$$$");
									bw.newLine();
									continue;
								}
								
							expressionTab.put(operand, (int) x);
						}	
							else if(!operand.contains("*")&&!operand.contains("/"))
							{
								List<String> list = new ArrayList<String>();
								  //System.out.println("taa"+  operand);
								  int minus=0;
								  int plus=0;
								    String R="";
									String [] result = operand.split("[-+]");
									String [] resultop= operand.split("(?<=[-+])|(?=[-+])");
									for(String s : result) {
									       if(s != null && s.length() > 0) {
									          list.add(s);
									       }
									    }

									    result = list.toArray(new String[list.size()]);
									    list.clear();
									    for(String s : resultop) {
										       if(s != null && s.length() > 0) {
										          list.add(s);
										       }
										    }
										    resultop = list.toArray(new String[list.size()]);

										    
										    for (int i = 0; i < resultop.length; i++) {
										    	//System.out
														//.println("resulttop:"+resultop[i]);
												
											}
										    for (int i = 0; i < result.length; i++) {
										    	//System.out
														//.println("result:"+result[i]);
												
											}
									if(!resultop[0].substring(0, 1).equals("-"))
										plus++;
								for (int i = 0; i < resultop.length; i++) {
									
									
									if(resultop[i].equals("+"))
										plus++;
									
									if(resultop[i].equals("-"))
										minus++;
										
									
								}
								
								
									
									if(result.length%2!=0)
									{
										//System.out.println("Fdfsds");
										bw.write("$$$ Invalid Relative Addressing $$$");
										bw.newLine();
										continue;
									}
									else if(minus!=plus)
									{
										
										//System.out.println("33333333");
										bw.write("$$$ Invalid Relative Addressing $$$");
										bw.newLine();
										continue;
									}

							}
						}
						if (flagOrg)// org
						{
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
							bw.newLine();
							locctrOrg += 3;
						} else {
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
							bw.newLine();
							locctr += 3;
						}
					}
					else if (opcode.equalsIgnoreCase("use"))
					{ 
						if(!operand.equals(""))
						{//System.out.println("suck");
							if(!blockloc.containsKey(operand))
							{//System.out.println("sss"+blockName);
							System.out.println("sss"+locctr);
								blockloc.put(blockName, locctr);
								blockName=operand;
								counterno++;
								blockno.put(blockName, counterno);
								blockno1.put(counterno, blockName);
								
								flaguse=true;
								if (flagOrg) // org
								{System.out.println("FFF");
									locctrOrg = 0;
									String locctrEqu ="";
									locctrEqu = Integer.toHexString(locctrOrg);
									int y = 4 - locctrEqu .length();
									for (int i = 1; i <= y; i++) {
										locctrEqu = "0" + locctrEqu;
									}
									bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
									bw.newLine();
							} 
								else {
									
									locctr = 0;
									String locctrEqu ="";
									locctrEqu = Integer.toHexString(locctr);
									int y = 4 - locctrEqu .length();
									for (int i = 1; i <= y; i++) {
										locctrEqu = "0" + locctrEqu;
									}
									bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
									bw.newLine();
								}
							}
							else
							{ 
								blockloc.put(blockName, locctr);
								locctr = blockloc.get(operand);
								blockName=operand;
								
								if (flagOrg) // org
								{
									locctrOrg = locctr;
									String locctrEqu ="";
									locctrEqu = Integer.toHexString(locctrOrg);
									int y = 4 - locctrEqu .length();
									for (int i = 1; i <= y; i++) {
										locctrEqu = "0" + locctrEqu;
									}
									bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
									bw.newLine();
								} 
								else {
									String locctrEqu ="";
									locctrEqu = Integer.toHexString(locctr);
									int y = 4 - locctrEqu .length();
									for (int i = 1; i <= y; i++) {
										locctrEqu = "0" + locctrEqu;
									}
									bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
									bw.newLine();
								}
								
							}
						}
						else if(operand.equals(""))
						{//blockloc.put("default", locctr);
							//System.out.println("locloc "+locctr);
							blockloc.put(blockName, locctr);
							locctr=blockloc.get("default");
							blockName="default";
							if (flagOrg) // org
							{
								locctrOrg = locctr;
								String locctrEqu ="";
								locctrEqu = Integer.toHexString(locctrOrg);
								int y = 4 - locctrEqu .length();
								for (int i = 1; i <= y; i++) {
									locctrEqu = "0" + locctrEqu;
								}
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
								bw.newLine();
							} 
							else {
								
								String locctrEqu ="";
								locctrEqu = Integer.toHexString(locctr);
								int y = 4 - locctrEqu .length();
								for (int i = 1; i <= y; i++) {
									locctrEqu = "0" + locctrEqu;
								}
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrEqu);
								bw.newLine();
							}
						}
						
					}

					// LTORG in literal
					else if (opcode.equalsIgnoreCase("ltorg")) {
						if (flagOrg) {
							flagltorg = true;
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
							bw.newLine();
							for (int i = 0; i < general.size(); i++) {
								String temp = "";
								temp = general.get(i);
								String regex = "\\d+";
								if (!temp.substring(0, 1).equals("*")&& (temp.substring(1, 2).equals("c")|| temp.substring(1, 2).equals("x")
										|| temp.substring(1).matches(regex))) {

									// System.out.println("Ltorg C or X or Constant");
									littabAddress.put(temp, locctrOrg);
									bw.write("*        "+ temp+ "                                "+blockno.get(blockName)+ " "+locctrOrg);
									bw.newLine();
									if (temp.substring(1, 2).equals("c")) {
										int z = temp.length() - 4;
										locctrOrg = locctrOrg + z;
									} else if (temp.substring(1, 2).equals("x")) {
										int z = temp.length() - 4;
										locctrOrg = locctrOrg + (z / 2);
									} else {
										locctrOrg += 3;
									}
								
								} else if (temp.substring(0, 1).equals("*")) {
									// System.out.println("Ltorg *");
									bw.write("*        "+ "*"+ "                                "+blockno.get(blockName)+" "+ locctrOrg);
									bw.newLine();
									locctrOrg += 3;
								}

							}
							// littabAddress.clear();
							littabAsterisk.clear();
							// littabValue.clear();
							general.clear();
							generalCount = 0;
						} else {
							flagltorg = true;
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
							bw.newLine();
							for (int i = 0; i < general.size(); i++) {
								String temp = "";
								temp = general.get(i);
								String regex = "\\d+";
								if (!temp.substring(0, 1).equals("*")&& (temp.substring(1, 2).equals("c")|| temp.substring(1, 2).equals("x") 
										|| temp.substring(1).matches(regex))) {

									// System.out.println("Ltorg C or X or Constant");
									littabAddress.put(temp, locctr);
									bw.write("*        "+ temp+ "                                "+blockno.get(blockName)+" "+ locctr);
									bw.newLine();
									if (temp.substring(1, 2).equals("c")) {
										int z = temp.length() - 4;
										locctr = locctr + z;
									} else if (temp.substring(1, 2).equals("x")) {
										int z = temp.length() - 4;
										locctr = locctr + (z / 2);
									} else {
										locctr += 3;
									}
								} else if (temp.substring(0, 1).equals("*")) {
									// System.out.println("Ltorg *");
									bw.write("*        "+ "*"+ "                                "+blockno.get(blockName)+" " +locctr);
									bw.newLine();
									locctr += 3;
								}

							}
							// littabAddress.clear();
							littabAsterisk.clear();
							// littabValue.clear();
							general.clear();
							generalCount = 0;
						}
					}

					else if (opcode.equalsIgnoreCase("equ")) // using EQU // Directive
					{
						//System.out.println(operand);
						int x = 0;
						String regex = "\\d+";
						if (operand.matches(regex)) {
							x = Integer.parseInt(operand);
						} 
						else if (symtab.containsKey(operand)) {
							x = symtab.get(operand);
						} 
						else if (operand.equals("*")) {
							x = locctr;
						} 
						else if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))) {
							
							if(operand.matches(".*\\d+.*"))
							{
							//System.out.println(operand);
							ScriptEngineManager mgr = new ScriptEngineManager();
						    ScriptEngine engine = mgr.getEngineByName("JavaScript");
						    String foo = operand;
						    
						    if(engine.eval(foo) instanceof Integer){
						    int y =   (int) engine.eval(foo);
						    //System.out.println(y);
						     x = (int) Math.floor(y);
						     }
						    else if(engine.eval(foo) instanceof Double){
						    	Double y=(Double)engine.eval(foo);
						    	 //System.out.println(y);
							     x = (int) Math.floor(y);
						    	
						    }
						   
								if (x < 0) {
									System.out.println("invaild Addressing");
									bw.write("$$$$ Invalid Addressing $$$$");
									bw.newLine();
									continue;
								}
						}
							else if(!operand.contains("*")&&!operand.contains("/"))
							{
								List<String> list = new ArrayList<String>();
								  //System.out.println("taa"+  operand);
								  int minus=0;
								  int plus=0;
								  String R="";
									String [] result = operand.split("[-+]");
									String [] resultop= operand.split("(?<=[-+])|(?=[-+])");
									for(String s : result) {
									       if(s != null && s.length() > 0) {
									          list.add(s);
									       }
									    }

									    result = list.toArray(new String[list.size()]);
									    list.clear();
									    for(String s : resultop) {
										       if(s != null && s.length() > 0) {
										          list.add(s);
										       }
										    }
										    resultop = list.toArray(new String[list.size()]);

								
									if(!resultop[0].substring(0, 1).equals("-"))
										plus++;
								for (int i = 0; i < resultop.length; i++) {
									
									
									if(resultop[i].equals("+"))
										plus++;
									
									if(resultop[i].equals("-"))
										minus++;
										
									
								}
								
								
									
									if(result.length%2!=0)
									{

										//System.out.println("Fdfsds");
										bw.write("$$$ Invalid Relative Addressing $$$");
										bw.newLine();
										continue;
									}
									else if(minus!=plus)
									{

										//System.out.println("33333333");
										bw.write("$$$ Invalid Relative Addressing $$$");
										bw.newLine();
										continue;
									}
										
									
									else
									{
										if(resultop[0].equals("-"))
									
										
									R="-";
									
									
									for (int i = 0; i < result.length; i++) 
									{
										if(resultop[0].equals("-"))
									{
										
									
									if(Pass1.symtab.containsKey(result[i]))
									{
										//System.out.println("asdf");
										R=R+Pass1.symtab.get(result[i]);
										if(i!=result.length-1)
										R=R+resultop[2*i+2];
										
									}
									else
									{ 

										bw.write("$$$ Invalid Symbol $$$");
										bw.newLine();
									//System.out.println("ddddddd");
										continue loop1;
										
									}
									
								}
										else{
										if(Pass1.symtab.containsKey(result[i]))
										{
											//System.out.println("asdf");
											R=R+Pass1.symtab.get(result[i]);
											if(i!=result.length-1)
											R=R+resultop[2*i+1];
											
										}
										else
										{   

											bw.write("$$$ Invalid Symbol $$$");
											bw.newLine();
										//System.out.println("ddddddd");
											continue loop1;
											
										}
										}
									}
									//System.out.println("#^^#^#^#^#^^# "+R);
									x = 0;
									//System.out.println(operand);
									ScriptEngineManager mgr = new ScriptEngineManager();
								    ScriptEngine engine = mgr.getEngineByName("JavaScript");
								    String foo = R;
								    if(engine.eval(foo) instanceof Integer){
								    int y = (int) engine.eval(foo);
								    //System.out.println(y);
								     x = (int) Math.floor(y);
								     }
								    else if(engine.eval(foo) instanceof Double){
								    	Double y=(Double)engine.eval(foo);
								    	 //System.out.println(y);
									     x = (int) Math.floor(y);
								    }
								   
										if (x < 0) {

											//System.out.println(x);
											System.out.println("invaild Addressing");
											bw.write("$$$$ Invalid Addressing $$$$");
											bw.newLine();
											continue;
										}

									}
							}
						}
						else 
						{
							bw.write("$$$$ Error in label $$$$");
							bw.newLine();
							continue;
						}
						
						// System.out.println(label);
						String locctrEqu = "";
						locctrEqu = Integer.toHexString(x);
						System.out.println(locctrEqu);
						int y = 4 - locctrEqu.length();
						for (int i = 1; i <= y; i++) {
							locctrEqu = "0" + locctrEqu;
						}
						bw.write(s + "                                "+blockno.get(blockName)+" " +locctrEqu);
						bw.newLine();
						//System.out.println("label : "+label);
						//System.out.println("block no : "+ blockno.get(blockName));
						symtab.put(label, x);
					} else if (operand.equals("") && opcode.equalsIgnoreCase("org")) {
						// System.out.println("inside");
						locctrOrg = 0;
						String locOrg = Integer.toHexString(locctr);
						int y = 4 - locOrg.length();
						for (int i = 1; i <= y; i++) {
							locOrg = "0" + locOrg;
						}
						flagOrg = false;
						bw.write(s + "                                "+blockno.get(blockName)+" "+ locOrg);
						bw.newLine();
					} else if (opcode.equalsIgnoreCase("org")) { // System.out.println(opcode);
						String locOrg = "";
						flagOrg = true;
						if (symtab.containsKey(operand)) {
							locctrOrg = symtab.get(operand);
						} 
						else if (operand.equals("*")) {
							// System.out.println(locctr);
							locctrOrg = locctr;
						} 
						else {
							bw.write("$$$$ Error in Label $$$$");
							bw.newLine();
							continue;
						}
						locOrg = Integer.toHexString(locctrOrg);
						int x = 4 - locOrg.length();
						for (int i = 1; i <= x; i++) {
							locOrg = "0" + locOrg;
						}
						bw.write(s + "                                "+blockno.get(blockName)+" "+ locOrg);
						bw.newLine();

					}

					else if (opcode.equalsIgnoreCase("word")) {
						if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))

								&& operand.matches(".*\\d+.*")) {	
								int x = 0;
								System.out.println(operand);
								ScriptEngineManager mgr = new ScriptEngineManager();
							    ScriptEngine engine = mgr.getEngineByName("JavaScript");
							    String foo = operand;
							    if(engine.eval(foo) instanceof Integer){
							    int y = (int) engine.eval(foo);
							    System.out.println(y);
							     x = (int) Math.floor(y);
							     }
							    else if(engine.eval(foo) instanceof Double){
							    	Double y=(Double)engine.eval(foo);
							    	 System.out.println(y);
								     x = (int) Math.floor(y);
							    }
							   // ObjectCode = Integer.toHexString(x);
							    
									if (x < 0) {
										System.out.println("invaild Addressing");
										bw.write("$$$$ Invalid Addressing $$$$");
										bw.newLine();
										continue;
									}
								
							}
						if (flagOrg) // org
						{
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
							bw.newLine();
							locctrOrg += 3;
						} 
						else {
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
							bw.newLine();
							locctr += 3;
						}
					} 
					else if (opcode.equalsIgnoreCase("resw")) {
						int z = 0;
						if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))

								&& operand.matches(".*\\d+.*")) {	
							
								System.out.println(operand);
								ScriptEngineManager mgr = new ScriptEngineManager();
							    ScriptEngine engine = mgr.getEngineByName("JavaScript");
							    String foo = operand;
							    
							    if(engine.eval(foo) instanceof Integer)
							    {
							    int y = (int) engine.eval(foo);
							    System.out.println(y);
							     z = (int) Math.floor(y);
							     operand = Integer.toString(z);
							     }
							    else if(engine.eval(foo) instanceof Double){
							    	Double y=(Double)engine.eval(foo);
							    	 System.out.println(y);
								     z = (int) Math.floor(y);
								     operand = Integer.toString(z);
							    }
							   // ObjectCode = Integer.toHexString(x);
							    
									if (z < 0) {
										System.out.println("invaild Addressing");
										bw.write("$$$$ Invalid Addressing $$$$");
										bw.newLine();
										continue;
									}
								
							}
						if (flagOrg) // org
						{
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
							bw.newLine();
							if (operand.equals("*")) {
								int x = locctrOrg;
								locctrOrg = locctrOrg + (3 * x);
							} 
							else {
								int x = Integer.parseInt(operand);
								locctrOrg = locctrOrg + (3 * x);
							}
						} 
						else {
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
							bw.newLine();
							if (operand.equals("*")) {
								int x = locctr;
								locctr = locctr + (3 * x);
							} 
							else {
								int x = Integer.parseInt(operand);
								locctr = locctr + (3 * x);
							}
						}
					}

					else if (opcode.equalsIgnoreCase("resb")) // org
					{
						if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))

								&& operand.matches(".*\\d+.*")) {	
								int x = 0;
								System.out.println(operand);
								ScriptEngineManager mgr = new ScriptEngineManager();
							    ScriptEngine engine = mgr.getEngineByName("JavaScript");
							    String foo = operand;
							    if(engine.eval(foo) instanceof Integer){
							    int y = (int) engine.eval(foo);
							    System.out.println(y);
							     x = (int) Math.floor(y);
							     operand = Integer.toString(x);
							     }
							    else if(engine.eval(foo) instanceof Double){
							    	Double y=(Double)engine.eval(foo);
							    	 System.out.println(y);
								     x = (int) Math.floor(y);
								     operand = Integer.toString(x);
							    }
							   // ObjectCode = Integer.toHexString(x);
							    
									if (x < 0) {
										System.out.println("invaild Addressing");
										bw.write("$$$$ Invalid Addressing $$$$");
										bw.newLine();
										continue;
									}
								
							}
						if (flagOrg) {
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
							bw.newLine();
							if (operand.equals("*")) {
								int x = locctrOrg;
								locctrOrg = locctrOrg + x;
							} 
							else {
								int x = Integer.parseInt(operand);
								locctrOrg = locctrOrg + x;
							}
						} 
						else {
							bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
							bw.newLine();
							if (operand.equals("*")) {
								int x = locctr;
								locctr = locctr + x;
							} 
							else {
								int x = Integer.parseInt(operand);
								locctr = locctr + x;
							}

						}
					} 
					else if (opcode.equalsIgnoreCase("byte")) // org
					{ 
						if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))

							&& operand.matches(".*\\d+.*")) {	
							int x = 0;
							System.out.println(operand);
							ScriptEngineManager mgr = new ScriptEngineManager();
						    ScriptEngine engine = mgr.getEngineByName("JavaScript");
						    String foo = operand;
						    if(engine.eval(foo) instanceof Integer){
						    int y = (int) engine.eval(foo);
						    System.out.println(y);
						     x = (int) Math.floor(y);
						     }
						    else if(engine.eval(foo) instanceof Double){
						    	Double y=(Double)engine.eval(foo);
						    	 System.out.println(y);
							     x = (int) Math.floor(y);
						    }
						   // ObjectCode = Integer.toHexString(x);
						    
								if (x < 0) {
									System.out.println("invaild Addressing");
									bw.write("$$$$ Invalid Addressing $$$$");
									bw.newLine();
									continue;
								}
							
						}
						
						 if (flagOrg) {
							if ((operand.substring(0, 1)).equalsIgnoreCase("c")) {
								int z = operand.length() - 3;
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
								bw.newLine();
								// int x = operand.length();
								locctrOrg = locctrOrg + z;
								flag = 1;
							} 
							else {
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctrOrg);
								bw.newLine();
								// int x = operand.length();
								locctrOrg = locctrOrg + 1;
							}
						} 
						else {
							if ((operand.substring(0, 1)).equalsIgnoreCase("c")) {
								int z = operand.length() - 3;
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
								bw.newLine();
								// int x = operand.length();
								locctr = locctr + z;
								flag = 1;
							} 
							else {
								bw.write(s + "                                "+blockno.get(blockName)+" "+ locctr);
								bw.newLine();
								// int x = operand.length();
								locctr = locctr + 1;
							}
						}
					} 
					else
					// flag = 1;
					// error
					// mala2ahash fel opcode
					{
						bw.write("$$$$ ILLEGAL MACHINE INSTRUCTION $$$$");
						bw.newLine();
						// System.out.println("ERORR instruction");
					}
				}

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}catch (ScriptException e){
			e.printStackTrace();
		}
				
			finally {
			try {
				// System.out.println("cscdscds");
				bw.close();
				osw.close();
				out.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
