package services;

import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Created by mituba on 2017/07/23.
 */
public class Extracter {
    public String getExtractFile(String fileName, String kindOfBirthmark) throws IOException, ScriptException {
        ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
        ScriptRunner runner = builder.build();
        String[] arg = { "./extract.js", fileName, kindOfBirthmark};
        runner.runsScript(arg);
        return fileName + ".csv";
    }
}
