package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertProductActivity : AppCompatActivity() {

    private lateinit var txtProductName: TextInputEditText
    private lateinit var txtProductPrice: TextInputEditText
    private lateinit var txtProductDescription: TextInputEditText
    private lateinit var txtProductStock: TextInputEditText
    private lateinit var btnProductSave: MaterialButton
    private lateinit var btnProductClose: MaterialButton
    private lateinit var dbReference: DatabaseReference
    private var id: String? = null
    private lateinit var auth: FirebaseAuth

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_STOCK = "extra_stock"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insert_product)

        initElementAndViews()
        buttonsListeners()
    }

    private fun save() {
        val name = txtProductName.text?.toString()?.trim().orEmpty()
        val price = txtProductPrice.text?.toString()?.trim().orEmpty()
        val description = txtProductDescription.text?.toString()?.trim().orEmpty()
        val stock = txtProductStock.text?.toString()?.trim().orEmpty()
        val UID = auth.currentUser?.uid ?: return
        val regex = arrayOf(
            Regex("^([1-9]([0-9]{1,3})?(\\.[0-9]{0,2})?)$"),
            Regex("^([1-9]([0-9]{1,2})?)$")
        )

        if (name.isEmpty() && price.isEmpty() && description.isEmpty() && stock.isEmpty()) {

            Toast.makeText(this, "Error: Campos vacíos", Toast.LENGTH_SHORT).show()
            return

        } else if (!price.matches(regex[0])) {

            txtProductPrice.error = "Error: Solo se aceptan valores de 1 - 9999.99"
            return

        } else if (!stock.matches(regex[1])) {

            txtProductStock.error = "Error: Solo se aceptan valores de 1 - 999"
            return

        }

        if (id.isNullOrEmpty()) {

            val generateId = dbReference.push().key ?: return
            val product = Product(generateId, name, price.toFloat(), description, stock.toInt(), UID)

            dbReference.child(generateId).setValue(product)
                .addOnSuccessListener { Toast.makeText(this, "Producto almacenado", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show() }

        }
        else {

            val product = Product(id, name, price.toFloat(), description, stock.toInt(), UID)

            dbReference.child(id!!).setValue(product)
                .addOnSuccessListener { Toast.makeText(this, "Información de producto actualizada", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show() }

            finish()

        }
    }

    private fun initElementAndViews() {

        txtProductName = findViewById(R.id.txtProductName)
        txtProductPrice = findViewById(R.id.txtProductPrice)
        txtProductDescription = findViewById(R.id.txtProductDescription)
        txtProductStock = findViewById(R.id.txtProductStock)
        btnProductSave = findViewById(R.id.btnProductSave)
        btnProductClose = findViewById(R.id.btnProductClose)
        dbReference = FirebaseDatabase.getInstance().reference.child("Product")
        auth = FirebaseAuth.getInstance()

        id = intent.getStringExtra(EXTRA_ID)
        txtProductName.setText(intent.getStringExtra(EXTRA_NAME))
        txtProductPrice.setText(intent.getStringExtra(EXTRA_PRICE))
        txtProductDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
        txtProductStock.setText(intent.getStringExtra(EXTRA_STOCK))

    }

    private fun buttonsListeners() {
        btnProductSave.setOnClickListener { save() }

        btnProductClose.setOnClickListener {
            val intent = Intent(this, ProductoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}