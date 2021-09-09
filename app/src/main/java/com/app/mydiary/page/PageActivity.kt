package com.app.mydiary.page

import android.os.Bundle
import android.view.View
import com.app.mydiary.R
import com.app.mydiary.application.PageApplication
import com.app.mydiary.bases.BaseActivity
import com.app.mydiary.model.PageVO
import kotlinx.android.synthetic.main.activity_page.*
import java.util.*

class PageActivity : BaseActivity() {

    private var idPage: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)
        setupToolBar(toolbar,"Volta",true)
        setupPage()
        btnSavePage.setOnClickListener { onClickSavePage() }
    }

    private fun setupPage(){
        idPage = intent.getIntExtra("index",-1)
        if (idPage == -1){
            btnDeletePage.visibility = View.GONE
            return
        }
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(1500)
            var lista = PageApplication.instance.helperDB?.searchPages("$idPage",true) ?: return@Runnable
            var page = lista.getOrNull(0) ?: return@Runnable

            runOnUiThread {
                etTitle.setText(page.title)
                etText.setText(page.text)
                etDate.setText(page.text)
                progress.visibility = View.GONE
            }
        }).start()
    }

    private fun onClickSavePage(){
        val title = etTitle.text.toString()
        val text = etText.text.toString()
        val date = etDate.text.toString()
        val pages = PageVO(
            idPage,
            title,
            text,
            date
        )
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(1500)
            if(idPage == -1) {
                PageApplication.instance.helperDB?.savePage(pages)
            }else{
                PageApplication.instance.helperDB?.updatePage(pages)
            }
            runOnUiThread {
                progress.visibility = View.GONE
                finish()
            }
        }).start()
    }

    fun onClickDeletePage(view: View) {
        if(idPage > -1){
            progress.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(1500)
                PageApplication.instance.helperDB?.deletePage(idPage)
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()
        }
    }
}