/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class DocumentParser {

    //This variable will hold all terms of each document in an array.
    private List <String[]> termsDocsArray = new ArrayList<>();
    private List <String[]> termsDocsArrayU = new ArrayList<>();
    private List <String> allTerms = new ArrayList<>(); //to hold all terms
    private List <double[]> tfidfDocsVector = new ArrayList<>();
    private List <Double> idfSave = new ArrayList<>();
    private List <double[]> tfidfDocsVectorQuery = new ArrayList<>();
    

    /**
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    
    public DocumentParser() {
        
    }
    
    
    public void parseFiles(String folderPath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(folderPath).listFiles();
        BufferedReader in = null;
        Kamus kamus = new Kamus();
        List <String> hasilStemm = new ArrayList<>();
        for (File f : allfiles) { //foreach for (int i=0; i<allfile.size; i++)
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                
                System.out.println(sb.toString());
                
                String[] tokenizedTerms = sb.toString().toLowerCase().replaceAll("[!\"$%&'()*\\+,.;:/<=>?\\[\\]^~_\\`{|}…0987654321]", "").split("\\W+");   //to get individual terms
                List <String> listTermDoc = new ArrayList<>(Arrays.asList(tokenizedTerms));
                listTermDoc.removeAll(kamus.readStopWord());  //stopword pada TermDoc
                    
                for (String stemm : listTermDoc){
                    Stemming st = new Stemming(); //Stemm
                    
                    //System.out.println(stemm);
                    
                    stemm = st.kataDasar(stemm);
                    
                    //System.out.println(stemm);
                    
                    
                    hasilStemm.add(stemm);
                       
                    if(!allTerms.contains(stemm)){ //menghindari duplikat entri. untuk mendapatkan seluruh term pada dokumen
                        allTerms.add(stemm);
                    }
                }                
                String[] termDocAfterStopWord = hasilStemm.toArray(new String[0]);
                
                termsDocsArray.add(termDocAfterStopWord); //token yang sudah dipraproses masuk ke termsDocArrays
                hasilStemm.clear();
            }
        }

    }
    
    public List <String> getAllTerms() {
        return allTerms;
    }
    
    public List <String[]> getTermsDocsArray() {
        return termsDocsArray;
    }
    
    
    /**
     * Method to create termVector according to its tfidf score.
     */
    
    
    
    
    
}

