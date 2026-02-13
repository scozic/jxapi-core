package org.jxapi.exchanges.demo;


import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class TickerMetaTest {

    @Test
    public void testDefaultConstructor() {
        TickerMeta tickerMeta = new TickerMeta();
        assertNull(tickerMeta.getChains());
    }

    @Test
    public void testSetAndGetChains() {
        TickerMeta tickerMeta = new TickerMeta();
        tickerMeta.setChains(Arrays.asList("chain1", "chain2"));

        assertNotNull(tickerMeta.getChains());
        assertEquals(2, tickerMeta.getChains().size());
        assertEquals("chain1", tickerMeta.getChains().get(0));
        assertEquals("chain2", tickerMeta.getChains().get(1));
    }

    @Test
    public void testDeepClone() {
        TickerMeta original = new TickerMeta();
        original.setChains(Arrays.asList("chain1", "chain2"));

        TickerMeta clone = original.deepClone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
        assertNotSame(original.getChains(), clone.getChains());
    }


    @Test
    public void testCompareTo() {
        // Case 1: Both chains are null
        TickerMeta tickerMeta1 = new TickerMeta();
        tickerMeta1.setChains(null);

        TickerMeta tickerMeta2 = new TickerMeta();
        tickerMeta2.setChains(null);

        assertEquals(0, tickerMeta1.compareTo(tickerMeta2));

        // Case 2: This chains is null, other is not
        tickerMeta2.setChains(Arrays.asList("chain1"));
        assertTrue(tickerMeta1.compareTo(tickerMeta2) < 0);

        // Case 3: Other chains is null, this is not
        tickerMeta1.setChains(Arrays.asList("chain1"));
        tickerMeta2.setChains(null);
        assertTrue(tickerMeta1.compareTo(tickerMeta2) > 0);

        // Case 4: Different sizes
        tickerMeta1.setChains(Arrays.asList("chain1"));
        tickerMeta2.setChains(Arrays.asList("chain1", "chain2"));
        assertTrue(tickerMeta1.compareTo(tickerMeta2) < 0);
        assertTrue(tickerMeta2.compareTo(tickerMeta1) > 0);

        // Case 5: Same size, different elements
        tickerMeta1.setChains(Arrays.asList("chain1", "chain3"));
        tickerMeta2.setChains(Arrays.asList("chain1", "chain2"));
        assertTrue(tickerMeta1.compareTo(tickerMeta2) > 0);
        assertTrue(tickerMeta2.compareTo(tickerMeta1) < 0);

        // Case 6: Same size, same elements
        tickerMeta1.setChains(Arrays.asList("chain1", "chain2"));
        tickerMeta2.setChains(Arrays.asList("chain1", "chain2"));
        assertEquals(0, tickerMeta1.compareTo(tickerMeta2));
    }
    
    @Test
    public void testEquals() {
      TickerMeta tickerMeta1 = new TickerMeta();
      tickerMeta1.setChains(Arrays.asList("chain1", "chain2"));

      TickerMeta tickerMeta2 = new TickerMeta();
      tickerMeta2.setChains(Arrays.asList("chain1", "chain2"));

      TickerMeta tickerMeta3 = new TickerMeta();
      tickerMeta3.setChains(Collections.singletonList("chain1"));

      assertTrue(tickerMeta1.equals(tickerMeta1));
      assertTrue(tickerMeta1.equals(tickerMeta2));
      assertFalse(tickerMeta1.equals(tickerMeta3));
      assertFalse(tickerMeta1.equals(null));
      assertFalse(tickerMeta1.equals("foo"));
    }

    @Test
    public void testEqualsAndHashCode() {
        TickerMeta tickerMeta1 = new TickerMeta();
        tickerMeta1.setChains(Arrays.asList("chain1", "chain2"));

        TickerMeta tickerMeta2 = new TickerMeta();
        tickerMeta2.setChains(Arrays.asList("chain1", "chain2"));

        TickerMeta tickerMeta3 = new TickerMeta();
        tickerMeta3.setChains(Collections.singletonList("chain1"));

        assertEquals(tickerMeta1, tickerMeta2);
        assertNotEquals(tickerMeta1, tickerMeta3);
        assertEquals(tickerMeta1.hashCode(), tickerMeta2.hashCode());
        assertNotEquals(tickerMeta1.hashCode(), tickerMeta3.hashCode());
    }

    @Test
    public void testBuilder() {
        TickerMeta tickerMeta = new TickerMeta.Builder()
                .addChain("chain1")
                .addChain("chain2")
                .build();

        assertNotNull(tickerMeta.getChains());
        assertEquals(2, tickerMeta.getChains().size());
        assertEquals("chain1", tickerMeta.getChains().get(0));
        assertEquals("chain2", tickerMeta.getChains().get(1));
    }
    
    @Test
    public void testToString() {
      TickerMeta tickerMeta1 = new TickerMeta();
      tickerMeta1.setChains(Arrays.asList("chain1", "chain2"));
      assertEquals("TickerMeta{\"chains\":[\"chain1\",\"chain2\"]}", tickerMeta1.toString());
    }
}
