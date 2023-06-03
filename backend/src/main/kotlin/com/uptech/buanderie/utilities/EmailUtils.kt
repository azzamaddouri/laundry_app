package com.uptech.buanderie.utilities

import com.mailjet.client.ClientOptions
import com.mailjet.client.MailjetClient
import com.mailjet.client.MailjetRequest
import com.mailjet.client.errors.MailjetException
import com.mailjet.client.resource.Emailv31
import com.mailjet.client.transactional.SendContact
import com.mailjet.client.transactional.SendEmailsRequest
import com.mailjet.client.transactional.TrackOpens
import com.mailjet.client.transactional.TransactionalEmail
import org.json.JSONArray
import org.json.JSONObject

object EmailUtils {
    var options = ClientOptions.builder()
        .apiKey("b3e3740fcdb5ffb1aa662338dff97979")
        .apiSecretKey("2943a4e6a237d601d1e0a511b18709e9")
        .build()

    var client = MailjetClient(options)

    @Throws(MailjetException::class)
    fun sendResetPasswordEmail(email: String, firstName: String, lastName: String, token: String) {
        val resetUrl = Utils.baseUrl + "reset-password?token=" + token

        val request =  MailjetRequest(Emailv31.resource)
            .property(
                Emailv31.MESSAGES,  JSONArray()
                .put( JSONObject()
                    .put(
                        Emailv31.Message.FROM,  JSONObject()
                        .put("Email", "noreply@uptech.com.tn")
                        .put("Name", "Reset mot de passe"))
                    .put(
                        Emailv31.Message.TO,  JSONArray()
                        .put( JSONObject()
                            .put("Email", email)
                            .put("Name", "$firstName $lastName")))
                    .put(Emailv31.Message.TEMPLATEID, 3678526)
                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                    .put(Emailv31.Message.SUBJECT, "Reset mot de passe")
                    .put(
                        Emailv31.Message.VARIABLES,  JSONObject()
                        .put("resetPasswordLink", resetUrl))))

        val response = client.post(request)
        println(response.status)
        println(response.data)

    }

    @Throws(MailjetException::class)
    fun sendSetPasswordEmail(email: String, firstName: String, lastName: String, token: String) {
        val setUrl = Utils.baseUrl + "set-password?token=" + token

        val request =  MailjetRequest(Emailv31.resource)
            .property(
                Emailv31.MESSAGES, JSONArray()
                .put(
                    JSONObject()
                    .put(
                        Emailv31.Message.FROM, JSONObject()
                        .put("Email", "noreply@uptech.com.tn")
                        .put("Name", "Configuration Mot de Passe"))
                    .put(
                        Emailv31.Message.TO, JSONArray()
                        .put( JSONObject()
                            .put("Email", email)
                            .put("Name", "$firstName $lastName")))
                    .put(Emailv31.Message.TEMPLATEID, 3674901)
                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                    .put(Emailv31.Message.SUBJECT, "Configuration Mot de Passe")
                    .put(
                        Emailv31.Message.VARIABLES,  JSONObject()
                        .put("setPasswordLink", setUrl ))))

        val response = client.post(request)
        println(response.status)
        println(response.data)

    }

    /*@Throws(MailjetException::class)
    fun sendDevis(email: String, firstName: String, lastName: String, itemList:MutableList<DevisEmailObj>,totalHT : Double,tva:Int , totalTTC:Double) {


        val request = MailjetRequest(Emailv31.resource)
            .property(Emailv31.MESSAGES, JSONArray()
                .put(JSONObject()
                    .put(Emailv31.Message.FROM, JSONObject()
                        .put("Email", "noreply@uptech.com.tn")
                        .put("Name", "Devis 3Stars Service"))
                    .put(Emailv31.Message.TO, JSONArray()
                        .put(JSONObject()
                            .put("Email", email)
                            .put("Name", "$firstName $lastName")))
                    .put(Emailv31.Message.TEMPLATEID, 3697017)
                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                    .put(Emailv31.Message.SUBJECT, "Devis 3Stars Service")
                    .put(Emailv31.Message.VARIABLES, JSONObject()
                        .put("itemList", itemList)
                        .put("totalht", totalHT)
                        .put("tva", tva)
                        .put("totalttc", totalTTC))))

        val response = client.post(request)
        println(response.status)
        println(response.data)

    }*/



}