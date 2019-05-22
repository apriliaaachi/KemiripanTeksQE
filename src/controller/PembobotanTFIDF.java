/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Asus
 */
import java.util.Arrays;
import java.util.List;

/**
 * Class to calculate TfIdf of term.
 * @author Mubin Shrestha
 */
public class PembobotanTFIDF {
    
    /**
     * Calculates the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String[] totalterms, String termToCheck) {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        return count / totalterms.length;
    }

    /**
     * Calculates idf of term termToCheck
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(List<String[]> listDoc, String termToCheck) {
        double count = 0;
        for (String[] ss : listDoc) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return 1 + Math.log(listDoc.size() / count);
    }
    
    public int dfCalculator(List<String[]> listDoc, String termToCheck) {
        int count = 0;
        for (String[] ss : listDoc) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
    
    public double tfPairCalculator(String[] totalterms, String[] termToCheck) {
        double countArray0 = 0;  //to count the overall occurrence of the term termToCheck
        double countArray1 = 0;
        //setiap string isi array dokumen dicek tengan string isi list allTerm
        String data1 = termToCheck[0];
        String data2 = termToCheck[1];
        
        for (String s : totalterms) {
            //jika string a = string b
            if (s.equalsIgnoreCase(data1)) {
                countArray0++;
            }
        }
        
        for (String s : totalterms) {
            //jika string a = string b
            if (s.equalsIgnoreCase(data2)) {
                countArray1++;
            }
        }
        
        if (countArray0 < countArray1){
            return countArray0; 
        }else{
            return countArray1;
        }
    }
    
    

    /**
     * Calculated idf of term termToCheck
     * @param listDoc : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfPairCalculator(List<String[]> listDoc, String[] termToCheck) {
        double count = 0;
        String term1 = termToCheck[0];
        String term2 = termToCheck[1];
        
        for (String[] ss : listDoc) {
            if (Arrays.asList(ss).contains(term1) && Arrays.asList(ss).contains(term2)) {
                count++;
            }
        }
        return Math.log10(listDoc.size() / count);                   
 
    }
}
