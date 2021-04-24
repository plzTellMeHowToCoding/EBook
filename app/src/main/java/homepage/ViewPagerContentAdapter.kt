package homepage

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.frag_book.view.*


class ViewPagerContentAdapter(private val titleList: Array<String>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Log.d("TAG", "@@@@ in 0 page ")
                BookFragment()
            }
            1 -> {
                Log.d("TAG", "@@@@ in 1 page ")
                MagazineFragment()
            }
            // IDE 提醒要 + else branch，這邊隨便回傳一個 fragment
            else -> {
                BookFragment()
            }
        }
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(titleList.isNotEmpty()) titleList[position] else ""
    }

}