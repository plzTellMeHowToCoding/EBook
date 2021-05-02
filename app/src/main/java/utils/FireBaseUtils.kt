package utils

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *  Firebase 相關功能都寫在這，好讓外部統一使用
 */

class FireBaseUtils {
    private val a = "c"
    companion object{
        private val collectionRef = FirebaseFirestore.getInstance().collection("BooksCollection")
        private val firestorageRef = FirebaseStorage.getInstance().reference
        private val TAG = "FirebaseUtils"
        private val bookList = mutableListOf<Book>()

        private fun executeUpload(bookInfo : Book){
            collectionRef.document().set(bookInfo)
                .addOnSuccessListener {
                    Log.d(TAG, "@@@@ upload book info success")
                }
                .addOnFailureListener {
                    Log.d(TAG, "@@@@ upload book info failure")
                }
        }

        /**
         *  先處理圖片的上傳，因上傳後會得到上傳完成的 Uri
         *  再將得到的 Uri 和書籍資料一併填入至 firebase 中
         *  這樣之後要讀取照片時就可以直接從 firebase 將 Uri 取出，再載入至 imageView 中
         *
         *  ==============================================================
         *  由於 UploadTask.TaskSnapshot.storage.downloadUrl 返回時間可能較長
         *  避免 return 的 Uri 還沒拿到就執行上傳作業，因此在這邊把上傳的 function
         *  包在 .addOnSuccessListener 內，等到確實拿到了 Uri 後再進行資料的上傳
         *  ==============================================================
         */
        fun setUploadInfoAndUpload(filePath : String, imageUri : Uri, name : String,
                      author : String, version : String, publishDate : String,
                      publisher: String, size : String, isbn : String,
                      translator : String, relatedLink : String){
            firestorageRef.child(filePath).putFile(imageUri)
                .addOnSuccessListener {
                    // 這邊 return 的 URI 可能因為時間過長，導致外部在存取的時候仍為 null
                    it.storage.downloadUrl.addOnSuccessListener { result ->
                        val book = Book(name,author,version.toInt(),publishDate.toInt(),publisher,size,isbn,translator,relatedLink,result.toString())
                        executeUpload(book)
                    }
                    Log.d(TAG, "@@@@ upload image success")
                }
                .addOnFailureListener {
                    Log.d(TAG, "upload image failure")
                }
                .addOnProgressListener {
                    getUploadProgress((100.0 * it.bytesTransferred / it.totalByteCount).toInt())
                }
        }
        fun getUploadProgress(progress : Int) = progress

        /**
         *  TODO : collectionRef 回傳的是 Map 資料結構，需再將其資料從中取出轉為 Book 物件後寫入 List 中
         *
         */
        fun getBookInfo():List<Book>{
            collectionRef.get().addOnSuccessListener {
                for(book in it){
                    Log.d(TAG, "@@@@ ${book.data}")
                }
            }.addOnFailureListener {
                Log.d(TAG, "@@@@ get data failure")
            }
            return bookList
        }
    }
}