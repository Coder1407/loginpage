package loginpage.src;
import java.io.*;
import java.net.*;
// import java.util.*;
class WebServer {
    static boolean shutdownCL = false;
    public boolean pauseS = false;
    static int port = 9000;
    static ServerSocket ss;
    static ProfileManager pros;
    public static void main(String args[]) {
        pros = new ProfileManager();
        System.out.println("Starting Server!");
        try {
            ss = new ServerSocket(port);
            while(!ss.isClosed()) {
                Socket client = ss.accept();
                processClient(client);
                client.close();
            }
        }
        catch(IOException e) {
            System.out.println("\tServer Socket error : " + e);
        }
    }
    public static void processClient(Socket client) {
        String rqst = "";
        String rpns = "";
        try {
            // Thread.sleep(300);
            // InputStreamReader isr =  new InputStreamReader(client.getInputStream());
            // while(isr.ready()) {
            //     rqst += (char) isr.read();
            // }
            BufferedInputStream isr = new BufferedInputStream(client.getInputStream(), 100);
            while(isr.available() == 0);
            while(isr.available() > 0) {
                rqst += (char)isr.read();
            }
            System.out.println("\nRequest recieved from client!----->\n"+rqst);
            PrintStream ps = new PrintStream(client.getOutputStream(), true);
            HttpInterpreter hi = new HttpInterpreter(rqst);
            hi.processRequest();
            rpns = hi.getResponse();
            System.out.println("\nSending client a response!----->\n"+rpns);
            ps.print(rpns);
            ps.close();
            isr.close();
        }
        catch(IOException e) {
            System.out.println("\tClient process error : " + e);
        }
        // catch(InterruptedException e) {
        //     System.out.println("Interrupted exception : " + e);
        // }
    }
}