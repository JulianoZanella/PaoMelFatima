package br.com.julianozanella.doces.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.adapter.ClientsAdapter
import br.com.julianozanella.doces.extensions.messageLong
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.util.toBase64
import br.com.julianozanella.doces.viewModel.ClientViewModel
import kotlinx.android.synthetic.main.fragment_clientes.view.*

class ClientsFragment : Fragment() {

    private lateinit var viewModel: ClientViewModel
    private lateinit var adapter: ClientsAdapter
    private var callback: ClientsSelection? = null
    private var btnClosed = true

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            callback = activity as ClientsSelection
        } catch (e: ClassCastException) {
            Log.d("Error", "This activity not implement clientSelection\n${e.stackTrace}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!)[ClientViewModel::class.java]
        adapter = ClientsAdapter(activity!!.application)
        if (activity is ClientsSelection) {
            adapter.selectable = true
        }
        adapter.setOnItemClickListener(object : ClientsAdapter.ItemClickListener {
            override fun onClickListener(position: Int, view: View?, client: Client?) {
                if (activity is ClientsSelection) {
                    adapter.notifySelected(position)
                    callback?.onSelectClient(client)
                } else {
                    activity?.messageLong(getString(R.string.message_press_to_edit))
                }
            }

        })
        adapter.setOnItemLongClickListener(object : ClientsAdapter.ItemLongClickListener {
            override fun onLongClickListener(position: Int, view: View?, client: Client?) {
                if (client != null) {
                    showDialog(client)
                }
            }
        })
        viewModel.getClients().observe(this, Observer<List<Client>> { clients -> adapter.setClients(clients) })
    }

    fun showDialog(client: Client) {
        val newFragment = EditClientDialogFragment.newInstance(client)
        newFragment.show(fragmentManager, "dialog")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_clientes, container, false)
        rootView.btnAddClient.setOnClickListener {
            rootView.btnAddClient.setImageDrawable(activity!!.getDrawable(
                    if (btnClosed) R.drawable.ic_clear_black_24dp
                    else R.drawable.ic_add_black_24dp))
            rootView.btn_add_contact.visibility = if (btnClosed) View.VISIBLE else View.GONE
            rootView.btn_add_person.visibility = if (btnClosed) View.VISIBLE else View.GONE
            btnClosed = !btnClosed
        }
        rootView.btn_add_contact.setOnClickListener { selectContact() }
        rootView.btn_add_person.setOnClickListener { selectPerson() }
        rootView.rVClients.adapter = adapter
        rootView.rVClients.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        rootView.rVClients.addItemDecoration(dividerItemDecoration)
        return rootView
    }

    private fun selectPerson() {
        val dialog = EditClientDialogFragment.newInstance(null)
        dialog.show(fragmentManager, "dialogAdd")
    }

    private fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                val contactUri: Uri = data.data
                val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val indexNumber = 0
                val indexName = 1
                val indexPhoto = 2

                val cursor: Cursor = activity?.contentResolver!!.query(contactUri, projection, null, null, null)
                if (cursor.moveToFirst()) {
                    val number = cursor.getString(indexNumber)
                    val name = cursor.getString(indexName)
                    val id = cursor.getInt(indexPhoto)
                    val client = Client()
                    client.name = name
                    client.phone = number
                    client.idContact = id.toLong()
                    getPhoto(id, client)
                }
                cursor.close()
            }
        }
    }

    private fun getPhoto(id: Int, client: Client) {
        val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(activity?.contentResolver,
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong()))
        if (inputStream != null) {
            val photo = BitmapFactory.decodeStream(inputStream)
            if (photo != null) {
                client.photoArray = photo.toBase64()
            }
        } else {
            client.photoArray = null
        }
        assert(inputStream != null)
        inputStream?.close()
        saveClient(client)
    }

    private fun saveClient(client: Client) {
        viewModel.insert(client)
    }

    interface ClientsSelection {
        fun onSelectClient(client: Client?)
    }

    companion object {
        const val REQUEST_SELECT_CONTACT = 12
    }


}
