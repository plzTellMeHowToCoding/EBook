package utils

/**
 *  存放電子書相關資料，預計要有以下幾項資訊
 *  @param name 書名
 *  @param author 作者
 *  @param publisher 出版社
 *  @param bookImageId 書的圖片ID
 *  4. 是否可外借
 *  5. 分類
 *  @author vincent - created on 16, April, 2021
 * */
class Book(val name : String, val author : String, val publisher : String, val bookImageId : Int) {
}