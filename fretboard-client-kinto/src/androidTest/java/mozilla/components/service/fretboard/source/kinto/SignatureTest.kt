package mozilla.components.service.fretboard.source.kinto

import android.support.test.runner.AndroidJUnit4
import android.util.Base64
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

@RunWith(AndroidJUnit4::class)
class SignatureTest {
    @Test
    fun testSignature() {
        val publicKeyBytes = Base64.decode("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE6Sn1qgMg7IdiEswcnG/GOyai8oAciiPbyNlmdlVmhk2H5gdbUPDxY72a7Q6mXjvA4bNiojK77P7qM4jNs7HpWEIXdJs+c93TLougQ+kjygfNyu8Z9q9plqZQGcbsJzyr", 0)
        val spec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        val publicKey = keyFactory.generatePublic(spec)
        val dsa = Signature.getInstance("SHA384withECDSA")
        dsa.initVerify(publicKey)
        val str = "{\"data\":[{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"id\":\"02d7caa1-cd9e-6949-084c-18bc9d468b6b\",\"last_modified\":1482262302021,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$|^org.mozilla.firefox_beta\$\"},\"name\":\"triple-readerview-bookmark-prompt\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Enable Leanplum SDK - Bug 1351571 \\nExpand English Users to more region - Bug 1411066\\nEnable  50% eng|zho|deu globally for Leanplum. see https://bugzilla.mozilla.org/show_bug.cgi?id=1411066#c8\",\"id\":\"12f8f0dc-6401-402e-9e7d-3aec52576b87\",\"last_modified\":1523549895713,\"match\":{\"appId\":\"^org.mozilla.firefox_beta\$|^org.mozilla.firefox\$\",\"lang\":\"eng|zho|deu|fra|ita|ind|por|spa|pol|rus\",\"regions\":[]},\"name\":\"leanplum-start\",\"schema\":1523549592861},{\"buckets\":{\"max\":\"50\",\"min\":\"0\"},\"description\":\"Arrange tabs in two columns in portrait mode (tabs tray)\",\"id\":\"14fdc9f3-cf11-4bee-84f6-98495d08c61f\",\"last_modified\":1482242613284,\"match\":{},\"name\":\"compact-tabs\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"0\",\"min\":\"0\"},\"id\":\"1829570e-f582-298b-63b3-3c9d8380be6b\",\"last_modified\":1467794476875,\"match\":{},\"name\":\"content-notifications-8am\"},{\"buckets\":{\"max\":\"100\",\"min\":\"50\"},\"id\":\"1d05fa3e-095f-b29a-d9b6-ab3a578efd0b\",\"last_modified\":1482264639326,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$|^org.mozilla.firefox_beta\$\"},\"name\":\"promote-add-to-homescreen\",\"schema\":1480618438089,\"values\":{\"lastVisitMaximumAgeMs\":600000,\"lastVisitMinimumAgeMs\":30000,\"minimumTotalVisits\":5}},{\"buckets\":{\"max\":\"0\",\"min\":\"0\"},\"id\":\"3e4cef10-3a87-3cdd-4562-0062c2a9125b\",\"last_modified\":1467794476938,\"match\":{},\"name\":\"content-notifications-12hrs\"},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Show addon menu item in top-level.\",\"id\":\"46894232-177a-4cd1-b620-47c0b8e5e2aa\",\"last_modified\":1498599522440,\"match\":{},\"name\":\"top-addons-menu\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"id\":\"4d2fa5c3-18b2-8734-49be-fe58993d2cf6\",\"last_modified\":1485853244635,\"match\":{\"appId\":\"\"},\"name\":\"download-content-catalog-sync\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Allows apps to open tabs in a customized UI.\",\"id\":\"5e23b482-8800-47be-b6dc-1a3bb6e455d4\",\"last_modified\":1510211043874,\"match\":{\"regions\":[]},\"name\":\"custom-tabs\",\"schema\":1510207707840},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"id\":\"5e4277e0-1029-ea14-1b74-5d25d301c5dc\",\"last_modified\":1497643056372,\"match\":{\"appId\":\"^org.mozilla.fennec|org.mozilla.firefox_beta\"},\"name\":\"offline-cache\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Show a setting in \\\"experimental features\\\" for enabling/disabling activity stream.\",\"id\":\"7a022463-67fd-4ba3-8b06-a79d0c5e1fdc\",\"last_modified\":1496331790186,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$\"},\"name\":\"activity-stream-setting\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Enable Activity stream by default for users in the \\\"opt out\\\" group.\",\"id\":\"7d504093-67c4-4afb-adf5-5ad23c7c1995\",\"last_modified\":1500969355986,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$\",\"regions\":[]},\"name\":\"activity-stream-opt-out\",\"schema\":1498764179980},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"id\":\"9a53ebfa-772d-d2d8-8307-f98943310360\",\"last_modified\":1467794477013,\"match\":{},\"name\":\"bookmark-history-menu\"},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Bug 1232439 - Show full-page edit bookmark dialog\",\"id\":\"9ae1019b-9107-47c5-83f3-afa73360b020\",\"last_modified\":1498690258010,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$\"},\"name\":\"full-bookmark-management\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"0\",\"min\":\"0\"},\"id\":\"c011528e-e03a-7272-6d8b-ef1d4bea4689\",\"last_modified\":1467794476838,\"match\":{},\"name\":\"content-notifications-5pm\"},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Enable/Disable Activity Stream\",\"id\":\"d4fd9cfb-4c8b-4963-b21e-1c2f4bcd61d6\",\"last_modified\":1496331773809,\"match\":{\"appId\":\"^org.mozilla.fennec.*\$\"},\"name\":\"activity-stream\",\"schema\":1480618438089},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"id\":\"d9f9f124-a4d6-47db-a9f4-cf0d00915088\",\"last_modified\":1477907551487,\"match\":{},\"name\":\"hls-video-playback\",\"schema\":1467794476773},{\"buckets\":{\"max\":\"0\",\"min\":\"0\"},\"id\":\"d9fd5223-965c-2f0d-a798-b8cbc96f6e09\",\"last_modified\":1467794476893,\"match\":{},\"name\":\"whatsnew-notification\"},{\"buckets\":{\"max\":\"100\",\"min\":\"0\"},\"description\":\"Gate flag for controlling if background telemetry processing (sync ping) is enabled or not.\",\"id\":\"e6f9d217-3f43-478f-bff3-7829d7b9eeeb\",\"last_modified\":1496971360625,\"match\":{},\"name\":\"process-background-telemetry\",\"schema\":1480618438089}],\"last_modified\":\"1523907700330\"}"
        val data = str
        val strByte = data.toByteArray(StandardCharsets.UTF_8)
        dsa.update(strByte)

        val foo = "kRhyWZdLyjligYHSFhzhbyzUXBoUwoTPvyt9V0e-E7LKGgUYF2MVfqpA2zfIEDdqrImcMABVGHLUx9Nk614zciRBQ-gyaKA5SL2pPdZvoQXk_LLsPhEBgG4VDnxG4SBL".replace("-", "+").replace("_", "/")

        val signatureBytes = Base64.decode("kRhyWZdLyjligYHSFhzhbyzUXBoUwoTPvyt9V0e-E7LKGgUYF2MVfqpA2zfIEDdqrImcMABVGHLUx9Nk614zciRBQ-gyaKA5SL2pPdZvoQXk_LLsPhEBgG4VDnxG4SBL".replace("-", "+").replace("_", "/"), 0)
        val verified = dsa.verify(signatureBytes)

        assertTrue(verified)
    }
}
