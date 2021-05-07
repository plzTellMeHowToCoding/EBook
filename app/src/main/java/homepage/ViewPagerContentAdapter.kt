package Homepage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vincent.ebook.R


class ViewPagerContentAdapter(private val titleList: Array<String>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return BookFragment()
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("TAG", "@@@@ init tab  ")
        return if(titleList.isNotEmpty()) titleList[position] else ""
    }

    fun getTabView(context : Context, position : Int) : View {
        val view = LayoutInflater.from(context).inflate(R.layout.home_tab_category,null)
        view.findViewById<TextView>(R.id.home_tab_category_tv).text = titleList[position]
        return view
    }
}