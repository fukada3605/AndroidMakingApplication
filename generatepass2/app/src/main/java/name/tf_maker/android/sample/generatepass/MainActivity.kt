package name.tf_maker.android.sample.generatepass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val geneButton = findViewById<Button>(R.id.generatePass)

        geneButton.setOnClickListener {

            var isValid = true
            val lengthEditText = findViewById<EditText>(R.id.inPassLength)
            val lengthText = lengthEditText.text.toString()

            if (lengthText.isEmpty()){
                lengthEditText.error = "文字数を入力してください"
                isValid = false
            }

            if (lengthText=="0"){
                lengthEditText.error = "1以上の整数を入力してください"
                isValid = false
            }
            if (isValid) {
                val passLen = Integer.parseInt(lengthText)

                val addBEngSwitch = findViewById<Switch>(R.id.addBigList)
                val addNumSwitch = findViewById<Switch>(R.id.addNumList)
                val addSymbolSwitch = findViewById<Switch>(R.id.addSymbolList)
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("passLen", passLen)
                intent.putExtra("setNum", addNumSwitch.isChecked)
                intent.putExtra("setBEng", addBEngSwitch.isChecked)
                intent.putExtra("setSymbol", addSymbolSwitch.isChecked)

                startActivity(intent)
            }
        }
    }
}
