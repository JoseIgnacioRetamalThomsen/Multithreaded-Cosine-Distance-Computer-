/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

import java.util.NoSuchElementException;

/**
 * Implementation of shingable that produce k-mers shingles.
 * <p>
 * The size of the k-mers can be set.
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public class KShingler implements Shingable
{

    /*
     * Use a StringBuilder, a new one is created each time, when the first line is added bill
     * added to the StringBuilder, when another line is added(after all shingler are taken) if
     * will be append to any character remaining.
     * 
     */
    /**
     * Length of k-mers
     */
    private int length;
    /**
     * start of next k-mer
     */
    private int position;
    /**
     * One line of input
     */
    private StringBuilder line;

    /**
     * Create a new KShingler object with k-mers size of parameter value.
     * 
     * @param shingleLength size of the k-mers in characters.
     */
    public KShingler(int shingleLength)
    {
        this.length = shingleLength;
        this.position = 0;
        line = null;

    }

    /**
     * {@inheritDoc}
     */
    public boolean addLine(CharSequence line)
    {
        // check for first line
        if (this.line == null)
        {
            this.line = new StringBuilder(line);

        } else
        {
            // check if there is next(Only can add line if there is not)
            if (hasNextShingle())
                return false;

            this.line = new StringBuilder(this.line.substring(position, this.line.length()));
            this.line.append(line);
            position = 0;
        }

        return true;

    }

    /**
     * {@inheritDoc}
     */
    public int nextShingle()
    {
        if (!hasNextShingle())
            throw new NoSuchElementException();

        int hash = line.substring(position, position + length).toString().hashCode();
        position += length;

        return hash;

    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNextShingle()
    {
        return (line.length() - position) >= length;

    }

    /**
     * {@inheritDoc}
     */
    public boolean hasLast()
    {
        return (line.length() - position) > 0;

    }

    /**
     * {@inheritDoc}
     */
    public int lastShingle()
    {
        if (!hasLast())
            throw new NoSuchElementException();

        int hash = line.substring(position, line.length()).toString().hashCode();
        line = new StringBuilder();
        position = 0;

        return hash;

    }

}
