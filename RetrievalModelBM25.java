/**
 *  Copyright (c) 2017, Carnegie Mellon University.  All Rights Reserved.
 */

/**
 *  An object that stores parameters for the unranked Boolean
 *  retrieval model (there are none) and indicates to the query
 *  operators how the query should be evaluated.
 */
public class RetrievalModelBM25 extends RetrievalModel
{
    public static final double INVALID = -1;

    public static double k1 = INVALID;
    public static double k3 = INVALID;
    public static double b = INVALID;


    public String defaultQrySopName ()
    {
        return new String ("#sum");
    }



}
