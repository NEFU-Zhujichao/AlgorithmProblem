import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsParameters;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class T1 {

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/add", new TestHandler1());
        server.createContext("/mult", new TestHandler2());
        server.start();
    }

    static  class TestHandler1 implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String str = exchange.getRequestURI().getQuery();
            String response;
            double double1 =Double.parseDouble(str.substring(str.indexOf("=")+1,str.indexOf("&")));
            double double2 = Double.parseDouble(str.substring(str.indexOf("&")+3));
            double num1 =double1+double2;
            if(str.contains("."))
                response = String.valueOf(num1);
            else
                response = String.valueOf((int)num1);
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static  class TestHandler2 implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String str = exchange.getRequestURI().getQuery();
            String response;
            double double1 =Double.parseDouble(str.substring(str.indexOf("=")+1,str.indexOf("&")));
            double double2 = Double.parseDouble(str.substring(str.indexOf("&")+3));
            double num1 =double1*double2;
            if(str.contains("."))
                response = String.valueOf(num1);
            else
                response = String.valueOf((int)num1);
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}