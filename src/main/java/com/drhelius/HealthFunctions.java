package com.drhelius;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

public class HealthFunctions {

    private static HttpStatus status = HttpStatus.OK;

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

        HttpStatus ret = LazyClient.isReady() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        
        context.getLogger().info("Health is " + ret.toString());

        return request.createResponseBuilder(ret).body("Status is " + ret.toString()).build();
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

        return request.createResponseBuilder(status).body("CustomHealth is " + status.toString()).build();
    }

    @FunctionName("ChangeHealth")
    public HttpResponseMessage changeHealth(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "changehealth")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("ChangeHealth HTTP trigger processing a request...");

        final String query = request.getQueryParameters().get("code");
        final String statusString = request.getBody().orElse(query);

        if (statusString == null) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.valueOf(Integer.parseInt(statusString));
        }

        return request.createResponseBuilder(HttpStatus.OK).body("CustomHealth changed to " + status.toString()).build();
    }        
}

