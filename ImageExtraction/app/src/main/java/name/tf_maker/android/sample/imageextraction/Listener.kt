package name.tf_maker.android.sample.imageextraction

import android.graphics.Bitmap

//抽象クラス
open class Listener {
    open fun onSuccess(outputimg: Bitmap?,nowloading:Boolean){
        
    }
}