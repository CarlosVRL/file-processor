package app;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class MainTest {
    @Test
    public void main_canReadTwoColumnsWithHeader() throws Exception {
        File resource = new File("src/test/resources/test-data.csv");
        String filename = resource.getAbsolutePath();
        Main.main(new String[]{filename});
    }

    @Test
    public void main_willPrintUsageForEmptyArgs() {
        Main.main(new String[]{});
    }
}
