package Homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.frag_book.*
import Utils.Book

/**
 *  日後可考慮抽出 baseFragment 類別，將諸如 setCategoryList , setContentList 方法提出來寫
 *  2021.04.23 vincent
 */

class BookFragment : Fragment() {

    private val filterContentList = arrayListOf<Book>()
    private val homeActivity by lazy{
        activity as HomeActivity
    }
    private val adapter by lazy{
        BookAdapter(homeActivity,filterContentList)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_book,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }
    private fun initBookList(){
        /*repeat(50){
            val index = (0 until bookList.size).random()
            contentList.add(bookList[index])
        }*/
        filterContentList.addAll(bookList)
    }

    private fun initRecyclerView(){
        initBookList()
        if(activity != null) {
            val layoutManager = GridLayoutManager(homeActivity,3)
            home_frag_book_list.layoutManager = layoutManager
            home_frag_book_list.adapter = adapter
        }
    }

    /**
     *  接收經過篩選後的資料，並通知 recycler view 進行畫面更新
     */
    fun setFilterResult(filterResultList : List<Book>){
        Log.d("TAG", "@@@@ FilterCondition")
        filterContentList.clear()
        filterContentList.addAll(filterResultList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        private var bookList = mutableListOf<Book>()
        fun setContentList(books : List<Book>){
            bookList.addAll(books)
        }
    }
}