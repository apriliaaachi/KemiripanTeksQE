/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Asus
 */
public class Document {
    private String isiDokumen;
    private String judulDokumen;
    

    public Document(String judulDokumen, String isiDokumen) {
        this.isiDokumen = isiDokumen;
        this.judulDokumen = judulDokumen;
    }

    public String getIsiDokumen() {
        return isiDokumen;
    }

    public void setIsiDokumen(String isiDokumen) {
        this.isiDokumen = isiDokumen;
    }

    public String getJudulDokumen() {
        return judulDokumen;
    }

    public void setJudulDokumen(String judulDokumen) {
        this.judulDokumen = judulDokumen;
    }
}
