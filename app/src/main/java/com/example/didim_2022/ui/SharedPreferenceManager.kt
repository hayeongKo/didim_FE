import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveCount(context: Context, count:Int) {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("ccount", count)
    editor.apply()
}

fun getCount(context:Context): Int {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("ccount", 0)!!
}

fun savePerfect(context: Context, cperfect : Int) {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("cperfect",cperfect)
    editor.apply()
}

fun getPerfect(context:Context): Int {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("cperfect", 0)!!
}

fun saveGood(context: Context, cgood : Int) {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("cgood", cgood)
    editor.apply()
}

fun getGood(context: Context): Int {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("cgood", 0)!!
}

fun saveBad(context: Context, cbad : Int) {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("cbad", cbad)
    editor.apply()
}

fun getBad(context: Context): Int {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("cbad", 0)!!
}

fun saveMiss(context: Context, cgood : Int) {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("cmiss", cgood)
    editor.apply()
}

fun getMiss(context: Context): Int {
    val spf = context.getSharedPreferences("sensor", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("cmiss", 0)!!
}
