package app;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void main_canReadTwoColumnsWithHeader() throws Exception {
        String args[] = {"Hello, Worlds", "Filename"};
        Main.main(args);
    }
}
