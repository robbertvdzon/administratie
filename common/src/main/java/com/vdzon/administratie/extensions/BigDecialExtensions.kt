package com.vdzon.administratie.extensions

import java.math.BigDecimal

fun BigDecimal.normalizedCopy()= BigDecimal(this.toDouble()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
fun BigDecimal.compareToNormalized(other:BigDecimal)= this.normalizedCopy().compareTo(other.normalizedCopy())

