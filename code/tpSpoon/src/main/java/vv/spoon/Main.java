package vv.spoon;

import org.apache.commons.io.FileUtils;
import spoon.compiler.Environment;
import spoon.compiler.SpoonCompiler;
import spoon.processing.ProcessingManager;
import spoon.processing.Processor;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.FactoryImpl;
import spoon.reflect.visitor.FragmentDrivenJavaPrettyPrinter;
import spoon.support.DefaultCoreFactory;
import spoon.support.JavaOutputProcessor;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;
import vv.spoon.logger.LogWriter;
import vv.spoon.logger.ShutdownHookLog;
import vv.spoon.processor.LogProcessor;


import java.io.File;
import java.io.IOException;


public class Main {
    protected String outputDirectory;
    protected String projectDirectory;
    protected String srcDirectory;


    public static void main(String[] args) throws IOException {
        Main main = new Main(args[0], args[1], args[2]);
        main.initOutputDirectory();
        main.instru();
    }

    public Main(String projectDirectory, String srcDirectory, String outputDirectory) {
        this.projectDirectory = projectDirectory;
        this.srcDirectory = srcDirectory;
        this.outputDirectory = outputDirectory;
    }


    public void instru() throws IOException {
        String src = projectDirectory + System.getProperty("path.separator") + srcDirectory;
        String out = outputDirectory + System.getProperty("path.separator") + srcDirectory;

        Factory factory = initSpoon(src);



        Processor processor = new LogProcessor();
        applyProcessor(factory, processor);


        Environment env = factory.getEnvironment();
        env.useSourceCodeFragments(true);
        applyProcessor(factory, new SimpleJavaOutputProcessor(new File(out), new FragmentDrivenJavaPrettyPrinter(env)));

        copyLoggerFile(outputDirectory, srcDirectory);
    }

    protected void initOutputDirectory() throws IOException {
        File dir = new File(outputDirectory);
        dir.mkdirs();
        FileUtils.copyDirectory(new File(projectDirectory), dir);
    }

    protected Factory initSpoon(String srcDirectory) {
        StandardEnvironment env = new StandardEnvironment();
        env.setVerbose(true);
        env.setDebug(true);

        DefaultCoreFactory f = new DefaultCoreFactory();
        Factory factory = new FactoryImpl(f, env);
        SpoonCompiler c = new JDTBasedSpoonCompiler(factory);
        for (String dir : srcDirectory.split(System.getProperty("path.separator")))
            try {
                c.addInputSource(new File(dir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            c.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory;
    }

    protected void applyProcessor(Factory factory, Processor processor) {
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(processor);
        pm.process();
    }

    protected void copyLoggerFile(String tmpDir, String src) throws IOException {
        File dir = new File(tmpDir+"/"+src+"/vv/spoon/logger");
        FileUtils.forceMkdir(dir);
        String packagePath = System.getProperty("user.dir")+"/src/main/java/vv/spoon/logger/";
        FileUtils.copyFileToDirectory(new File(packagePath + LogWriter.class.getSimpleName() + ".java"), dir);
        FileUtils.copyFileToDirectory(new File(packagePath + ShutdownHookLog.class.getSimpleName() + ".java"), dir);
    }
}
