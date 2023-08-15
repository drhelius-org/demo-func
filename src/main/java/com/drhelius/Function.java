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
    private static InitClient initClient = new InitClient();
    private static LazyClient lazyClient = new LazyClient();

    @FunctionName("Warmup")
    public void warmup( @WarmupTrigger Object warmupContext, ExecutionContext context) {
        context.getLogger().info("Function App instance is warming up 🌞🌞🌞");
        lazyClient.init();
        context.getLogger().info("Function App instance is warm 🌞🌞🌞");
    }

    @FunctionName("RunInit")
    public HttpResponseMessage runInit(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "runinit")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("RunInit HTTP trigger processing a request...");

        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        initClient.run();

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("RunLazy")
    public HttpResponseMessage runLazy(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "runlazy")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("RunLazy HTTP trigger processing a request...");

        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        lazyClient.run();

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
        context.getLogger().info("ChangeStatus HTTP trigger processing a request...");

        final String query = request.getQueryParameters().get("code");
        final String statusString = request.getBody().orElse(query);

        if (statusString == null) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.valueOf(Integer.parseInt(statusString));
        }

        return request.createResponseBuilder(HttpStatus.OK).body("Status changed to " + status.toString()).build();
    }

    @FunctionName("CustomHealth")
    public HttpResponseMessage customHealth(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "customhealth")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("CustomHealth HTTP trigger processing a request...");

        return request.createResponseBuilder(status).body("Status is " + status.toString()).build();
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
        context.getLogger().info("Health HTTP trigger processing a request...");

        HttpStatus ret = initClient.isReady() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        
        context.getLogger().info("Health is " + ret.toString());

        return request.createResponseBuilder(ret).body("Status is " + ret.toString()).build();
    }    
}

