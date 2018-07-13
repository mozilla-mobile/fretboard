/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.fretboard.sample

import android.app.Application
import android.content.ComponentName
import android.content.Context
import mozilla.components.service.fretboard.Fretboard
import mozilla.components.service.fretboard.scheduler.jobscheduler.JobSchedulerSyncScheduler
import mozilla.components.service.fretboard.source.kinto.KintoExperimentSource
import mozilla.components.service.fretboard.storage.flatfile.FlatFileExperimentStorage
import java.io.File

val Context.application: SampleApp
    get() = applicationContext as SampleApp

val Context.fretboard: Fretboard
    get() = application.fretboard

class SampleApp : Application() {

    lateinit var fretboard: Fretboard

    override fun onCreate() {
        super.onCreate()
        fretboard = Fretboard(
            KintoExperimentSource(EXPERIMENTS_BASE_URL, EXPERIMENTS_BUCKET, EXPERIMENTS_COLLECTION),
            FlatFileExperimentStorage(File(filesDir, EXPERIMENTS_FILE))
        )
        fretboard.loadExperiments()
        val scheduler = JobSchedulerSyncScheduler(this)
        scheduler.schedule(EXPERIMENTS_JOB_ID, ComponentName(this, ExperimentsSyncService::class.java))
    }

    companion object {
        private const val EXPERIMENTS_FILE = "experiments.json"
        private const val EXPERIMENTS_JOB_ID = 7
        private const val EXPERIMENTS_BASE_URL = "https://firefox.settings.services.mozilla.com/v1"
        private const val EXPERIMENTS_BUCKET = "fennec"
        private const val EXPERIMENTS_COLLECTION = "experiments"
    }
}
