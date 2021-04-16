package homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.vincent.ebook.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_header.*

/**
 *  顯示主頁
 *  @author vincent created on 16, April, 2021
 * */

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initToolbar()
        initNavView()
    }

    private fun initToolbar(){
        setSupportActionBar(home_toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.menu_home_toolbar_more_icon)
        }
    }

    /**
     * 由於 header view 中的 component 不能直接呼叫 setOnClickListener，故要先找到 headerView Object 後
     * 再利用 headerView object 呼叫 findViewById()，得到 headerView 中的其他 component
     */
    private fun initNavView(){
        val headerView = home_toolbar_nav_view.getHeaderView(0)
        val headerEdit = headerView.findViewById<TextView>(R.id.home_nav_header_edit)
        headerEdit.setOnClickListener {
            if(home_nav_header_remove.visibility == View.INVISIBLE){
                setNavHeaderItemTextAndVisible("Cancel",View.VISIBLE)
            }else{
                setNavHeaderItemTextAndVisible("Edit",View.INVISIBLE)
            }
        }
        home_toolbar_nav_view.setCheckedItem(R.id.firstLibrary)
        home_toolbar_nav_view.setNavigationItemSelectedListener {
            Toast.makeText(this,"clicked first library",Toast.LENGTH_SHORT).show()
            true
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar,menu)
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

}