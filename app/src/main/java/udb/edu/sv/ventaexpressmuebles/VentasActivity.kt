package udb.edu.sv.ventaexpressmuebles

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class VentasActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var spnCustomers: Spinner
    private lateinit var rcvProducts: RecyclerView
    private lateinit var lblTotal: TextView
    private lateinit var btnRegisterSale: MaterialButton

    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ProductSaleAdapter

    private val selectedItems = mutableListOf<SaleItem>()
    private val customers = mutableListOf<Customer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        btnBack = findViewById(R.id.btnBack)
        spnCustomers = findViewById(R.id.spnCustomers)
        rcvProducts = findViewById(R.id.rcvProductsSale)
        lblTotal = findViewById(R.id.lblTotal)
        btnRegisterSale = findViewById(R.id.btnRegisterSale)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        adapter = ProductSaleAdapter(mutableListOf()) { item ->
            updateItem(item)
        }
        rcvProducts.layoutManager = LinearLayoutManager(this)
        rcvProducts.adapter = adapter

        loadCustomers()
        loadProducts()

        btnRegisterSale.setOnClickListener { saveSale() }

        // Botón físico de atrás
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // Botón de regresar
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadCustomers() {

        val UID = auth.currentUser?.uid ?: return

        dbReference.child("Customer")
            .orderByChild("employeeId")
            .equalTo(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                customers.clear()
                for (child in snapshot.children) {
                    val c = child.getValue(Customer::class.java)
                    if (c != null) customers.add(c)
                }
                val names = customers.map { it.fullName ?: "" }
                spnCustomers.adapter = ArrayAdapter(
                    this@VentasActivity,
                    android.R.layout.simple_spinner_item,
                    names
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadProducts() {

        val UID = auth.currentUser?.uid ?: return

        dbReference.child("Product")
            .orderByChild("employeeId")
            .equalTo(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Product>()
                for (child in snapshot.children) {
                    val p = child.getValue(Product::class.java)
                    if (p != null) list.add(p)
                }
                adapter.setData(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateItem(item: SaleItem) {
        selectedItems.removeAll { it.productId == item.productId }
        selectedItems.add(item)
        val total = selectedItems.sumOf { it.quantity * it.price.toDouble() }
        lblTotal.text = "Total: $${"%.2f".format(total)}"
    }

    private fun saveSale() {
        val position = spnCustomers.selectedItemPosition
        if (position == -1 || selectedItems.isEmpty()) {
            Toast.makeText(this, "Seleccione cliente y productos", Toast.LENGTH_SHORT).show()
            return
        }

        val customer = customers[position]
        val saleId = dbReference.child("Sales").push().key ?: return
        val total = selectedItems.sumOf { it.quantity * it.price.toDouble() }
        val sale = Sale(
            id = saleId,
            customerId = customer.id,
            customerName = customer.fullName,
            items = selectedItems,
            total = total.toFloat(),
            date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
            employeeId = auth.currentUser?.uid
        )

        dbReference.child("Sales").child(saleId).setValue(sale)
            .addOnSuccessListener {
                Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la venta", Toast.LENGTH_SHORT).show()
            }
    }
}