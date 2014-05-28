import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Main {
	
	public static ConnectionHandler connectionsHandler;
	public static DataModel dataHandler;
	
	public static boolean bolRegging;
	public static boolean bolGettingQuestion;
	public static boolean bolAskingQuestion;
	public static PopupMenu popup;
	public static TrayIcon trayIcon;
	public static MenuItem logItem;
    public static MenuItem toggleItem;
    public static MenuItem labelItem;
    public static MenuItem quitItem;
    public static WindowHandler windowHandler;
    public static PacketHandler packetHandler;
    public static TimeHandler timeHandler;
    public static Encryptor enc;
    private static boolean bolIsToggledOn;
    
    
    
	public static void main(String args[]) {
		enc = new Encryptor();
		connectionsHandler = new ConnectionHandler();
		timeHandler = new TimeHandler();
		dataHandler = new DataModel();
		windowHandler = new WindowHandler();
		// setup agent program
		trayIcon = null;
		if (SystemTray.isSupported()) {
			final SystemTray tray = SystemTray.getSystemTray();
			Image trayImage = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/silverG.png")); 
			
			ActionListener logListener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 System.out.println("pressed login/logout");
	                 if (DataModel.getBolIsLoggedIn()) {
	                	 connectionsHandler.postLogout();
	                 } else {
	                	 if (windowHandler == null) {
	                		 windowHandler = new WindowHandler();
	                	 }
	                	 windowHandler.setupWindow();
	                 }
	             }
	         };
	         
	    
	         
	         ActionListener toggleListener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 System.out.println("pressed toggle");
	                 toggle();
	             }
	         };
	         
	         MouseListener iconListener = new MouseListener() {
	             

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						 System.out.println("pressed icon");
		                 if (DataModel.getBolIsLoggedIn()) {
		                	 toggle();
		                 } else {
		                	 if (windowHandler == null) {
		                		 windowHandler = new WindowHandler();
		                	 }
		                	 windowHandler.setupWindow();
		                 }
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					//required inherited method
				}

				@Override
				public void mouseExited(MouseEvent e) {
					//required inherited method
				}

				@Override
				public void mousePressed(MouseEvent e) {
					//required inherited method
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					//required inherited method
				}
	         };
	         
	         ActionListener quitListener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 System.out.println("pressed quit");
	            	 try {
						DataModel.save();
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						tray.remove(trayIcon);
						System.exit(0);
					}
	             }
	         };	 
	         
	         /*
	         ActionListener listener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	                 
	             }
	         };*/
	         
	         
	         popup = new PopupMenu(); 
	         
	         logItem = new MenuItem();
	         toggleItem = new MenuItem();
	         quitItem = new MenuItem();
	         labelItem = new MenuItem();
	         
	         logItem.addActionListener(logListener);
	         toggleItem.addActionListener(toggleListener);
	         quitItem.addActionListener(quitListener);
	         
	         
	         toggleItem.setLabel("Resume queue monitoring");
	         logItem.setLabel("Sign in / Sign up");
	         quitItem.setLabel("Quit");
	         
	         toggleItem.setEnabled(false);
	         labelItem.setEnabled(false);
	         
	         popup.add(toggleItem);
	         popup.add(logItem);
	         popup.add(quitItem);
	         
	         
	         
	         
	         
	         // construct a TrayIcon
	         trayIcon = new TrayIcon(trayImage, "GameQ", popup);
	         // set the TrayIcon properties
	         trayIcon.addMouseListener(iconListener);
	         // ...
	         // add the tray image
	         try {
	             tray.add(trayIcon);
	         } catch (AWTException e) {
	             System.err.println(e);
	         }
	         // setup pCap
	         System.out.println("starting pCap");
	         packetHandler = new PacketHandler();
	         System.out.println("started pCap");
	         System.out.println(DataModel.getEmail() + "<----<--<--<---<--");
	         System.out.println(DataModel.getPassword() + "<----<--<--<---<--");
	         System.out.println(DataModel.getBolIsLoggedIn() + "<----<--<--<---<--");
	         if (DataModel.getBolIsLoggedIn() &&  !DataModel.getEmail().isEmpty() && !DataModel.getPassword().isEmpty()) {
	        	 setAlreadyConnected();
	         } else {
	        	 setDisconnected();
	         }
	         System.out.println("main() end");
		} else {
			alert("Access was denied to the sytem tray or it doesn't exist! GameQ could not run correctly.");
			System.exit(0);
		}
		
	}
	
	
	/**
	 * updates datamodel for new state
	 * updates menu items
	 * toggles tracking on
	 */
	public static void setConnected() {
		System.out.println(WindowHandler.mLine1);
		DataModel.setEmail(WindowHandler.mLine1);
		System.out.println(WindowHandler.mLine2);
		DataModel.setPassword(WindowHandler.mLine2);
		DataModel.setBolIsLoggedIn(true);
		//DataModel.setBolIsRegisteredForNotifications(true);
		//DataModel.setToken(token);
		toggleOn();
		toggleItem.setEnabled(true);
		logItem.setLabel("Logout");
		labelItem.setLabel(DataModel.getEmail());
		System.out.println(DataModel.getEmail() + "<-------------");
		popup.insert(labelItem, 0);
		connectionsHandler.postToken(DataModel.getToken(), DataModel.getEmail());
		WindowHandler.frame.setVisible(false);
		
	}
	/**
	 * does the same as setConnected but skips setting the datasince it's already been done
	* updates menu items
	 * toggles tracking on
	 */
	public static void setAlreadyConnected() {
		toggleOn();
		toggleItem.setEnabled(true);
		logItem.setLabel("Logout");
		labelItem.setLabel(DataModel.getEmail());
		System.out.println(DataModel.getEmail() + "<-------------");
		popup.insert(labelItem, 0);
		connectionsHandler.postToken(DataModel.getToken(), DataModel.getEmail());
		
	}
	
	/**
	 * updates datamodel for new state
	 * updates menu items
	 * toggles tracking off
	 */
	public static void setDisconnected() {
		DataModel.setEmail("");
		DataModel.setPassword("");
		DataModel.setBolIsLoggedIn(false);
		//DataModel.setBolIsRegisteredForNotifications(true);
		//DataModel.setToken(token);
		toggleOff();
		toggleItem.setEnabled(false);
		popup.remove(labelItem);
		logItem.setLabel("Sign in / Sign up");
	}
	
	/**
	 * toggles tracking off
	 * posts logout via connectionsHandler
	 */
	public static void logout() {
		connectionsHandler.postLogout();
	}
	
	/**
	 * Force the logout locally,
	 * fuck the server, it will handle the
	 * connection loss in <1min
	 */
	public static void forceLogout() {
		setDisconnected();
	}
	
	/**
	 * attempts to login via connectionsHandler
	 */
	public static void attemptLogin() {
		System.out.println("pwddamnit: " + WindowHandler.mLine1);
		connectionsHandler.postLogin(WindowHandler.mLine1, WindowHandler.mLine2);
	}
	/**
	 * attempts to register via connectionsHandler
	 */
	public static void attemptRegister() {
		connectionsHandler.postRegister(WindowHandler.mLine1, "", "", 1, 0, "", WindowHandler.mLine2, WindowHandler.mLine3, WindowHandler.mLine4);
		WindowHandler.questionEmail = WindowHandler.mLine1;
	}
	/**
	 * attempts to get the secret question via connectionsHandler
	 */
	public static void attemptGetQuestion() {
		WindowHandler.questionEmail = WindowHandler.mLine1;
		connectionsHandler.postGetSecret(WindowHandler.mLine1);
	}
	/**
	 * attempts to validate secret via connectionsHandler
	 */
	public static void attemptAnswerQuestion() {
		connectionsHandler.postCheckSecret(WindowHandler.questionEmail, WindowHandler.mLine1, WindowHandler.question);
	}
	/**
	 * toggles the pCap tracking on /off
	 */
	private static void toggle() {
		if (bolIsToggledOn) {
			toggleOff();
		} else {
			toggleOn();
		}
		
	}
	/**
	 * toggles the pCap tracking on
	 */
	private static void toggleOn() {
		bolIsToggledOn = true;
		timeHandler.startQuickTimer();
		Image trayImage = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/redG.png")); 
		trayIcon.setImage(trayImage);
		toggleItem.setLabel("Pause Monitoring");
	}
	/**
	 * toggles the pCap tracking off
	 */
	private static void toggleOff() {
		bolIsToggledOn = false;
		timeHandler.stopQuickTimer();
		Image trayImage = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/silverG.png")); 
		trayIcon.setImage(trayImage);
		toggleItem.setLabel("Resume Monitoring");
	}
	
	public static void alert(String message) {
		JFrame frame = new JFrame("GameQ - Alert");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, message);
	}
}