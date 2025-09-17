package udb.edu.sv.ventaexpressmuebles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CustomerAdapter(
    private var list: MutableList<Customer>,
    private val listener: OnCustomerAction
): RecyclerView.Adapter<CustomerAdapter.CustomerVH>() {

    interface OnCustomerAction {

        fun onEdit(customer: Customer)
        fun onDelete(customer: Customer)

    }

    inner class CustomerVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        val lblCustomerFullName: TextView = itemView.findViewById(R.id.lblCustomerFullName)
        var lblCustomerEmail: TextView = itemView.findViewById(R.id.lblCustomerEmail)
        val lblCustomerPhoneNumber: TextView = itemView.findViewById(R.id.lblCustomerPhoneNumber)
        val btnCustomerEdit: MaterialButton = itemView.findViewById(R.id.btnCustomerEdit)
        val btnCustomerDelete: MaterialButton = itemView.findViewById(R.id.btnCustomerDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return CustomerVH(view)

    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) {

        val customer = list[position]

        holder.lblCustomerFullName.text = customer.fullName
        holder.lblCustomerEmail.text = customer.email
        holder.lblCustomerPhoneNumber.text = customer.phoneNumber
        holder.btnCustomerEdit.setOnClickListener { listener.onEdit(customer) }
        holder.btnCustomerDelete.setOnClickListener { listener.onDelete(customer) }

    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<Customer>) {

        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()

    }

}