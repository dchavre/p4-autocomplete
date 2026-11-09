import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }
        initialize(terms, weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (prefix.length() > MAX_PREFIX) {
            prefix = prefix.substring(0, MAX_PREFIX);
        }
        if (myMap.containsKey(prefix)) {
            List<Term> all = myMap.get(prefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<>();

        for (int i = 0; i < terms.length; i++) {
            Term t = new Term(terms[i], weights[i]);
            String word = terms[i];
            int maxIndex = Math.min(word.length(), MAX_PREFIX);
            for (int j = 0; j <= maxIndex; j++) {
                String prefix = word.substring(0, j);
                myMap.putIfAbsent(prefix, new ArrayList<>());
                myMap.get(prefix).add(t);
            }
        }

        for (String key : myMap.keySet()) {
            Collections.sort(myMap.get(key),
                    Comparator.comparing(Term::getWeight).reversed());
        }
    }

    @Override
    public int sizeInBytes() {
        if (mySize == 0) {
            for (String key : myMap.keySet()) {
                mySize += BYTES_PER_CHAR * key.length();           
                for (Term t : myMap.get(key)) {
                    mySize += BYTES_PER_CHAR * t.getWord().length()
                            + BYTES_PER_DOUBLE;                    
                }
            }
        }
        return mySize;
    }
}