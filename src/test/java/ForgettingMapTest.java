import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForgettingMapTest {
	
	@Test
    public void testAddAndFind() {
		String key = "this is a key";
		String value = "this is a value";
		String alternateKey = "a different key";
		String alternateValue = "a different value";
		ForgettingMap<String, String> map = new ForgettingMap<String, String>(2);
        map.add(key, value);
        map.add(alternateKey, alternateValue);
        assertEquals(value, map.find(key));
        assertEquals(alternateValue, map.find(alternateKey));
        map.add(alternateKey, value);
        assertEquals(value, map.find(alternateKey));
    }
	
	@Test
    public void testCapacity() {
		ForgettingMap<Integer, String> map = new ForgettingMap<Integer, String>(5);
		for (int i = 0; i < 10; i++) {
			map.add(i, "a value");
		}
        assertEquals(5, map.size());
        for (int i = 5; i < 10; i++) {
        	assertEquals("a value", map.find(i));
        }
    }
	
	@Test
    public void testFrequency() {
		ForgettingMap<Integer, String> map = new ForgettingMap<Integer, String>(1);
		map.add(0, "a value");
		for (int i = 0; i < 10; i++) {
			map.find(0);
		}
        assertEquals("{0=a value=10}", map.toString());
    }
	
	@Test
    public void testLeastFrequentRemoval() {
		ForgettingMap<Integer, String> map = new ForgettingMap<Integer, String>(5);
		for (int i = 0; i < 10; i++) {
			map.add(i, "a value");
			for (int k = 0; k < 10 - i; k++) {
				map.find(i);
			}
		}
        for (int i = 0; i < 4; i++) {
        	assertEquals("a value", map.find(i));
        }
        assertEquals("a value", map.find(9));
    }
	
	@Test
    public void testLeastFrequentRemovalTieBreaker() {
		ForgettingMap<Integer, String> map = new ForgettingMap<Integer, String>(3);
		map.add(0, "a value");
		map.add(1, "a value");
		map.add(2, "a value");
		map.find(0);
		map.find(1);
		map.find(2);
		map.find(2);
		map.add(3, "a value");
		assertEquals(null, map.find(0));
        for (int i = 1; i <= 3; i++) {
        	assertEquals("a value", map.find(i));
        }
    }
	
	@Test
    public void testRemoval() {
		ForgettingMap<Integer, String> map = new ForgettingMap<Integer, String>(1);
		map.add(0, "a value");
		map.add(1, "a value");
		map.remove(0);
		assertEquals("a value", map.find(1));
		assertEquals(1, map.size());
    }

}
