package com.vdzon.administratie.rest

import com.google.inject.Injector

interface Rest{
    fun initRest(injector: Injector)
}