package com.example.labproject.menu;

public class EbookData
{

    private String pdfUri,pdfTitle;

    public EbookData() {
    }

    public EbookData(String pdfUri, String pdfTitle) {
        this.pdfUri = pdfUri;
        this.pdfTitle = pdfTitle;
    }

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }
}
