package Server;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;

public class Server {
    private int port;
    // This is not a reserved port number
    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "Lab3keystore.ks";
    static final String TRUSTSTORE = "Lab3truststore.ks";
    static final String KEYSTOREPASS = "margherita";
    static final String TRUSTSTOREPASS = "margherita";
    SSLSocket socket = null;
    
    public Server( int port ) {
    	this.port = port;
    }
    
    public PrintWriter download(String file){
        //PrintWriter download_stream = new PrintWriter();
        return null;
    }
    
    public String upload(String in_text, String file_name){
        return("Upload hopefully successful");
    }
    
    public String delete(String file){
        return("Delete hopefully deletrious");
    }
    
    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load( new FileInputStream("serverfiles/" + KEYSTORE ), KEYSTOREPASS.toCharArray() );
			
            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load( new FileInputStream("serverfiles/" + TRUSTSTORE ), TRUSTSTOREPASS.toCharArray() );
			
            KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
            kmf.init( ks, KEYSTOREPASS.toCharArray() );
			
            TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
            tmf.init( ts );
			
            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
            SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
            sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			
            System.out.println("\n>>>> SecureAdditionServer: active ");
            socket = (SSLSocket)sss.accept();
            
            // Listen for inputs here
            
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            //PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );
            PrintWriter out = new PrintWriter( "serverfiles/output.txt");
            String str;
	
            str = in.readLine();
            while(true){
                out.print(str);
                str = in.readLine();
                if(!str.equals("")){
                    out.print("\n");
                }
                else{
                    break;
                }
            }
            
            /*
            while ( !(str = in.readLine()).equals("")) {
		out.println(str);
            }*/
            socket.close();
            out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
        
    public static void main( String[] args ) {
	int port = DEFAULT_PORT;
	if (args.length > 0 ) {
            port = Integer.parseInt( args[0] );
	}
	Server the_server = new Server( port );
        the_server.run();
    }
}
