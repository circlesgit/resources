package xyz.hellmouth.jenkinsxml2jobdsl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Writer {

    private static final String JOB_DRIECTORY = "jobs";

    private static PrintWriter makeWriter(final String filepath) throws FileNotFoundException {
        File file = new File(JOB_DRIECTORY + "/" + filepath + ".groovy");
        file.getParentFile().mkdirs();

        return new PrintWriter(file);
    }

    public static void writeToFile(final String filepath, final String contents) throws FileNotFoundException {
        System.out.println("Writing " + JOB_DRIECTORY + "/" + filepath + ".groovy");

        PrintWriter out = makeWriter(filepath);
        out.write(contents);
        out.close();
    }
}
