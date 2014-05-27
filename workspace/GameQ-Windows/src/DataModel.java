
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

public class DataModel {

	private static String filename;
	private static String token;
	private static String email;
	private static String password;
	private static boolean bolIsLoggedIn;
	private static boolean bolIsRegisteredForNotifications;
	
	public DataModel() {
		token = "";
		email = "";
		password = "";
		bolIsLoggedIn = false;
		bolIsRegisteredForNotifications = false;
		
		
		//System.out.println(filename);
		
		//filename = "C:/Users/Ludvig Fr�berg/Desktop/workspace/GameQ-Windows/bin/res/stg.cdtx";
		
		filename = System.getProperty("user.dir") + "\\bin\\res\\stg.cdtx";
		System.out.println(filename);
		try {
			load();
			System.out.println("loaded");
		} catch (IOException e) {
			System.out.println("didn't load");
			e.printStackTrace();
		}
		
		
		
	}
	
	public static String getToken() {
		return token;
	}
	public static String getEmail() {
		return email;
	}
	public static String getPassword() {
		return password;
	}
	public static boolean getBolIsLoggedIn() {
		return bolIsLoggedIn;
	}
	public static boolean getBolIsRegisteredForNotifications() {
		return bolIsRegisteredForNotifications;
	}
	
	public static void setToken(String token) {
		DataModel.token = token;
	}
	public static void setEmail(String email) {
		DataModel.email = email;
	}
	public static void setPassword(String password) {
		DataModel.password = password;
	}
	public static void setBolIsLoggedIn(boolean bolIsLoggedIn) {
		DataModel.bolIsLoggedIn = bolIsLoggedIn;
	}
	public static void setBolIsRegisteredForNotifications(boolean bolIsRegisteredForNotifications) {
		DataModel.bolIsRegisteredForNotifications = bolIsRegisteredForNotifications;
	}
	
	
	
		
	
	public void save() throws IOException {
		File file = new File(filename);
		if (file.exists() ) {
		       file.delete();       
		}   
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} 
		
		byte[] bytes = Encryptor.encrypt("email="+email+"&password="+password+"&token="+token+"&bolIsLoggedIn="+bolIsLoggedIn+"&bolIsRegisteredForNotifications="+bolIsRegisteredForNotifications);
		/*FileWriter fw;
		try {
			fw = new FileWriter(filename, true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}*/
		FileOutputStream fos = new FileOutputStream(filename);
		try {
			fos.write(bytes);			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}/*
		BufferedWriter bw = new BufferedWriter(fw); 
		try {
			bw.write(pwd.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}*/
	}
	
	private void load() throws IOException {
		StringBuilder fileString = new StringBuilder();
		System.out.println("foodstuffs");
		System.out.println(filename);
		System.out.println("shitstuff");
		File file = new File(filename);
		if(!file.exists()) {
		    try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} 
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				fileString.append(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		byte[] bytes = FileUtils.readFileToByteArray(file);
		
		String fullDecrypted = Encryptor.decrypt(bytes);
		String[] splitString = fullDecrypted.split("&");
		if (splitString.length != 5) {
			System.out.println(splitString[0]);
			System.out.println(splitString.length);
			return;
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(splitString[i]);
			String a = splitString[i];
			String[] b = a.split("=");
			if (b.length != 2) {
				System.out.println("empty field");
				splitString[i] = "";
				if (i == 3 || i == 4) {
					splitString[i] = "false";
				}
			} else {
				splitString[i] = b[1];
			}
		}
		email = splitString[0];
		password = splitString[1];
		token = splitString[2];
		bolIsLoggedIn = Boolean.parseBoolean(splitString[3]);
		bolIsRegisteredForNotifications = Boolean.parseBoolean(splitString[4]);
	}
	
	public static void copy(InputStream input,
		      OutputStream output,
		      int bufferSize)
		      throws IOException {
		    byte[] buf = new byte[bufferSize];
		    int bytesRead = input.read(buf);
		    while (bytesRead != -1) {
		      output.write(buf, 0, bytesRead);
		      bytesRead = input.read(buf);
		    }
		    output.flush();
		  }
	
}
