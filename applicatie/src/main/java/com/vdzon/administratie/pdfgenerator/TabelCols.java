package com.vdzon.administratie.pdfgenerator;

public class TabelCols {
    private int[] col = new int[10];

    public TabelCols(int col1, int col2, int col3) {
        col[0] = col1;
        col[1] = col2;
        col[2] = col3;
    }

    public TabelCols(int col1, int col2, int col3, int col4, int col5) {
        col[0] = col1;
        col[1] = col2;
        col[2] = col3;
        col[3] = col4;
        col[4] = col5;
    }

    public int getCol(int i){
        return col[i];
    }

}
