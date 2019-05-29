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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to calculate TfIdf of term.
 * @author Mubin Shrestha
 */
public class PembobotanTFIDF {
    
    private List<Double> idfSave = new ArrayList<>();
    private List<double[]> tfidfDocsVector = new ArrayList<>();
    private double[] tfidfQueryvectors;
    private List<String> allTerms;

    public PembobotanTFIDF(List<String> termsUniq) {
         allTerms = termsUniq;
    }

    public PembobotanTFIDF() {
    }
    
      
    public void tfIdfCalculator(List<String[]> termsDocsArray) {
        double tf; //term frequency
        double idf = 0; //inverse document frequency
        double tfidf; //term requency inverse document frequency  
        
        
        for (int i = 0; i < termsDocsArray.size(); i++) {
            System.out.println(termsDocsArray.get(i).length);
            for (int j = 0; j < termsDocsArray.get(i).length; j++) {
                System.out.println(termsDocsArray.get(i)[j]);
            }
        }
        
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            
            System.out.println("terms" + allTerms.size());
            for (String terms : allTerms) {
                
                tf = tfCalculator(docTermsArray, terms);
                idf = idfCalculator(termsDocsArray, terms);
                
                tfidf = tf*idf ;
                tfidfvectors[count] = tfidf;
                
                idfSave.add(idf);
                count++;
            }

            tfidfDocsVector.add(tfidfvectors);  //string document vectors;  
            
 
        }
        
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            System.out.println(tfidfDocsVector.get(i).length);
            double[] sum = tfidfDocsVector.get(i);

            for (int j = 0; j < sum.length; j++) {
                //System.out.print(sum[j]);
            }
        }
  
    }
    
    public List <double[]> getTfidfDocsVector() {
        return tfidfDocsVector;
    }
    
    public List<Double> getIDFSave() {
        return idfSave;
    }

    public void tfidfQueryCalculator(String[] termsDocsArrayU, List<Double> idf) {
        double tf; //term frequency
        double tfidf; //term requency inverse document frequency        
        
        tfidfQueryvectors = new double[allTerms.size()];
            
        int count = 0;
        for (String terms : allTerms) {
            tf = tfCalculator(termsDocsArrayU, terms);
            tfidf = tf * idf.get(count);
            tfidfQueryvectors[count] = tfidf;
            
            System.out.println(terms+" : " + "tf*idf : " + tf +"*"+ idf.get(count) +" = " + tfidf );
                
            count++;
        }
  
    }
    
    public double[] getTfidfQueryVector() {
        return tfidfQueryvectors;
    }
    
    
    public double tfCalculator(String[] totalterms, String termToCheck) {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        return count;
    }


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
        
        System.out.println(listDoc.size() + "/" +count + " = " + Math.log(listDoc.size() / count));
        return Math.log10(listDoc.size() / count);
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
        System.out.println(data2);
        
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
