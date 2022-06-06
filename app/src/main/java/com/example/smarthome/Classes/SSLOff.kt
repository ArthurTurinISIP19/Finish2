package com.example.smarthome.Classes

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class SSLOff {
    fun trustAllCertificates() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }

                    override fun checkClientTrusted(
                        certs: Array<X509Certificate>,
                        AuthType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        certs: Array<X509Certificate>,
                        AuthType: String
                    ) {
                    }
                }
            )
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { arg0, arg1 -> true }
        } catch (e: Exception) {
        }
    }
}