package com.baeldung.httpsender;

import com.baeldung.data.Group;
import com.baeldung.data.ResponseData;
import com.baeldung.httpclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class sender {
    public static void main(String[] args)
    {
        try {
            String async_data = "";
            // Completable future can be used with concurrent api's such as runnable/callable
            CompletableFuture<String> test_data = new CompletableFuture<>();
            Executors.newFixedThreadPool(1).submit(()->{
               test_data.complete("100");
               return null;
            });
            // this is a blocking call on the future . the current executing thread i.e. main will block until future is ready
               System.out.println(test_data.get());

            CompletableFuture<String> future_data = new CompletableFuture<>();

            //Straight forward HTTP GET
            HttpResponse<String> output = HttpClientHeader.callWithCustomHeader("http://localhost:8080/auth/realms/CPE/.well-known/openid-configuration");
            System.out.println(output.body());

           // leveraging jackson deserialization by converting returned string in HTTP GET into a POJO
            ResponseData res = new ObjectMapper().readValue(HttpClientHeader.callWithCustomHeaderPojoBody("http://localhost:8080/auth/realms/CPE/.well-known/openid-configuration").body(), ResponseData.class);

            //Straight forward HTTP Post with form data. The content type header must be set appropriately else the client acts weird
            System.out.println(HttpClientPost.sendPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").body());

            // Async handling starts

            // HTTP post with form data in async . thenApply stage acts on the returned future , transforms the result to extract http body alone and returns another future
            // join() extracts the result from the future and is presumably blocking ?
            // thenApply runs in the same thread from executor which runs the connection
            async_data = (HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").thenApply(result -> result.body()).join());

            // more complex example for HTTP post with form data in async
            // first thenApply stage acts on returned future and invokes another async function( returning it as a future ) which returns the http response body as a future.
            // thenApply in previous step thus returns an embedded/nested  future
            // second thenApply processes the inner future and extracts out the result, i.e. the http response body
            // both the thenApply runs in same thread scope as the one where http connection happens
            // the async fn invoked by first thenApply alone runs in scope of a diff thread
            // final output is the outer future carrying the result .i.e. http response body returned by inner future
            future_data = HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").thenApply(result -> CompletableFuture.supplyAsync(() -> result.body())).thenApply(processed -> {

                return processed.join();

            });

            // relatable with preceeding case but uses the handle method which can be used for error/exception handling
            // http post with form data in async
            // returned future is prcoessed by handle method which returns the http response string as future to next stage
            // then apply transforms returned http response by extracting body alone and retunrs it in a future to next stage - i.e. handle method
            // handle method inspects if exception from previous stage is set or not and based on the outcome returns a fixed string or the http response body extracted by then apply
            // both thenapply and handle run in same thread scope as the one where http communication takes place
            // final stage is data retrieval from the returned future

            async_data = (HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").handle((s,t)->{return s;}).thenApply(result -> result.body()).handle((s,t)->{
                    if (t!=null)
                        return "exception";
                    else
                        return s;
            }).join());

            System.out.println(async_data);





            // http post with form data in async
            //returned future is processed by handle method which returns http res string as future to next stage
            // then apply transforms http response by extracting body alone and retunrs it in a future to next stage - i.e. next then apply method
            //then apply #2 hands off the future containing the extracted body wrapped in a future to an async thread that runs in common fork/join pool
            // the async thread converts resp body to upper case and returns the result as a future
            // then apply #2 now has a nested completable future which it hands over to then apply #3
            // then apply #3 hands off the future containing the extracted body wrapped in a future to an async thread that runs in common fork/join pool
            // the async thread unwraps the inner future and hands off to thread where then apply #3 was running
            // thread running then apply #3 does another unwrap to get outer future
            // thread running then apply #3 finally does a join to get the resultant data
            // handle and all then apply's run in scope of same thread
            // the async functions run in diff threads from common fork/join pool

            async_data = HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").handle((s,t)->{return s;}).thenApply(result -> result.body()).thenApply(rt->CompletableFuture.supplyAsync(()->rt.toUpperCase())).thenApply(r->CompletableFuture.supplyAsync(()->r.join())).join().join();


            System.out.println(async_data);

            // too lazy to write, its same as prev case but less complex and less handovers

            async_data = HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").handle((s,t)->{return s;}).thenApply(result -> result.body()).thenApply(rt->CompletableFuture.supplyAsync(()->rt.toUpperCase())).thenApply(r->r.join()).join();

            System.out.println(async_data);


            //  future_data.complete("cheated");
            System.out.println(future_data.get());

            // thencompose instead of then apply
            // it returns flattened future as result directly . this is different when compared to nested futures  in case of then apply - especially when supplying with a async function
            String out = HttpClientPost.sendAsyncPostWithFormDataKC("http://localhost:8080/auth/realms/CPE/protocol/openid-connect/token").thenCompose(result -> CompletableFuture.supplyAsync(() -> result.body())).thenCompose(result ->CompletableFuture.supplyAsync(()->result.toUpperCase())).join();
            System.out.println(out);


            Group gr = new Group("csr3");

            // using jackson serialziation to convert object to byte array
            System.out.println(HttpClientPost.sendSynchronousPostJson("http://localhost:8080/auth/admin/realms/CPE/groups", new ObjectMapper().writeValueAsBytes(gr)).body());


            System.out.println(future_data.get());


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
