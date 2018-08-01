package br.com.julianozanella.doces.adapter

import android.app.Application
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.julianozanella.doces.R
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.room.SaleRepository
import br.com.julianozanella.doces.util.toImage
import kotlinx.android.synthetic.main.clientes_item.view.*

class ClientsAdapter(private val application: Application) : RecyclerView.Adapter<ClientsAdapter.ClientViewHolder>() {

    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        val photo: ImageView = itemView.photo
        val name: TextView = itemView.name
        val phone: TextView = itemView.phone
        val value: TextView = itemView.value

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            itemClickListener!!.onClickListener(adapterPosition, p0, clients?.get(adapterPosition))
        }

        override fun onLongClick(p0: View?): Boolean {
            if(itemLongClickListener!= null) {
                itemLongClickListener!!.onLongClickListener(adapterPosition, p0, clients?.get(adapterPosition))
                return true
            }
            return false
        }

    }

    interface ItemClickListener {
        fun onClickListener(position: Int, view: View?, client: Client?)
    }

    interface ItemLongClickListener {
        fun onLongClickListener(position: Int, view: View?, client: Client?)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(application)
    private var clients: List<Client>? = null
    private var itemClickListener: ItemClickListener? = null
    private var itemLongClickListener: ItemLongClickListener? = null
    private lateinit var itemView: View
    private var selectedPosition = -1
    var selectable: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        itemView = mInflater.inflate(R.layout.clientes_item, parent, false)
        return ClientViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return clients?.size ?: 0
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        if (selectable) {
            holder.itemView.background = if (selectedPosition == position)
                application.getDrawable(R.drawable.selected_background)
            else
                application.getDrawable(R.drawable.unselected_background)
        }
        if (clients != null) {
            val current = clients!![position]
            holder.name.text = current.name
            holder.phone.text = current.phone
            val repository = SaleRepository(application)
            holder.value.text = application.getString(R.string.real_formatter, repository.getSales(current.id))
            if (current.photoArray != null) {
                holder.photo.setImageBitmap(current.photoArray!!.toImage())
            }
        }
    }

    fun setClients(clients: List<Client>?) {
        this.clients = clients
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun notifySelected(position: Int) {
        if (!selectable) return
        if (position == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(selectedPosition)
    }

    fun setOnItemLongClickListener(itemLongClickListener: ItemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener
    }

}