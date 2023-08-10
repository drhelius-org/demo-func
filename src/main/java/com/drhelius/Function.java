package com.drhelius;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.WarmupTrigger;

import java.util.Optional;

public class Function {

    private static HttpStatus status = HttpStatus.OK;
    private static boolean isWarm = false;

    private synchronized void warm(int milliseconds, ExecutionContext context) {
        if (!isWarm) {
            context.getLogger().info("Waiting 5 seconds to warm up");

            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            context.getLogger().info("Waited for 5 seconds");

            isWarm = true;            
        }
    }

    @FunctionName("Warmup")
    public void warmup( @WarmupTrigger Object warmupContext, ExecutionContext context) {
        context.getLogger().info("Function App instance is warming up 🌞🌞🌞");
        this.warm(5000, context); 
        context.getLogger().info("Function App instance is warm 🌞🌞🌞");
    }

    @FunctionName("Demo")
    public HttpResponseMessage demo(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "demo")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        this.warm(5000, context);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("ChangeStatus")
    public HttpResponseMessage changeStatus(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "status")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        final String query = request.getQueryParameters().get("code");
        final String statusString = request.getBody().orElse(query);

        if (statusString == null) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.valueOf(Integer.parseInt(statusString));
        }

        return request.createResponseBuilder(HttpStatus.OK).body("Status changed to " + status.toString()).build();
    }

    @FunctionName("Health")
    public HttpResponseMessage health(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "health")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        return request.createResponseBuilder(status).body("Status is " + status.toString()).build();
    }    
}
