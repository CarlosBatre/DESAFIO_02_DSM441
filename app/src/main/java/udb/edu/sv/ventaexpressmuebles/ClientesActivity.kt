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

class ClientesActivity : AppCompatActivity(), CustomerAdapter.OnCustomerAction {

    private lateinit var rcvCustomer: RecyclerView
    private lateinit var btnAddCustomer: FloatingActionButton
    private lateinit var dbReference: DatabaseReference
    private lateinit var adapter: CustomerAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clientes)

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

                    val list = mutableListOf<Customer>()

                    for (child in snapshot.children) {

                        val customer = child.getValue(Customer::class.java)
                        if (customer != null) list.add(customer)

                    }

                    adapter.setData(list)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ClientesActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }

            })

    }

    override fun onEdit(customer: Customer) {

        val intent = Intent(this, InsertCustomerActivity::class.java)
        intent.putExtra(InsertCustomerActivity.EXTRA_ID, customer.id)
        intent.putExtra(InsertCustomerActivity.EXTRA_FULLNAME, customer.fullName)
        intent.putExtra(InsertCustomerActivity.EXTRA_EMAIL, customer.email)
        intent.putExtra(InsertCustomerActivity.EXTRA_PHONENUMBER, customer.phoneNumber)
        startActivity(intent)

    }

    override fun onDelete(customer: Customer) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Quieres eliminar es usuario ${customer.fullName}?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                customer.id?.let { dbReference.child(it).removeValue() }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()

    }

    private fun initElementAndViews() {

        rcvCustomer = findViewById(R.id.rcvCustomers)
        btnAddCustomer = findViewById(R.id.btnAddCustomers)
        dbReference = FirebaseDatabase.getInstance().reference.child("Customer")
        auth = FirebaseAuth.getInstance()

        adapter = CustomerAdapter(mutableListOf(), this)
        rcvCustomer.layoutManager = LinearLayoutManager(this)
        rcvCustomer.adapter = adapter

    }

    private fun buttonsListeners() {

        var intent: Intent

        btnAddCustomer.setOnClickListener {

            intent = Intent(this, InsertCustomerActivity::class.java)
            startActivity(intent)

        }

    }

}