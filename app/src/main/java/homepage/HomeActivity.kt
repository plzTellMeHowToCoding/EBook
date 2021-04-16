package homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.frag_book.*
import kotlinx.android.synthetic.main.home_nav_header.*
import utils.Book

/**
 *  顯示主頁
 *  @author vincent created on 16, April, 2021
 * */

class HomeActivity : AppCompatActivity() {

    val books = mutableListOf(Book("A","A","A",R.drawable.apple),Book("B","B","B",R.drawable.banana),
                                                Book("C","C","C",R.drawable.cherry), Book("D","D","D",R.drawable.grape),
                                                Book("E","E","E",R.drawable.mango))
    val bookList = ArrayList<Book>()

    private val bookFrag = BookFragment()
    private val magazineFrag = MagazineFragment()
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initToolbar()
        initNavView()
        //initRecyclerView()
        initFrags()
        initTabLayout()
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

    private fun initBookList(){
        bookList.clear()
        repeat(50){
            val index = (0 until books.size).random()
            bookList.add(books[index])
        }
    }

    private fun initRecyclerView(){
        initBookList()
        val layoutManager = GridLayoutManager(this,3)
        val adapter = BookAdapter(this,bookList)
        home_frag_book_list.layoutManager = layoutManager
        home_frag_book_list.adapter = adapter
    }
}