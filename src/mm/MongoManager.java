package mm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


/**
 * The program is meant to be run from refreshMongo.sh
 * 
 * If you want to run it from eclipse, just delete ../ from the 3 paths below
 */
public class MongoManager 
{
	private static String FILE_SCREENS = "../input/mongoDataScreens.csv";
	private static String FILE_FIELDS  = "../input/mongoDataFields.csv";
	private static String OUTPUT 	   = "../output/";
	
	
	public static void main(String[] args) 
	{
		process();
	}

	
	/**********************************************************
	 * process
	 **********************************************************/
	private static void process() 
	{
		StringBuilder sb;
		
		String[] types = null, cols = null;
		
		int c = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_SCREENS))) 
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				c++;
					 if( c == 1 ) { types = line.split(","); continue; }
				else if( c == 2 ) { cols  = line.split(","); continue; } 
				else // values // Represents each screen
				{
					sb = new StringBuilder();
					
					String[] values = line.split(",");

					//if(c > 3)
					//	sb.append(",");
					
					sb.append("{");
					
					String fields = null;
					
					for(int i=0; i<values.length; i++)
					{
						String val  = values[i];
						String type = types[i];
						String col  = cols[i];

						if(i == 0)
						{
							String screenName = val;
							fields = getFields(screenName);
						}
						
						if(i != 0)
							sb.append(",");
						
						sb.append("\"");
						sb.append(col);
						sb.append("\":");

						if(type.equals("String"))
						{
							sb.append("\"");
							sb.append(val);
							sb.append("\"");
						}
						else if(type.equals("boolean"))
						{
							if(val.equals("TRUE"))
								sb.append("true");
							else
								sb.append("false");
						}
						else
						{
							sb.append(val);
						}
						
						///////////////////////////////////
						
						
						if(i == values.length - 1)
						{
							sb.append(",\"fields\":[");
							sb.append(fields); 
							sb.append("]");
						}

					}
					
					sb.append("}");
				}
					 
				output( c-2, sb.toString() );
			}
			
			// for(String s:types) { System.out.println(s); }
			// for(String s:cols ) { System.out.println(s); }

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**********************************************************
	 * output
	 **********************************************************/
	private static void output(int c, String content) 
	{
		String filename = OUTPUT + "data_" + c + ".json";
		PrintWriter writer = null;
		
		try 
		{
			writer = new PrintWriter(filename, "UTF-8");
			writer.println(content);
			pl("Created File " + filename);
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
		finally 
		{
			writer.close();
		}
	}


	/**********************************************************
	 * getFields
	 **********************************************************/
	private static String getFields(String screenName)
	{
		StringBuilder sb = new StringBuilder();
		String[] types = null, cols = null;
		
		int c = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_FIELDS))) 
		{
			String line;
			out: while ((line = br.readLine()) != null)
			{
				c++;
					 if( c == 1 ) { types = line.split(","); }
				else if( c == 2 ) { cols  = line.split(","); } 
				else // values // Represents each field
				{
					String[] values = line.split(",");

					in: for(int i=0; i<values.length; i++)
					{
						String val  = values[i];
						String type = types[i];
						String col  = cols[i];
						
						if(i == 0)
						{
							String screenName2 = val;
							
							if( !screenName2.equals(screenName) )
								continue out; // only show fields that are on this screen
							continue in; // don't put screen as field in fields
						}

						else if(i==1)
						{
							
							sb.append("{");
						}

						if(i > 1)
							sb.append(",");
						
						sb.append("\"");
						sb.append(col);
						sb.append("\":");

						if(type.equals("String"))
						{
							sb.append("\"");
							sb.append(val);
							sb.append("\"");
						}
						else if(type.equals("boolean"))
						{
							if(val.equals("TRUE"))
								sb.append("true");
							else
								sb.append("false");
						}
						else
						{
							sb.append(val);
						}
						
						if(i == values.length-1)
							sb.append("},");
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString().substring(0, sb.toString().length()-1);
	}


	/**********************************************************
	 * pl
	 **********************************************************/
	private static void pl(String s) 
	{
		System.out.println(s);
	}

}
