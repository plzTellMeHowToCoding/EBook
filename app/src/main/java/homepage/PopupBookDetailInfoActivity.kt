package Homepage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.bumptech.glide.Glide
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_popup_book_detail_info.*


/**
 *  點選主頁中的書本項目後，彈出點擊的書本詳細資訊頁面
 */
class PopupBookDetailInfoActivity : AppCompatActivity() {
    private val popupBookDetailInfoFrag by lazy{
        PopupBookDetailInfoFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_book_detail_info)
        initWindow()
        setupBookDetailInfoFragment()
        initAppBarCloseButton()
        setupBookDetailInfoImgBookPic()
    }

    private fun setupBookDetailInfoImgBookPic(){
        Glide.with(this).load(bookPicUri).into(popup_book_detail_info_img_book_pic)
        Log.d("TAG", "@@@@ bookPicUri = $bookPicUri")
    }

    // 設定 Popup activity 的樣式
    private fun initWindow(){
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val height = dm.heightPixels
        val width = dm.widthPixels
        window.setBackgroundDrawableResource(R.drawable.round_corner)
        window.setLayout((width * .8).toInt(),(height * .8).toInt())
    }

    // 設定顯示書本詳細資訊的 fragment
    private fun setupBookDetailInfoFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.popup_book_detail_info_container,popupBookDetailInfoFrag)
        popupBookDetailInfoFrag.arguments = Bundle().also {
            it.putString("name", bookName)
            it.putString("author", bookAuthor)
            it.putInt("version", bookVersion)
            it.putInt("publishYear", bookPublishYear)
            it.putString("publisher", bookPublisher)
            it.putString("outline", bookOutline)
        }
        transaction.commit()
    }

    // 點擊 appbar 的 x 可以關閉 popup activity
    private fun initAppBarCloseButton(){
        popup_book_detail_info_img_close.setOnClickListener {
            finish()
        }
    }

    companion object{
        private var bookPicUri : Uri? = null

        fun setBookPicUri(picUri : Uri){
            bookPicUri = picUri
            Log.d("TAG", "@@@@ picUri = $bookPicUri")
        }
        private var bookName : String = ""
        private var bookAuthor : String = ""
        private var bookVersion : Int = 0
        private var bookPublishYear : Int = 0
        private var bookPublisher : String = ""
        private var bookOutline : String = ""

        fun setBookInfo(name : String, author : String, version : Int, publishYear : Int, publisher : String, outline : String){
            bookName = name
            bookAuthor = author
            bookVersion = version
            bookPublishYear = publishYear
            bookPublisher = publisher
            bookOutline = outline
        }
    }
}