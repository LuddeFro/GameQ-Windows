import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;



public class WindowHandler {


	public static final int frameWidth = 500;
	public static final int frameHeight = 300;
	
	public static final int imageSize = 256;
	
	public static final int middleXOffset = 10;
    public static final int fieldWidth = 200;
    public static final int fieldYOffset = 20;
    public static final int fieldHeight = 25;
    public static final int bonusFieldYOffset = fieldYOffset*2*3/4 + fieldHeight;
    public static final int bonusField2YOffset = (bonusFieldYOffset - fieldYOffset) + bonusFieldYOffset;
    public static final int bonusField3YOffset = (bonusFieldYOffset - fieldYOffset) + bonusField2YOffset;
    public static final int bonusField4YOffset = (bonusFieldYOffset - fieldYOffset) + bonusField3YOffset;
    public static final int buttonWidth = 95;
    public static final int buttonHeight = 25;
    public static final int buttonYOffset = fieldHeight*2 + fieldYOffset*3*3/4;
    
    public static JTextField txtEmail;
	public static JPasswordField txtPassword;
	public static JTextField txtSecretQ;
	public static JPasswordField txtSecret;
	
	public static JPasswordField txt1Secure;
	public static JTextField txt2Insecure;
	public static JPasswordField txt3Secure;
	public static JButton btnSetDeviceName;
	public static JButton btnSetPassword;
	public static JButton btnSetSecret;
	
	public static JButton btnRegging;
	public static JButton btnForgotten;
	public static JButton btnLogin;
	public static JLabel label;
	
	public static String mLine1;
	public static String mLine2;
	public static String mLine3;
	public static String mLine4;
	
	public static String question;
	public static String questionEmail;
	
	
	private TextPrompt promptEmail;
	private TextPrompt promptPassword;
	private TextPrompt promptSecretQ;
	private TextPrompt prompt1Secure;
	private TextPrompt prompt2Insecure;
	private TextPrompt prompt3Secure;
	
	public static JFrame frame;
	
    public WindowHandler() {
    	if (frame == null) {
			frame = new JFrame("GameQ");
		}
    	Color myWhite = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        Color myTransWhite = new Color(1.0f, 1.0f, 1.0f, 0.5f);
        Color myRed = new Color(0.905f, 0.298f, 0.235f, 1.0f);
        Color myDarkGray = new Color(0.1333f, 0.1333f, 0.1333f, 1.0f);
        Color cloudWhite = new Color(0.9255f, 0.9411f, 0.9450f, 1.0f);
    	
    	ActionListener loginListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mLine1 = txtEmail.getText();
        		mLine2 = new String(txtPassword.getPassword());
        		mLine3 = txtSecretQ.getText();
        		mLine4 = new String(txtSecret.getPassword());
            	if (Main.bolRegging) {
            		Main.attemptRegister();
            	} else if (Main.bolGettingQuestion) {
            		Main.attemptGetQuestion();
            	} else if (Main.bolAskingQuestion) {
            		Main.attemptAnswerQuestion();
            	} else if (Main.bolSettingSecret) {
            		mLine1 = new String(txt1Secure.getPassword());
            		mLine2 = txt2Insecure.getText();
            		mLine3 = new String(txt3Secure.getPassword());
            		Main.attemptSetSecret();
            	} else if (Main.bolSettingPass) {
            		mLine1 = new String(txt1Secure.getPassword());
            		Main.attemptSetPassword();
            	} else if (Main.bolSettingDeviceName) {
            		Main.attemptSetDeviceName();
            	} else {
            		Main.attemptLogin();
            	}
            }
        };
    	
    	ActionListener forgotListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Main.bolRegging || Main.bolAskingQuestion || Main.bolGettingQuestion) {
                	
                		setupLogin();
                	
                } else {
                	
                		txtSecret.setVisible(false);
                		txtSecretQ.setVisible(false);
                		txtPassword.setVisible(false);
                		btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusFieldYOffset, buttonWidth, buttonHeight);
                		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusFieldYOffset, buttonWidth, buttonHeight);
                		btnForgotten.setText("Cancel");
                		btnLogin.setText("OK");
                		promptEmail.setPlaceholderString("Email");
                		Main.bolGettingQuestion = true;
                		Main.bolAskingQuestion = false;
                		Main.bolRegging = false;
                	
                }
                
            }
        };
        
        ActionListener registerListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	txtSecret.setVisible(true);
        		txtSecretQ.setVisible(true);
        		txtPassword.setVisible(true);
        		btnRegging.setVisible(false);
        		btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField4YOffset, buttonWidth, buttonHeight);
        		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusField4YOffset, buttonWidth, buttonHeight);
        		btnForgotten.setText("Cancel");
        		btnLogin.setText("Sign Up");
        		promptEmail.setPlaceholderString("Email");
        		Main.bolGettingQuestion = false;
        		Main.bolAskingQuestion = false;
        		Main.bolRegging = true;
            }
        };
        
        ActionListener setPassListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setupSetPass();
            }
        };
        
        ActionListener setSecretListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setupSetSecret();
            }
        };
        
        ActionListener setDeviceNameListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setupSetDeviceName();
            }
        };
        
        
		
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource(Main.res + "redG32.png")));
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
		
		btnForgotten = new JButton();
		btnForgotten.setText("Forgot?");
		btnForgotten.addActionListener(forgotListener);
		
		btnRegging = new JButton();
		btnRegging.setText("Sign Up");
		btnRegging.addActionListener(registerListener);
		
		btnSetPassword = new JButton();
		btnSetPassword.setText("Change password");
		btnSetPassword.addActionListener(setPassListener);
		
		btnSetSecret = new JButton();
		btnSetSecret.setText("Change secret question");
		btnSetSecret.addActionListener(setSecretListener);
		
		btnSetDeviceName = new JButton();
		btnSetDeviceName.setText("Change device name");
		btnSetDeviceName.addActionListener(setDeviceNameListener);
		
		
		txtEmail = new JTextField(30);
		txtPassword = new JPasswordField(30);
		txtSecretQ = new JTextField(30);
		txtSecret = new JPasswordField(30);
		
		txt1Secure = new JPasswordField(30);
		txt2Insecure = new JTextField(30);
		txt3Secure = new JPasswordField(30);
		
		promptEmail = new TextPrompt("Email", txtEmail);
		promptEmail.changeAlpha(0.5f);
		promptPassword = new TextPrompt("Password", txtPassword);
		promptPassword.changeAlpha(0.5f);
		promptSecretQ = new TextPrompt("Secret Question/Hint", txtSecretQ);
		promptSecretQ.changeAlpha(0.5f);
		TextPrompt promptSecret = new TextPrompt("Secret Answer", txtSecret);
		promptSecret.changeAlpha(0.5f);
		prompt1Secure = new TextPrompt("Password", txt1Secure);
		prompt1Secure.changeAlpha(0.5f);
		prompt2Insecure = new TextPrompt("Secret Question/Hint", txt2Insecure);
		prompt2Insecure.changeAlpha(0.5f);
		prompt3Secure = new TextPrompt("Secret Answer", txt3Secure);
		prompt3Secure.changeAlpha(0.5f);
		
		frame.getContentPane().add(txtEmail);
		frame.getContentPane().add(txtPassword);
		frame.getContentPane().add(txtSecret);
		frame.getContentPane().add(txtSecretQ);
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(btnForgotten);
		frame.getContentPane().add(btnRegging);
		frame.getContentPane().add(btnSetPassword);
		frame.getContentPane().add(btnSetSecret);
		frame.getContentPane().add(btnSetDeviceName);
		frame.getContentPane().add(txt1Secure);
		frame.getContentPane().add(txt2Insecure);
		frame.getContentPane().add(txt3Secure);
		
		KeyListener keyListen = new KeyListener() {
        	@Override
            public void keyPressed(KeyEvent e) { 
            	if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            		System.out.println("pressed Enter");
            		mLine1 = txtEmail.getText();
            		mLine2 = new String(txtPassword.getPassword());
            		mLine3 = txtSecretQ.getText();
            		mLine4 = new String(txtSecret.getPassword());
                	if (Main.bolRegging) {
                		Main.attemptRegister();
                	} else if (Main.bolGettingQuestion) {
                		Main.attemptGetQuestion();
                	} else if (Main.bolAskingQuestion) {
                		Main.attemptAnswerQuestion();
                	} else if (Main.bolSettingSecret) {
                		mLine1 = new String(txt1Secure.getPassword());
                		mLine2 = txt2Insecure.getText();
                		mLine3 = new String(txt3Secure.getPassword());
                		Main.attemptSetSecret();
                	} else if (Main.bolSettingPass) {
                		mLine1 = new String(txt1Secure.getPassword());
                		Main.attemptSetPassword();
                	} else if (Main.bolSettingDeviceName) {
                		Main.attemptSetDeviceName();
                	} else {
                		Main.attemptLogin();
                	}
            		
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
        btnForgotten.addKeyListener(keyListen);
        btnRegging.addKeyListener(keyListen);
		txtSecretQ.addKeyListener(keyListen);
		txtSecret.addKeyListener(keyListen);
		txtEmail.addKeyListener(keyListen);
		txtPassword.addKeyListener(keyListen);
		btnSetDeviceName.addKeyListener(keyListen);
		btnSetSecret.addKeyListener(keyListen);
		btnSetPassword.addKeyListener(keyListen);
		txt3Secure.addKeyListener(keyListen);
		txt2Insecure.addKeyListener(keyListen);
		txt1Secure.addKeyListener(keyListen);
		
	
		txtEmail.setBounds(frameWidth/2-(fieldWidth+middleXOffset), fieldYOffset, fieldWidth, fieldHeight);
		txtPassword.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusFieldYOffset, fieldWidth, fieldHeight);
		txtSecretQ.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField2YOffset, fieldWidth, fieldHeight);
		txtSecret.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField3YOffset, fieldWidth, fieldHeight);
		
		txt1Secure.setBounds(txtEmail.getBounds());
		txt2Insecure.setBounds(txtPassword.getBounds());
		txt3Secure.setBounds(txtSecretQ.getBounds());
		
		
		btnLogin.setFocusPainted(false);
		btnRegging.setFocusPainted(false);
		btnForgotten.setFocusPainted(false);
		btnSetDeviceName.setFocusPainted(false);
		btnSetSecret.setFocusPainted(false);
		btnSetPassword.setFocusPainted(false);
		
		 
		
		try {
			BufferedImage image;
			image = ImageIO.read(Main.class.getResource(Main.res + "256black.png"));
			label = new JLabel(new ImageIcon(image));
	        frame.getContentPane().add(label);
	        label.setBounds((frameWidth/2 - imageSize)/2 + frameWidth/2  -middleXOffset/2, (frameHeight - imageSize)/2-middleXOffset*2, imageSize, imageSize);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		frame.getContentPane().setBackground(cloudWhite);
		/*
		btnForgotten.setBackground(myRed);
		btnForgotten.setBorder(BorderFactory.createEmptyBorder());
        btnForgotten.setFocusPainted(false);
        */
		
		
    		
    }
    
    public void setupSetPass() {
    	Main.bolSettingDeviceName = false;
    	Main.bolSettingSecret = false;
    	Main.bolSettingPass = true;
		
    	btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField2YOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusField2YOffset, buttonWidth, buttonHeight);
		
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		txtEmail.setVisible(false);
		txtPassword.setVisible(true);
		txt1Secure.setVisible(true);
		txt2Insecure.setVisible(false);
		txt3Secure.setVisible(false);
		btnLogin.setVisible(true);
		btnForgotten.setVisible(true);
		btnRegging.setVisible(false);
		btnSetPassword.setVisible(true);
		btnSetSecret.setVisible(true);
		btnSetDeviceName.setVisible(true);
		txt1Secure.setText("");
		txtPassword.setText("");
		promptPassword.setPlaceholderString("New password");
		prompt1Secure.setPlaceholderString("Old Password");
		
    }
    	
    public void setupSetSecret() {
    	Main.bolSettingDeviceName = false;
    	Main.bolSettingSecret = true;
    	Main.bolSettingPass = false;
		
    	btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField3YOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusField3YOffset, buttonWidth, buttonHeight);
		
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		txtEmail.setVisible(false);
		txtPassword.setVisible(false);
		txt1Secure.setVisible(true);
		txt2Insecure.setVisible(true);
		txt3Secure.setVisible(true);
		btnLogin.setVisible(true);
		btnForgotten.setVisible(true);
		btnRegging.setVisible(false);
		btnSetPassword.setVisible(true);
		btnSetSecret.setVisible(true);
		btnSetDeviceName.setVisible(true);
		txt1Secure.setText("");
		txt2Insecure.setText("");
		txt3Secure.setText("");
		prompt1Secure.setPlaceholderString("Password");
		
    }
    		
    public void setupSetDeviceName() {
    	Main.bolSettingDeviceName = true;
    	Main.bolSettingSecret = false;
    	Main.bolSettingPass = false;
		
    	btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusFieldYOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusFieldYOffset, buttonWidth, buttonHeight);
		
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		txtEmail.setVisible(true);
		txtPassword.setVisible(false);
		txt1Secure.setVisible(false);
		txt2Insecure.setVisible(false);
		txt3Secure.setVisible(false);
		btnLogin.setVisible(true);
		btnForgotten.setVisible(true);
		btnRegging.setVisible(false);
		btnSetPassword.setVisible(true);
		btnSetSecret.setVisible(true);
		btnSetDeviceName.setVisible(true);
		
		
		promptEmail.setPlaceholderString("Device name");
		txtEmail.setText(DataModel.getDeviceName());
		
    }
    
    public void setupSettings() {
    	Main.bolRegging = false;
    	Main.bolAskingQuestion = false;
    	Main.bolGettingQuestion = false;
    	Main.bolSettingDeviceName = false;
    	Main.bolSettingSecret = false;
    	Main.bolSettingPass = false;
		
    	btnSetSecret.setBounds(frameWidth/2-(fieldWidth+middleXOffset), frameHeight-buttonHeight*3, fieldWidth, buttonHeight);
		btnSetPassword.setBounds(frameWidth/2-(fieldWidth+middleXOffset), frameHeight-buttonHeight*4-5, fieldWidth, buttonHeight);
		btnSetDeviceName.setBounds(frameWidth/2-(fieldWidth+middleXOffset), frameHeight-buttonHeight*5-10, fieldWidth, buttonHeight);
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		txtEmail.setVisible(false);
		txtPassword.setVisible(false);
		txt1Secure.setVisible(false);
		txt2Insecure.setVisible(false);
		txt3Secure.setVisible(false);
		btnLogin.setVisible(false);
		btnForgotten.setVisible(false);
		btnRegging.setVisible(false);
		btnSetPassword.setVisible(true);
		btnSetSecret.setVisible(true);
		btnSetDeviceName.setVisible(true);
		
		frame.setVisible(true);
    }
    
    public void setupAnswer(String question) {
    	this.question = question;
    	txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		btnRegging.setVisible(true);
		txtPassword.setVisible(false);
		
		btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusFieldYOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusFieldYOffset, buttonWidth, buttonHeight);
		btnForgotten.setText("Cancel");
		
		btnLogin.setText("Answer");
		promptEmail.setPlaceholderString(question);
		Main.bolGettingQuestion = false;
		Main.bolAskingQuestion = true;
		Main.bolRegging = false;
		txtEmail.setText("");
		
    }
    
    
    public void setupLogin() {
    	txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		btnRegging.setVisible(true);
		txtPassword.setVisible(true);
		btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField2YOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusField2YOffset, buttonWidth, buttonHeight);
		btnForgotten.setText("Forgot?");
		btnLogin.setText("Sign In");
		txtEmail.setText("");
		promptEmail.setPlaceholderString("Email");
		Main.bolAskingQuestion = false;
		Main.bolGettingQuestion = false;
		Main.bolRegging = false;
		txtSecretQ.setText("");
		txtSecret.setText("");
		txtPassword.setText("");
		txtEmail.setText(questionEmail);
    }
    
    public void disableAll() {
    	txtEmail.setEnabled(false);
    	txtPassword.setEnabled(false);
    	txtSecret.setEnabled(false);
    	txtSecretQ.setEnabled(false);
    	btnRegging.setEnabled(false);
    	btnLogin.setEnabled(false);
    	btnForgotten.setEnabled(false);
    }
    
    public void enableAll() {
    	txtEmail.setEnabled(true);
    	txtPassword.setEnabled(true);
    	txtSecret.setEnabled(true);
    	txtSecretQ.setEnabled(true);
    	btnRegging.setEnabled(true);
    	btnLogin.setEnabled(true);
    	btnForgotten.setEnabled(true);
    }
    
    public void setupWindow() {
    	Main.bolRegging = false;
    	Main.bolAskingQuestion = false;
    	Main.bolGettingQuestion = false;
    	Main.bolSettingDeviceName = false;
    	Main.bolSettingSecret = false;
    	Main.bolSettingPass = false;
		
    	btnForgotten.setBounds(frameWidth/2-(fieldWidth+middleXOffset), bonusField2YOffset, buttonWidth, buttonHeight);
		btnLogin.setBounds(frameWidth/2-(fieldWidth+middleXOffset) + (fieldWidth-buttonWidth*2) + buttonWidth, bonusField2YOffset, buttonWidth, buttonHeight);
		btnRegging.setBounds(frameWidth/2-(fieldWidth+middleXOffset), frameHeight-buttonHeight*3, fieldWidth, buttonHeight);
		
		txtSecret.setVisible(false);
		txtSecretQ.setVisible(false);
		txtEmail.setVisible(true);
		txtPassword.setVisible(true);
		txt1Secure.setVisible(false);
		txt2Insecure.setVisible(false);
		txt3Secure.setVisible(false);
		btnLogin.setVisible(true);
		btnForgotten.setVisible(true);
		btnRegging.setVisible(true);
		btnSetPassword.setVisible(false);
		btnSetSecret.setVisible(false);
		btnSetDeviceName.setVisible(false);
		txtEmail.setText(DataModel.getEmail());
		txtPassword.setText("");
		promptEmail.setPlaceholderString("Email");
		promptPassword.setPlaceholderString("Password");
		promptSecretQ.setPlaceholderString("Secret Question/Hint");
		frame.setVisible(true);
    }
    
    
    
}
