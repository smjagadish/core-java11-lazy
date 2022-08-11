package com.baeldung.httpclient;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpClientPost {

    public static HttpResponse<String> sendSynchronousPost(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public static HttpResponse<String> sendSynchronousPostJson(String serviceUrl,byte[] arr) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .headers("Content-Type","application/json")
                .headers("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJaS2cwOUt3enhjLWFCaGV4U09hMVVGQlIxUlRlSHl5U25xZGt0RTN3WUhnIn0.eyJleHAiOjE2NTk5OTIwNDAsImlhdCI6MTY1OTk5MTc0MCwianRpIjoiNjk4NzZiZGMtZWExNi00YmE0LWExZDItZWMzNzllOGNhZGNkIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL0NQRSIsImF1ZCI6InJlYWxtLW1hbmFnZW1lbnQiLCJzdWIiOiI0ODE2NjI3Yi1lOTZiLTRkMWUtYTBiYS0wYzE2NDZhNzRhNjQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jbGkiLCJzZXNzaW9uX3N0YXRlIjoiYjZmM2VkYmUtMmFhZi00OWY5LWFhYjctN2VhOTBiZjE3NGMxIiwiYWNyIjoiMSIsInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwidmlldy1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJiNmYzZWRiZS0yYWFmLTQ5ZjktYWFiNy03ZWE5MGJmMTc0YzEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIifQ.EjmFV6FSuPKv8kAkpId6kLIsUBVjdBlSI8QkX7AGw3pcMPo_DjfxJmRPuzH8-J_1G8GCDplUbyjULPXK9NVxmNdAq9sTMBZ4ymxYq92oeAJReibwWHL3bja0KMPcmgNFLaTTnh-fMKJ0C4FEb6cJPcjqpsXwp5ItB9WCx1GRf-6mUfuxpw1Fu90xKrdA96oSvito5pnjEud2nYRmvdTLmb8zzpRwZX0vzQRA0AjDhEwKlenrfk2f_U73bJfA0rZy3xhyRqWFCG0kd7ZGLXAld74JjgV35tqvHEww2dHCxX6yEZxxtclHEq_B0vDQZsVSo6-lL1GTPg0BXZ3yWKrSrw")
                .POST(HttpRequest.BodyPublishers.ofByteArray(arr))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static CompletableFuture<HttpResponse<String>> sendAsynchronousPost(String serviceUrl) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();

        CompletableFuture<HttpResponse<String>> futureResponse = client
          .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return futureResponse;
    }

    public static List<CompletableFuture<HttpResponse<String>>> sendConcurrentPost(List<String> serviceUrls) {
        HttpClient client = HttpClient.newHttpClient();

        List<CompletableFuture<HttpResponse<String>>> completableFutures = serviceUrls.stream()
          .map(URI::create)
          .map(HttpRequest::newBuilder)
          .map(builder -> builder.POST(HttpRequest.BodyPublishers.noBody()))
          .map(HttpRequest.Builder::build)
          .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
          .collect(Collectors.toList());

        return completableFutures;
    }

    public static HttpResponse<String> sendPostWithAuthHeader(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.noBody())
          .header("Authorization", "Basic " + Base64.getEncoder()
            .encodeToString(("baeldung:123456").getBytes()))
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static HttpResponse<String> sendPostWithAuthClient(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
          .authenticator(new Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(
                    "baeldung",
                    "123456".toCharArray());
              }
          })
          .build();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static HttpResponse<String> sendPostWithJsonBody(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.ofString("{\"action\":\"hello\"}"))
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static HttpResponse<String> sendPostWithFormData(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> formData = new HashMap<>();
        formData.put("username", "baeldung");
        formData.put("message", "hello");

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static HttpResponse<String> sendPostWithFileData(String serviceUrl, Path file) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(serviceUrl))
          .POST(HttpRequest.BodyPublishers.ofFile(file))
          .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    private static String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
    public static HttpResponse<String> sendPostWithFormDataKC(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "admin-cli");
        formData.put("grant_type","password");
        formData.put("username", "user");
        formData.put("password","user");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
             //   .version(HttpClient.Version.HTTP_1_1)
                .headers("Content-Type","application/x-www-form-urlencoded")
                .build();


        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public static CompletableFuture<HttpResponse<String>> sendAsyncPostWithFormDataKC(String serviceUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "admin-cli");
        formData.put("grant_type","password");
        formData.put("username", "user");
        formData.put("password","user");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
                //   .version(HttpClient.Version.HTTP_1_1)
                .headers("Content-Type","application/x-www-form-urlencoded")
                .build();


        CompletableFuture<HttpResponse<String>> response = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
