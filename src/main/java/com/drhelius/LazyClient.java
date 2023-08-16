package com.drhelius;

public class LazyClient {

    private static volatile LazyClient instance;
    private static volatile boolean isReady = false;
    
    public static boolean isReady() {
        return isReady;
    }

    private LazyClient() {
        System.out.println("LazyClient constructor executing...");
        init();
        System.out.println("LazyClient constructor executed.");
    }

    private void init() {
        int milliseconds = 10000;
        System.out.println("LazyClient Waiting " + milliseconds + " milliseconds to initialize the client");

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isReady = true;

        System.out.println("LazyClient Waited for " + milliseconds + " milliseconds");
    }

    public static LazyClient getInstance() {

        LazyClient result = instance;

        if (result != null) {
            return result;
        }

        synchronized(LazyClient.class) {
            if (instance == null) {
                instance = new LazyClient();
            }
            return instance;
        }
    }

    public void run() {
        System.out.println("LazyClient executed");
    }    
}
