/**
 *  Copyright (c) 2017, Carnegie Mellon University.  All Rights Reserved.
 */

/**
 *  An object that stores parameters for the unranked Boolean
 *  retrieval model (there are none) and indicates to the query
 *  operators how the query should be evaluated.
 */
public class RetrievalModelIndri extends RetrievalModel
{
    public static final double INVALID = -1;

    public static double mu = INVALID;
    public static double lambda = INVALID;



    public String defaultQrySopName ()
    {
        return new String ("#and");
    }



}
