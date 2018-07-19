/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.fretboard.source.kinto

import mozilla.components.service.fretboard.ExperimentDownloadException
import mozilla.components.service.fretboard.ExperimentSource
import mozilla.components.service.fretboard.JSONExperimentParser
import mozilla.components.service.fretboard.SyncResult
import org.json.JSONArray
import org.json.JSONObject

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
    client: HttpClient = HttpURLConnectionHttpClient()
) : ExperimentSource {
    var validateSignature = true
    private val kintoClient = KintoClient(client, baseUrl, bucketName, collectionName)
    private val signatureVerifier = SignatureVerifier(client, kintoClient)

    override fun getExperiments(syncResult: SyncResult): SyncResult {
        val experimentsDiff = getExperimentsDiff(syncResult)
        val updatedExperiments = mergeExperimentsFromDiff(experimentsDiff, syncResult)
        if (validateSignature &&
            !signatureVerifier.validSignature(updatedExperiments.experiments, updatedExperiments.lastModified)) {
            throw ExperimentDownloadException("Signature verification failed")
        }
        return updatedExperiments
    }

    private fun getExperimentsDiff(syncResult: SyncResult): String {
        val lastModified = syncResult.lastModified
        return if (lastModified != null) {
            kintoClient.diff(lastModified)
        } else {
            kintoClient.get()
        }
    }

    private fun mergeExperimentsFromDiff(experimentsDiff: String, syncResult: SyncResult): SyncResult {
        val experiments = syncResult.experiments
        val mutableExperiments = experiments.toMutableList()
        val experimentParser = JSONExperimentParser()
        val diffJsonObject = JSONObject(experimentsDiff)
        val data = diffJsonObject.get(DATA_KEY)
        val experimentsJsonArray = data as JSONArray
        var maxLastModified: Long? = syncResult.lastModified
        for (i in 0 until experimentsJsonArray.length()) {
            val experimentJsonObject = experimentsJsonArray[i] as JSONObject
            val experiment = mutableExperiments.singleOrNull { it.id == experimentJsonObject.getString(ID_KEY) }
            if (experiment != null) {
                mutableExperiments.remove(experiment)
            }
            if (!experimentJsonObject.has(DELETED_KEY)) {
                mutableExperiments.add(experimentParser.fromJson(experimentJsonObject))
            }
            val lastModifiedDate = experimentJsonObject.getLong(LAST_MODIFIED_KEY)
            if (maxLastModified == null || lastModifiedDate > maxLastModified) {
                maxLastModified = lastModifiedDate
            }
        }
        return SyncResult(mutableExperiments, maxLastModified)
    }

    companion object {
        private const val ID_KEY = "id"
        private const val DATA_KEY = "data"
        private const val DELETED_KEY = "deleted"
        private const val LAST_MODIFIED_KEY = "last_modified"
    }
}
