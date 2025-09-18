package udb.edu.sv.ventaexpressmuebles

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class ProductSaleAdapter(
    private var list: MutableList<Product>,
    private val onQuantityChange: (SaleItem) -> Unit
) : RecyclerView.Adapter<ProductSaleAdapter.ProductSaleVH>() {

    inner class ProductSaleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblName: TextView = itemView.findViewById(R.id.lblSaleProductName)
        val lblPrice: TextView = itemView.findViewById(R.id.lblSaleProductPrice)
        val txtQuantity: TextInputEditText = itemView.findViewById(R.id.txtSaleQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSaleVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_sale, parent, false)
        return ProductSaleVH(view)
    }

    override fun onBindViewHolder(holder: ProductSaleVH, position: Int) {
        val product = list[position]
        holder.lblName.text = product.name
        holder.lblPrice.text = "$${product.price}"

        holder.txtQuantity.setText("")

        holder.txtQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val qty = s?.toString()?.toIntOrNull() ?: 0
                val item = SaleItem(product.id, product.name, qty, product.price ?: 0f)
                onQuantityChange(item)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}