package com.imageclarity

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.facebook.react.bridge.Promise

@ReactModule(name = ImageClarityModule.NAME)
class ImageClarityModule(reactContext: ReactApplicationContext) :
  NativeImageClaritySpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  override fun isImageBlack(imagePath: String, promise: Promise) {
      try {
          val bitmap = BitmapFactory.decodeFile(imagePath) ?: throw Exception("Bitmap decode failed")
          val isBlack = checkBlack(bitmap)
          promise.resolve(isBlack)
      } catch (e: Exception) {
          promise.reject("ERR_BLACK_CHECK", e)
      }
  }

  override fun isImageBlurry(imagePath: String, promise: Promise) {
      try {
          val bitmap = BitmapFactory.decodeFile(imagePath) ?: throw Exception("Bitmap decode failed")
          val isBlur = checkBlur(bitmap)
          promise.resolve(isBlur)
      } catch (e: Exception) {
          promise.reject("ERR_BLUR_CHECK", e)
      }
  }

  private fun checkBlack(bitmap: Bitmap, threshold: Int = 30, tolerance: Double = 0.99): Boolean {
      val scaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true)

      val width = scaled.width
      val height = scaled.height
      val pixels = IntArray(width * height)
      scaled.getPixels(pixels, 0, width, 0, 0, width, height)

      var darkPixelCount = 0
      for (pixel in pixels) {
          val r = Color.red(pixel)
          val g = Color.green(pixel)
          val b = Color.blue(pixel)
          val brightness = (r + g + b) / 3
          if (brightness <= threshold) {
              darkPixelCount++
          }
      }

      scaled.recycle()
      val darkRatio = darkPixelCount.toDouble() / pixels.size.toDouble()
      return darkRatio >= tolerance
  }

  private fun checkBlur(bitmap: Bitmap, threshold: Double = 800.0): Boolean {
      val scaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true)

      val width = scaled.width
      val height = scaled.height

      val grayPixels = IntArray(width * height)
      val buffer = IntArray(width * height)
      scaled.getPixels(buffer, 0, width, 0, 0, width, height)

      for (i in buffer.indices) {
          val p = buffer[i]
          val r = (p shr 16) and 0xFF
          val g = (p shr 8) and 0xFF
          val b = p and 0xFF
          grayPixels[i] = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
      }

      val kernel = arrayOf(
          intArrayOf(-1, -1, -1),
          intArrayOf(-1,  8, -1),
          intArrayOf(-1, -1, -1)
      )

      val laplacianValues = DoubleArray((width - 2) * (height - 2))
      var index = 0

      for (y in 1 until height - 1) {
          for (x in 1 until width - 1) {
              var sum = 0
              for (ky in -1..1) {
                  for (kx in -1..1) {
                      val pixelGray = grayPixels[(y + ky) * width + (x + kx)]
                      sum += pixelGray * kernel[ky + 1][kx + 1]
                  }
              }
              laplacianValues[index++] = sum.toDouble()
          }
      }

      val mean = laplacianValues.average()
      val variance = laplacianValues.map { val diff = it - mean; diff * diff }.average()

      scaled.recycle()

      return variance < threshold
  }

  companion object {
    const val NAME = "ImageClarity"
  }
}
