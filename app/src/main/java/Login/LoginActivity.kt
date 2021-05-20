package Login

import Register.RegisterActivity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vincent.ebook.MainActivity
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = Firebase.auth
        setFontType()
        setLoginIcon()
        processGoToRegisterPage()
        processLogin()
    }

    private fun setFontType(){
        login_tv_login.typeface = Typeface.createFromAsset(assets,"fonts/Vonique 64 Bold.ttf")
    }

    private fun setLoginIcon(){
        login_img_icon.setImageResource(R.drawable.login_surface_icon)
    }

    // 處理忘記密碼邏輯
    private fun processForgotPassword(){
        login_txv_forgot_password.setOnClickListener {

        }
    }

    /**
     * 處理按下登入邏輯
     *
     */
    private fun processLogin() {
        login_btn_login.setOnClickListener {
            val email = login_edit_email.text.toString()
            val password = login_edit_password.text.toString()
                if (!(email.isNullOrEmpty() || password.isNullOrEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(firebaseAuth.currentUser.isEmailVerified){
                            if(it.isSuccessful) {
                                MainActivity.startMainActivity(this)
                            }
                        } else {
                            Toast.makeText(this, "信箱尚未驗證，請至註冊信箱點選驗證信件", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "帳號或密碼錯誤，請重新輸入", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun processGoToRegisterPage(){
        login_txv_register.setOnClickListener {
            RegisterActivity.startRegisterActivity(this)
        }
    }
}