package Corba;

/** 
 * Helper class for : FlightOperations
 *  
 * @author OpenORB Compiler
 */ 
public class FlightOperationsHelper
{
    /**
     * Insert FlightOperations into an any
     * @param a an any
     * @param t FlightOperations value
     */
    public static void insert(org.omg.CORBA.Any a, Corba.FlightOperations t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract FlightOperations from an any
     *
     * @param a an any
     * @return the extracted FlightOperations value
     */
    public static Corba.FlightOperations extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return Corba.FlightOperationsHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the FlightOperations TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "FlightOperations" );
        }
        return _tc;
    }

    /**
     * Return the FlightOperations IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:Corba/FlightOperations:1.0";

    /**
     * Read FlightOperations from a marshalled stream
     * @param istream the input stream
     * @return the readed FlightOperations value
     */
    public static Corba.FlightOperations read(org.omg.CORBA.portable.InputStream istream)
    {
        return(Corba.FlightOperations)istream.read_Object(Corba._FlightOperationsStub.class);
    }

    /**
     * Write FlightOperations into a marshalled stream
     * @param ostream the output stream
     * @param value FlightOperations value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, Corba.FlightOperations value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to FlightOperations
     * @param obj the CORBA Object
     * @return FlightOperations Object
     */
    public static FlightOperations narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FlightOperations)
            return (FlightOperations)obj;

        if (obj._is_a(id()))
        {
            _FlightOperationsStub stub = new _FlightOperationsStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to FlightOperations
     * @param obj the CORBA Object
     * @return FlightOperations Object
     */
    public static FlightOperations unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FlightOperations)
            return (FlightOperations)obj;

        _FlightOperationsStub stub = new _FlightOperationsStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
