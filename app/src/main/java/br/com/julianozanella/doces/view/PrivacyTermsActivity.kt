package br.com.julianozanella.doces.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.julianozanella.doces.R
import kotlinx.android.synthetic.main.activity_poltica_privacidade.*

class PrivacyTermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poltica_privacidade)
        supportActionBar?.title = getString(R.string.privacy)
        wv_content.loadUrl("file:///android_asset/politica_privacidade.html")
    }
}
