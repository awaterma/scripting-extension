import mx.ecosur.sistemas.complejos.netlogo.scripting.Engine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;

/**
 */
public class EngineTest {

    @Test
    public void testFileInput() throws IOException, ParseException {
        String[] args = { "-file", "src/test/resources/script.nlogo" };
        Engine.main(args);
    }

    @Test
    public void testArgsInput() throws IOException, ParseException {
        String [] args = { "open src/test/resources/SimpleModel.nlogo", "setup", "print (var1)", "print (var2)" };
        Engine.main(args);
    }
}
