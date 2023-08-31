package expo.modules.supertorch

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_FASTEST
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class TooOldAndroidException : CodedException (
  message = "Too old Android (unlucky)"
)
class ExpoSuperTorchModule : Module() {
  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun definition() = ModuleDefinition {
    // Silly initialization block
    val context = appContext.reactContext
    val cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val handler = Handler(Looper.getMainLooper())
    val torchCamera = cameraManager.cameraIdList[0]
    var torchState = false;
    val torchCallback = object : CameraManager.TorchCallback() {
      override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
        super.onTorchModeChanged(cameraId, enabled)
        torchState = enabled
        }
      }
    cameraManager.registerTorchCallback(torchCallback, handler)
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val sensorEventListener = object : SensorEventListener {
      override fun onSensorChanged(event: SensorEvent) {
        val azimuth = event.values[0]
        val pitch = event.values[1]

        if (-0.2 < azimuth && azimuth < 0.2 && -0.2 < pitch && pitch < 0.2) {
          cameraManager.setTorchMode(torchCamera, true)
        } else {
          cameraManager.setTorchMode(torchCamera, false)
        }
      }

      override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Ignore accuracy changes
      }
    }
    val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    sensorManager.registerListener(sensorEventListener, sensor, SENSOR_DELAY_FASTEST)
    // initialization block

    Name("ExpoSuperTorch")

    AsyncFunction("fireTorch") {
      cameraManager.setTorchMode(torchCamera, true)
    }

    AsyncFunction("stopTorch") {
      cameraManager.setTorchMode(torchCamera, false);
    }

    AsyncFunction("toggleTorch") {
      cameraManager.setTorchMode(torchCamera, !torchState)
    }

    AsyncFunction("getTorchIntensity") {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return@AsyncFunction cameraManager.getTorchStrengthLevel(torchCamera)
      } else {
        throw TooOldAndroidException()
      }
    }
  }
}
