package name.tf_maker.android.sample.imageextraction

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import java.io.File
import java.util.*

//インターフェースでつなぎの役割を担い
//onSuccessメソッドでファイル名を送信

class ImgSynthesizer(var context: Context) : AsyncTask<ArrayList<Bitmap>, Void?, String>() {
    var selectedItem: String? = null
    var endWITH = "mp3"
    var filelist = 0
    public var listener : Listener?=null
    var file:File?=null
    private var pt = 21
    var outputimg:Bitmap?=null;
    var nowloading:Boolean=false
    fun MyAsyncTask(context: Context) {
        this.context = context
    }
    override fun doInBackground(vararg params: ArrayList<Bitmap>): String{
        Log.d("処理確認","非同期開始")
        imgExtraction(params[0])
        return "hoge"
    }

    private fun imgExtraction(srcImgs : ArrayList<Bitmap>):Unit {
        nowloading=true
        publishProgress()
        Log.d("処理確認","imgExtraction関数スタート")
        // 画像配列の最小サイズ
        var listimgMinWidth:Int=90000
        var listimgMinHeight:Int=90000
        Log.d("処理確認","サイズ初期化")
        for (j in 0 until srcImgs.size){
            if (srcImgs[j].width<listimgMinWidth){
                listimgMinWidth=srcImgs[j].width
            }
            if (srcImgs[j].height<listimgMinHeight){
                listimgMinHeight=srcImgs[j].height
            }
        }
        // 出力画像用の配列
        val dstImg : Bitmap = Bitmap.createBitmap(listimgMinWidth, listimgMinHeight, Bitmap.Config.ARGB_8888)

        // 画像の高さ・幅
        val width : Int = dstImg.width
        val height : Int = dstImg.height

        // 画素値を格納する変数
        var sumR: Int
        var sumG: Int
        var sumB: Int
        var resultR: Int
        var resultG: Int
        var resultB: Int
        var rgb :Int

        Log.d("処理確認","画像合成開始")
        // 画像合成
        for(j in 0..height - 1 step 1) {
            for (i in 0..width - 1 step 1) {
                sumR= 0
                sumG= 0
                sumB= 0
                for (n in 0 until srcImgs.size){
                    rgb = srcImgs[n].getPixel(i, j)
                    sumR+= Color.red(rgb).toInt()
                    sumG+= Color.green(rgb).toInt()
                    sumB+= Color.blue(rgb).toInt()
                }
                resultR=(sumR/srcImgs.size).toInt()
                resultG=(sumG/srcImgs.size).toInt()
                resultB=(sumB/srcImgs.size).toInt()

                dstImg.setPixel(i, j, Color.rgb(resultR,resultG,resultB))
            }
        }

        outputimg=dstImg
        nowloading=false
        Log.d("処理確認","合成終了")
        return
    }

    override fun onProgressUpdate(vararg values: Void?) {
        listener?.onSuccess(outputimg,nowloading)
    }

    public override fun onPostExecute(results: String) {
        listener?.onSuccess(outputimg,nowloading)
    }

    fun setListean(listener: Listener?) {
        this.listener = listener
    }
}
