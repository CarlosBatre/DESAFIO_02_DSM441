package udb.edu.sv.ventaexpressmuebles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class SaleAdapter(
    private var list: MutableList<Sale>,
    private val onClick: (Sale) -> Unit
): RecyclerView.Adapter<SaleAdapter.SaleVH>() {

    inner class SaleVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val lblCustomer: TextView = itemView.findViewById(R.id.lblSaleCustomer)
        val lblDate: TextView = itemView.findViewById(R.id.lblSaleDate)
        val lblTotal: TextView = itemView.findViewById(R.id.lblSaleTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale, parent, false)
        return SaleVH(view)
    }

    override fun onBindViewHolder(holder: SaleVH, position: Int) {
        val sale = list[position]
        holder.lblCustomer.text = "Cliente: ${sale.customerName}"
        holder.lblDate.text = "Fecha: ${sale.date}"
        holder.lblTotal.text = "Total: $${sale.total}"

        holder.itemView.setOnClickListener { onClick(sale) }
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<Sale>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}