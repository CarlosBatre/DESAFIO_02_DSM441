package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
class SalesHistoryActivity : AppCompatActivity() {

    private lateinit var rcvSales: RecyclerView
    private lateinit var adapter: SaleAdapter
    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_history)

        rcvSales = findViewById(R.id.rcvSalesHistory)
        adapter = SaleAdapter(mutableListOf()) { sale ->
            val intent = Intent(this, SaleDetailActivity::class.java)
            intent.putExtra("saleId", sale.id)
            startActivity(intent)
        }
        rcvSales.layoutManager = LinearLayoutManager(this)
        rcvSales.adapter = adapter

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference.child("Sales")

        loadSales()
    }

    private fun loadSales() {
        val uid = auth.currentUser?.uid ?: return

        dbReference.orderByChild("employeeId").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Sale>()
                    for (child in snapshot.children) {
                        val s = child.getValue(Sale::class.java)
                        if (s != null) list.add(s)
                    }
                    adapter.setData(list)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}