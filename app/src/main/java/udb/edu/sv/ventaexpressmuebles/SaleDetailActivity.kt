package udb.edu.sv.ventaexpressmuebles

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class SaleDetailActivity : AppCompatActivity() {

    private lateinit var lblCustomer: TextView
    private lateinit var lblDate: TextView
    private lateinit var lblTotal: TextView
    private lateinit var rcvItems: RecyclerView
    private lateinit var adapter: SaleItemAdapter
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_detail)

        lblCustomer = findViewById(R.id.lblSaleDetailCustomer)
        lblDate = findViewById(R.id.lblSaleDetailDate)
        lblTotal = findViewById(R.id.lblSaleDetailTotal)
        rcvItems = findViewById(R.id.rcvSaleItems)

        adapter = SaleItemAdapter(mutableListOf())
        rcvItems.layoutManager = LinearLayoutManager(this)
        rcvItems.adapter = adapter

        dbReference = FirebaseDatabase.getInstance().reference.child("Sales")

        val saleId = intent.getStringExtra("saleId") ?: return
        loadSaleDetail(saleId)
    }

    private fun loadSaleDetail(saleId: String) {
        dbReference.child(saleId).get().addOnSuccessListener {
            val sale = it.getValue(Sale::class.java)
            if (sale != null) {
                lblCustomer.text = "Cliente: ${sale.customerName}"
                lblDate.text = "Fecha: ${sale.date}"
                lblTotal.text = "Total: $${sale.total}"
                adapter.setData(sale.items)
            }
        }
    }
}
