/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.fretboard.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import mozilla.components.service.fretboard.ExperimentDescriptor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainButton = findViewById<Button>(R.id.mainButton)
        val descriptor = ExperimentDescriptor("226a913c-5f8e-4fc4-9edb-46536616e12e")
        fretboard.withExperiment(this, descriptor) {
            mainButton.setBackgroundColor(Color.RED)
        }
        val secondButton = findViewById<Button>(R.id.secondButton)
        val secondDescriptor = ExperimentDescriptor("226a913c-5f8e-4fc4-9edb-46536616e12e")
        secondButton.isEnabled = fretboard.isInExperiment(this, secondDescriptor)
    }
}
