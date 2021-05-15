package Utils

import java.text.SimpleDateFormat

/**
 *  格式化時間
 *  @author vincent created on 8, May, 2021
 */
object TimeUtils {
    // 顯示年 ex. 2021
    private const val DATE_FORMAT_YYYY = "yyyy"

    // 顯示年月 ex. 202105
    private const val DATE_FORMAT_YYYY_MM = "yyyyMM"

    fun getDateYear(timeMillis : Long) = SimpleDateFormat(DATE_FORMAT_YYYY).format(timeMillis)

    fun getDateYearAndMonth(timeMillis: Long) = SimpleDateFormat(DATE_FORMAT_YYYY_MM).format(timeMillis)
}