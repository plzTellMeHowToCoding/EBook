package ManagerPage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_manager.*
import kotlinx.android.synthetic.main.view_upload_book.*
import Utils.FireBaseUtils
import java.io.File

class ManagerActivity : AppCompatActivity() {

    private val uploadBookImageOptions = arrayOf("使用相機拍照","從相簿選擇")
    private val managerActivity = this
    private lateinit var uploadBookImageFile : File
    private lateinit var uploadBookImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        initTakePhoto()
        initButton()
    }

    private fun initButton(){

        manager_button_submit.setOnClickListener {
            val name = manager_edit_name.text.toString()
            val author = manager_edit_author.text.toString()
            val version = manager_edit_version.text.toString()
            val publishData = manager_edit_publish_date.text.toString()
            val publisher = manager_edit_publisher.text.toString()
            val size = manager_edit_size.text.toString()
            val isbn = manager_edit_ISBN.text.toString()
            val translator = manager_edit_translator.text.toString()
            val relatedLink = manager_edit_related_link.text.toString()

            if (!(name.isNullOrBlank()
                        || author.isNullOrBlank()
                        || version.isNullOrBlank()
                        || publishData.isNullOrBlank()
                        || publisher.isNullOrBlank()
                        || size.isNullOrBlank()
                        || isbn.isNullOrBlank()
                        || translator.isNullOrBlank()
                        || relatedLink.isNullOrBlank())
            ) {
                FireBaseUtils.setUploadInfoAndUpload(uploadBookImageUri.lastPathSegment ?: "",uploadBookImageUri, name, author,
                version,publishData,publisher,size,isbn,translator,relatedLink,System.currentTimeMillis())
                Log.d("TAG", "@@@@ after upload book info ")
            }
        }
        manager_button_cancel.setOnClickListener {

        }
    }

    /**
     *  將使用者輸入的圖書資訊上傳至雲端
     *  先取出各個 edittext 的 value，再判斷是否為空，若不為空的話就可以上傳
     */


    private fun checkPermission(){
        val permission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
        }else{
            //uploadBookImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            100 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //uploadBookImage()
                }else{

                }
            }
        }
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