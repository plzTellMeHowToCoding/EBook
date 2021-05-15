package Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.frag_magazine.*
import Utils.Book

/**
 *  日後可考慮抽出 baseFragment 類別，將諸如 setCategoryList , setContentList 方法提出來寫
 *  not use now
 *  2021.04.23 vincent
 */

class MagazineFragment : Fragment(){
    private val contentList = ArrayList<Book>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_magazine,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }
    private fun initBookList(){
        //bookList.clear()
        repeat(50){
            val index = (0 until bookList.size).random()
            contentList.add(bookList[index])
        }
    }

    private fun initRecyclerView(){
        initBookList()
        if(activity != null) {
            val homeActivity = activity as HomeActivity
            val layoutManager = GridLayoutManager(homeActivity,3)
            val adapter = BookAdapter(homeActivity, contentList)
            //val adapter = BookAdapter(homeActivity, bookList)
            home_frag_magazine_list.layoutManager = layoutManager
            home_frag_magazine_list.adapter = adapter
        }
    }

    // 初始化分類的 tab
    /**private fun initTabCategory(){
        for(i in tabDetails.indices){
            frag_magazine_tab_category.addTab(frag_magazine_tab_category.newTab())
            frag_magazine_tab_category.getTabAt(i)?.text = tabDetails[i]
        }
    }*/

    companion object {
        private var tabDetails = mutableListOf<String>()
        private var bookList = mutableListOf<Book>()
        fun setContentList(books : List<Book>){
            //bookList = books.toMutableList()
            bookList.addAll(books)
        }
    }
}