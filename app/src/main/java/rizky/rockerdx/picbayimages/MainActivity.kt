package rizky.rockerdx.picbayimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rizky.rockerdx.picbayloader.PixaBayActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this,PixaBayActivity::class.java)
        intent.putExtra(PixaBayActivity.INTENT.key,"14612099-74f1204a323215fb6b293a2f4")
        intent.putExtra(PixaBayActivity.INTENT.query,"flower")
        intent.putExtra(PixaBayActivity.INTENT.collumn,3)
        startActivity(intent)
    }
}
