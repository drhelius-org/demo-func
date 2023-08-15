package com.drhelius;

public class InitClient {

    private boolean isReady = false;
    
    public boolean isReady() {
        return isReady;
    }

    public InitClient() {
        System.out.println("InitClient constructor executing...");
        init();
        System.out.println("InitClient constructor executed.");
    }

    public void run() {
        init();
        System.out.println("InitClient executed");
    }

    private synchronized void init() {
        int milliseconds = 30000;

        System.out.println("InitClient initializing...");

        if (!isReady) {
            System.out.println("InitClient Waiting " + milliseconds + " milliseconds to initialize the client");

            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("InitClient Waited for " + milliseconds + " milliseconds");

            isReady = true;            
        }
    }
}

