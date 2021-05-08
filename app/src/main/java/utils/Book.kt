package utils

import android.net.Uri

/**
 *  存放電子書相關資料：
 *  @param name 書名
 *  @param author 作者
 *  @param version 版次
 *  @param publishDate 出版日期
 *  @param publisher 出版社
 *  @param size 書的尺寸
 *  @param isbn ISBN
 *  @param translator 譯者
 *  @param relatedLink 書本的相關連結（未來可放導向至其他網頁的連結，如：博客來、金石堂等）
 *  @param uri 圖片上傳至雲端的連結
 *  @param uploadTime 資料寫入 Firebase 的時間
 *  ============== 日後可多增加以下欄位 ==============
 *  4. 是否可外借
 *  5. 分類
 *  ============== 日後可多增加以上欄位 ==============
 *
 *  vincent - created on 16, April, 2021
 * */
class Book(val name : String,
           val author : String,
           val version : Int,
           val publishDate : Int,
           val publisher : String,
           val size : String,
           val isbn : String,
           val translator : String,
           val relatedLink : String,
           val uri : String,
           val uploadTime : Long) {
}