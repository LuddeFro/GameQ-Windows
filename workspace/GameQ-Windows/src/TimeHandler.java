import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.Timer;


public class TimeHandler {
	public Timer quickTimer;
	public Timer dotaCDTimer;
	private static final int quickSpeedMillis = 1000;
	private static final int dotaCDMillis = 5000;
	private Buffer dotaQBuffer;
	private Buffer dotaCBuffer;
	private Buffer honQBuffer;
	private boolean bolDotaCD; 
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
	
	public void startQuickTimer() {
		QuickTimerHandler handler = new QuickTimerHandler();
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
		quickTimer.stop();
	}
	
	public class QuickTimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {


			System.out.println("tick");
		    // get processes
			StringBuilder processesBuilder = new StringBuilder();
			 try {
		            String process;
		            // getRuntime: Returns the runtime object associated with the current Java application.
		            // exec: Executes the specified string command in a separate process.
		            Process p = Runtime.getRuntime().exec("ps -few");
		            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            while ((process = input.readLine()) != null) {
		                System.out.println(process);// <-- Print all Process here line
                        								// by line
		                processesBuilder.append(process); 
		            }
		            input.close();
			 } catch (Exception err) {
		            err.printStackTrace();
		     }
		    
		    String processes = processesBuilder.toString();
		    
		    System.out.println("<<<processes checked>>>");
		    
		    //-------------------check processes------------------
		    boolean honRunning = false;
		    boolean dotaRunning = false;
		    boolean csgoRunning = false;
		    if (!processes.contains("Heroes") || !processes.contains("Newerth")) {
		        honRunning = false;
		        //NSLog(@"hon not running, false alarm");
		    } else {
		        honRunning = true;
		        //NSLog(@"hon running, probably true alarm");
		    }
		    if (!processes.contains("dota")) {
		        dotaRunning = false;
		        //NSLog(@"dota not running, false alarm");
		    } else {
		        dotaRunning = true;
		        //NSLog(@"dota running, probably true alarm");
		    }
		    if (!processes.contains("csgo")) {
		        csgoRunning = false;
		        //NSLog(@"csgo not running, false alarm");
		    } else {
		        csgoRunning = true;
		        //NSLog(@"csgo running, probably true alarm");
		    }


		    dotaQBuffer.increment(PCapThread.dotaQPack);
		    dotaCBuffer.increment(PCapThread.dotaCPack);
		    honQBuffer.increment(PCapThread.honQPack);
		    
		    PCapThread.dotaCPack = 0;
		    PCapThread.dotaQPack = 0;
		    PCapThread.honQPack = 0;
		    
		    int honQ = honQBuffer.bufferValue();
		    int dotaQ = dotaQBuffer.bufferValue();
		    int dotaC = dotaCBuffer.bufferValue();
		    
		    
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
		    
		    
		    
		    if (dotaQ > 0 && dotaRunning) {
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
		
		
}
	
	
	
	
	
	
	
	
