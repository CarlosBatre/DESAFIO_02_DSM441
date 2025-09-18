package udb.edu.sv.ventaexpressmuebles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class SaleItemAdapter(
    private var list: MutableList<SaleItem>
): RecyclerView.Adapter<SaleItemAdapter.ItemVH>() {

    inner class ItemVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val lblName: TextView = itemView.findViewById(R.id.lblSaleItemName)
        val lblInfo: TextView = itemView.findViewById(R.id.lblSaleItemInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_item, parent, false)
        return ItemVH(view)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = list[position]
        holder.lblName.text = item.productName
        holder.lblInfo.text = "Cantidad: ${item.quantity} x $${item.price}"
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<SaleItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
