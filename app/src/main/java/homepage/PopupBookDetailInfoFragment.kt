package Homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.frag_popup_book_detail_info.*

class PopupBookDetailInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.frag_popup_book_detail_info,null)
    }

    private fun processBorrowBook(){
        popup_book_detail_info_button_borrow.setOnClickListener {
            Log.d("TAG", "@@@@ click borrow button")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(arguments != null) {
            Log.d("TAG", "==== name = ${arguments?.getString("name")} ")
            val name = arguments?.getString("name")
            val author = arguments?.getString("author")
            val version = arguments?.getInt("version")
            val publishYear = arguments?.getInt("publishYear")
            val publisher = arguments?.getString("publisher")
            val outline = arguments?.getString("outline")
            setupBookInfoDesc(name!!, author!!, version!!, publishYear!!, publisher!!, outline!!)
        }
        processBorrowBook()
    }

    fun setupBookInfoDesc(name : String, author : String, version : Int, publishYear : Int, publisher : String, outline : String) {
        popup_book_detail_info_content_name.text = name
        popup_book_detail_info_content_author.text = author
        popup_book_detail_info_content_version.text = version.toString()
        popup_book_detail_info_content_publish_year.text = publishYear.toString()
        popup_book_detail_info_content_publisher.text = publisher
        popup_book_detail_info_content_outline.text = outline
    }
}