package br.com.julianozanella.doces.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.extensions.messageLong
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.viewModel.ClientViewModel
import kotlinx.android.synthetic.main.fragment_edit_client_dialog.view.*


class EditClientDialogFragment : DialogFragment() {
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
        val rView = inflater.inflate(R.layout.fragment_edit_client_dialog, container, false)
        var update = Client()
        client?.let { it ->
            update = Client(it.id, it.name, it.phone, it.idContact, it.photoArray)
            rView.edit_name.text = Editable.Factory.getInstance().newEditable(it.name)
            rView.edit_phone.text = Editable.Factory.getInstance().newEditable(it.phone)
        }
        rView.btn_save_contact.setOnClickListener {
            val name = rView.edit_name.text.toString()
            val phone = rView.edit_phone.text.toString()
            if (client != null) {
                update(update, name, phone)
            } else {
                save(name, phone)
            }

        }
        rView.btn_cancel_contact.setOnClickListener { dismiss() }
        return rView
    }

    private fun save(name: String, phone: String) {
        if (name.isBlank() || phone.isBlank()) {
            activity?.messageLong(getString(R.string.message_fill_all))
        } else {
            val client = Client()
            client.name = name
            client.phone = phone
            viewModel.insert(client)
            dismiss()
        }
    }


    private fun update(client: Client, name: String, phone: String) {
        client.name = name
        client.phone = phone
        viewModel.update(client)
        dismiss()
    }


    companion object {
        private const val CLIENT = "client"

        @JvmStatic
        fun newInstance(client: Client?) =
                EditClientDialogFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(CLIENT, client)
                    }
                }
    }
}
