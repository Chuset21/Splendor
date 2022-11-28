package hexanome.fourteen.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class RestLauncherTest {
    static RestLauncher restLauncher;

    @BeforeEach
    public void setUp() {
        restLauncher = new RestLauncher();
        //RestLauncher.main(new String[]{"Test"});
    }
    @Test
    public void testNotNull() {
        assertNotEquals(null, restLauncher);
    }

}