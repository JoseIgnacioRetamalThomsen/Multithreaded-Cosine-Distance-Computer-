package ie.gmit.sw.base;
/**
 * Implemented for identify different instances of a   class.
 * 
 * @author Jose I. Retamal
 *
 */
public interface Identifiable
{
    /**
     * Set the  name that distinguish the object.
     * 
     * @param name Name that distinguish the object
     */
    void setName(String name);

    /**
     * Return the Name that distinguish the object.
     * 
     * @return Name that distinguish the object
     */
    String getName(); 

}
