import java.util.Comparator;

/**
 * Factory pattern for obtaining PrefixComparator objects
 * without calling new. Users simply use
 *
 *     Comparator<Term> comp = PrefixComparator.getComparator(size)
 *
 * @author owen astrachan
 * @date October 8, 2020
 * @date October 2025
 */
public class    PrefixComparator implements Comparator<Term> {

    private int myPrefixSize; // size of prefix

    /**
     * private constructor, called by getComparator
     * @param prefix is prefix used in compare method
     */
    private PrefixComparator(int prefix) {
        myPrefixSize = prefix;
    }


    /**
     * Factory method to return a PrefixComparator object
     * @param prefix is the size of the prefix to compare with
     * @return PrefixComparator that uses prefix
     */
    public static PrefixComparator getComparator(int prefix) {
        return new PrefixComparator(prefix);
    }


    @Override
    /**
     * Use at most myPrefixSize characters from each of v and w
     * to return a value comparing v and w by words (.getWord()). 
     * Comparisons should be made based on the first 
     * (at most) myPrefixSize chars in v and w.
     * @return < 0 if v < w, == 0 if v == w, and > 0 if v > w
     */
    public int compare(Term v, Term w) {
        String vw = v.getWord();
        String ww = w.getWord();
    int len = Math.min(myPrefixSize, Math.min(vw.length(), ww.length()));

    for (int k = 0; k < len; k++) {
        if (vw.charAt(k) != ww.charAt(k)) {
            return vw.charAt(k) - ww.charAt(k);
        }
    }
    int vlen = Math.min(vw.length(), myPrefixSize);
    int wlen = Math.min(ww.length(), myPrefixSize);
    return vlen - wlen;
}
}
