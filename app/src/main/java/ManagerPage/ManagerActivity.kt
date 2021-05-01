package ManagerPage

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_manager.*
import kotlinx.android.synthetic.main.view_upload_book.*
import utils.Book
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ManagerActivity : AppCompatActivity() {

    private val uploadBookImageOptions = arrayOf("使用相機拍照","從相簿選擇")
    private val managerActivity = this
    private lateinit var uploadBookImageFile : File
    private lateinit var uploadBookImageUri : Uri
    private val firestore = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var downloadImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        initTakePhoto()
        initButton()
    }

    private fun initButton(){

        manager_button_submit.setOnClickListener {
            uploadBookImage()
            if(this::downloadImageUri.isInitialized) {
                uploadBookInfo(downloadImageUri.toString())
            }
        }
        manager_button_cancel.setOnClickListener {

        }
    }

    private fun uploadBookInfo(downloadUri : String) {
        /**
         *  測試讀取 & 寫入 firebase，先暫時留在這，等之後要讀寫 data 時再搬去合宜的區塊
         *  */

        val name = manager_edit_name.text.toString()
        val author = manager_edit_author.text.toString()
        val version = manager_edit_version.text.toString()
        val publishData = manager_edit_publish_date.text.toString()
        val publisher = manager_edit_publisher.text.toString()
        val size = manager_edit_size.text.toString()
        val isbn = manager_edit_ISBN.text.toString()
        val translator = manager_edit_translator.text.toString()
        val relatedLink = manager_edit_related_link.text.toString()

        if(!(name.isNullOrBlank()
            && author.isNullOrBlank()
            && version.isNullOrBlank()
            && publishData.isNullOrBlank()
            && publisher.isNullOrBlank()
            && size.isNullOrBlank()
            && isbn.isNullOrBlank()
            && translator.isNullOrBlank()
            && relatedLink.isNullOrBlank())){

            val book = Book(name,
                author,
                version.toInt(),
                publishData.toInt(),
                publisher,
                size,
                isbn,
                translator,
                relatedLink,
                downloadUri)

            firestore.collection("BooksCollection").document("Book").set(book)
                .addOnSuccessListener {
                    Toast.makeText(this,"upload success",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"upload Fail",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadBookImage(){

        val riversRef = storageRef.child(uploadBookImageUri.lastPathSegment ?: "")
        riversRef.putFile(uploadBookImageUri).addOnSuccessListener {
            Log.d("TAG", "@@@ upload success ")
            it.storage.downloadUrl.addOnSuccessListener {
                downloadImageUri = it
                //uploadBookInfo(it)
            }
            manager_upload_progress.visibility = View.GONE
        }.addOnFailureListener {

        }.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
            if(manager_upload_progress.visibility == View.GONE){
                manager_upload_progress.visibility = View.VISIBLE
                manager_upload_progress.progress = progress
                Log.d("TAG", "@@@@ progress = $progress")
                if(progress >= 100){
                    manager_upload_progress.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadImage(ref : StorageReference){
        ref.downloadUrl
            .addOnSuccessListener {
                Log.d("TAG", "@@@@ download url =  ${it.toString()}")
            }.addOnFailureListener {
                Log.d("TAG", "@@@@ exception = ${it.printStackTrace()}")
            }
    }

    private fun checkPermission(){
        val permission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
        }else{
            uploadBookImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            100 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    uploadBookImage()
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