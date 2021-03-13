
/**
 * ***
 **
 ** USCA ACSC415 *
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

class WebServerHint {

    public static void main(String argv[]) throws Exception {
        String clientSentence = " ";
        String serverSentence = " ";
        ServerSocket welcomeSocket=null;

        // Create Server Socket
        try {
            welcomeSocket= new ServerSocket(80);

            while (true) {
                // create client socket, blocking if no incoming request.
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Accept connection from" + connectionSocket.getRemoteSocketAddress());

                // input stream
                Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
 
                // ouput stream
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // get the message from client 
                // HTTP Request   -----   GET /main.html HTTP/1.1              
                clientSentence =inFromClient.nextLine();
                System.out.println("From Client:" + clientSentence);

                //Tokenize this line, check whether it is a valid request.
                String[] temp = clientSentence.split(" ");
                System.out.println("Http Request Method:" + temp[0]);
                System.out.println("File path: "+ temp[1]);
                
                // Get the file if the request is valid
                                if (!temp[0].contains("GET")){
                                    break;
                                }
                
                // You need to ignore the request for favicon.ico.
                              else if (temp[1].equals("/favicon.ico")){
                                    temp[1]=" index.html";
                                }
               // else{
                // Otherwise get the name of the file -- temp[1] and send the file requested by client
                // I will show an example which opens a pdf file.
                try{
                    Path inFilePath = Paths.get(temp[1].substring(1));  // current directory
    
                    // read file into byte arrays (all in memory!!)
                    byte[] buffer = Files.readAllBytes(inFilePath);   // read bytes to a buffer

                     // construct the response message and send it to client
                    String requestline = "HTTP/1.1 200 OK\r\n";
                    String headlines1="";
                    String headlines2="";
                    if(temp[1].contains(".pdf")){
                        headlines1= "Content-Type: application/pdf\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".html")){
                        headlines1= "Content-Type: text/html\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".txt")){
                        headlines1= "Content-Type: text/plain\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".jpg")){
                        headlines1= "Content-Type: image/jpg\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".png")){
                        headlines1= "Content-Type: image/png\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".bmp")){
                        headlines1= "Content-Type: image/bmp\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    if(temp[1].contains(".gif")){
                        headlines1= "Content-Type: image/gif\r\n";
                        headlines2= "Content-Length:"+buffer.length+"\r\n\r\n";
                    }
                    

                    // send the requestline and headlines 
                    outToClient.writeBytes(requestline+headlines1+headlines2);

                    // send the entity body  
                    outToClient.write(buffer,0,buffer.length);
                    outToClient.flush();

                }catch(Exception e) {
                    // send 404 request if the file is not found 
                    // more codes need to be added here. 
                    Path inFilePath = Paths.get("404.html");  // current directory
    
                    // read file into byte arrays (all in memory!!)
                    byte[] buffer = Files.readAllBytes(inFilePath);
                    String requestline = "HTTP/1.1 404 Not Found\r\n";
                    String headlines1="Content-Type: text/html\r\n";
                    String headlines2="Content-Length: "+buffer.length+"\r\n\r\n";
                    outToClient.writeBytes(requestline+headlines1+headlines2);
                    System.out.println(requestline+headlines1+headlines2);
                    outToClient.write(buffer,0,buffer.length);
                    outToClient.flush();
                    


                    System.err.println( e.getMessage());
                }

                // close stream and socket
                inFromClient.close();
                outToClient.close();
                connectionSocket.close();

            }
        }catch (IOException e) {
            System.err.println("Caught Exception " + e.getMessage());
        }finally{
            if (welcomeSocket !=null ) welcomeSocket.close();  // no need in java 7 above
        }

    }
}
