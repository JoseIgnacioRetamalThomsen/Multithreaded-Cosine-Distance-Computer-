/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

/**
 * Types of cosine calculator.
 * <p>
 * Cosine calculator type can be normal, which is fine for no so big amount of shingles
 * counted(most of the time will be related to the size of the file but also depends on
 * the size of the shingles).Normal is faster but may give funny values if the size is to
 * big. Big can hold bigger size but is slower.
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public enum CosineCalculatorType
{
    Normal, Big;
}
