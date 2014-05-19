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
	
	public static TrayIcon trayIcon;
	public static MenuItem logItem;
    public static MenuItem toggleItem;
    public static WindowHandler windowHandler;
    public static PacketHandler packetHandler;
    
    
    
	public static void main(String args[]) {
		connectionsHandler = new ConnectionHandler();
		dataHandler = new DataModel();
		// setup agent program
		trayIcon = null;
		if (SystemTray.isSupported()) {
			final SystemTray tray = SystemTray.getSystemTray();
			Image trayImage = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/Qwhite.png")); 
			
			ActionListener logListener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 System.out.println("pressed login/logout");
	                 if (dataHandler.getBolIsLoggedIn()) {
	                	 connectionsHandler.postLogout();
	                	 windowHandler = new WindowHandler();
	                	 windowHandler.setupWindow();
	                	 setDisconnected();
	                 } else {
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
		                 if (dataHandler.getBolIsLoggedIn()) {
		                	 toggle();
		                 } else {
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
	            	 if (dataHandler.getBolIsLoggedIn()) {
	            		 connectionsHandler.postLogout();
	            	 }
	            	 try {
						dataHandler.save();
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
	         
	         
	         PopupMenu popup = new PopupMenu(); 
	         
	         logItem = new MenuItem();
	         toggleItem = new MenuItem();
	         MenuItem quitItem = new MenuItem();
	         
	         logItem.addActionListener(logListener);
	         toggleItem.addActionListener(toggleListener);
	         quitItem.addActionListener(quitListener);
	         
	         toggleItem.setLabel("Resume queue monitoring");
	         logItem.setLabel("Sign in / Sign up");
	         quitItem.setLabel("Quit");
	         
	         toggleItem.setEnabled(false);
	         
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
		//TODO
	}
	
	/**
	 * updates datamodel for new state
	 * updates menu items
	 * toggles tracking off
	 */
	public static void setDisconnected() {
		//TODO
	}
	
	/**
	 * toggles tracking off
	 * posts logout via connectionsHandler
	 */
	public static void logout() {
		//TODO
	}
	
	/**
	 * Force the logout locally,
	 * fuck the server, it will handle the
	 * connection loss in <1min
	 */
	public static void forceLogout() {
		
	}
	
	/**
	 * attempts to login via connectionsHandler
	 */
	public static void attemptLogin() {
		//TODO
	}
	/**
	 * attempts to register via connectionsHandler
	 */
	public static void attemptRegister() {
		//TODO
	}
	/**
	 * toggles the pCap tracking on /off
	 */
	private static void toggle() {
		//TODO
	}
	/**
	 * toggles the pCap tracking on
	 */
	private static void toggleOn() {
		//TODO
	}
	/**
	 * toggles the pCap tracking off
	 */
	private static void toggleOff() {
		//TODO
	}
	
	private static void alert(String message) {
		JFrame frame = new JFrame("GameQ - Alert");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, message);
	}
}