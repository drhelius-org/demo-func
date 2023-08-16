package com.drhelius;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.WarmupTrigger;

public class WarmupFunction {

    @FunctionName("Warmup")
    public void warmup( @WarmupTrigger Object warmupContext, ExecutionContext context) {
        context.getLogger().info("## Function App instance is warming up 🌞🌞🌞");
        LazyClient.getInstance().run();
        context.getLogger().info("## Function App instance is warm 🌞🌞🌞");
    }
}

