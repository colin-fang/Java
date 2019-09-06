public interface DictionaryADT 
{
    /* Inserts the given Configuration object referenced by data in the dictionary. Throws
       a DictionaryException if the configuration string stored in data is already in the
       dictionary                                                                            */
    public int put(Configuration data) throws DictionaryException;

    /* Removes the Configuration object with configuration string config from the dictionary. 
       Throws a DictionaryException if the configuration string config is not in the 
       dictionary.                                                                           */    
    public void remove(String config) throws DictionaryException;

    /* Returns the score in the Configuration object with configuration string config stored 
       in the dictionary, or -1 if the configuration string config is not in the dictionary. */
    public int getScore(String config);

}