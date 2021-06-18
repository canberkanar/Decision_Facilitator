package com.decisionFacilitation.ahp;

import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Imports data from CSV File
 *
 * @author canberk
 */
public class CSVImport {

    private List<List<List<String>>> myData = new ArrayList<>();
    private int realLength;
    private int matrixIndex;

    private static final Logger myLogger = LogManager.getLogger();//log4j logger

    /**
     * Reads Matrix to Memory
     *
     * @param csvLocation
     */
    public CSVImport(String csvLocation) {

        try {//Imports CSV file into an array
            String[] nextLine;
            CSVReader myReader = new CSVReader(new FileReader(csvLocation));

            nextLine = myReader.readNext();


            for (matrixIndex = 0; (nextLine != null) && !Arrays.toString(nextLine).isEmpty(); matrixIndex++) {
                for (int z = 1; z < nextLine.length && !nextLine[z].isEmpty(); z++) {
                    realLength = z + 1;
                }
                myData.add(new ArrayList<>());

                for (int i = 0; i < realLength; i++) {
                    myData.get(matrixIndex).add(new ArrayList<>());

                    for (int j = 0; j < realLength; j++) {
                        if (!nextLine[j].isEmpty()) {
                            myData.get(matrixIndex).get(i).add(nextLine[j]);
                        } else {
                            myData.get(matrixIndex).get(i).add(null);
                        }
                    }
                    nextLine = myReader.readNext();
                }
                nextLine = myReader.readNext();//Skips empty row
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            myLogger.error("File not Found\n" + e);
        } catch (IOException i) {
            myLogger.error("Input Output Error\n" + i);
        }
    }

    /**
     * Reads the Criteria
     *
     * @param csvLocation
     * @param matrixIndex
     * @return CSVLabel which holds the Criteria Read
     */
    public ArrayList<String> fillmyLabel(String csvLocation, int matrixIndex) {//Returns an ArrayList with the Criteria of the desired Matrix
        ArrayList<String> CSVLabel = new ArrayList<>();

        for (int j = 1; j < myData.get(matrixIndex).get(0).size() && myData.get(matrixIndex).get(0).get(j) != null; j++) {//j is initially 1 because 0 holds the Label of the Matrix, not the Criteria
            CSVLabel.add(myData.get(matrixIndex).get(0).get(j));
        }
        return CSVLabel;
    }

    /**
     * Reads Comparison Values from the Matrix
     *
     * @param LengthofArray
     * @param csvLocation
     * @param matrixIndex
     * @return comparisonArray which holds the Comparison Values read
     * @throws Exception
     */
    public double[] setComparisonArray(int LengthofArray, String csvLocation, int matrixIndex) throws Exception {//Returns an Array with the Comparison Values of the desired Matrix
        double[] comparisonArray = new double[LengthofArray];
        int i = 0;
        int j = 0;

        try {
            int t = 0;
            for (i = 1; i < myData.get(matrixIndex).size(); i++) {//i is initially 1 because 0 holds the Label of the Matrix, not the Criteria
                for (j = 1 + i; j < myData.get(matrixIndex).get(i).size(); j++) {//j iterates through the top half of the Matrix
                    if (Double.parseDouble(myData.get(matrixIndex).get(i).get(j)) == 0) {//Throws Exception if 0 is given as a comparative degree
                        throw new InvalidValueasComparativeDegreeException();
                    }
                    comparisonArray[t] = Double.parseDouble(myData.get(matrixIndex).get(i).get(j));
                    t++;
                }
            }
        } catch (NumberFormatException ex) {
            throw new InvalidInputException("\nUndesired Input at " + matrixIndex + ". Matrix\nRow:" + (i + 1) + " Column:" + (j + 1) + "\nAll Comparison Values must be Numeric!!!", ex);
        }
        return comparisonArray;
    }

    boolean isMainCriteria(int index) {
        return myData.get(index).get(0).get(0) == null;
    }

    /**
     * @return matrixIndex which holds the current index of the Matrix
     */
    public int getMatrixIndex() {
        return matrixIndex;
    }

    /**
     * @param matrixIndex
     * @return Label of the matrix
     */
    public String getMatrixLabel(int matrixIndex) {//Returns the ID of the matrix. Root Matrix's ID is 'null'
        return myData.get(matrixIndex).get(0).get(0);
    }
}
