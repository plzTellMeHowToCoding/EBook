package ManagerPage

import android.app.Activity
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.view_upload_book.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ManagerActivity : AppCompatActivity() {

    private val uploadBookImageOptions = arrayOf("使用相機拍照","從相簿選擇")
    private val managerActivity = this
    private lateinit var uploadBookImageFile : File
    private lateinit var uploadBookImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        initTakePhoto()
    }

    private fun initTakePhoto(){
        /** 彈出對話視窗，讓使用者決定要用相機拍相片上傳還是從相簿選照片上傳
         *  0 -> 相機拍照
         *  1 -> 相簿選擇
         */
        manager_image_edit.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setItems(uploadBookImageOptions) { _, which ->
                    when(which){
                        0 -> {
                            uploadBookImageFile = File(externalCacheDir,System.currentTimeMillis().toString()+"upload_book_image.jpg")
                            if(uploadBookImageFile.exists()){
                                uploadBookImageFile.delete()
                            }
                            uploadBookImageFile.createNewFile()
                            uploadBookImageUri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                FileProvider.getUriForFile(managerActivity,"com.vincent.Ebook",uploadBookImageFile)
                            }else{
                                Uri.fromFile(uploadBookImageFile)
                            }
                            val intent = Intent("android.media.action.IMAGE_CAPTURE")
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,uploadBookImageUri)
                            startActivityForResult(intent, TAKE_PHOTO)
                        }
                        1 -> {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(intent, CHOOSE_PHOTO)
                        }
                    }
                }
            }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            TAKE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK){
                    Glide.with(this).load(uploadBookImageUri).into(manager_image_content)
                }
            }
            CHOOSE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK){
                    val uri = data?.data
                    Glide.with(this).load(uri).into(manager_image_content)
                }
            }
        }
    }

    companion object{
        private const val TAKE_PHOTO = 1
        private const val CHOOSE_PHOTO = 2
    }
}