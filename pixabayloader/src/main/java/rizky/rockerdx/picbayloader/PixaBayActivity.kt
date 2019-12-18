package rizky.rockerdx.picbayloader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_pixa_bay.*
import kotlinx.android.synthetic.main.error_layout.*

class PixaBayActivity : AppCompatActivity(),ImageAdapter.PaginationAdapterCallback,HitPresenter.View {
    object INTENT {
        const val key = "pixa_key"
        const val query = "pixa_query"
        const val collumn = "pixa_collumn"
    }

    private var hitPresenter: HitPresenter? = null
    private var hitAdapter: ImageAdapter? = null
    private var currentquery = "flower"
    private var currentkey = "flower"
    private var currentcollumn = 2
    private var currentPage = 1
    private var totalpage = 0

    private var loading = false
    private var lastpage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixa_bay)

        currentquery = intent.getStringExtra(INTENT.query)
        currentkey = intent.getStringExtra(INTENT.key)
        currentcollumn = intent.getIntExtra(INTENT.collumn,2)

        initView()
        hitPresenter!!.getEverything(
            this,
            currentquery,
            currentkey,
            currentPage,
            true
        )
    }

    fun initView() {
        hitPresenter = HitPresenter(this)
        hitAdapter = ImageAdapter(null, this)
        recyclerView.adapter = hitAdapter
        error_btn_retry.setOnClickListener {
            hitPresenter!!.getEverything(
                this@PixaBayActivity,
                currentquery,
                currentkey,
                currentPage,
                true
            )
        }
        val gridLayoutManager = GridLayoutManager(this,currentcollumn)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(gridLayoutManager) {

            override fun loadMoreItems() {
                loading = true
                currentPage += 1
                hitPresenter!!.getEverything(this@PixaBayActivity, currentquery,currentkey, currentPage, false)
            }

            override fun getTotalPageCount(): Int {
                return totalpage
            }

            override fun isLastPage(): Boolean {
                return lastpage
            }

            override fun isLoading(): Boolean {
                return loading
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        hitPresenter!!.unsubscribe()
    }

    override fun retryPageLoad() {
        hitPresenter!!.getEverything(this, currentquery, currentkey,currentPage, true)
    }

    override fun onSuccessGetData(articleList: MutableList<Hit>?, refresh: Boolean) {
        loading = false
        if (currentPage > 1) {
            hitAdapter!!.removeLoadingFooter()
        }
        stopLoading()

        if (refresh) hitAdapter!!.hitList.clear()
        hitAdapter!!.addAll(articleList)
        hitAdapter!!.notifyDataSetChanged()
        if (currentPage <= hitPresenter!!.totalPage)
            hitAdapter!!.addLoadingFooter()
        else lastpage = true
    }

    override fun onErrorGetData(throwable: Throwable?) {
        Log.d("tes", "article error newsactivity")
        showErrorView()
        if (currentPage > 1) {
            hitAdapter!!.showRetry(true)
        }}

    override fun onEmptyNews() {
        Toast.makeText(applicationContext, "No result found", Toast.LENGTH_SHORT).show()
    }

    fun stopLoading() {
        if (containerList.visibility == View.GONE) {
            containerList.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    fun startLoading() {
        if (containerList.visibility == View.VISIBLE) {
            containerList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showErrorView() {
        if (error_layout.visibility == View.GONE) {
            error_layout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}
