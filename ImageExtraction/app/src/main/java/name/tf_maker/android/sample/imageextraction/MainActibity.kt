package name.tf_maker.android.sample.imageextraction

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.util.*


internal class MainActivity : Activity() {

    var canInitImgArray1: Boolean=true
    var imgList1 : ArrayList<Bitmap?> = arrayListOf()
    var outputImg:Bitmap?=null

    var resultImgView: ImageView? = null

    private val REQUEST_GALLERY = 0
    /** Called when the activity is first created.  */

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultImgView = findViewById<View>(R.id.resultImgView) as ImageView

        val selectImgBtn=findViewById<Button>(R.id.selectimg)
        val synthesisImgBtn=findViewById<Button>(R.id.synthesisimg)
        val resetBtn =findViewById<Button>(R.id.resetBtn)
        val imgOutputBtn=findViewById<Button>(R.id.imgoutputBtn)
        val editName=findViewById<EditText>(R.id.EditFileText)

        val endwithstr= arrayListOf("jpg", "png")
        var endWith="jpg"

        val spinner = findViewById<Spinner>(R.id.Endwith)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, endwithstr
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val spinner = parent as Spinner
                val item = spinner.selectedItem as String
                endWith=item
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
        // ギャラリー呼び出し
        selectImgBtn.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(Intent.createChooser(intent, "画像を選択"), REQUEST_GALLERY)
        }

        synthesisImgBtn.setOnClickListener {
            if (imgList1.isEmpty()){
                onMessage("画像を選択してください")
                return@setOnClickListener
            }
            //合成画像作成
            val imgSynthesizer = ImgSynthesizer(this)
            imgSynthesizer.setListean(createListener())
            imgSynthesizer.execute(imgList1)
        }

        resetBtn.setOnClickListener {
            imgList1 = arrayListOf()
            outputImg=null
            resultImgView?.setImageBitmap(null)
        }

        imgOutputBtn.setOnClickListener {
            if (outputImg==null){
                onMessage("画像を合成してください")
                return@setOnClickListener
            }

            val filename=editName.text.toString()
            if (filename.isEmpty()) {
                editName.error="ファイル名を入力してください。"
                return@setOnClickListener
            }

            try {
                val fos = FileOutputStream(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .path + "/"+filename+"."+endWith, false
                )
                if (endWith=="jpg"){
                    outputImg?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }else if (endWith=="png"){
                    outputImg?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
                fos.flush()
                fos.close()
                onMessage("ファイルを出力しました。")
            } catch (e: java.lang.Exception) {
                onMessage(e.toString())
            }
        }

    }

    private fun createListener(): Listener {
        return object : Listener() {
            override fun onSuccess(outputimg: Bitmap?, nowloading: Boolean) {
                if (nowloading){
                    progress_bar.visibility=android.widget.ProgressBar.VISIBLE
                }else{
                    progress_bar.visibility=android.widget.ProgressBar.INVISIBLE
                }
                if (outputimg!=null){
                    outputImg=outputimg
                    resultImgView?.setImageBitmap(outputimg)
                }
            }
        }
    }

    private fun createListener2(): Listener2 {
        return object : Listener2() {
            override fun onSuccess(canInitImgArray: Boolean, imgList: ArrayList<Bitmap?>) {
                canInitImgArray1=canInitImgArray
                imgList1=imgList
                onMessage("画像を選択しました。")
                Log.d("画像数",imgList1.size.toString())
            }
        }
    }

    private fun onMessage(msg:String){
        val context = applicationContext
        val t = Toast.makeText(
            context,
            msg,
            Toast.LENGTH_LONG
        )
        t.show()
    }

    override fun onStart() {
        Log.d("処理確認","開始")
        super.onStart()
    }

    override fun onResume() {
        Log.d("処理確認","動作")
        super.onResume()
    }

    override fun onPause() {
        Log.d("処理確認","一時停止")
        super.onPause()
    }

    override fun onStop() {
        Log.d("処理確認","停止")
        super.onStop()
    }

    /*fun addmeter(atai:Int,Max:Int){
        progress_bar.progress=Max
        onHogeProgressChanged(atai)
    }*/

    /*private fun onHogeProgressChanged(percentage: Int) {
        val animation = ObjectAnimator.ofInt(progress_bar, "progress", percentage)
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }*/

    private class ActivityResult(
        val requestCode: Int,
        val resultCode: Int,
        val data: Intent
    )

    private var activityResult: ActivityResult? = null
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        Log.d("実行","onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        activityResult = ActivityResult(requestCode, resultCode, data)
    }

    override fun onPostResume() {
        Log.d("実行","onPostResume")
        super.onPostResume()
        if (activityResult != null) {
            onPostResumeWithActivityResult(
                activityResult!!.requestCode,
                activityResult!!.resultCode,
                activityResult!!.data
            )
            activityResult = null
        }
    }


    private fun onPostResumeWithActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        Log.d("実行","onPostResumeWithActivityResult")
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
                val imgLoader = ImgLoader(this)
                imgLoader.setcanInitImgArray(canInitImgArray1)
                imgLoader.setImgList(imgList1)
                imgLoader.setListean(createListener2())
                imgLoader.execute(data)
            Log.d("クリア","了解")
        }
    }

    /*
    private fun rgbToGray(srcImg : Bitmap) : Bitmap {
        // 出力画像用の配列
        var dstImg : Bitmap = Bitmap.createBitmap(srcImg.width, srcImg.height, Bitmap.Config.ARGB_8888)

        // 画像の高さ・幅
        val width : Int = dstImg.width
        val height : Int = dstImg.height

        // 画素値を格納する変数
        var rgb : Int
        var gray : Int

        // グレースケール変換
        for(j in 0..height - 1 step 1) {
            for (i in 0..width - 1 step 1) {
                rgb = srcImg.getPixel(i, j)
                gray = (Color.red(rgb) * 0.3 + Color.green(rgb) * 0.59 + Color.blue(rgb) * 0.11).toInt()
                dstImg.setPixel(i, j, Color.rgb(gray, gray, gray))
            }
        }

        return dstImg
    }
    */
}