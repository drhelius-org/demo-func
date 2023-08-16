package com.drhelius;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.WarmupTrigger;

public class WarmupFunctions {

    @FunctionName("Warmup")
    public void warmup( @WarmupTrigger Object warmupContext, ExecutionContext context) {
        context.getLogger().info("## Function App instance is warming up 🌞🌞🌞");
        LazyClient.getInstance().run();
        context.getLogger().info("## Function App instance is warm 🌞🌞🌞");
    }

    @FunctionName("CustomWarmup")
    public HttpResponseMessage CustomWarmup( @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "customwarmup")
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("## CustomWarmup warming up 🌞🌞🌞");
        LazyClient.getInstance().run();
        context.getLogger().info("## CustomWarmup is warm 🌞🌞🌞");

        return request.createResponseBuilder(HttpStatus.OK).body("Warm").build();
    }
}

