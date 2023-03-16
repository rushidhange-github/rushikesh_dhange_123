import java.io.*;
import java.net.*;

class Server{
    
    ServerSocket server;
    Socket socket;

    BufferedReader br;  //read the data
    PrintWriter out;    //writing the data

    //Constructor..
    public Server(){
        try {
            server = new ServerSocket(7778);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //reading data from br
            out = new PrintWriter(socket.getOutputStream());    //writing data form out

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
        public void startReading(){
            //thread- read karun data deil
            Runnable r1 =()->{
                System.out.println("Reader started..");
                try{
                while(true){
                
                String msg;
                
                   msg = br.readLine();
                   if (msg.equals("Exit"))
                    {
                        System.out.println("Client terminated the chat");
                        
                        socket.close();

                        break;
                    }
                    System.out.println("Server : "+ msg);

                } 
            }
                
           catch (Exception e) {
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
                try{

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
                }catch (Exception e) {
                    // e.printStackTrace();

                    System.out.println("Connection Closed");

                }

                

            }; 
            new Thread(r2).start();
        }


    public static void main(String[] args) {
        System.out.println("This is server..going to start server..!");
        new Server();
    }
}