package br.com.julianozanella.doces.adapter

import android.app.Application
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.room.SaleRepository
import kotlinx.android.synthetic.main.vendas_item.view.*
import java.text.DateFormat

class SaleAdapter(private val application: Application) : RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {

    inner class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        override fun onClick(p0: View?) {
            itemClickListener?.onClickListener(adapterPosition, p0, sales?.get(adapterPosition))
        }

        override fun onLongClick(p0: View?): Boolean {
            if (longClickListener != null) {
                longClickListener?.onLongClickListener(adapterPosition, p0, sales?.get(adapterPosition))
                return true
            }
            return false
        }

        val name: TextView = itemView.sell_name_client
        val date: TextView = itemView.sell_date
        val value: TextView = itemView.sell_value
        val pay: CheckBox = itemView.sell_pay

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            pay.setOnClickListener(this)
        }

    }

    interface ItemClickListener {
        fun onClickListener(position: Int, view: View?, sale: Sale?)

    }

    interface ItemLongClickListener {
        fun onLongClickListener(position: Int, view: View?, sale: Sale?)
    }


    private var itemClickListener: ItemClickListener? = null
    private var longClickListener: ItemLongClickListener? = null
    private val mInflater: LayoutInflater = LayoutInflater.from(application)
    private var sales: List<Sale>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val itemView = mInflater.inflate(R.layout.vendas_item, parent, false)
        return SaleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sales?.size ?: 0
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        if (sales != null) {
            val current = sales!![position]
            val repository = SaleRepository(application)
            val nome = repository.getNameClient(current.idClient)
            holder.name.text = nome
            val sd = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            holder.date.text = sd.format(current.saleDate).substringBefore("/")
            holder.value.text = application.getString(R.string.real_formatter, current.purchase)
            holder.pay.isChecked = current.pay
        }
    }

    fun setSales(sales: List<Sale>?) {
        this.sales = sales
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(longClickListener: ItemLongClickListener) {
        this.longClickListener = longClickListener
    }

}