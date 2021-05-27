package Homepage

import ManagerPage.ManagerActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_header.*
import Utils.FireBaseUtils
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 *  顯示主頁
 *  @author vincent created on 16, April, 2021
 * */

class HomeActivity : AppCompatActivity() {

    private var tabCategory = arrayOf("全部","新到圖書","目前可借","作者","出版年","F","G","H","I","J","K","L","M","N")
    private val bookFrag by lazy {
        BookFragment()
    }
    private var isLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Log.d("TAG", "@@@ isLogin = ${intent.getBooleanExtra("isLogin",false)} ")
        isLogin = intent.getBooleanExtra("isLogin",false)
        initToolbar()
        initNavView()
        initHomeTabLayout()
        initHomeContent()
        initFloatingBtn()
        // 這邊將 bookFrag 物件傳入，讓 FireBase 在更新完一些資料後能調用 bookFrag 的 function
        FireBaseUtils.setFrag(bookFrag)
    }

    private fun initHomeContent(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_content_container,bookFrag)
        transaction.commit()
    }

    // 初始化要顯示在 viewPager 裡的內容
    private fun initHomeTabLayout() {
        for (i in tabCategory.indices) {
            //val tab = home_tab_category.getTabAt(i)
            val tab = home_tab_category.newTab()
            val view = LayoutInflater.from(this).inflate(R.layout.home_tab_category, null)
            view.findViewById<TextView>(R.id.home_tab_category_tv).text = tabCategory[i]
            tab?.customView = view
            home_tab_category.addTab(tab)
        }
        home_tab_category.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //Toast.makeText(this@HomeActivity, "click", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //Toast.makeText(this@HomeActivity, "click", Toast.LENGTH_SHORT).show()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        FireBaseUtils.getAllBook()
                    }
                    1 -> {
                        Toast.makeText(this@HomeActivity, "click 1", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {

                    }
                    3 -> {
                        FireBaseUtils.getFilterBook("author","vincent")
                    }
                    4 -> {

                    }
                }
            }

        })
        //home_view_pager_content_area.adapter = bookContentAdapter
        //home_tab_category.setupWithViewPager(home_view_pager_content_area)
        // 隱藏選中的 tablayout 的底線
        //home_tab_category.setSelectedTabIndicator(0)

        /*home_view_pager_content_area.addOnPageChangeListener(object :
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
             每當 viewpager 觸發換頁動作（ex. 圖書 -> 雜誌），會呼叫 onPageSelected 方法
             故在這方法中去重新設置 home_tab_category 分類
            override fun onPageSelected(position: Int) {

            }
        })*/
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
        setNavHeaderUserName()
        setNavMenu()
        processNavMenu()
        processNavBottom()
    }

    // 處理 navigation view 中間 menu 邏輯
    private fun processNavMenu(){
        if(isLogin) {
            home_toolbar_nav_view.setCheckedItem(R.id.go_to_manager_page)
            home_toolbar_nav_view.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.go_to_manager_page -> {
                        ManagerActivity.startManagerActivity(this)
                    }
                    R.id.borrow_book -> {
                        Toast.makeText(this, "clicked borrow book", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    //處理 navigation view 最下方 Add Library 邏輯
    private fun processNavBottom(){
        home_nav_add_library.setOnClickListener{
            Toast.makeText(this,"Clicked add library",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNavMenu(){
        if(isLogin) {
            home_toolbar_nav_view.inflateMenu(R.menu.menu_home_nav_library)
        }else{
            home_toolbar_nav_view.inflateMenu(R.menu.menu_home_nav_no_login)
        }
    }

    private fun setNavHeaderUserName(){
        val headerView = home_toolbar_nav_view.getHeaderView(0)
        if(isLogin) {
            val firebaseAuth = Firebase.auth
            headerView.findViewById<TextView>(R.id.home_nav_header_title_name).text = firebaseAuth.currentUser.email
        }else{
            headerView.findViewById<TextView>(R.id.home_nav_header_title_name).text = "尚未登入哦！"
        }
    }

    //將當前顯示的列表移動至最上方
    private fun initFloatingBtn(){
        home_floating_button_go_to_top.setOnClickListener {
            bookFrag.view?.findViewById<RecyclerView>(R.id.home_frag_book_list)?.smoothScrollToPosition(View.SCROLL_INDICATOR_TOP)
        }
    }
    
    companion object{
        fun startHomeActivity(context : Context, isLogin : Boolean){
            val intent = Intent(context,HomeActivity::class.java)
            intent.putExtra("isLogin",isLogin)
            context.startActivity(intent)
        }
    }
}