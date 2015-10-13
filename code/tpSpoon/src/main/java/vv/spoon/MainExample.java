package vv.spoon;

import vv.spoon.processor.LogProcessor;

import java.io.IOException;


public class MainExample {

    public static void main(String[] args) throws IOException {
        Instru instru = new Instru(args[0], args[1], new LogProcessor());

        //copy the project (args[0]) in the output directory (args[1])
        instru.initOutputDirectory();

        //instrumentalize the java code of output directory with LogProcessor
        instru.instru();
    }

}
