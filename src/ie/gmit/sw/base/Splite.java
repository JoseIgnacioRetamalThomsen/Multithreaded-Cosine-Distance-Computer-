/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.base;

/**
 * Contains split functions for group word shingles
 * 
 * @author Jose I. Retamal
 *
 */
public class Splite
{

    /**
     * Splite a string in words, not consider punctuation, numbers are considered words and
     * number appended to a words are considered one word.
     * 
     * @param line
     * @return
     */
    public String[] splitWordsNumber(String line)
    {

        return line.split("[^a-zA-Z0-9]++");

    }

    public String[] splitChar(String line, int splitLength)
    {

        StringBuilder sb = new StringBuilder();

        return line.split("[^a-z-]++");

    }
}
