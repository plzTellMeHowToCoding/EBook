package Homepage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vincent.ebook.R
import Utils.Book

class BookAdapter (val context : Context, val bookList : List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_book,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupView(bookList[position])
        holder.launchDetailInfoView()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val bookImage = view.findViewById<ImageView>(R.id.recycler_item_book_image)
        val bookName = view.findViewById<TextView>(R.id.recycler_item_book_name)
        val bookLayout = view.findViewById<CardView>(R.id.recycler_item_book)

        fun setupView(book : Book){
            Glide.with(context).load(Uri.parse(book.uri)).into(bookImage)
            bookName.text = book.name
        }

        fun launchDetailInfoView(){
            bookLayout.setOnClickListener {
                val intent = Intent(context,
                    PopupBookDetailInfoActivity::class.java)
                context.startActivity(intent)
                setPopupActivityBookPicUriAndInfoDesc()
            }
        }
        //將書目的圖片 Uri 和書目資訊傳給 Popup activity
        private fun setPopupActivityBookPicUriAndInfoDesc(){
            val book = bookList[adapterPosition]
            PopupBookDetailInfoActivity.setBookPicUri(Uri.parse(book.uri))
            PopupBookDetailInfoActivity.setBookInfo(book.name,book.author,book.version,book.publishDate,book.publisher,"this is outline")
        }
    }
}