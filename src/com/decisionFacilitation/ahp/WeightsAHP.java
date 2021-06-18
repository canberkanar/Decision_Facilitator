package com.decisionFacilitation.ahp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Runs the AHP Algorithm
 *
 * @author Ali Canberk ANAR
 */
public class WeightsAHP {

    private static final Logger myLogger = LogManager.getLogger();//log4j logger

    public static void main(String argv[]) throws Exception {

        String CSVLocation = "sampleInputs/NestedElectionCandidateWeights.csv";//File Should be located at the project's path

        Map<String, Double> myWeights = new HashMap<>();//Holds Criteria and their Weights
        double normalizationValue = 0;

        CSVImport myCSVImport = new CSVImport(CSVLocation);//Reads data from the CSV File

        int NumberofMatrix = myCSVImport.getMatrixIndex();

        for (int MatrixIndex = 0; MatrixIndex < NumberofMatrix; MatrixIndex++) {//Applies AHP algorithm to each matrix

            if (myCSVImport.isMainCriteria(MatrixIndex)) {
                myLogger.debug("*******************************************************************Root AHP*******************************************************************");
            } else {
                myLogger.debug("*******************************************************************" + myCSVImport.getMatrixLabel(MatrixIndex) + " AHP*******************************************************************");
            }

            ArrayList<String> label;
            label = myCSVImport.fillmyLabel(CSVLocation, MatrixIndex);

            String[] myLabels = new String[label.size()];
            myLabels = label.toArray(myLabels);//Holds the criterias of the matrix being processed


            int numberOfCriteria = myLabels.length;

            AHP myAHP = new AHP(numberOfCriteria);

            double[] myPairwiseComparisonArray = myAHP.getPairwiseComparisonArray();
            myPairwiseComparisonArray = myCSVImport.setComparisonArray(myPairwiseComparisonArray.length, CSVLocation, MatrixIndex);
            myAHP.setPairwiseComparisonArray(myPairwiseComparisonArray);

            myLogger.debug("\n" + myAHP + "\n");//Logs the matrix being processed with the Debug level

            for (int i = 0; i < myAHP.getNrOfPairwiseComparisons(); i++) {//Displays PairWiseComparisons
                myLogger.debug("Importance of " + myLabels[myAHP.getIndicesForPairwiseComparison(i)[0]] + " compared to " +
                        myLabels[myAHP.getIndicesForPairwiseComparison(i)[1]] + " = " + myPairwiseComparisonArray[i]);//Logs the Comparative Degrees of Indices with the Info Level
            }

            myLogger.info("\nConsistency Index: " + myAHP.getConsistencyIndex() + "\nConsistency Ratio: " + myAHP.getConsistencyRatio() + "%\n");

            if (myCSVImport.isMainCriteria(MatrixIndex)) {//Calculates the normalizationValue
                normalizationValue = 100;
                myLogger.debug("Root Matrix");
            } else {
                myLogger.debug("SubMatrix");
                myLogger.debug("Matrix Label: " + myCSVImport.getMatrixLabel(MatrixIndex));

                for (Map.Entry<String, Double> entry : myWeights.entrySet()) {
                    if (entry.getKey().equals(myCSVImport.getMatrixLabel(MatrixIndex))) {
                        normalizationValue = entry.getValue();
                        break;//Prevents unnecessary comparisons
                    }
                }
            }
            myLogger.debug("Sum of the weights:" + normalizationValue);    //Displays the sum of the weights of the factors at the current Matrix
            myLogger.info("Weights ");

            for (int k = 0; k < myAHP.getWeights().length; k++) {//Displays Weights of the Criteria
                myWeights.put(myLabels[k], myAHP.getWeights()[k] * normalizationValue);
                myLogger.info(myLabels[k] + ": " + myAHP.getWeights()[k] * normalizationValue);
            }
        }
    }
}
