package com.drhelius;

public class LazyClient {

    private boolean isReady = false;
    
    public boolean isReady() {
        return isReady;
    }

    public LazyClient() {

    }

    public void run() {
        init();
        System.out.println("LazyClient executed");
    }

    public synchronized void init() {
        int milliseconds = 30000;

        if (!isReady) {
            System.out.println("LazyClient Waiting " + milliseconds + " milliseconds to initialize the client");

            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("LazyClient Waited for " + milliseconds + " milliseconds");

            isReady = true;            
        }
    }
}

