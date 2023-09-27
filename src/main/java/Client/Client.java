package Client;

import java.util.*;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import javax.net.ssl.*;

import Server.Server;

public class Client {
    private InetAddress host;
    private int port;
    // This is not a reserved port number 
    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "Lab3keystore.ks";
    static final String TRUSTSTORE = "Lab3truststore.ks";
    static final String KEYSTOREPASS = "margherita";
    static final String TRUSTSTOREPASS = "margherita";
    SSLSocket socket;
    
    public Client(InetAddress host, int port) {
	this.host = host;
	this.port = port;
    }
    
    public void connect_to_server(){
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load( new FileInputStream("clientfiles/" + KEYSTORE ), KEYSTOREPASS.toCharArray() );
			
            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load( new FileInputStream( "clientfiles/" + TRUSTSTORE ), TRUSTSTOREPASS.toCharArray() );
			
            KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
            kmf.init( ks, KEYSTOREPASS.toCharArray() );
			
            TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
            tmf.init( ts );
			
            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
            SSLSocketFactory sslFact = sslContext.getSocketFactory();      	
            socket = (SSLSocket)sslFact.createSocket(host, port);
            socket.setEnabledCipherSuites( socket.getSupportedCipherSuites() );
            System.out.println("\n>>>> SSL/TLS handshake completed");
        }
        catch( Exception x ) {
            x.printStackTrace();
	}
    }
    
    private String readFile(String file) {
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return content.toString();	
    }
    
    public void upload(String file){
        String textfile = readFile("clientfiles/" + file);
        System.out.println("Uploading the text: " + textfile);
        
        try{
            PrintWriter socketOut = new PrintWriter( socket.getOutputStream(), true );
            socketOut.println( textfile );
            
            System.out.println( ">>>> Sending textfile to SecureAdditionServer" );
        }
        catch( Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        Client the_client = null;
        try{
            InetAddress host_address = InetAddress.getLocalHost();
            the_client = new Client(host_address, DEFAULT_PORT);
            the_client.connect_to_server();
        }
        catch( Exception ex){
            ex.printStackTrace();
        }
        
        while(true){
            System.out.println("========================");
            System.out.println("===FILE TRANSFER MENU===");
            System.out.println("========================");
            System.out.println("== 1: Download file   ==");
            System.out.println("== 2: Upload file     ==");
            System.out.println("== 3: Delete file     ==");
            System.out.println("== 4: View local dir  ==");
            System.out.println("== 5: View server dir ==");
            System.out.println("========================");

            Scanner sc = new Scanner(System.in);
            int user_input = sc.nextInt();
            switch(user_input) {
                case 1:
                    sc.nextLine();
                    System.out.println("Enter the name of the file you wish to download.");
                    String dl_file = sc.nextLine();
                    System.out.println("Downloading file: " + dl_file);
                    break;
                case 2:
                    sc.nextLine();
                    System.out.println("Enter the name of the file you wish to upload.");
                    String ul_file = sc.nextLine();
                    the_client.upload(ul_file);
                    break;
                case 3:
                    sc.nextLine();
                    System.out.println("Enter the name of the file you wish to delete.");
                    String delete_file = sc.nextLine();
                    break;
                default:
                    System.out.println("You have selected: Regicide");
            }
        }
    }
}
