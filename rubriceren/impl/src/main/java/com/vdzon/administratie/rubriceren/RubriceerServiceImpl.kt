package com.vdzon.administratie.rest.rubriceren

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.rubriceren.model.RubriceerRegel
import com.vdzon.administratie.rubriceren.model.RubriceerRegels
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRule
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRuleCommit
import com.vdzon.administratie.util.ReflectionUtils.callMethod
import com.vdzon.administratie.util.ReflectionUtils.getInstance
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import spark.Request
import spark.Response
import java.lang.reflect.Method
import java.util.*
import javax.inject.Inject

class RubriceerServiceImpl : RubriceerService {
    @Inject
    lateinit internal var daoService: UserDao

    private val methodsAnnotatedWithRule: Set<Method>
    private val methodsAnnotatedWithCommit: Set<Method>

    init {
        val reflections = Reflections(RUBRICEER_PACKAGE, MethodAnnotationsScanner())
        methodsAnnotatedWithRule = reflections.getMethodsAnnotatedWith(RubriceerRule::class.java)
        methodsAnnotatedWithCommit = reflections.getMethodsAnnotatedWith(RubriceerRuleCommit::class.java)

    }

    override fun getRubriceerRegels(req: Request, res: Response): RubriceerRegels {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        val regels = getRubriceerRegels(gebruiker)
        return RubriceerRegels(rubriceerRegelList = regels)
    }

    override fun rubriceerRegels(req: Request, res: Response): SingleAnswer {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        val rubriceerRegels = getRubriceerRegels(req)
        processRubriceerRegels(gebruiker, rubriceerRegels)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun processRubriceerRegels(gebruiker: Gebruiker, rubriceerRegels: RubriceerRegels) {
        methodsAnnotatedWithCommit.forEach { method -> callRubriceerCommitAction(method, gebruiker, rubriceerRegels) }
    }

    @Throws(java.io.IOException::class)
    private fun getRubriceerRegels(req: Request): RubriceerRegels {
        val regelsJson = req.body()
        val mapper = jacksonObjectMapper()
        return mapper.readValue(regelsJson, RubriceerRegels::class.java)
    }

    private fun getRubriceerRegels(gebruiker: Gebruiker): List<RubriceerRegel> {
        val regels = ArrayList<RubriceerRegel>()
        for (afschrift in gebruiker.defaultAdministratie.afschriften) {
            updateRegels(gebruiker, regels, afschrift)
        }
        return regels
    }

    private fun updateRegels(gebruiker: Gebruiker, regels: List<RubriceerRegel>, afschrift: Afschrift) {
        methodsAnnotatedWithRule.forEach { method -> callRubriceerAction(method, gebruiker, regels, afschrift) }
    }

    private fun callRubriceerAction(method: Method, gebruiker: Gebruiker, regels: List<RubriceerRegel>, afschrift: Afschrift) {
        val o = getInstance(method.declaringClass)
        val boekingenCache = BoekingenCache(gebruiker.defaultAdministratie.boekingen)
        callMethod(method, o, gebruiker, regels, afschrift, boekingenCache)
    }

    private fun callRubriceerCommitAction(method: Method, gebruiker: Gebruiker, rubriceerRegels: RubriceerRegels) {
        val o = getInstance(method.declaringClass)
        rubriceerRegels.rubriceerRegelList.forEach { regel -> callMethod(method, o, regel, gebruiker) }
    }

    companion object {
        private val RUBRICEER_PACKAGE = "com.vdzon.administratie.rubriceren.rubriceerRegels"
    }

}
