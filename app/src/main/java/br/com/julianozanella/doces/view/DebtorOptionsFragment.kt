package br.com.julianozanella.doces.view


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.extensions.messageLong
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.viewModel.ClientViewModel
import kotlinx.android.synthetic.main.fragment_fiado_options.view.*
import java.net.URLEncoder

class DebtorOptionsFragment : DialogFragment() {

    private var client: Client? = null
    private lateinit var viewModel: ClientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            client = it.getSerializable(CLIENT) as Client?
        }
        viewModel = ViewModelProviders.of(activity!!)[ClientViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rView = inflater.inflate(R.layout.fragment_fiado_options, container, false)
        rView.btn_cancel_quit.setOnClickListener { dismiss() }
        rView.btn_paidAll.setOnClickListener {
            if (client != null) {
                viewModel.payCharge(client!!.id)
                activity?.messageLong(getString(R.string.message_paid_all))
                dismiss()
            }
        }
        rView.btn_whats.setOnClickListener {
            if (client != null) sendWhats(client!!)
            dismiss()
        }
        return rView
    }

    private fun sendWhats(client: Client) {
        activity ?: return
        val packageManager = activity?.packageManager ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        val regex = Regex("[^0-9]")
        val onlyNumbers: String = client.phone.replace(regex, "")
        val phone = "55${if (onlyNumbers.startsWith("4")) onlyNumbers else "47$onlyNumbers"}"
        val valor: Float = viewModel.getCharge(client.id)
        val message = activity!!.getString(R.string.message_debt, valor)
        try {
            val url = "https://api.whatsapp.com/send?phone=$phone&text=${URLEncoder.encode(message, "UTF-8")}"
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(url)
            if (intent.resolveActivity(packageManager) != null) {
                activity?.startActivity(intent)
            }
        } catch (e: Exception) {
            activity?.messageLong(getString(R.string.error_whats))
        }
    }

    companion object {
        private const val CLIENT = "client"

        @JvmStatic
        fun newInstance(client: Client?) =
                DebtorOptionsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(CLIENT, client)
                    }
                }
    }


}
