package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductoActivity : AppCompatActivity(), ProductAdapter.OnProductAction {

    private lateinit var rcvProducts: RecyclerView
    private lateinit var btnAddProduct: FloatingActionButton
    private lateinit var dbReference: DatabaseReference
    private lateinit var adapter: ProductAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto)

        initElementAndViews()
        buttonsListeners()
        listenChanges()

    }

    private fun listenChanges() {

        val UID = auth.currentUser?.uid ?: return

        dbReference.orderByChild("employeeId")
            .equalTo(UID)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = mutableListOf<Product>()
                    for (child in snapshot.children) {
                        val product = child.getValue(Product::class.java)
                        if (product != null) list.add(product)
                    }
                    adapter.setData(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProductoActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })

    }

    override fun onEdit(product: Product) {

        val intent = Intent(this, InsertProductActivity::class.java)
        intent.putExtra(InsertProductActivity.EXTRA_ID, product.id)
        intent.putExtra(InsertProductActivity.EXTRA_NAME, product.name)
        intent.putExtra(InsertProductActivity.EXTRA_PRICE, product.price.toString())
        intent.putExtra(InsertProductActivity.EXTRA_DESCRIPTION, product.description)
        intent.putExtra(InsertProductActivity.EXTRA_STOCK, product.stock.toString())
        startActivity(intent)

    }

    override fun onDelete(product: Product) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Quieres eliminar ${product.name} de tu inventario?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                product.id?.let { dbReference.child(it).removeValue() }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()

    }

    private fun initElementAndViews() {

        rcvProducts = findViewById(R.id.rcvProducts)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        dbReference = FirebaseDatabase.getInstance().reference.child("Product")
        auth = FirebaseAuth.getInstance()

        adapter = ProductAdapter(mutableListOf(), this)
        rcvProducts.layoutManager = LinearLayoutManager(this)
        rcvProducts.adapter = adapter

    }

    private fun buttonsListeners() {

        var intent: Intent

        btnAddProduct.setOnClickListener {

            intent = Intent(this, InsertProductActivity::class.java)
            startActivity(intent)

        }

    }

}