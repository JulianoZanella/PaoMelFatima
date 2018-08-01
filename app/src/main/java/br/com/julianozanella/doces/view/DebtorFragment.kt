package br.com.julianozanella.doces.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.adapter.DebtorsAdapter
import br.com.julianozanella.doces.extensions.messageLong
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.viewModel.ClientViewModel
import kotlinx.android.synthetic.main.fragment_fiado.view.*

class DebtorFragment : Fragment() {

    private lateinit var viewModel: ClientViewModel
    private lateinit var adapter: DebtorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!)[ClientViewModel::class.java]
        adapter = DebtorsAdapter(activity!!.application)
        viewModel.getDebtors().observe(this, Observer { clients -> adapter.setClients(clients) })
        adapter.setOnItemClickListener(object : DebtorsAdapter.ItemClickListener {
            override fun onClickListener(position: Int, view: View?, client: Client?) {
                activity?.messageLong(getString(R.string.hint_options))
            }
        })
        adapter.setOnItemLongClickListener(object : DebtorsAdapter.ItemLongClickListener {
            override fun onLongClickListener(position: Int, view: View?, client: Client?) {
                if (client != null) {
                    val dialog = DebtorOptionsFragment.newInstance(client)
                    dialog.show(fragmentManager, "dialogOptions")
                }
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fView = inflater.inflate(R.layout.fragment_fiado, container, false)
        fView.rVDevedores.adapter = adapter
        fView.rVDevedores.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        fView.rVDevedores.addItemDecoration(dividerItemDecoration)
        return fView
    }
}
