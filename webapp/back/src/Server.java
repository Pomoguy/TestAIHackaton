import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {
    private static final String ENV_PORT = System.getenv("PORT") != null ? System.getenv("PORT") : "8080";

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(ENV_PORT);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // /
        server.createContext("/", (HttpExchange exchange) -> {
            String resp = "{\"ok\":true,\"service\":\"java-back\",\"message\":\"Hello from Java!\"}";
            byte[] bytes = resp.getBytes();
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        });

        // /health
        server.createContext("/health", (HttpExchange exchange) -> {
            byte[] bytes = "OK".getBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("✅ Java server started on http://0.0.0.0:" + port);
    }
}
