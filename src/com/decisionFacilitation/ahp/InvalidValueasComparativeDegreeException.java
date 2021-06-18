package com.decisionFacilitation.ahp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Triggered if Comperative Degree is 0
 *
 * @author canberk
 */
public class InvalidValueasComparativeDegreeException extends Exception {

    private static final Logger myLogger = LogManager.getLogger();//log4j logger

    private static final long serialVersionUID = 8113777991895709335L;

    public InvalidValueasComparativeDegreeException() {
        super("\n0 can't be given as a Comperative Degree. Use 1 instead if equal Comperative Degree is pursued.");
        myLogger.error("\n0 can't be given as a Comperative Degree. Use 1 instead if equal Comperative Degree is pursued.");
    }
}
