package com.ipopov.usplash

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipopov.usplash.ui.base.BaseActivity
import com.ipopov.usplash.ui.base.onAction
import com.ipopov.usplash.ui.search.PhotoSearchAdapter
import com.ipopov.usplash.ui.search.PhotoSearchViewModel
import com.ipopov.usplash.ui.search.model.PhotoSearchVm

class MainActivity : BaseActivity<PhotoSearchViewModel>() {
    override val modelClass = PhotoSearchViewModel::class.java

    override val viewRoot: View? get() = viewContainer

    private var viewContainer: View? = null

    private lateinit var viewMaterialList: RecyclerView
    private lateinit var photoSearchAdapter: PhotoSearchAdapter

    private lateinit var viewSearchQuery: AppCompatEditText
    private var viewSearchButtonSearch: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        model.searchObserver.observe(this, Observer {
            val items = it.items
                .mapNotNull { item -> item.urls?.regular }
                .map { url -> PhotoSearchVm(url) }

            when {
                it.started -> showMaterials(items)
                it.continued -> addMaterials(items)
            }
        })
        model.loadingObserver.observe(this, Observer { loading ->
            if (loading) {
                photoSearchAdapter.startLoading()
            } else {
                photoSearchAdapter.stopLoading()
            }
        })
    }

    private fun showMaterials(items: List<PhotoSearchVm>) {
        photoSearchAdapter.setMaterials(items)
    }

    private fun addMaterials(items: List<PhotoSearchVm>) {
        photoSearchAdapter.addMaterials(items)
    }

    private fun initViews() {
        photoSearchAdapter = PhotoSearchAdapter()

        viewMaterialList = findViewById(R.id.view_search_photo_list)
        viewMaterialList.setHasFixedSize(true)
        viewMaterialList.setItemViewCacheSize(5)
        viewMaterialList.addOnScrollListener(getScrollListener())
        viewMaterialList.adapter = photoSearchAdapter
        viewMaterialList.layoutManager = LinearLayoutManager(this)

        initSearchTextView()
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = viewMaterialList.layoutManager as? LinearLayoutManager ?: return
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!model.isLoading && !model.isLastPage) {
                    if (lastPosition >= 0 && totalItemCount - lastPosition < model.prefetch) {
                        model.onContinueSearch()
                    }
                }
            }
        }
    }

    private fun initSearchTextView() {
        viewSearchQuery = findViewById(R.id.view_search_query)
        viewSearchQuery.onAction {
            if (it == EditorInfo.IME_ACTION_SEARCH) {
                applySearch(viewSearchQuery.text.toString())
            }
        }

        viewSearchButtonSearch = findViewById(R.id.view_search_button_search)
        viewSearchButtonSearch?.setOnClickListener {
            applySearch(viewSearchQuery.text.toString())
        }
    }

    private fun applySearch(query: String) {
        val formatted = query.trim()
        if (formatted.isNotEmpty()) {
            viewSearchQuery.clearFocus()
            model.onStartSearch(formatted)
        }
    }

}
