import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;



public class WindowHandler {


	public static final int frameWidth = 500;
	public static final int frameHeight = 300;
	
	
	public static final int middleXOffset = 10;
    public static final int fieldWidth = 200;
    public static final int fieldYOffset = 20;
    public static final int fieldHeight = 25;
    public static final int bonusFieldYOffset = fieldYOffset*2*3/4 + fieldHeight;
    public static final int buttonWidth = 120;
    public static final int buttonHeight = 30;
    public static final int buttonYOffset = fieldHeight*2 + fieldYOffset*3*3/4;
    
    public static JTextField txtEmail;
	public static JTextField txtPassword;
	public static JTextField txtSecretQ;
	public static JTextField txtSecret;
	public static JButton btnRegging;
	public static JButton btnLogin;
	
	public static JFrame frame;
	
    public WindowHandler() {
    	if (frame == null) {
			frame = new JFrame("GameQ");
		}
    	
    	
    	ActionListener loginListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.attemptLogin();
            }
        };
    	
    	ActionListener registerListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Main.bolRegging) {
                	if (txtSecret == null || txtSecretQ == null) {
                		return;
                	} else {
                		txtSecret.setVisible(false);
                		txtSecretQ.setVisible(false);
                		btnRegging.setBounds(frameWidth/2-(fieldWidth/2+middleXOffset)-buttonWidth/2, bonusFieldYOffset, buttonWidth, buttonHeight);
                		btnLogin.setBounds(frameWidth/2 + middleXOffset +(fieldWidth/2 - buttonWidth/2), bonusFieldYOffset, buttonWidth, buttonHeight);
                		btnRegging.setText("Join GameQ");
                		btnLogin.setText("Sign In");
                		
                	}
                } else {
                	if (txtSecret == null || txtSecretQ == null) {
                		return;
                	} else {
                		txtSecret.setVisible(true);
                		txtSecretQ.setVisible(true);
                		btnRegging.setBounds(frameWidth/2-(fieldWidth/2+middleXOffset)-buttonWidth/2, buttonYOffset, buttonWidth, buttonHeight);
                		btnLogin.setBounds(frameWidth/2 + middleXOffset +(fieldWidth/2 - buttonWidth/2), buttonYOffset, buttonWidth, buttonHeight);
                		btnRegging.setText("Cancel");
                		btnLogin.setText("Join Now");
                	}
                }
                Main.bolRegging = !Main.bolRegging;
            }
        };
        
        
		
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/Qwhite.png")));
        frame.setResizable(false);
        frame.setSize(frameWidth, frameHeight);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frameWidth/2, dim.height/2-frameHeight/2);
		frame.setLayout(null);
		
		// add components
		// frame.getContentPane().add(empyLabel, BorderLayout.CENTER);
		
		btnLogin = new JButton();
		btnLogin.setText("Sign In");
		btnLogin.addActionListener(loginListener);
		
		btnRegging = new JButton();
		btnRegging.setText("Join GameQ");
		btnRegging.addActionListener(registerListener);
		
		txtEmail = new JTextField(30);
		txtPassword = new JTextField(30);
		txtSecretQ = new JTextField(30);
		txtSecret = new JTextField(30);
		
		TextPrompt promptEmail = new TextPrompt("Email", txtEmail);
		promptEmail.changeAlpha(0.5f);
		TextPrompt promptPassword = new TextPrompt("Password", txtPassword);
		promptPassword.changeAlpha(0.5f);
		TextPrompt promptSecretQ = new TextPrompt("Secret Question/Hint", txtSecretQ);
		promptSecretQ.changeAlpha(0.5f);
		TextPrompt promptSecret = new TextPrompt("Secret Answer", txtSecret);
		promptSecret.changeAlpha(0.5f);
		
		frame.getContentPane().add(txtEmail);
		frame.getContentPane().add(txtPassword);
		frame.getContentPane().add(txtSecret);
		frame.getContentPane().add(txtSecretQ);
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(btnRegging);
		
		KeyListener keyListen = new KeyListener() {
        	@Override
            public void keyPressed(KeyEvent e) { 
            	if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            		System.out.println("pressed Enter");
            		Main.attemptLogin();
            	}
            }

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
        };
        frame.addKeyListener(keyListen);
        
        btnLogin.addKeyListener(keyListen);
		btnRegging.addKeyListener(keyListen);
		txtSecretQ.addKeyListener(keyListen);
		txtSecret.addKeyListener(keyListen);
		txtEmail.addKeyListener(keyListen);
		txtPassword.addKeyListener(keyListen);
		
	
		txtEmail.setBounds(frameWidth/2-(fieldWidth+middleXOffset), fieldYOffset, fieldWidth, fieldHeight);
		txtPassword.setBounds(frameWidth/2+middleXOffset, fieldYOffset, fieldWidth, fieldHeight);
		txtSecret.setBounds(frameWidth/2+middleXOffset, bonusFieldYOffset, fieldWidth, fieldHeight);
		txtSecretQ.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusFieldYOffset, fieldWidth, fieldHeight);
    		
    }
    
    
    
    
    
    
    
    public void setupWindow() {
    	Main.bolRegging = false;
		
		btnRegging.setBounds(frameWidth/2-(fieldWidth/2+middleXOffset)-buttonWidth/2, bonusFieldYOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2 + middleXOffset +(fieldWidth/2 - buttonWidth/2), bonusFieldYOffset, buttonWidth, buttonHeight);
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		
		frame.setVisible(true);
    }
    
    
    
}
