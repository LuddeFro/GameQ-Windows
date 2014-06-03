import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;


public class TimeHandler {
	public Timer quickTimer;
	public Timer dotaCDTimer;
	public Timer csgoCDTimer;
	public Timer timeTimer;
	private static final int quickSpeedMillis = 1000;
	private static final int dotaCDMillis = 5000;
	private static final int csgoCDMillis = 20000;
	private static final int timeSpeedMillis = 60000;
	private Buffer dotaQBuffer;
	private Buffer dotaCBuffer;
	private Buffer honQBuffer;
	public Buffer dota174Buffer;
	public Buffer dota190Buffer;
	private Buffer dota206Buffer;
	private Buffer csgoQBuffer;
	private Buffer csgoGameBuffer;
	private boolean bolDotaCD; 
	private boolean bolCSGOCD; 
	private boolean bolFirstTick;
	private ConnectionHandler connectionsHandler;
	
	private static final int kNOGAME = 0;
	private static final int kHEROES_OF_NEWERTH = 1;
	private static final int kDOTA2 = 2;
	private static final int kCS_GO = 3;
	private static final int kOFFLINE = 0; //app running, but no game
	private static final int kONLINE = 1; //game running
	private static final int kINGAME = 2; //game running and in match
	private static final int kNotRunningGameQ = 4; //self explanatory
	private static final int kNUMBER_OF_GAMES = 4; //kNOGAME counts as 1
	
	private boolean[] inGameArray;
	private boolean[] onlineArray;
	
	
	
	
	
	
	public TimeHandler() {
		honQBuffer = new Buffer(5);
		dotaQBuffer = new Buffer(5);
		dotaCBuffer = new Buffer(5);
		dota174Buffer = new Buffer(6);
		dota190Buffer = new Buffer(4);
		dota206Buffer = new Buffer(3);
		csgoGameBuffer = new Buffer(5);
		csgoQBuffer = new Buffer(3);
		
		bolDotaCD = false;
		inGameArray = new boolean[kNUMBER_OF_GAMES];
		onlineArray = new boolean[kNUMBER_OF_GAMES];
		connectionsHandler = new ConnectionHandler();
		bolFirstTick = true;
	}
	
	public void triggerDotaCD() {
		DotaCDHandler handler = new DotaCDHandler();
		if (dotaCDTimer != null) {
			if (dotaCDTimer.isRunning()) {
				dotaCDTimer.restart();
				bolDotaCD = true;
				return;
			} 
		} 
		
		dotaCDTimer = new Timer(dotaCDMillis, handler);
		dotaCDTimer.start();
		bolDotaCD = true;
		
	}
	
	public void triggerCSGOCD() {
		csgoCDHandler handler = new csgoCDHandler();
		if (csgoCDTimer != null) {
			if (csgoCDTimer.isRunning()) {
				csgoCDTimer.restart();
				bolCSGOCD = true;
				return;
			} 
		} 
		
		csgoCDTimer = new Timer(csgoCDMillis, handler);
		csgoCDTimer.start();
		bolCSGOCD = true;
		
	}
	
	public void startTimeTimer() {
		TimeTimerHandler handler = new TimeTimerHandler();
		if (timeTimer != null) {
			if (timeTimer.isRunning()) {
				timeTimer.restart();
				return;
			} 
		} 
		
		timeTimer = new Timer(timeSpeedMillis, handler);
		timeTimer.start();
	}
	
	public void stopTimeTimer() {
		timeTimer.stop();
	}
	
	public void startQuickTimer() {
		QuickTimerHandler handler = new QuickTimerHandler();
		PCapThread.dotaCPack = 0;
		PCapThread.dotaQPack = 0;
		PCapThread.honQPack = 0;
		bolFirstTick = true;
		if (quickTimer != null) {
			if (quickTimer.isRunning()) {
				quickTimer.restart();
				return;
			} 
		} 
		
		quickTimer = new Timer(quickSpeedMillis, handler);
		quickTimer.start();
	}
	
	public void stopQuickTimer() {
		if (quickTimer != null) {
			quickTimer.stop();
		}
	}
	
	public class QuickTimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {


			System.out.println("tick");
		    
			List<String> processes = listRunningProcesses();
		      String result = "";

		      // display the result 
		      Iterator<String> it = processes.iterator();
		      int i = 0;
		      while (it.hasNext()) {
		         result += it.next() +",";
		         i++;
		         if (i==10) {
		             result += "\n";
		             i = 0;
		         }
		      }
		      //----
		    System.out.println("<<<processes checked>>>");
		    
		    //-------------------check processes------------------
		    boolean honRunning = false;
		    boolean dotaRunning = false;
		    boolean csgoRunning = false;
		    if (!result.contains("hon.exe")) {
		        honRunning = false;
		        //NSLog(@"hon not running, false alarm");
		    } else {
		        honRunning = true;
		        //NSLog(@"hon running, probably true alarm");
		    }
		    if (!result.contains("dota.exe")) {
		        dotaRunning = false;
		        //NSLog(@"dota not running, false alarm");
		    } else {
		        dotaRunning = true;
		        //NSLog(@"dota running, probably true alarm");
		    }
		    if (!result.contains("csgo.exe")) {
		        csgoRunning = false;
		        //NSLog(@"csgo not running, false alarm");
		    } else {
		        csgoRunning = true;
		        //NSLog(@"csgo running, probably true alarm");
		    }


		    dotaQBuffer.increment(PCapThread.dotaQPack);
		    dotaCBuffer.increment(PCapThread.dotaCPack);
		    honQBuffer.increment(PCapThread.honQPack);
		    dota174Buffer.increment(PCapThread.dota174Pack);
		    dota190Buffer.increment(PCapThread.dota190Pack);
		    dota206Buffer.increment(PCapThread.dota206Pack);
		    csgoQBuffer.increment(PCapThread.csgoQPack);
		    csgoGameBuffer.increment(PCapThread.csgoGamePack);
		    
		    
		    PCapThread.dotaCPack = 0;
		    PCapThread.dotaQPack = 0;
		    PCapThread.honQPack = 0;
		    PCapThread.dota174Pack = 0;
		    PCapThread.dota190Pack = 0;
		    PCapThread.dota206Pack = 0;
		    PCapThread.csgoQPack = 0;
		    PCapThread.csgoGamePack = 0;
		    
		    int honQ = honQBuffer.bufferValue();
		    int dotaQ = dotaQBuffer.bufferValue();
		    int dotaC = dotaCBuffer.bufferValue();
		    int dota174 = dota174Buffer.bufferValue();
		    int dota190 = dota190Buffer.bufferValue();
		    int dota206 = dota206Buffer.bufferValue();
		    int csgoQ = csgoQBuffer.bufferValue();
		    int csgoGame = csgoGameBuffer.bufferValue();
		    
		    
		    // ---------------- HON handler ----------------------
		    //NSLog(@"HoN");
		    if (honQ > 1 && honRunning) {
		        // user is in game
		        inGame(kHEROES_OF_NEWERTH);
		    } else if (honRunning){
		        online(kHEROES_OF_NEWERTH);
		        //got no packets but it's on
		    } else {
		        // user is not in game
		    	offline(kHEROES_OF_NEWERTH);
		    }
		   
		    // -------------- HON handler end --------------------
		    
		    
		    // ---------------- DOTA handler ----------------------
		    
		    
		    
		    if ((dotaQ > 1 && dotaRunning) || (dotaRunning && dota174 > 0 && dota190 > 0 && dota206 > 0 && ((dota206 + dota190) >= 4))) {
		        inGame(kDOTA2); //potentially sends notification
		    }
		    if (dotaC > 1 && dotaRunning) {
		    	bolFirstTick = true;
		        // user is in game
		        triggerDotaCD(); //tricks the app in to not sending a notification
		                          // this is not the queue pop, but the fact of being in a game
		        inGame(kDOTA2);
		    } else if (dotaRunning){
		    	online(kDOTA2);
		        
		    } else {
		        // user is not in game
		    	offline(kDOTA2);
		    }
		    // -------------- DOTA handler end --------------------
		    
		    // ---------------- CSGO handler ----------------------
		    
		    
		    
		    if (csgoQ == 2 && csgoRunning) {
		        inGame(kCS_GO); //potentially sends notification
		    }
		    if (csgoGame > 40 && csgoRunning) {
		    	
		        // user is in game
		        triggerCSGOCD(); 
		        inGame(kCS_GO);
		    } else if (csgoRunning){
		    	online(kCS_GO);
		        
		    } else {
		        // user is not in game
		    	offline(kCS_GO);
		    }
		    // -------------- CSGO handler end --------------------
		    
		    
		    
		    
		    
		    
		    bolFirstTick = false;
		    //NSLog(@"");
			
		}
		
	}
	
	public class DotaCDHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			dotaCDTimer.stop();
			bolDotaCD = false;
			
		}
		
	}
	
	public class csgoCDHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			csgoCDTimer.stop();
			bolCSGOCD = false;
			
		}
		
	}
	
	public class TimeTimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			connectionsHandler.postTimeUpdate(DataModel.getToken());
			
		}
		
	}
	
	
	public void offline(int game) {
		boolean bolOnline = onlineArray[game];
		onlineArray[game] = false;
		inGameArray[game] = false;
		for (boolean bol : inGameArray) {
			if (bol == true) {
				return;
			}
		}
		for (boolean bol : onlineArray) {
			if (bol == true) {
				return;
			}
		}
		if (!bolOnline) {
			// do nothing if status was already offline, (initialized to offline)
		} else {
			connectionsHandler.postStatusUpdate(game, kOFFLINE, DataModel.getToken());
		}
	}
	
	public void online(int game) {
		if (bolDotaCD && game == kDOTA2) {
			return;
		} else if (bolCSGOCD && game == kCS_GO) {
			return;
		}
		boolean bolWasInGame = inGameArray[game];
		inGameArray[game] = false;
		for (boolean bol : inGameArray) {
			if (bol == true) {
				return;
			}
		}
		if (onlineArray[game] && !bolWasInGame) {
			// do nothing if status already online, (initialized to offline)
		} else {
			connectionsHandler.postStatusUpdate(game, kONLINE, DataModel.getToken());
		}
		onlineArray[game] = true;
	}

	public void inGame(int game) {
		if (bolFirstTick && !bolDotaCD) {
	    	connectionsHandler.postStatusUpdate(game, kINGAME, DataModel.getToken());
		} else if (inGameArray[game]){
			//do nothing
		} else if (!inGameArray[game]) {
			connectionsHandler.postPush(game, DataModel.getToken(), DataModel.getEmail());
			System.out.println("pushing");
		}
		inGameArray[game] = true;
		onlineArray[game] = true;
		triggerDotaCD();
	}
	
	public static List<String> listRunningProcesses() {
	    List<String> processes = new ArrayList<String>();
	    try {
	      String line;
	      Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
	      BufferedReader input = new BufferedReader
	          (new InputStreamReader(p.getInputStream()));
	      while ((line = input.readLine()) != null) {
	          if (!line.trim().equals("")) {
	              // keep only the process name
	              processes.add(line);
	          }

	      }
	      input.close();
	    }
	    catch (Exception err) {
	      err.printStackTrace();
	    }
	    return processes;
	  }
		
		
}
	
	
	
	
	
	
	
	
