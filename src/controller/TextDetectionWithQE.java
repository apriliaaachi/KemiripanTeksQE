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
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class TextDetectionWithQE {
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
            
            String join = String.join(" ", hasilStemm);
            System.out.println(getExpansionTerm(join));
            termDocArray = getExpansionTerm(join).split(" ");
            
            for (int i = 0; i < termDocArray.length; i++) {
                System.out.println(termDocArray[i]);
            }
                
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
        
        //System.out.println(keyword);
        
        String kata[] = keyword.split(" ");
        for(String w : kata){
            expandKata.add(w);
            System.out.println("expanda kata : " + w);
            for(String[] ss : bacaThesaurus){
                String kata1 = ss[0];
                String kata2 = ss[1];
                
                if (w.equalsIgnoreCase(kata1)){
                    expandKata.add(kata2);
                }
            }
        }
        
        //expandKata = new ArrayList<String>(new LinkedHashSet<String>(expandKata));
        String expandTerms = String.join(" ",expandKata); //ubah list jd string
        return expandTerms;
   }

}
