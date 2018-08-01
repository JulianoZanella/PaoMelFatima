package br.com.julianozanella.doces.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.adapter.SaleAdapter
import br.com.julianozanella.doces.extensions.messageLong
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.viewModel.SaleViewModel
import kotlinx.android.synthetic.main.fragment_vendas.view.*
import java.util.*

class SalesFragment : Fragment() {

    private lateinit var viewModel: SaleViewModel
    private lateinit var adapter: SaleAdapter
    private lateinit var months: Array<String>
    private lateinit var monthTXView: TextView
    private var monthValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!)[SaleViewModel::class.java]
        adapter = SaleAdapter(activity!!.application)
        months = resources.getStringArray(R.array.months)
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        monthValue = month
        viewModel.setFilter(calendar.get(Calendar.YEAR), monthValue + 1)
        viewModel.getSalesByMonthYear().observe(this, Observer { it -> adapter.setSales(it) })

        adapter.setOnItemClickListener(object : SaleAdapter.ItemClickListener {
            override fun onClickListener(position: Int, view: View?, sale: Sale?) {
                if (sale != null) {
                    if (view?.id == R.id.sell_pay) {
                        val check = view as CheckBox
                        sale.pay = check.isChecked
                        sale.payDate = Date()
                        viewModel.update(sale)
                    } else {
                        activity?.messageLong(getString(R.string.message_press_to_edit))
                    }
                }
            }
        })
        adapter.setOnItemLongClickListener(object : SaleAdapter.ItemLongClickListener {
            override fun onLongClickListener(position: Int, view: View?, sale: Sale?) {
                if (sale != null) showDialog(sale)
            }

        })
    }

    private fun showDialog(sale: Sale) {
        val dialog = EditSaleDialogFragment.newInstance(sale)
        dialog.show(fragmentManager, "dialog")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_vendas, container, false)
        rootView.fB_add_sell.setOnClickListener {
            val intent = Intent(activity, AddSaleActivity::class.java)
            startActivity(intent)
        }
        rootView.rV_sells.adapter = adapter
        rootView.rV_sells.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        rootView.rV_sells.addItemDecoration(dividerItemDecoration)
        monthTXView = rootView.sell_month
        setMonth(monthValue)
        monthTXView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH)
            setMonth(month)
        }
        rootView.sell_right.setOnClickListener { nextMonth() }
        rootView.sell_left.setOnClickListener { previousMonth() }
        rootView.spinner.setSelection(0, true)
        val v: View = rootView.spinner.selectedView
        (v as TextView).setTextColor(Color.BLACK)

        rootView.spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.setYear(p0?.getItemAtPosition(pos).toString().toInt())
                (view as TextView).setTextColor(Color.BLACK)
            }
        })
        viewModel.getTotalValue().observe(this, Observer { it -> rootView.sell_total.text = getString(R.string.total_saled, it) })
        viewModel.getPaidValue().observe(this, Observer { it -> rootView.sell_total_pay.text = getString(R.string.total_received, it) })

        return rootView
    }

    private fun nextMonth() {
        monthValue++
        if (monthValue == 12) monthValue = 0
        setMonth(monthValue)
    }

    private fun previousMonth() {
        monthValue--
        if (monthValue == -1) monthValue = 11
        setMonth(monthValue)
    }

    private fun setMonth(num: Int) {
        viewModel.setMonth(num + 1)
        monthTXView.text = months[num]
    }

}
