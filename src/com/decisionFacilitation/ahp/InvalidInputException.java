package com.decisionFacilitation.ahp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Triggered if Comperative Degree is not Numeric
 *
 * @author canberk
 */
public class InvalidInputException extends Exception {

    private static final Logger myLogger = LogManager.getLogger();//log4j logger

    private static final long serialVersionUID = 159557176276899073L;

    public InvalidInputException(String myString, Exception ex) {
        super(myString, ex);
        myLogger.error(myString, ex);
    }
}
