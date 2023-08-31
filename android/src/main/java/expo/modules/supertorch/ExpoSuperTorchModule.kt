package expo.modules.supertorch

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoSuperTorchModule : Module() {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoSuperTorch')` in JavaScript.
    val context = appContext.reactContext
    val cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val torchCamera = cameraManager.cameraIdList[0]
    var torchState = false;
    val torchCallback = object : CameraManager.TorchCallback() {
      override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
        super.onTorchModeChanged(cameraId, enabled)
        torchState = enabled
        Log.d("torchState", enabled.toString())
        }
      }
    // ???
    Looper.prepare()
    cameraManager.registerTorchCallback(torchCallback, null)

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
//      try {
//        return@AsyncFunction cameraManager?.getTorchStrengthLevel(cameraManager.cameraIdList[0])
//      } catch (e: Exception) {
//        throw Exception("It seems your Android is too old sadge" + e.message)
//      }
    }
  }
}
