package homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.frag_magazine.*
import utils.Book

class MagazineFragment : Fragment(){
    val books = mutableListOf(
        Book("AA","A","A", R.drawable.apple), Book("BB","B","B", R.drawable.banana),
        Book("CC","C","C", R.drawable.cherry), Book("DD","D","D", R.drawable.grape),
        Book("EE","E","E", R.drawable.mango)
    )
    val bookList = ArrayList<Book>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_magazine,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }
    private fun initBookList(){
        bookList.clear()
        repeat(50){
            val index = (0 until books.size).random()
            bookList.add(books[index])
        }
    }

    private fun initRecyclerView(){
        initBookList()
        if(activity != null) {
            val homeActivity = activity as HomeActivity
            val layoutManager = GridLayoutManager(homeActivity,3)
            val adapter = BookAdapter(homeActivity, bookList)
            home_frag_magazine_list.layoutManager = layoutManager
            home_frag_magazine_list.adapter = adapter
        }
    }
}