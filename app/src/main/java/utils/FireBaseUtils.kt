package utils

import Homepage.BookFragment
import Homepage.MagazineFragment
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *  Firebase 相關功能都寫在這，好讓外部統一使用
 */

class FireBaseUtils {
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
         *  FireStore 若有成功撈到資料的話會在 addOnSuccessListener 中執行
         *  這邊將設定頁面的 content 擺在 addOnSuccessListener 中
         *  避免可能因等待時間過長導致回傳資料太久，導致傳入長度為 0 的 List 給 Adapter
         *  =======================================================================
         *  collectionRef 回傳的是 Map 資料結構，需再將其資料轉為 Book 物件再寫入 List 中
         */
        fun setBookInfo(){
            collectionRef.get().addOnSuccessListener {
                for(book in it){
                    val bookMap = book.data
                    Log.d(TAG, "@@@@ book data = ${book.data}")
                    bookList.add(Book(
                        bookMap["name"].toString(),
                        bookMap["author"].toString(),
                        bookMap["version"].toString().toInt(),
                        bookMap["publishDate"].toString().toInt(),
                        bookMap["publisher"].toString(),
                        bookMap["size"].toString(),
                        bookMap["isbn"].toString(),
                        bookMap["translator"].toString(),
                        bookMap["relatedLink"].toString(),
                        bookMap["uri"].toString()))
                }
                BookFragment.setContentList(bookList)
                MagazineFragment.setContentList(bookList)
            }.addOnFailureListener {
                // TODO 若 firebase 回傳失敗，可考慮直接通知 UI 顯示錯誤畫面
                Log.d(TAG, "@@@@ get data failure")
            }
        }
    }
}