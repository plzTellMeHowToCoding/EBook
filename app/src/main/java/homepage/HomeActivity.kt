package homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_header.*
import utils.Book

/**
 *  顯示主頁
 *  @author vincent created on 16, April, 2021
 * */

class HomeActivity : AppCompatActivity() {

    private val tabTitles = arrayOf("圖書","雜誌")
    private var tabDetails = arrayOf("新到圖書","全部","目前可借","D","E","F","G","H","I","J","K","L","M","N")
    val books = mutableListOf(
        Book("A","A","A",R.drawable.apple), Book("B","B","B",R.drawable.banana),
        Book("C","C","C",R.drawable.cherry), Book("D","D","D",R.drawable.grape),
        Book("E","E","E",R.drawable.mango)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initToolbar()
        initNavView()
        initViewPagerContent()
        BookFragment.setContentList(books)
        MagazineFragment.setContentList(books)
        initTabCategoryLayout()
        /**
         *  測試讀取 & 寫入 firebase，先暫時留在這，等之後要讀寫 data 時再搬去合宜的區塊
        //val firestore = FirebaseFirestore.getInstance()
        //val book = Book("First","vincent","non",R.drawable.apple)
        //firestore.collection("Books").document("First").set(book)
        //    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
        //    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
        firestore.collection("Books").document("First").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
        */
    }


    // 初始化要顯示在 viewPager 裡的內容
    private fun initViewPagerContent(){
        val bookContentAdapter = ViewPagerContentAdapter(tabTitles,supportFragmentManager)
        home_view_pager_content_area.adapter = bookContentAdapter
        home_tab.setupWithViewPager(home_view_pager_content_area)
        // 隱藏選中的 tablayout 的底線
        home_tab.setSelectedTabIndicator(0)
        home_view_pager_content_area.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("TAG", "@@@@ onPageScrollStateChanged: ")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("TAG", "@@@@ onPageScrolled: ")
            }

            /**
             *  每當 viewpager 觸發換頁動作（ex. 圖書 -> 雜誌），會呼叫 onPageSelected 方法
             *  故在這方法中去重新設置 home_tab_category 分類
             */
            override fun onPageSelected(position: Int) {
                home_tab_category.removeAllTabs()
                tabDetails.reverse()
                initTabCategoryContent(tabDetails)
            }
        })
    }

    // 監聽 tab 的點擊事件
    private fun initTabCategoryLayout(){
        home_tab_category.setSelectedTabIndicator(0)
        initTabCategoryContent(tabDetails)
        home_tab_category.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        Log.d("TAG", "Clicked ${tab?.position}")
                    }
                }

            }
        )
    }

    // 初始化 tab 分類
    private fun initTabCategoryContent(tabDetails : Array<String>){
        for(i in tabDetails.indices){
            val tab = home_tab_category.newTab()
            val view = LayoutInflater.from(this).inflate(R.layout.home_tab_category,null)
            view.findViewById<TextView>(R.id.home_tab_category_tv).text = tabDetails[i]+"\n"+"(2000)"
            tab.customView = view
            home_tab_category.addTab(tab)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar,menu)
        menu?.let {
            initToolbarSearchView(menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_home_toolbar_book -> {
                Toast.makeText(this,"clicked home_toolbar_book",Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> {
                home_drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private fun initToolbarSearchView(menu : Menu){
        val item = menu.findItem(R.id.menu_home_toolbar_search)
        val searchView = item.actionView as SearchView
        searchView.isIconified = true
    }

    private fun initToolbar(){
        setSupportActionBar(home_toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.menu_home_toolbar_more_icon)
        }
    }

    private fun initNavView(){
        processNavHeader()
        processNavMenu()
        processNavBottom()
    }

    // 處理 navigation view 中間 menu 邏輯
    private fun processNavMenu(){
        home_toolbar_nav_view.setCheckedItem(R.id.firstLibrary)
        home_toolbar_nav_view.setNavigationItemSelectedListener {
            Toast.makeText(this,"clicked first library",Toast.LENGTH_SHORT).show()
            true
        }
    }

    /**
     * 處理 navigation view 最上方 edit, remove 邏輯
     *
     * 由於 header view 中的 component 不能直接呼叫 setOnClickListener，故要先找到 headerView Object 後
     * 再利用 headerView object 呼叫 findViewById()，得到 headerView 中的其他 component
     */
    private fun processNavHeader(){
        val headerView = home_toolbar_nav_view.getHeaderView(0)
        val headerEdit = headerView.findViewById<TextView>(R.id.home_nav_header_edit)
        headerEdit.setOnClickListener {
            if(home_nav_header_remove.visibility == View.INVISIBLE){
                setNavHeaderItemTextAndVisible("Cancel",View.VISIBLE)
            }else{
                setNavHeaderItemTextAndVisible("Edit",View.INVISIBLE)
            }
        }
    }

    //處理 navigation view 最下方 Add Library 邏輯
    private fun processNavBottom(){
        home_nav_add_library.setOnClickListener{
            Toast.makeText(this,"Clicked add library",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @param title 設定要顯示的標題
     * @param showOrNot 設定是否顯示 remove icon
     * */
    private fun setNavHeaderItemTextAndVisible(title : String, showOrNot:Int){
        home_nav_header_edit.text = title
        home_nav_header_remove.visibility = showOrNot
    }
}