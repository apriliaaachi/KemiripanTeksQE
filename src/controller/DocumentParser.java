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
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        Kamus kamus = new Kamus();
        List <String> hasilStemm = new ArrayList<>();
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                List <String> listTermDoc = new ArrayList<String>(Arrays.asList(tokenizedTerms));
                listTermDoc.removeAll(kamus.readStopWord());  //stopword pada TermDoc
                    
                for (String stemm : listTermDoc){
                    Stemming st = new Stemming(); //Stemm
                    stemm = st.kataDasar(stemm);
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
    
    public void parseFilesU(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        Kamus kamus = new Kamus();
        List <String> hasilStemm = new ArrayList<>();
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                List <String> listTermDoc = new ArrayList<String>(Arrays.asList(tokenizedTerms));
                listTermDoc.removeAll(Arrays.asList(kamus.readStopWord()));  //stopword pada TermDoc
                    
                for (String stemm : listTermDoc){
                    Stemming st = new Stemming(); //Stemm
                    stemm = st.kataDasar(stemm);
                    hasilStemm.add(stemm);

                }                
                String[] termDocAfterStopWord = hasilStemm.toArray(new String[0]);
                
                termsDocsArrayU.add(termDocAfterStopWord); //token yang sudah dipraproses masuk ke termsDocArrays
                hasilStemm.clear();
            }
        }

    }

    /**
     * Method to create termVector according to its tfidf score.
     */
    public void tfIdfCalculator() {
        double tf; //term frequency
        double idf = 0; //inverse document frequency
        double tfidf; //term requency inverse document frequency        
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new PembobotanTFIDF().tfCalculator(docTermsArray, terms);
                idf = new PembobotanTFIDF().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
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
        
        System.out.println("idf " + idfSave.size());
        
        
    }

    public void tfidfQueryCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency        
        for (String[] docTermsArray : termsDocsArrayU) {
            double[] tfidfvectors = new double[allTerms.size()];
            
            int count = 0;
            for (String terms : allTerms) {
                tf = new PembobotanTFIDF().tfCalculator(docTermsArray, terms);
                tfidf = tf * idfSave.get(count);
                tfidfvectors[count] = tfidf;
                
                count++;
            }
  
            tfidfDocsVectorQuery.add(tfidfvectors);  //string document vectors;    
    
        }
        
        for (int i = 0; i < tfidfDocsVectorQuery.size(); i++) {
            System.out.println(tfidfDocsVectorQuery.get(i).length);
            for (int j = 0; j < tfidfDocsVectorQuery.get(i).length; j++) {
                //System.out.print(idfSave.get(i)[j]);
            }
            //System.out.println("");
        }
        
    }
    
    public void getCosineSimilarity() {
        for (int i = 0; i < tfidfDocsVectorQuery.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                    + new CosineSimilarity().cosineSimilarity
                    (
                       tfidfDocsVectorQuery.get(i), 
                       tfidfDocsVector.get(j)
                    )
                );
            }
        }
    }
    
    public String getExpansionTerm(String keyword) throws FileNotFoundException{
        //baca kamus Thesaurus Akhir (validasi Kateglo)
        Scanner sc = new Scanner(new File("hasilThesaurusSize2.txt"));
        List<String> lines = new ArrayList<String>();
        List<String[]> bacaThesaurus = new ArrayList<>();
        List <String> expandKata = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
  
        String[] arr = lines.toArray(new String[0]);
       // System.out.println("get Thesaurus : "+Arrays.toString(arr));
        
        for(String ss : arr){
            bacaThesaurus.add(ss.split(" "));
        }
        
        //get expand term
        String kata[] = keyword.split(" ");
        for(String w : kata){
            expandKata.add(w);
            for(String[] ss : bacaThesaurus){
                String kata1 = ss[0];
                String kata2 = ss[1];
                
                if (w.equalsIgnoreCase(kata1)){
                    expandKata.add(kata2);
                }
            }
        }
        expandKata = new ArrayList<String>(new LinkedHashSet<String>(expandKata));
        String expandTerms = String.join(" ",expandKata); //ubah list jd string
        return expandTerms;
   }
}

