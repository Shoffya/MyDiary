package com.app.mydiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mydiary.adapter.PageAdapter
import com.app.mydiary.application.PageApplication
import com.app.mydiary.bases.BaseActivity
import com.app.mydiary.model.PageVO
import com.app.mydiary.page.PageActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress
import java.lang.Exception


class MainActivity : BaseActivity() {

    private var adapter: PageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListView()
        setupOnClicks()

    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        ivSearch.setOnClickListener { onClickSearch() }
    }

    private fun setupListView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        onClickSearch()
    }

    private fun onClickAdd(){
        val intent = Intent(this, PageActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this, PageActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickSearch(){
        val busca = etSearch.text.toString()
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(1500)
            var listaFiltrada: List<PageVO> = mutableListOf()
            try {
                listaFiltrada = PageApplication.instance.helperDB?.searchPages(busca) ?: mutableListOf()
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            runOnUiThread {
                adapter = PageAdapter(this,listaFiltrada) {onClickItemRecyclerView(it)}
                recyclerView.adapter = adapter
                progress.visibility = View.GONE
                Toast.makeText(this,"Buscando por $busca",Toast.LENGTH_SHORT).show()
            }
        }).start()
    }

}