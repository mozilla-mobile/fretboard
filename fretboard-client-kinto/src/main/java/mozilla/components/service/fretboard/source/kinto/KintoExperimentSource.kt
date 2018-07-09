/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.fretboard.source.kinto

import android.util.Base64
import mozilla.components.service.fretboard.Experiment
import mozilla.components.service.fretboard.ExperimentDownloadException
import mozilla.components.service.fretboard.ExperimentSource
import mozilla.components.service.fretboard.JSONExperimentParser
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

/**
 * Class responsible for fetching and
 * parsing experiments from a Kinto server
 *
 * @param baseUrl Kinto server url
 * @param bucketName name of the bucket to fetch
 * @param collectionName name of the collection to fetch
 */
class KintoExperimentSource(
    val baseUrl: String,
    val bucketName: String,
    val collectionName: String,
    private val client: HttpClient = HttpURLConnectionHttpClient()
) : ExperimentSource {
    var validateSignature = false
    private val kintoClient = KintoClient(client, baseUrl, bucketName, collectionName)

    override fun getExperiments(experiments: List<Experiment>): List<Experiment> {
        val experimentsDiff = getExperimentsDiff(client, experiments)
        val updatedExperiments = mergeExperimentsFromDiff(experimentsDiff, experiments)
        if (validateSignature && !validSignature(updatedExperiments)) {
            throw ExperimentDownloadException("Signature verification failed")
        }
        return updatedExperiments
    }

    private fun validSignature(experiments: List<Experiment>): Boolean {
        val sortedExperiments = experiments.sortedBy { it.id }
        val resultJson = JSONArray()
        val parser = JSONExperimentParser()
        for (experiment in sortedExperiments) {
            resultJson.put(parser.toJson(experiment))
        }
        val metadata = kintoClient.getMetadata()
        val metadataJson = JSONObject(metadata).getJSONObject("data")
        val signatureJson = metadataJson.getJSONObject("signature")
        val signature = signatureJson.getString("signature")
        val publicKey = signatureJson.getString("public_key")
        val lastModified = metadataJson.getLong("last_modified")
        val signedJson = "{'data':$resultJson, 'last_modified':\"$lastModified\"}"
        return validSignature(signedJson, signature, publicKey)
    }

    private fun validSignature(signedJson: String, signature: String, publicKeyString: String): Boolean {
        val publicKeyBytes = Base64.decode(publicKeyString, 0)
        val spec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        val publicKey = keyFactory.generatePublic(spec)
        val dsa = Signature.getInstance("SHA384withECDSA")
        dsa.initVerify(publicKey)
        dsa.update(signedJson.toByteArray(StandardCharsets.UTF_8))
        val signatureBytes = Base64.decode(signature.replace("-", "+").replace("_", "/"), 0)
        return dsa.verify(signatureBytes)
    }

    private fun getExperimentsDiff(client: HttpClient, experiments: List<Experiment>): String {
        val lastModified = getMaxLastModified(experiments)
        return if (lastModified != null) {
            kintoClient.diff(lastModified)
        } else {
            kintoClient.get()
        }
    }

    private fun mergeExperimentsFromDiff(experimentsDiff: String, experiments: List<Experiment>): List<Experiment> {
        val mutableExperiments = experiments.toMutableList()
        val experimentParser = JSONExperimentParser()
        val diffJsonObject = JSONObject(experimentsDiff)
        val data = diffJsonObject.get(DATA_KEY)
        if (data is JSONObject) {
            if (data.getBoolean(DELETED_KEY)) {
                mergeDeleteDiff(data, mutableExperiments)
            }
        } else {
            mergeAddUpdateDiff(experimentParser, data as JSONArray, mutableExperiments)
        }
        return mutableExperiments
    }

    private fun mergeDeleteDiff(data: JSONObject, mutableExperiments: MutableList<Experiment>) {
        mutableExperiments.remove(mutableExperiments.single { it.id == data.getString(ID_KEY) })
    }

    private fun mergeAddUpdateDiff(
        experimentParser: JSONExperimentParser,
        experimentsJsonArray: JSONArray,
        mutableExperiments: MutableList<Experiment>
    ) {
        for (i in 0 until experimentsJsonArray.length()) {
            val experimentJsonObject = experimentsJsonArray[i] as JSONObject
            val experiment = mutableExperiments.singleOrNull { it.id == experimentJsonObject.getString(ID_KEY) }
            if (experiment != null)
                mutableExperiments.remove(experiment)
            mutableExperiments.add(experimentParser.fromJson(experimentJsonObject))
        }
    }

    private fun getMaxLastModified(experiments: List<Experiment>): Long? {
        var maxLastModified: Long = -1
        for (experiment in experiments) {
            val lastModified = experiment.lastModified
            if (lastModified != null && lastModified > maxLastModified) {
                maxLastModified = lastModified
            }
        }
        return if (maxLastModified > 0) maxLastModified else null
    }

    companion object {
        private const val ID_KEY = "id"
        private const val DATA_KEY = "data"
        private const val DELETED_KEY = "deleted"
    }
}
