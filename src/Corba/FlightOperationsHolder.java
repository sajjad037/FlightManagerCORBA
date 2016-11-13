package Corba;

/**
 * Holder class for : FlightOperations
 * 
 * @author OpenORB Compiler
 */
final public class FlightOperationsHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal FlightOperations value
     */
    public Corba.FlightOperations value;

    /**
     * Default constructor
     */
    public FlightOperationsHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public FlightOperationsHolder(Corba.FlightOperations initial)
    {
        value = initial;
    }

    /**
     * Read FlightOperations from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = FlightOperationsHelper.read(istream);
    }

    /**
     * Write FlightOperations into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        FlightOperationsHelper.write(ostream,value);
    }

    /**
     * Return the FlightOperations TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return FlightOperationsHelper.type();
    }

}
