package Register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        firebaseAuth = Firebase.auth
        processRegister()

    }

    override fun onStart() {
        super.onStart()
        // 若使用者不為 null ， 則更新登入後對應的 UI
        if(firebaseAuth.currentUser != null){

        }
    }

    private fun verifyNameIsNotEmptyOrNull() : Boolean{
        return register_edtxv_name.text.isNullOrEmpty()
    }

    private fun verifyEmailIsValid() : Boolean{
        return register_edtxv_email_address.text.isNullOrEmpty()
    }

    private fun checkPassword() : Boolean{
        val password = register_edtxv_password.text
        val checkedPassword = register_edtxv_recheck_password.text
        return password == checkedPassword
    }

    /**
     *  按下註冊後會寄一封認證 mail 至信箱
     */
    private fun processRegister(){
        register_btn_submit.setOnClickListener {
/*            if(verifyNameIsNotEmptyOrNull()){
                register_edtxv_name.hint = "請輸入姓名！"
                register_edtxv_name.setHintTextColor(R.color.error_message_color)
                register_edtxv_name.requestFocus()
            }
            if(!checkPassword()){
                register_edtxv_password.hint = "兩次密碼輸入不正確"
                register_edtxv_password.setHintTextColor(R.color.error_message_color)
                register_edtxv_password.requestFocus()
            }
            if(verifyEmailIsValid()){
               register_edtxv_email_address.hint = "請輸入電子郵件"
               register_edtxv_email_address.setHintTextColor(R.color.error_message_color)
               register_edtxv_email_address.requestFocus()
            }*/
            firebaseAuth.createUserWithEmailAndPassword(register_edtxv_email_address.text.toString(),register_edtxv_recheck_password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("TAG", "@@@@ createUserWithEmail:success")
                        val user = firebaseAuth.currentUser
                        user.sendEmailVerification()
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    Log.d("TAG", "@@@@ sent email...")
                                }
                            }.addOnFailureListener {
                                Log.d("TAG", "@@@@ email not sent... ")
                            }
                    }else{
                        Log.d("TAG", "@@@@ createUserWithEmail:failure ${it.exception}")
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object{
        fun startRegisterActivity(context : Context){
            val intent = Intent(context,RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}