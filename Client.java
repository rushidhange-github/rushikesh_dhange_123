import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {
    Socket socket;

    BufferedReader br;  //read the data
    PrintWriter out;    //writing the data

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20 );

    public Client(){
        

        
        try {
            
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1",7778);  //Request automatically send hoil
            System.out.println("Connection done!");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //reading data from br
            out = new PrintWriter(socket.getOutputStream());    //writing data form out
            createGUI();
            handleEvents();
            startReading();
            // startWriting();   //writing() --> code is for only console

        } catch (Exception e) {

        }
    }
    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Action perform when we hit the ENTER key
                // System.out.println("Key Released "+ e.getKeyCode());  // TO GET a key value
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed Enter");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Client : "+ contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

        });

    }
    private void createGUI() {
        
        //GUI Code...

        this.setTitle("Client Messanger[END]");
        this.setSize(500,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        //Code for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("1.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame Layout

        this.setLayout(new BorderLayout());

        //Adding the Components
        this.add(heading,BorderLayout.NORTH);

        JScrollPane jScrollPane = new JScrollPane(messageArea);  // Scroll Bar 

        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    public void startReading(){
        //thread- read karun data deil
        Runnable r1 =()->{
            System.out.println("Reader started..");
            try {
            while(true){
            
            String msg;
            
               msg = br.readLine();
               if (msg.equals("Exit"))
                {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat..!");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                    // System.out.println("Rushi : "+ msg);

                    messageArea.append("Client : "+ msg+"\n");
            } 
                
            }catch (IOException e) {
                // e.printStackTrace();
           
                System.out.println("Connection Closed");

            }
        };
        new Thread(r1).start();     //Thread Starting
    }
    public void startWriting(){
        // thread- data user kadun gheun client kade send karen
        Runnable r2 =()->{
            System.out.println("Writer started..");
            try {
            
            while(!socket.isClosed()){

                    BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("Exit")){
                        socket.close();
                        break;
                    }
                }
                System.out.println("Connection Closed"); 

            }catch (Exception e) {

                e.printStackTrace();            
            }

        };
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("This is Client....!");
        new Client(); 
    }
}
