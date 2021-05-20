package ForgotPassword

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        firebaseAuth = Firebase.auth
        setFontType()
        processForgotPassword()
    }

    private fun setFontType(){
        forgot_tv_forgot.typeface = Typeface.createFromAsset(assets,"fonts/Vonique 64 Bold.ttf")
    }

    private fun processForgotPassword(){
        forgot_btn_submit.setOnClickListener {
            val userEmail = forgot_edit_email.text.toString()
            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"重置密碼信件已寄至信箱，請至信箱點選信件以重設密碼",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        fun startForgotPasswordActivity(context: Context){
            val intent = Intent(context,ForgotPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}