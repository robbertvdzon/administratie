package com.vdzon.administratie.rest.rubriceren

import com.vdzon.administratie.rubriceren.model.RubriceerRegels
import spark.Request
import spark.Response

interface RubriceerService {

    @Throws(Exception::class)
    fun getRubriceerRegels(req: Request, res: Response): RubriceerRegels

    @Throws(Exception::class)
    fun rubriceerRegels(req: Request, res: Response): com.vdzon.administratie.util.SingleAnswer
}
