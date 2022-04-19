package com.example.tasks.util.helper

import android.os.Build
import androidx.biometric.BiometricManager


class FingerPrintHelper(
    private val biometricManager: BiometricManager
) {

    fun authenticationAvailable(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            return true
        }
        return false
    }
}