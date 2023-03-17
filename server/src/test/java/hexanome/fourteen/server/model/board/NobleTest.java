package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class NobleTest {

    static Noble noble;
    static int prestigePoints = 3;
    static Gems cost;

    @BeforeAll
    public static void setUp() {
        cost = new Gems();
        cost.put(GemColor.BLACK, 3);
        cost.put(GemColor.BLUE, 2);
        cost.put(GemColor.RED, 3);
        noble = new Noble(prestigePoints, cost);
    }
    @Test
    public void testPrestigePoints() {
        assertEquals(prestigePoints, noble.prestigePoints());
    }

    @Test
    public void testCost() {
        Gems equCost = new Gems(cost);

        assertEquals(equCost.get(GemColor.BLACK), noble.cost().get(GemColor.BLACK));
        assertEquals(equCost.get(GemColor.BLUE), noble.cost().get(GemColor.BLUE));
        assertEquals(equCost.get(GemColor.RED), noble.cost().get(GemColor.RED));
        assertNull(noble.cost().get(GemColor.GREEN));
        assertNull(noble.cost().get(GemColor.WHITE));
        assertNull(noble.cost().get(GemColor.GOLD));
    }

    @Test
    public void testEquals() {
        assertEquals(noble, noble);
        assertFalse(noble.equals(null));
    }
}