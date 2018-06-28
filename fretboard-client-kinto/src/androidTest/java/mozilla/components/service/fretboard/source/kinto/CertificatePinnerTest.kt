/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import android.support.test.runner.AndroidJUnit4
import android.util.Base64
import mozilla.components.service.fretboard.source.kinto.CertificatePinner
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.internal.tls.SslClient
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection

@RunWith(AndroidJUnit4::class)
class CertificatePinnerTest {
    @Test
    fun testCheckCertificatePinningNoPins() {
        val server = MockWebServer()
        server.useHttps(SslClient.localhost().socketFactory, false)
        server.enqueue(MockResponse().setBody("B"))
        HttpsURLConnection.setDefaultSSLSocketFactory(SslClient.localhost().socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        val connection = server.url("/").url().openConnection() as HttpsURLConnection
        connection.connect()
        assertTrue(CertificatePinner().checkCertificatePinning(connection, setOf()))
    }

    @Test()
    fun testCheckCertificatePinningBadPin() {
        val server = MockWebServer()
        server.useHttps(SslClient.localhost().socketFactory, false)
        server.enqueue(MockResponse().setBody("B"))
        HttpsURLConnection.setDefaultSSLSocketFactory(SslClient.localhost().socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        val connection = server.url("/").url().openConnection() as HttpsURLConnection
        connection.connect()
        assertFalse(CertificatePinner().checkCertificatePinning(connection, setOf("b"), arrayOf(SslClient.localhost().trustManager)))
    }

    @Test
    fun testCheckCertificatePinningGoodPin() {
        val server = MockWebServer()
        server.useHttps(SslClient.localhost().socketFactory, false)
        server.enqueue(MockResponse().setBody("B"))
        HttpsURLConnection.setDefaultSSLSocketFactory(SslClient.localhost().socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        val connection = server.url("/").url().openConnection() as HttpsURLConnection
        connection.connect()
        val certificatePublicKey = connection.serverCertificates[0].publicKey.encoded
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(certificatePublicKey)
        val pin = Base64.encodeToString(digest.digest(), Base64.NO_WRAP)
        assertTrue(CertificatePinner().checkCertificatePinning(connection, setOf(pin), arrayOf(SslClient.localhost().trustManager)))
    }

    @Test
    fun testCheckCertificatePinningBadCertificate() {
        val server = MockWebServer()
        server.useHttps(SslClient.localhost().socketFactory, false)
        server.enqueue(MockResponse().setBody("B"))
        HttpsURLConnection.setDefaultSSLSocketFactory(SslClient.localhost().socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        val connection = server.url("/").url().openConnection() as HttpsURLConnection
        connection.connect()
        val certificatePublicKey = connection.serverCertificates[0].publicKey.encoded
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(certificatePublicKey)
        val pin = Base64.encodeToString(digest.digest(), Base64.NO_WRAP)
        assertFalse(CertificatePinner().checkCertificatePinning(connection, setOf(pin)))
    }
}
