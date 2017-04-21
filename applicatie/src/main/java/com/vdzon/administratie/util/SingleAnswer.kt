package com.vdzon.administratie.util

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SingleAnswer (val answer: String)