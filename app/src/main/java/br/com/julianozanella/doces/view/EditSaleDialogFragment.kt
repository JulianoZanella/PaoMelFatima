package br.com.julianozanella.doces.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.extensions.getPurchaseValue
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.util.MoneyTextWatcher
import br.com.julianozanella.doces.viewModel.SaleViewModel
import kotlinx.android.synthetic.main.fragment_edit_venda_dialog.view.*
import java.text.DateFormat


class EditSaleDialogFragment : DialogFragment() {

    private var sale: Sale? = null
    private lateinit var viewModel: SaleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sale = it.getSerializable(SALE) as Sale?
        }
        viewModel = ViewModelProviders.of(activity!!)[SaleViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rView = inflater.inflate(R.layout.fragment_edit_venda_dialog, container, false)
        val editValue = rView.edit_sell_value
        editValue.addTextChangedListener(MoneyTextWatcher(editValue))
        val editPay = rView.edit_sell_pay
        val editDate = rView.edit_sell_date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editDate.inputType =  EditText.AUTOFILL_TYPE_NONE
        }
        editDate.setOnClickListener {
            val picker = DatePickerFragment()
            picker.show(fragmentManager, "datePicker")
        }
        var updatedSale = Sale()
        sale?.let {
            updatedSale = Sale(it.id, it.idClient, it.purchase, it.pay, it.payDate, it.saleDate)
            editValue.setText(String.format("%.2f", it.purchase))
            editPay.isChecked = it.pay
            viewModel.updateDate.value = it.saleDate
        }
        viewModel.updateDate.observe(activity!!, Observer {
            if (it != null) {
                val sd = DateFormat.getDateInstance(DateFormat.SHORT)
                editDate.setText(sd.format(it))
                updatedSale.saleDate = it
            }
        })
        rView.btn_edit_sell_cancel.setOnClickListener { dismiss() }
        rView.btn_edit_sell_delete.setOnClickListener { delete(sale) }
        rView.btn_edit_sell_save.setOnClickListener {
            updatedSale.pay = editPay.isChecked
            val value: Float = activity?.getPurchaseValue(editValue.text.toString()) ?: 0F
            updatedSale.purchase = value
            if (updatedSale.pay) updatedSale.payDate = updatedSale.saleDate
            update(updatedSale)
        }
        return rView
    }

    fun update(sale: Sale) {
        viewModel.update(sale)
        dismiss()
    }

    fun delete(sale: Sale?) {
        if (sale != null) {
            viewModel.delete(sale)
        }
        dismiss()
    }

    companion object {

        private const val SALE = "sale"
        @JvmStatic
        fun newInstance(sale: Sale) =
                EditSaleDialogFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(SALE, sale)
                    }
                }
    }
}
