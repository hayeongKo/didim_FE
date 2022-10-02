package com.example.didim_2022.ui

import android.content.SharedPreferences
import com.example.didim_2022.ui.PreferenceHelper.set
import com.example.didim_2022.ui.PreferenceHelper.get
class SharedManager(context: FootActivity2) {
    private val prefs:SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentSensor(sensor: Sensor) {
        prefs["count"] = sensor.count
        prefs["score"] = sensor.score
        prefs["ajudge"] = sensor.ajudge
        prefs["miss"] = sensor.miss
        prefs["bad"] = sensor.bad
        prefs["good"] = sensor.good
        prefs["perfect"] = sensor.perfect
    }
    fun getCurrentSensor():Sensor {
        return Sensor().apply {
            count = prefs["count", ""]
            score = prefs["score", ""]
            ajudge = prefs["ajudge", ""]
            miss = prefs["miss", 0]
            bad = prefs["bad", 0]
            good = prefs["good", 0]
            perfect = prefs["perfect", 0]
        }
    }
}