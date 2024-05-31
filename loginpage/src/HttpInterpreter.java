package loginpage.src;
import java.util.*;
import java.io.*;
// import java.nio.file.Files;
// import java.lang.Object.Path;

class HttpInterpreter {
    String request = "", response = "";
    static String folder = "ElectionDAPP/";
    static String defaultFile = folder + "Webpage/page.html";
    HttpInterpreter(String req) {
        request = req;
        response = "";
    }
    public void processRequest() {
        // System.out.println("Recieved request : \n" + request);
        String httpheader = readFile(folder + "/Webpage/responseHeader.txt");
        // System.out.println("httpheader : " + httpheader);
        StringTokenizer rq = new StringTokenizer(request, "\n", false);
        StringTokenizer header;
        String line = "", file = "", method = "", content = "";
        if(rq.hasMoreTokens()) {
            header = new StringTokenizer(rq.nextToken(), " ", false);
            if(header.countTokens() >= 2) {
                method = header.nextToken();
                file = header.nextToken();
            }
            while(rq.hasMoreTokens()) {
                line = rq.nextToken();
                if((line).equals("\r")) {
                    while(rq.hasMoreTokens())
                        content += rq.nextToken();
                    break;
                }
            }
        }
        // System.out.println("Recieved body : " +  content);
        response = httpheader;
        // System.out.println("Request : " + method);
        if(method.equals("GET")) {
            if(file.equals("/")) {
                file = defaultFile;
            }
            String message = readFile(file);
            String[] meta = getType(file);
            response += "\r\nContent-type: " + meta[0] + ";";
            response += "\r\nContent-length: " + message.length() + ";\r\n\r\n";
            response += readFile(file);
        }
        else if(method.equals("POST")) {    
            String topic = content.substring(0, content.indexOf(">"));
            // System.out.println("Recieved a post ------> "+topic);
            if(topic.equals("LOGIN")) {
                response += "\r\nContent-type: text/plain;\r\n\r\n";
                String name = content.substring(content.indexOf("username:")+9, content.indexOf("password:"));
                String pass = content.substring(content.indexOf("password:")+9, content.length());
                if(WebServer.pros.add(name, pass)==-1) {
                    // response += "\r\nContent-type: text/html;\r\n\r\n" + readFile("Webpage/nameRecog.html");
                    response += "voted";
                }
                else {
                    response += "not voted";
                }
                WebServer.pros.display();
                // System.out.println("\tabcde:" + "abcde".indexOf("cd"));
            }
        }
    }
    public static String readFile(String filePath) {
        String fileData = "";
        try {
            if(filePath.charAt(0) == '/') {
                filePath = filePath.substring(1, filePath.length());
            }
            // System.out.println("Filepath = " + filePath);
            FileReader fr = new FileReader(filePath);
            int ch;
            while((ch = fr.read()) != -1) {
                fileData += (char) ch;
            }
            fr.close();
        }
        catch(Exception e) {
            System.out.println("/tFile read error : " + e);
        }
        return fileData;
    }
    public static String[] getType(String filePath) {
        String name = new File(filePath).getName();
        String ext =  name.substring(name.indexOf('.')+1, name.length());
        String type = "";
        String data = "";
        if(ext.equals("html")){
            type = "text/html";
            data = readFile(filePath);
        }else if(ext.equals("js")){
            type = "text/javascript";
            data = readFile(filePath);
        }
        // else if(ext.equals("ico")){
        //     type = "image/ico";
        //     data = Files.readAllBytes(Path.get(filePath));
        // }
        else{
            type = "text/plain";
            data = readFile(filePath);
        }
        return new String[] {type, data};
    }
    public String getResponse() {
        return response;
    }
}