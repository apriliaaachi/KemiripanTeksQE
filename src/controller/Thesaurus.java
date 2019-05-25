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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author Asus
 */
public class Thesaurus {
    private List <String[]> termsDocArrays = new ArrayList<>();
    private List <String[]> termsDocArraysNotDuplicate = new ArrayList<>();
    private List <String> allTerms = new ArrayList<>();
    private List <String> pairTerms = new ArrayList<>();
    private List<String[]> allPairsTerms = new ArrayList<>();
    private List <double[]> tfidfDocsVectorTerm = new ArrayList<>();
    private List <double[]> tfidfDocsVectorPairTerm = new ArrayList<>();
    private List <Double> totalTermWij = new ArrayList<>();
    private List <Double> dfVector = new ArrayList<>();
    private List <Double> totalPairsWijk = new ArrayList<>();
    private List <Double> clusterWeight = new ArrayList<>();
    private List <String[]> validPairTerm = new ArrayList<>();
    private List <String> expandTerm = new ArrayList<>();
    private String[] dok = new String[120];
    
    
    
    public Thesaurus(List <String[]> termsDoc, List <String>allUniqueTerms) {
        termsDocArrays = termsDoc;
        allTerms = allUniqueTerms;
    }
    
    public void prepareData (int windowSize) throws FileNotFoundException, IOException {
        
        //termDocArrays remove same value on string array
        for(String[] listTermDoc : termsDocArrays){
            listTermDoc = new LinkedHashSet<String>(Arrays.asList(listTermDoc)).toArray(new String[0]);
            termsDocArraysNotDuplicate.add(listTermDoc);
        }
        
        //get pair windowsSize 2. menggunakan dokumen yang disetiap dokumen tidak ada kata yang duplikat. 
        //untuk mendapatkan pasangan term disetiap dokumen
        for(String[] listTermDoc : termsDocArraysNotDuplicate){
            
            for (int i = 0; i < listTermDoc.length; i++){
                for(int j = i-windowSize; j <= i+windowSize; j++){
                    if(j>=0 && j<listTermDoc.length && i!=j){
                        pairTerms.add(listTermDoc[i]+ " "+listTermDoc[j]);
                    }
                }
            }
        }
        
        //pair yg isinya string, diubah jadi list<string>
        List <List<String>> bc = new ArrayList<>();         
        for(String pair : pairTerms){
            List<String> cc = new ArrayList<>(Arrays.asList(pair.split(" "))); 
            if(!bc.contains(cc)){ // menghindari duplikat pairTerms. untuk foreach type data primitif String/String[] tidak boleh ada operasi add/remove, jadi ubah dalam bentuk list(bc) 
                bc.add(cc);
            }
        }  
        
        //rubah List<String> jadi String[]
        for(List<String> l : bc){
            String [] startArr = l.toArray(new String[0]);
            allPairsTerms.add(startArr);
        }
                
       
    }
    
    public void tfIdfTermCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency 
        double df = 0;
       
        //get tfidf vector
        for (String[] docTermsArray : termsDocArrays) {
            double[] tfidfvectors = new double[allTerms.size()];
                      
            int count = 0;
            for (String terms : allTerms) {
                tf = new PembobotanTFIDF().tfCalculator(docTermsArray, terms);
                idf = new PembobotanTFIDF().idfCalculator(termsDocArrays, terms);  
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;               
                count++; 
            }
            tfidfDocsVectorTerm.add(tfidfvectors);  //storing document vectors;              
        }
        
        //get df
        for (String terms : allTerms) {
            df = new PembobotanTFIDF().dfCalculator(termsDocArrays, terms);
            dfVector.add(df);
        }
                
        //get total Wij        
        double[] total = new double [allTerms.size()];
        for(double[] s : tfidfDocsVectorTerm){
            for (int i=0; i<total.length; i++){
                total[i] += s[i]; 
            }
        }
        
        //simpan double[] total ke dalam List <Double> totalTermWij
        for (double d : total){
            totalTermWij.add(d);
        } 
            
    }
    
    /**
     * Fungsi untuk mengembalikan index allTerm untuk pencarian saat mendapatkan nilai pada pairTerm kata ke2
     */
    public int locateWord(String cari, List <String> y){     
        int index = 0;
        for (int i=0; i<=y.size()-1; i++){
            if (y.get(i).equals(cari)){
                index = i;
                break;
            }
        }
        return index;
    }    
      
    /**
     * Method to create termVector according to its tfidf pairAllTerm score.
     * output : tfidf vector allPairTerm
     * output : total dari penjumlahan bobot/tfidf allPairTerm setiap dokumen
     */
    public void tfIdfPairsTermCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency
                
        //get tfidf vector pairTerm
        for (String[] docTermsArray : termsDocArrays) { 
            double[] tfidfvectors = new double[allPairsTerms.size()];
            int count = 0;
    
            for (String[] terms : allPairsTerms) {      
                tf = new PembobotanTFIDF().tfPairCalculator(docTermsArray, terms);
                idf = new PembobotanTFIDF().idfPairCalculator(termsDocArrays, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;               
                count++; 
            }         
            tfidfDocsVectorPairTerm.add(tfidfvectors);  //storing document vectors;              
        }
                     
        //to get total Wijk
        double[] total = new double [allPairsTerms.size()];
        for(double []s : tfidfDocsVectorPairTerm){           
            for (int i=0; i<total.length; i++){
                total[i] += s[i];
            }
        }
       
        //simpan double[] total ke dalam LIST
        for (double d : total){
            totalPairsWijk.add(d);
        } 
    }
    
    /**
     * Methode untuk menghitung nilai ClusterWeight
     * input: totalWij , totalPairWijk, df
     * output : kemiripan hasilclusterweight diatas 0,5 menjadi kandidat thesaurus. hasil disimpan dalam file text
     * @throws IOException 
     */
    public void clusterWeight() throws IOException{
        int j = 0;
        double hasilClusterWeight;
        
        for (String [] s : allPairsTerms){   
            String data1 = s[0];
            String data2 = s[1];
            Double WijBobot = totalTermWij.get(locateWord(data1, allTerms));
            Double dfk = dfVector.get(locateWord(data2, allTerms));
            Double bobotWijk = totalPairsWijk.get(j);
            double N = termsDocArrays.size();
      
            hasilClusterWeight = (bobotWijk/WijBobot)*(Math.log10(N/dfk)/Math.log10(N));            
            System.out.println(Arrays.toString(s)+" , Wijk = "+bobotWijk+", Wij = "+WijBobot+", dfk = "+dfk+", N = "+N+ " CW = "+hasilClusterWeight);          
            j++;
            clusterWeight.add(hasilClusterWeight);
        }        
               
        //untuk validasii hasil clusterWeight yang nilainya diatas 0.5
        for (int i=0; i<allPairsTerms.size(); i++){ 
            if (clusterWeight.get(i) > 0.5 && !clusterWeight.get(i).isInfinite()){
                System.out.println(clusterWeight.get(i));
                validPairTerm.add(allPairsTerms.get(i));
            } 
        }  
                
        //print validPairTerm
        for (String[] ss: validPairTerm){
            System.out.println(Arrays.toString(ss));
        }
        System.out.println(validPairTerm.size());   
            
        //Tulis hasil thesaurus ke file .txt
        FileWriter fw = new FileWriter("hasilThesaurusSize2.txt"); 
        for(String[] str: validPairTerm) {
            for (int i = 0; i < str.length; i++) {
                fw.write(str[i] + " ");
            }     
            fw.write("\n");
        }
        fw.close(); 
                
    }
}
