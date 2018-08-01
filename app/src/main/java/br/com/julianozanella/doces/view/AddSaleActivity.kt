package br.com.julianozanella.doces.view

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.extensions.getPurchaseValue
import br.com.julianozanella.doces.extensions.messageShort
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.room.SaleRepository
import br.com.julianozanella.doces.util.MoneyTextWatcher
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_add_venda.*
import java.util.*

class AddSaleActivity : AppCompatActivity(), ClientsFragment.ClientsSelection {

    private var client: Client? = null
    private lateinit var mLocale: Locale
    private lateinit var mInterstitialAd: InterstitialAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_venda)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.id_propaganda_inter)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                finish()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                finish()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                finish()
            }
        }
        btn_add_sell_save.setOnClickListener { addSale() }
        mLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            Locale("pt", "BR")
        }
        add_sell_value.addTextChangedListener(MoneyTextWatcher(add_sell_value, mLocale))

    }

    private fun addSale() {
        val valorTXT: String = add_sell_value.text.toString()
        if (valorTXT.isBlank()) {
            messageShort(getString(R.string.message_value))
            return
        } else {
            if (client != null) {
                val idClient = client!!.id
                val valor: Float = getPurchaseValue(valorTXT)
                val paid: Boolean = add_sell_pay.isChecked
                val sale = Sale()
                sale.idClient = idClient
                sale.pay = paid
                sale.purchase = valor
                if (paid) sale.payDate = Date()
                saveSale(sale)
            } else {
                messageShort(getString(R.string.message_client))
            }
        }
    }

    private fun saveSale(sale: Sale) {
        val repository = SaleRepository(application)
        repository.insert(sale)
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            finish()
        }
    }

    override fun onSelectClient(client: Client?) {
        this.client = client
    }
}
