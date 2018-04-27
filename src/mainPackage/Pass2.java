package mainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Pass2 {

	FileInputStream in1;
	InputStreamReader reader1;
	BufferedReader br1;
	
	FileOutputStream out = null;
	OutputStreamWriter osw = null;
	BufferedWriter bw = null;

	FileOutputStream out1 = null;
	OutputStreamWriter osw1 = null;
	BufferedWriter bw1 = null;
	static boolean flags=true;
	static String s2;
	static String sz;
	static int count=0;
	static int countTest = 0;
	static int countMod = 0;
	static boolean flagWrite = false;
	static boolean flagcount = false;
	static boolean flagend = false;

	public void  read() {

		try {
			in1 = new FileInputStream("/Users/karim/Desktop/SystemOut.txt");
			reader1 = new InputStreamReader(in1);
			br1 = new BufferedReader(reader1);
			iterate2();
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				br1.close();
				reader1.close();
				in1.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		
		}

	
	public void iterate2(){
		s2 = "";
		String label = "";
		String opcode = "";
		String operand = "";
		String comment = "";
		String locctr = "";
		String block = "";
		String OperandAddress = "";
		String ObjectCode = "";
		
		try {
			out = new FileOutputStream("/Users/karim/Desktop/LISFILE.txt");
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			
			out1 = new FileOutputStream("/Users/karim/Desktop/OBJPROG.txt");
			osw1 = new OutputStreamWriter(out1);
			bw1 = new BufferedWriter(osw1);
			Loop:
			while((s2 = br1.readLine()) != null)
			{
				//System.out.println("label : "+label);
				//System.out.println("block no : "+ Pass1.blockno.get(Pass1.blockName));

				if(s2.substring(0, 1).equals("$"))
				{
					bw.write(s2);
					bw.newLine();
					continue;
					
				}
				else if(s2.substring(0, 1).equals("."))
				{
					bw.write(s2);
					bw.newLine();
					continue;
				}
				else if (s2.length() <= 35) {
					label = s2.substring(0, 8).trim();
					opcode = s2.substring(9, 16).trim();
					if(!opcode.equalsIgnoreCase("equ") && !opcode.equalsIgnoreCase("org"))// || !opcode.equals("org")
					{
					locctr = s2.substring(s2.lastIndexOf(' ') + 1);
					s2 = s2.substring(0, s2.lastIndexOf(' ')+1) + Integer.toHexString(Integer.parseInt(locctr));
					operand = s2.substring(17, s2.length()-(locctr.length()+1)).trim();
					}
				} 
				else if (s2.length() > 35) {
					label = s2.substring(0, 8).trim();
					opcode = s2.substring(9, 16).trim();
					if(!opcode.equalsIgnoreCase("equ") && !opcode.equalsIgnoreCase("org") && !opcode.equalsIgnoreCase("use"))// || !opcode.equals("org")
					{
						
					operand = s2.substring(17, 35).trim();
					locctr = s2.substring(s2.lastIndexOf(' ') + 1);
					String locctrtest= locctr;
					locctrtest = Integer.toHexString(Integer.parseInt(locctr));
					int x = 4 - locctrtest.length();
					 for (int i = 1 ; i <= x ; i++)
						{
						 locctrtest = "0"+locctrtest;
						}
					s2 = s2.substring(0, s2.lastIndexOf(' ')+1) + locctrtest;
					block = s2.substring(s2.lastIndexOf(' ')-1, s2.lastIndexOf(' '));
					//System.out.println(block);
					comment = s2.substring(35, s2.length()-(locctr.length()+1)).trim();
					//System.out.println(s2);
					}
					//System.out.println(opcode);
					
				}
				if(opcode.equalsIgnoreCase("start"))
				{
					//System.out.println("xx");

					bw.write(s2);
					bw.newLine();
					String progLength = Integer.toHexString(Pass1.ProgramLength);
					String startAdd = Integer.toHexString(Pass1.StartingAddress);
					//startAdd=String.format("%06",);
					int x=startAdd.length();
					for (int i = 1 ; i <= 6-x ; i++)
					{
						startAdd = "0"+startAdd;
					}
					int y = progLength.length();
					for (int i = 1 ; i <= 6-y ; i++)
					{
						progLength = "0"+progLength;
					}
					bw1.write("H^"+label+"^"+startAdd+"^"+progLength);
					bw1.newLine();
					bw1.write("T^"+startAdd);
					//text = "T^"+startAdd;
				}
				else
					if(opcode.equalsIgnoreCase("end"))
					{  
						bw.write(s2);
					    bw.newLine();
						//System.out.println(Pass1.generalCount);
						if(Pass1.generalCount !=0)//literal
						{
							flagend = true;
							Pass1.generalCount--;
							continue;
						}
						bw1.write("--"+Integer.toHexString(count));
						bw1.newLine();
						
						//text record to object program
						//new line
						String startAdd = Integer.toHexString(Pass1.StartingAddress);
						int x=startAdd.length();
						for (int i = 1 ; i <= 6-x ; i++)
						{
							startAdd = "0"+startAdd;
						}
						bw1.write("E^"+startAdd);
						System.out.println("END Pass 2");
						//break;
						//bw1.newLine();
					}
					else
						if(Opcode.tbl.containsKey(opcode))
						{
							String opVal = Integer.toHexString(Opcode.tbl.get(opcode)).toString();
							if(!operand.equals("") && operand.substring(0, 1).equals("=")) // opcode with literal
							{
								count+=3;
								countTest+=3;
								countMod+=3;
								
								if(operand.substring(1, 2).equals("c"))
								{
									String testval = operand.substring(3, operand.length()-1);
									String hex = "";
							        
							        for (int i=0 ; i < testval.length(); i++) 
							        {
							          hex = hex+Integer.toHexString(testval.charAt(i));
							        }      
							        testval = hex;
									if(Pass1.littabAddress.containsKey(operand))
									{
										//System.out.println(block);
										if(!block.equalsIgnoreCase("0"))
										{
											//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
										}
										else
										{
											
										OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand));
										}
										int x = 4 - OperandAddress.length();
										 for (int i = 1 ; i <= x ; i++)
											{
												OperandAddress = "0"+OperandAddress;
											}
										 ObjectCode = opVal+OperandAddress;
										 int y = 6 - ObjectCode.length();
										 for (int i = 1 ; i <= y ; i++)
											{
												ObjectCode = "0"+ObjectCode;
											}
										 bw.write(s2+" "+ObjectCode);
										 bw.newLine();
										 if(flagcount == true)
										 {
											 String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
											// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
											 flagcount = false;
										 }
										 if(flagWrite == true)
											{
												String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
												flagWrite = false;
											}
										bw1.write("^"+ObjectCode);
									}
									//else if not found in littabAddress
									else 
									{
										String key= null;
								        String value=testval;
								        for(Map.Entry entry: Pass1.littabValue.entrySet())
								        {
								            if(value.equals(entry.getValue()))
								            {
								                key = (String) entry.getKey();
								                break; //breaking because its one to one map
								            }
								        }//loop to find key from values
										//System.out.println("value "+key);
								        if(Pass1.littabAddress.containsKey(key))
										{	
								        	if(!block.equalsIgnoreCase("0"))
											{
												//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
												OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
											}
											else
											{
												
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key));
											}
											int x = 4 - OperandAddress.length();
											 for (int i = 1 ; i <= x ; i++)
												{
													OperandAddress = "0"+OperandAddress;
												}
											 ObjectCode = opVal+OperandAddress;
											 int y = 6 - ObjectCode.length();
											 for (int i = 1 ; i <= y ; i++)
												{
													ObjectCode = "0"+ObjectCode;
												}
											 bw.write(s2+" "+ObjectCode);
											 bw.newLine();
											 if(flagcount == true)
											 {
												 String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
												// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
												 flagcount = false;
											 }
											 if(flagWrite == true)
												{
													String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
													flagWrite = false;
												}
											bw1.write("^"+ObjectCode);
										}
									}									
								}
								else if(operand.substring(1, 2).equals("x"))
								{
									String testval;
									testval = operand.substring(3,operand.length()-1);
									testval = Integer.toHexString(Integer.parseInt(testval,16));
									if(Pass1.littabAddress.containsKey(operand))
									{
										
										if(!block.equalsIgnoreCase("0"))
										{
											//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
										}
										else
										{
											
										OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand));
										}
										int x = 4 - OperandAddress.length();
										 for (int i = 1 ; i <= x ; i++)
											{
												OperandAddress = "0"+OperandAddress;
											}
										 ObjectCode = opVal+OperandAddress;
										 int y = 6 - ObjectCode.length();
										 for (int i = 1 ; i <= y ; i++)
											{
												ObjectCode = "0"+ObjectCode;
											}
										 bw.write(s2+" "+ObjectCode);
										 bw.newLine();
										 if(flagcount == true)
										 {
											 String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
											// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
											 flagcount = false;
										 }
										 if(flagWrite == true)
											{
												String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
												flagWrite = false;
											}
										bw1.write("^"+ObjectCode);
									}
									// else if not found in littabAddress
									else 
									{
										String key= null;
								        String value=testval;
								        for(Map.Entry entry: Pass1.littabValue.entrySet())
								        {
								            if(value.equals(entry.getValue()))
								            {
								                key = (String) entry.getKey();
								                break; //breaking because its one to one map
								            }
								        }//loop to find key from values
										//System.out.println("value "+key);
								        if(Pass1.littabAddress.containsKey(key))
										{
											
								        	if(!block.equalsIgnoreCase("0"))
											{
												//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
												OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
											}
											else
											{
												
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key));
											}
											int x = 4 - OperandAddress.length();
											 for (int i = 1 ; i <= x ; i++)
												{
													OperandAddress = "0"+OperandAddress;
												}
											 ObjectCode = opVal+OperandAddress;
											 int y = 6 - ObjectCode.length();
											 for (int i = 1 ; i <= y ; i++)
												{
													ObjectCode = "0"+ObjectCode;
												}
											 bw.write(s2+" "+ObjectCode);
											 bw.newLine();
											 if(flagcount == true)
											 {
												 String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
												// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
												 flagcount = false;
											 }
											 if(flagWrite == true)
												{
													String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
													flagWrite = false;
												}
											bw1.write("^"+ObjectCode);
										}
									}
								}
								else if(operand.substring(1, 2).equals("*"))
								{
									
									if(!block.equalsIgnoreCase("0"))
									{
										//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
										OperandAddress = Integer.toHexString(Integer.parseInt(locctr)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
									}
									else
									{
										
									OperandAddress = Integer.toHexString(Integer.parseInt(locctr));
									}
								 int x = 4 - OperandAddress.length();
								 for (int i = 1 ; i <= x ; i++)
									{
										OperandAddress = "0"+OperandAddress;
									}
								 ObjectCode = opVal+OperandAddress;
								 int y = 6 - ObjectCode.length();
								 for (int i = 1 ; i <= y ; i++)
									{
										ObjectCode = "0"+ObjectCode;
									}
								 bw.write(s2+" "+ObjectCode);
								 bw.newLine();
								 if(flagcount == true)
								 {
									 String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
									// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
									 flagcount = false;
								 }
								 if(flagWrite == true)
									{
										String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
										flagWrite = false;
									}
								bw1.write("^"+ObjectCode);
								
								//System.out.println(countMod);
									//modification Records
									
								}
								else
								{
									String testval;
									testval = operand.substring(1,operand.length());
									testval = Integer.toHexString(Integer.parseInt(testval,16));
									if(Pass1.littabAddress.containsKey(operand))
									{
										
										if(!block.equalsIgnoreCase("0"))
										{
											//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
										}
										else
										{
											
										OperandAddress = Integer.toHexString(Pass1.littabAddress.get(operand));
										}
										int x = 4 - OperandAddress.length();
										 for (int i = 1 ; i <= x ; i++)
											{
												OperandAddress = "0"+OperandAddress;
											}
										 ObjectCode = opVal+OperandAddress;
										 int y = 6 - ObjectCode.length();
										 for (int i = 1 ; i <= y ; i++)
											{
												ObjectCode = "0"+ObjectCode;
											}
										 bw.write(s2+" "+ObjectCode);
										 bw.newLine();
										 if(flagcount == true)
										 {
											 String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
											// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
											 flagcount = false;
										 }
										 if(flagWrite == true)
											{
												String lct = Integer.toHexString(Integer.parseInt(locctr));
												x=lct.length();
												for (int i = 1 ; i <= 6-x ; i++)
												{
													lct = "0"+lct;
												}
												bw1.write("T^"+lct);
												flagWrite = false;
											}
										bw1.write("^"+ObjectCode);
									}
									//else if not found in littabAddress
									else 
									{
										String key= null;
								        String value=testval;
								        for(Map.Entry entry: Pass1.littabValue.entrySet())
								        {
								            if(value.equals(entry.getValue()))
								            {
								                key = (String) entry.getKey();
								                break; //breaking because its one to one map
								            }
								        }//loop to find key from values
										//System.out.println("value "+key);
								        if(Pass1.littabAddress.containsKey(key))
										{
											
								        	if(!block.equalsIgnoreCase("0"))
											{
												//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
												OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
											}
											else
											{
												
											OperandAddress = Integer.toHexString(Pass1.littabAddress.get(key));
											}
											int x = 4 - OperandAddress.length();
											 for (int i = 1 ; i <= x ; i++)
												{
													OperandAddress = "0"+OperandAddress;
												}
											 ObjectCode = opVal+OperandAddress;
											 int y = 6 - ObjectCode.length();
											 for (int i = 1 ; i <= y ; i++)
												{
													ObjectCode = "0"+ObjectCode;
												}
											 bw.write(s2+" "+ObjectCode);
											 bw.newLine();
											 if(flagcount == true)
											 {
												 String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
												// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
												 flagcount = false;
											 }
											 if(flagWrite == true)
												{
													String lct = Integer.toHexString(Integer.parseInt(locctr));
													x=lct.length();
													for (int i = 1 ; i <= 6-x ; i++)
													{
														lct = "0"+lct;
													}
													bw1.write("T^"+lct);
													flagWrite = false;
												}
											bw1.write("^"+ObjectCode);
										}
									}
								}
								
							}
							else if ((operand.contains("-")|| operand.contains("+")|| operand.contains("*") || operand.contains("/"))) // pass 2 Absolute constants Expressions
							{ 
								
								if(operand.matches(".*\\d+.*"))
								{
									count +=3;
									countMod+=3;
									countTest+=3;
								
								OperandAddress = Integer.toHexString(Pass1.expressionTab.get(operand));
								int x = 4 - OperandAddress.length();
								 for (int i = 1 ; i <= x ; i++)
									{
										OperandAddress = "0"+OperandAddress;
									}
								 ObjectCode = opVal+OperandAddress;
								 int y = 6 - ObjectCode.length();
								 for (int i = 1 ; i <= y ; i++)
									{
										ObjectCode = "0"+ObjectCode;
									}
								 bw.write(s2+" "+ObjectCode);
								 bw.newLine();
								 if(flagcount == true)
								 {
									 String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
									// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
									 flagcount = false;
								 }
								 if(flagWrite == true)
									{
										String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
										flagWrite = false;
									}
								bw1.write("^"+ObjectCode);
							}
								else if(!operand.contains("*")&&!operand.contains("/"))
								{
								    count +=3;
									countMod+=3;
									countTest+=3;
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
										    	System.out
														.println("resulttop:"+resultop[i]);
												
											}
										    for (int i = 0; i < result.length; i++) {
										    	System.out
														.println("result:"+result[i]);
												
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
										count -=3;
										countMod-=3;
										countTest-=3;
										//System.out.println("Fdfsds");
										bw.write("$$$ Invalid Relative Addressing $$$");
										bw.newLine();
										continue;
									}
									else if(minus!=plus)
									{
										count -=3;
										countMod-=3;
										countTest-=3;
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
										count -=3;
										countMod-=3;
										countTest-=3;
										bw.write("$$$ Invalid Symbol $$$");
										bw.newLine();
									//System.out.println("ddddddd");
										continue Loop;
										
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
											count -=3;
										   countMod-=3;
										   countTest-=3;
											bw.write("$$$ Invalid Symbol $$$");
											bw.newLine();
										//System.out.println("ddddddd");
											continue Loop;
											
										}
										}
									}
									//System.out.println("#^^#^#^#^#^^# "+R);
									int x = 0;
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
											count -=3;
											countMod-=3;
											countTest-=3;
											//System.out.println(x);
											System.out.println("invaild Addressing");
											bw.write("$$$$ Invalid Addressing $$$$");
											bw.newLine();
											continue;
										}
										OperandAddress = Integer.toHexString(x);
										int y = 4 - OperandAddress.length();
										 for (int i = 1 ; i <= y ; i++)
											{
												OperandAddress = "0"+OperandAddress;
											}
									 ObjectCode = opVal+OperandAddress;
									 int x1=6-ObjectCode.length();
									 for (int i = 1 ; i <= x1 ; i++)
										{
											ObjectCode = "0"+ObjectCode;
										}
									 bw.write(s2+" "+ObjectCode);
									 bw.newLine();
									 if(flagcount == true)
									 {
										 String lct = Integer.toHexString(Integer.parseInt(locctr));
											x1=lct.length();
											for (int i = 1 ; i <= 6-x1 ; i++)
											{
												lct = "0"+lct;
											}
											bw1.write("T^"+lct);
										// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
										 flagcount = false;
									 }
									 if(flagWrite == true)
										{
											String lct = Integer.toHexString(Integer.parseInt(locctr));
											x=lct.length();
											for (int i = 1 ; i <= 6-x1 ; i++)
											{
												lct = "0"+lct;
											}
											bw1.write("T^"+lct);
											flagWrite = false;
										}
									 
									 bw1.write("^"+ObjectCode);
									}
								}
								else
								{
									bw.write("$$$ Invalid Relative Addressing $$$");
									bw.newLine();
									continue;
								}
							}
							else if(Pass1.symtab.containsKey(operand)||operand.equals("*"))
							{
								count+=3;
								countTest+=3;
								countMod+=3;
								if(operand.equals("*"))
								{
									if(!block.equalsIgnoreCase("0"))
									{
										//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
										OperandAddress = Integer.toHexString(Integer.parseInt(locctr)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
									}
									else
									{
										
										OperandAddress = Integer.toHexString(Integer.parseInt(locctr));
									}
									
								}
								else
								{
									if(!block.equalsIgnoreCase("0"))
									{
										//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
										OperandAddress = Integer.toHexString(Pass1.symtab.get(operand)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
									}
									else
									{
										
										 OperandAddress = Integer.toHexString(Pass1.symtab.get(operand));
									}
								}
								int y = 4 - OperandAddress.length();
								 for (int i = 1 ; i <= y ; i++)
									{
										OperandAddress = "0"+OperandAddress;
									}
							 ObjectCode = opVal+OperandAddress;
							 int x=6-ObjectCode.length();
							 for (int i = 1 ; i <= x ; i++)
								{
									ObjectCode = "0"+ObjectCode;
								}
							 bw.write(s2+" "+ObjectCode);
							 bw.newLine();
							 if(flagcount == true)
							 {
								 String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
								// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								 flagcount = false;
							 }
							 if(flagWrite == true)
								{
									String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
									flagWrite = false;
								}
							 
							 bw1.write("^"+ObjectCode);
							}
							else if (operand.substring(operand.lastIndexOf(",")+1).equalsIgnoreCase("x"))
							{
								 count +=3;
								 countTest +=3;
								 countMod+=3;
								 operand = operand.substring(0, operand.lastIndexOf(","));
								 if(!block.equalsIgnoreCase("0"))
									{
										//System.out.println(Pass1.blockAdd.get(Pass1.blockno1.get(block)));
										OperandAddress = Integer.toHexString(Pass1.symtab.get(operand)+(int)Math.pow(2, 15)+Pass1.blockAdd.get(Pass1.blockno1.get(Integer.parseInt(block))));
									}
									else
									{
										
										OperandAddress = Integer.toHexString(Pass1.symtab.get(operand)+(int)Math.pow(2, 15));
									}
								 
								 ObjectCode = opVal+OperandAddress;
								 int x=ObjectCode.length();
								 for (int i = 1 ; i <= 6-x ; i++)
									{
										ObjectCode = "0"+ObjectCode;
									}
								 bw.write(s2+" "+ObjectCode);
								 bw.newLine();
								 if(flagcount == true)
								 {
									 String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
									 // bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
									 flagcount = false;
								 }
								 if(flagWrite == true)
									{
										String lct = Integer.toHexString(Integer.parseInt(locctr));
										x=lct.length();
										for (int i = 1 ; i <= 6-x ; i++)
										{
											lct = "0"+lct;
										}
										bw1.write("T^"+lct);
										flagWrite = false;
									}
								bw1.write("^"+ObjectCode);
							}
							else
							{
								if (opcode.equalsIgnoreCase("rsub"))
								{
									count+=3;
									countTest+=3;
									countMod+=3;
									opVal = Integer.toHexString(Opcode.tbl.get("rsub"));
									OperandAddress = "0000";
									ObjectCode = opVal + OperandAddress;
									int x=ObjectCode.length();
									 for (int i = 1 ; i <= 6-x ; i++)
										{
											ObjectCode = "0"+ObjectCode;
										}
									 bw.write(s2+" "+ObjectCode);
									 bw.newLine();
									 if(flagcount == true)
									 {
										 String lct = Integer.toHexString(Integer.parseInt(locctr));
											x=lct.length();
											for (int i = 1 ; i <= 6-x ; i++)
											{
												lct = "0"+lct;
											}
											bw1.write("T^"+lct);
										 flagcount = false;
									 }
									 if(flagWrite == true)
										{
											String lct = Integer.toHexString(Integer.parseInt(locctr));
											x=lct.length();
											for (int i = 1 ; i <= 6-x ; i++)
											{
												lct = "0"+lct;
											}
											bw1.write("T^"+lct);
											flagWrite = false;
										}
									 bw1.write("^"+ObjectCode);
									 continue;
								}//end of rsub
								
								bw.write("$$$$Can't Find The Symbol$$$$");
								bw.newLine();
								OperandAddress = "0000";
								
							}
							
							}//Opcode is one of the keys of the opTab
						else if(opcode.equalsIgnoreCase("use"))
						{
							bw.write(s2);
					        bw.newLine();
							continue;
						}
						else if (opcode.equalsIgnoreCase("ltorg")) // literals
						{
							bw.write(s2);
					        bw.newLine();
							continue;
						}
						else if (opcode.equalsIgnoreCase("equ")) // EQU directive
						{
							bw.write(s2);
					        bw.newLine();
							continue;
						}
						else if(opcode.equalsIgnoreCase("org")) // ORG directive
						{
							bw.write(s2);
					        bw.newLine();
							continue;
						}
						else if (opcode.substring(0, 1).equals("*")) //literals
						{
							
							count+=3;
							countTest+=3;
							countMod+=3;
							ObjectCode = Integer.toHexString(Integer.parseInt(locctr));
							int x=ObjectCode.length();
							for (int i = 1 ; i <= 6-x ; i++)
							{
								ObjectCode = "0"+ObjectCode;
							}
							bw.write(s2+" " + ObjectCode);
							bw.newLine();
							if(flagcount == true)
							 {
								 String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
								// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								 flagcount = false;
							 }
							 if(flagWrite == true)
								{
									String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
									flagWrite = false;
								}
							bw1.write("^"+ObjectCode);
							//modification record
							
							continue;
						}
						else if((opcode.substring(0, 1).equals("=")||opcode.equalsIgnoreCase("byte")||opcode.equalsIgnoreCase("word")))
							{
							if(opcode.substring(0, 1).equals("="))//literals
							{
							if(opcode.substring(1, 2).equals("c"))
							{
								String sub;
								sub = opcode.substring(3,opcode.length()-1);
								String hex = "";
						        
						        for (int i=0; i < sub.length(); i++) {
						          hex = hex+Integer.toHexString(sub.charAt(i));
						          //System.out.println("count of c'' "+i);
						          count+=1;  
						          countTest+=1;
						          countMod+=1;
						        }      
						        ObjectCode = hex;
//						        int x=ObjectCode.length();
//						        for (int i = 1 ; i <= 6-x ; i++)
//								{
//									ObjectCode = "0"+ObjectCode;
//								}
						        bw.write(s2 + " " + ObjectCode);
						        bw.newLine();
							}
							else if(opcode.substring(1, 2).equals("x"))
							{
								String sub;
								sub = opcode.substring(3,opcode.length()-1);
					            //String hex = "";
						        int y = sub.length();
						        if(y % 2==0)
						        {
						        for (int i=0; i < y/2; i++) 
						           {
						          //hex = hex+Integer.toHexString(sub.charAt(i));
						            count+=1;
						            countTest+=1;
						            countMod+=1;
						           } 
						        }
						        else
						        {
						        	for (int i=0; i < (y+1)/2; i++) 
						        	{
						        		count+=1;
						        		countTest+=1;
						        		countMod+=1;
						        	}
						        }
						        ObjectCode = sub;
//						        int x=ObjectCode.length();
//						        for (int i = 1 ; i <= 6-x ; i++)
//								{
//									ObjectCode = "0"+ObjectCode;
//								}
						        bw.write(s2 + " " + ObjectCode);
						        bw.newLine();
							}
							else if((0 < Integer.parseInt(opcode.substring(1, 2)))&&(Integer.parseInt(opcode.substring(1, 2)) < 9))
							{
								String sub;
								sub = opcode.substring(1,opcode.length());
					            //String hex = "";
						        int y = sub.length();
						        if(y % 2==0)
						        {
						        for (int i=0; i < y/2; i++) 
						           {
						          //hex = hex+Integer.toHexString(sub.charAt(i));
						            count+=1;
						            countTest+=1;
						            countMod+=1;
						           } 
						        }
						        else
						        {
						        	for (int i=0; i < (y+1)/2; i++) 
						        	{
						        		count+=1;
						        		countTest+=1;
						        		countMod+=1;
						        	}
						        }
						        ObjectCode = sub;
//						        int x=ObjectCode.length();
//						        for (int i = 1 ; i <= 6-x ; i++)
//								{
//									ObjectCode = "0"+ObjectCode;
//								}
						        bw.write(s2 + " " + ObjectCode);
						        bw.newLine();
							}
							}
							else
							{
							if(operand.substring(0, 1).equalsIgnoreCase("c"))
							{   
								
								String sub;
								sub = operand.substring(2,operand.length()-1);
								String hex = "";
						        
						        for (int i=0; i < sub.length(); i++) {
						          hex = hex+Integer.toHexString(sub.charAt(i));
						          //System.out.println("count of c'' "+i);
						          count+=1;  
						          countTest+=1;
						          countMod+=1;
						        }      
						        ObjectCode = hex;
//						        int x=ObjectCode.length();
//						        for (int i = 1 ; i <= 6-x ; i++)
//								{
//									ObjectCode = "0"+ObjectCode;
//								}
						        bw.write(s2 + " " + ObjectCode);
						        bw.newLine();
						        	
							}
							else if(operand.substring(0, 1).equalsIgnoreCase("x"))
							{
								//count+=3;
								String sub;
								sub = operand.substring(2,operand.length()-1);
					            //String hex = "";
						        int y = sub.length();
						        if(y % 2==0)
						        {
						        for (int i=0; i < y/2; i++) 
						           {
						          //hex = hex+Integer.toHexString(sub.charAt(i));
						            count+=1;
						            countTest+=1;
						            countMod+=1;
						           } 
						        }
						        else
						        {
						        	for (int i=0; i < (y+1)/2; i++) 
						        	{
						        		count+=1;
						        		countTest+=1;
						        		countMod+=1;
						        	}
						        }
						        ObjectCode = sub;
//						        int x=ObjectCode.length();
//						        for (int i = 1 ; i <= 6-x ; i++)
//								{
//									ObjectCode = "0"+ObjectCode;
//								}
						        bw.write(s2 + " " + ObjectCode);
						        bw.newLine();
						        	
							}
							else
							{
								count+=3;
								countTest+=3;
								countMod+=3;
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
								    ObjectCode = Integer.toHexString(x);
								    
										if (x < 0) {
											System.out.println("invaild Addressing");
											bw.write("$$$$ Invalid Addressing $$$$");
											bw.newLine();
											continue;
										}
									//expressionTab.put(operand, (int) x);
								}
								else
								{
								ObjectCode = Integer.toHexString(Integer.parseInt(operand));
								}
								//System.out.println(ObjectCode.length());
								int x=ObjectCode.length();
								for (int i = 1 ; i <= 6-x ; i++)
								{
									ObjectCode = "0"+ObjectCode;
								}
								bw.write(s2+" " + ObjectCode);
								bw.newLine();
								
							}
							}
//							for (int i = 1 ; i <= 6- x; i++)
//							{
//								ObjectCode = "0"+ObjectCode;
//							}
							int x = ObjectCode.length();
							if(flagcount == true)
							 {
								 String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
								// bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								 flagcount = false;
							 }
							 if(flagWrite == true)
								{
									String lct = Integer.toHexString(Integer.parseInt(locctr));
									x=lct.length();
									for (int i = 1 ; i <= 6-x ; i++)
									{
										lct = "0"+lct;
									}
									bw1.write("T^"+lct);
									flagWrite = false;
								}
							bw1.write("^"+ObjectCode);
							}
				if(flagend ==true && Pass1.generalCount!=0) // literals after end
				{
					Pass1.generalCount--;
				}
				else if(flagend == true && Pass1.generalCount==0)//literals after end
				{
					bw1.write("--"+Integer.toHexString(count));
					bw1.newLine();
					//text record to object program
					//new line
					String startAdd = Integer.toHexString(Pass1.StartingAddress);
					int x=startAdd.length();
					for (int i = 1 ; i <= 6-x ; i++)
					{
						startAdd = "0"+startAdd;
					}
					bw1.write("E^"+startAdd);
					System.out.println("END Pass 2");
					break;
					
				}
						else if(opcode.equalsIgnoreCase("resw")||opcode.equalsIgnoreCase("resb")||opcode.equalsIgnoreCase("use")||opcode.equalsIgnoreCase("equ")||opcode.equalsIgnoreCase("ltorg"))
						{
						
							bw.write(s2);
							bw.newLine();
							if(opcode.equalsIgnoreCase("resw")&&count!=0)
							{
								
								bw1.write("--"+Integer.toHexString(count));
								bw1.newLine();
								flagWrite = true;
								//bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								count = 0;
								countTest = 0;
								continue;
							}
							else if(opcode.equalsIgnoreCase("resb")&&count!=0)
							{
								
								bw1.write("--"+Integer.toHexString(count));
								bw1.newLine();
								flagWrite = true;
								//bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								count = 0;
								countTest = 0;
								continue;
							}
							else if(opcode.equalsIgnoreCase("use")&&count!=0)
							{
								
								bw1.write("--"+Integer.toHexString(count));
								bw1.newLine();
								flagWrite = true;
								//bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
								count = 0;
								countTest = 0;
								continue;
							}
						}
			
				if( count == 30 || (countTest + 3) > 30)
				{
					bw1.write("--"+Integer.toHexString(count));
					bw1.newLine();
					flagcount = true;
					//bw1.write("T^"+Integer.toHexString(Integer.parseInt(locctr)));
					count = 0;
					countTest = 0;
					continue;
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e){
			e.printStackTrace();
		}
		finally{
			try {
				bw1.close();
				osw1.close();
				out1.close();
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
