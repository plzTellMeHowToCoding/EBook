package homepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vincent.ebook.R
import utils.Book

class BookAdapter (val context : Context, val bookList : List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val bookImage = view.findViewById<ImageView>(R.id.recycler_item_book_image)
        val bookName = view.findViewById<TextView>(R.id.recycler_item_book_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_book,parent,false)
        view.setOnClickListener {
            Toast.makeText(parent.context,"clicked",Toast.LENGTH_SHORT).show()
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        Glide.with(context).load(book.bookImageId).into(holder.bookImage)
        holder.bookName.text = book.name
    }
}