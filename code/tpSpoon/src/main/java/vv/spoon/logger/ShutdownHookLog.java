package vv.spoon.logger;

public class ShutdownHookLog extends Thread {

    public void run() {
        LogWriter.writeLog();
    }
}
