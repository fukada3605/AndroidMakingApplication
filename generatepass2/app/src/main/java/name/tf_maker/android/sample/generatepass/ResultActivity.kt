package name.tf_maker.android.sample.generatepass

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val passLen = intent.getIntExtra("passLen",0)

        val setNumflag = intent.getBooleanExtra("setNum",false)
        val setBEngflag = intent.getBooleanExtra("setBEng",false)
        val setSymbolflag = intent.getBooleanExtra("setSymbol",false)

        val password = createPASS(passLen,setNumflag,setBEngflag,setSymbolflag)

        val resultText = findViewById<TextView>(R.id.resultView)
        resultText.text = getString(R.string.resultView_label,password)

        val clipButton = findViewById<Button>(R.id.CopyButton)

        clipButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Password", password)
            clipboard.setPrimaryClip(clip)
        }
    }
}

fun createPASS(
    length:Int=0,
    shouldsetNum:Boolean = false,
    shouldsetBEng:Boolean = false,
    shouldsetSymbol:Boolean= false):String{

    var passPath = "abcdefghijklmnopqestuvwxyz"

    var passtmp = arrayOf("")
    if(shouldsetNum){
        val tmp ="1234567890"
        passPath += tmp
        passtmp += tmp[Random.nextInt(tmp.length)].toString()
    }

    if(shouldsetBEng){
        val tmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        passPath += tmp
        passtmp += tmp[Random.nextInt(tmp.length)].toString()
    }
    if(shouldsetSymbol){
        val tmp = "!_><+*;:/-^|=?(){}[]"
        passPath += tmp
        passtmp += tmp[Random.nextInt(tmp.length)].toString()
    }


    val initlength = passtmp.size

    //ランダムにパーツから配列に代入
    for (i in initlength..length){
        val tmp = passPath[Random.nextInt(passPath.length)]
        passtmp += tmp.toString()
    }

    //パスワードテンプレートの配列の入れ替え
    for (i in 0..length){
        val rand= Random.nextInt(passtmp.size)
        val tmp = passtmp[i]
        passtmp[i]=passtmp[rand]
        passtmp[rand]=tmp
    }

    val password:String=passtmp.joinToString("")
    return password

}