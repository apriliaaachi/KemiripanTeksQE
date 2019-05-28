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
import java.util.List;

/**
 *
 * @author Asus
 */
public class TextDetectionWithoutQE {
    private String[] termDocArray;
    
    public void parseFilesU(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        BufferedReader in = null;
        Kamus kamus = new Kamus();
        List <String> hasilStemm = new ArrayList<>();

        if (file != null) {
            in = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
                
            String[] tokenizedTerms = sb.toString().toLowerCase().replaceAll("[!\"$%&'()*\\+,.;:/<=>?\\[\\]^~_\\`{|}…0987654321]", "").split("\\W+");   //to get individual terms
            List <String> listTermDoc = new ArrayList<String>(Arrays.asList(tokenizedTerms));
            listTermDoc.removeAll(kamus.readStopWord());  //stopword pada TermDoc
                    
            for (String stemm : listTermDoc){
                Stemming st = new Stemming(); //Stemm
                stemm = st.kataDasar(stemm);
                hasilStemm.add(stemm);

            }  
            
            termDocArray = hasilStemm.toArray(new String[0]);
                
            hasilStemm.clear();
        }

    }
    
    public String[] getTermDocArray() {
        return termDocArray;
    }
    
    public void getCosineSimilarity(List <double[]> tfidfDocsVector, double[] tfidfQueryvectors) {
        
        for (int j = 0; j < tfidfDocsVector.size(); j++) {
            System.out.println("between " + "query" + " and " + j + "  =  "
                + new CosineSimilarity().cosineSimilarity
                (
                   tfidfQueryvectors, 
                   tfidfDocsVector.get(j)
                )
            );
        }
        
    }

    
}
