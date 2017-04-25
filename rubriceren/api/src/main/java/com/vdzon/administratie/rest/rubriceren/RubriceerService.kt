package com.vdzon.administratie.rest.rubriceren

import spark.Request
import spark.Response

interface RubriceerService {

    @Throws(Exception::class)
    fun getRubriceerRegels(req: Request, res: Response): Any

    @Throws(Exception::class)
    fun rubriceerRegels(req: Request, res: Response): Any
}
