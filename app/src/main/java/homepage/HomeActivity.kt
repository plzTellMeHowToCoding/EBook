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
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_header.*
import kotlinx.android.synthetic.main.home_tab_detail_item.*
import utils.Book

/**
 *  顯示主頁
 *  @author vincent created on 16, April, 2021
 * */

class HomeActivity : AppCompatActivity() {

    private val bookFrag = BookFragment()
    private val magazineFrag = MagazineFragment()
    private var currentPosition = 0
    private val tabTitles = arrayOf("圖書","雜誌")
    private val tabDetails = arrayOf("新到圖書","全部","目前可借","D","E","F","G","H","I","J","K","L","M","N")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initToolbar()
        initNavView()
        //initRecyclerView()
        initFrags()
        initTabLayout()
        initTabDetailLayout()
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

    private fun initFrags(){
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.home_display_content_area,bookFrag,"Book")
        fragTransaction.add(R.id.home_display_content_area,magazineFrag,"Magazine")
        fragTransaction.hide(magazineFrag)
        fragTransaction.commit()
    }

    private fun initTabLayout(){
        // 初始化 tab 名稱
        for(i in tabTitles.indices){
            home_tab.addTab(home_tab.newTab())
            home_tab.getTabAt(i)?.text = tabTitles[i]
        }
        // 隱藏選中的 tablayout 的底線
        home_tab.setSelectedTabIndicator(0)
        home_tab.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //tab?.let {
                    //    switchFragment(tab.position)
                    //}
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //tab?.let {
                    //    switchFragment(tab.position)
                    //}
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        switchFragment(tab.position)
                    }
                }

            }
        )
    }

    private fun initTabDetailLayout(){
        for(i in tabDetails.indices){
            val tab = home_tab_detail.newTab()
            val view = LayoutInflater.from(this).inflate(R.layout.home_tab_detail_item,home_tab_detail,false)
            val tvCategory = view.findViewById<TextView>(R.id.tab_detail_category)
            tvCategory.text = tabDetails[i]
            tab.customView = view
            home_tab_detail.addTab(tab)
        }
    }

    private fun switchFragment(position : Int){
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        when(currentPosition){
            0 -> {
                fragTransaction.hide(bookFrag)
            }
            1 -> {
                fragTransaction.hide(magazineFrag)
            }
        }
        when(position){
            0 -> {
                fragTransaction.show(bookFrag)
            }

            1 -> {
                fragTransaction.show(magazineFrag)
            }
        }
        fragTransaction.commit()
        currentPosition = position
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