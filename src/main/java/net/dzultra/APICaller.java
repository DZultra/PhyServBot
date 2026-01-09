package net.dzultra;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APICaller {

    private static final String BASE_URL = "http://192.168.178.187:8000/";
    private static final String API_KEY = PhyServBotMain.json.get("api_key").getAsString();
    private static final HttpClient httpClient = HttpClient.newHttpClient();;

    /**
     * Call the API to start or stop a server
     *
     * @param serverId 1 for NeoForge
     * @param action   "start" or "stop"
     */
    public static void callServerRunner(int serverId, String action) {
        try {
            // Build the URL http://192.168.178.187:8000/start/1
            String url = BASE_URL + action + "/" + serverId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", API_KEY)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("API Response: " + response.statusCode() + " - " + response.body());

            // Consider 2xx responses as success
            if (response.statusCode() >= 200) {
                response.statusCode();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean callServerStatus(Duration timeout) {
        try {
            String url = BASE_URL + "status";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", API_KEY)
                    .timeout(timeout) // ⏱️ max wait time
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("API Response: " + response.statusCode() + " - " + response.body());

            return response.statusCode() == 200
                    && response.body().contains("\"status\":\"ok\"");

        } catch (java.net.http.HttpTimeoutException e) {
            System.out.println("Status request timed out.");
            return false;

        } catch (IOException | InterruptedException e) {
            System.out.println("Server is offline (no API reachable).");
            return false;
        }
    }
}
