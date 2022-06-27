import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.AbstractMap.SimpleEntry;

// An alternative approach would be to extend an existing thread safe map such as ConcurrentHashMap.

// Return types mirror HashMap return types.

// Generics allow for any type key or value.
public class ForgettingMap<K,V> {
	
    // LinkedHashMap is used as it is order-preserving, and I've used order of addition as the 'tie breaker' (first in, first out).
    // Frequency of retrieval is held as an integer along with the value in an Entry. I'm using the Entry type as if it were a tuple.
    private final Map<K,Entry<V,Integer>> keyValueFrequencyMap = new LinkedHashMap<K, Entry<V, Integer>>();
    private Integer capacity;

    ForgettingMap(Integer capacity) {
        this.capacity = capacity;
    }

    // Synchronised all state-changing methods to ensure thread safety.
    public synchronized K add(K key,V value) {
        if(!keyValueFrequencyMap.containsKey(key)) {
        	if(keyValueFrequencyMap.size() + 1 > capacity) {
        		keyValueFrequencyMap.remove(getLeastFrequentlyAccessedKey());
        	}
        	keyValueFrequencyMap.put(key, new SimpleEntry<V,Integer>(value, 0));
        	return key;
        }
        keyValueFrequencyMap.put(key, new SimpleEntry<V,Integer>(value, getFrequency(key)));
        return key;
    }

    public synchronized V find(K key) {
        Map.Entry<V, Integer> valueAndFrequency = getValueAndIncrementFrequency(key);
        if(valueAndFrequency != null) {
            return valueAndFrequency.getKey();
        }
        return null;
    }

    public synchronized V remove(K key) {
        V value = find(key);
        keyValueFrequencyMap.remove(key);
        return value;
    }
    
    public int size() {
        return keyValueFrequencyMap.size();
    }
    
    @Override
    public String toString() {
    	return keyValueFrequencyMap.toString();
    }

    // Private state-changing methods are not synchronised because the calling method already is.
    private Map.Entry<V, Integer> getValueAndIncrementFrequency(K key) {
        if(keyValueFrequencyMap.containsKey(key)){
            keyValueFrequencyMap.get(key).setValue(
                    keyValueFrequencyMap.get(key).getValue() + 1
            );
            return keyValueFrequencyMap.get(key);
        }
        return null;
    }

    private Integer getFrequency(K key) {
        Map.Entry<V, Integer> valueAndFrequency = keyValueFrequencyMap.get(key);
        if(valueAndFrequency != null) {
            return valueAndFrequency.getValue();
        }
        return null;
    }

    private K getLeastFrequentlyAccessedKey() {
        Map.Entry<K, Integer> least = new SimpleEntry<K, Integer>(null, Integer.MAX_VALUE);
        for (Map.Entry<K,Map.Entry<V,Integer>> entry: keyValueFrequencyMap.entrySet()) {
            Integer frequency = getFrequency(entry.getKey());
            if (frequency < least.getValue()) {
                least = new SimpleEntry<K,Integer>(entry.getKey(), frequency);
            }
        }
        return least.getKey();
    }

}
