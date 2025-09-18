package udb.edu.sv.ventaexpressmuebles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ProductAdapter(
    private var list: MutableList<Product>,
    private val listener: OnProductAction
): RecyclerView.Adapter<ProductAdapter.ProductVH>() {

    interface OnProductAction {

        fun onEdit(product: Product)
        fun onDelete(product: Product)

    }

    inner class ProductVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        val lblProductName: TextView = itemView.findViewById(R.id.lblProductName)
        val lblProductPriceAndStock: TextView = itemView.findViewById(R.id.lblProductPriceAndStock)
        val btnProductEdit: MaterialButton = itemView.findViewById(R.id.btnProductEdit)
        val btnProductDelete: MaterialButton = itemView.findViewById(R.id.btnProductDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductVH(view)

    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {

        val product = list[position]

        holder.lblProductName.text = product.name
        holder.lblProductPriceAndStock.text = "$${product.price} - Stock: ${product.stock}"
        holder.btnProductEdit.setOnClickListener { listener.onEdit(product) }
        holder.btnProductDelete.setOnClickListener { listener.onDelete(product) }

    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<Product>) {

        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()

    }

}