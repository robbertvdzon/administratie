package com.vdzon.administratie.pdfgenerator.pdfutil

class TabelCols {
    private val col = IntArray(10)

    constructor(col1: Int, col2: Int, col3: Int) {
        col[0] = col1
        col[1] = col2
        col[2] = col3
    }

    constructor(col1: Int, col2: Int, col3: Int, col4: Int, col5: Int) {
        col[0] = col1
        col[1] = col2
        col[2] = col3
        col[3] = col4
        col[4] = col5
    }

    fun getCol(i: Int): Int {
        return col[i]
    }

}
