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
import com.google.firebase.database.ServerValue

class InsertCustomerActivity : AppCompatActivity() {

    private lateinit var txtCustomerFullName: TextInputEditText
    private lateinit var txtCustomerEmail: TextInputEditText
    private lateinit var txtCustomerPhoneNumber: TextInputEditText
    private lateinit var btnCustomerSave: MaterialButton
    private lateinit var btnCustomerClose: MaterialButton
    private lateinit var dbReference: DatabaseReference
    private var id: String? = null
    private lateinit var auth: FirebaseAuth

    companion object {

        const val EXTRA_ID: String = "extra_id"
        const val EXTRA_FULLNAME: String = "extra_fullname"
        const val EXTRA_EMAIL: String = "extra_email"
        const val EXTRA_PHONENUMBER: String = "extra_phonenumber"

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insert_customer)

        initElementAndViews()
        buttonsListeners()

    }

    private fun save() {

        val fullName = txtCustomerFullName.text?.toString()?.trim().orEmpty()
        val email = txtCustomerEmail.text?.toString()?.trim().orEmpty()
        val phoneNumber = txtCustomerPhoneNumber.text?.toString()?.trim().orEmpty()
        val UID = auth.currentUser?.uid ?: return
        val regex = arrayOf(
            Regex("^([A-Z][a-z]{3,10}.[A-Z][a-z]{3,10})$"),
            Regex("^([\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,})$"),
            Regex("^((\\+503.)?([2,7]\\d{3}-\\d{4}))$")
        )

        if(fullName.isEmpty() && email.isEmpty() && phoneNumber.isEmpty()) {

            Toast.makeText(this, "Error: Campos vacíos", Toast.LENGTH_SHORT).show()
            return

        }
        else if(!fullName.matches(regex[0])) {

            txtCustomerFullName.error = "Error: digitar un nombre y un apellido"
            return

        }
        else if(!email.matches(regex[1])) {

            txtCustomerEmail.error = "Error: Correo inválido"
            return

        }
        else if(!phoneNumber.matches(regex[2])) {

            txtCustomerPhoneNumber.error = "Error: Formato incorrecto (+503 ****-****)"
            return

        }

        if(id.isNullOrEmpty()) {

            val generateId = dbReference.push().key ?: return
            val customer = Customer(generateId, fullName, email, phoneNumber, UID)

            dbReference.child(generateId).setValue(customer)
                .addOnSuccessListener { Toast.makeText(this, "Cliente registrado", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show() }

        }
        else {

            val customer = Customer(id, fullName, email, phoneNumber, UID)

            dbReference.child(id!!).setValue(customer)
                .addOnSuccessListener { Toast.makeText(this, "Información del cliente actualizada", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show() }

            finish()

        }

    }

    private fun initElementAndViews() {

        txtCustomerFullName = findViewById(R.id.txtCustomerFullName)
        txtCustomerEmail = findViewById(R.id.txtCustomerEmail)
        txtCustomerPhoneNumber = findViewById(R.id.txtCustomerPhoneNumber)
        btnCustomerSave = findViewById(R.id.btnCustomerSave)
        btnCustomerClose = findViewById(R.id.btnCustomerClose)
        dbReference = FirebaseDatabase.getInstance().reference.child("Customer")
        auth = FirebaseAuth.getInstance()

        id = intent.getStringExtra(EXTRA_ID)
        txtCustomerFullName.setText(intent.getStringExtra(EXTRA_FULLNAME))
        txtCustomerEmail.setText(intent.getStringExtra(EXTRA_EMAIL))
        txtCustomerPhoneNumber.setText(intent.getStringExtra(EXTRA_PHONENUMBER))

    }

    private fun buttonsListeners() {

        var intent: Intent

        btnCustomerSave.setOnClickListener { save() }

        btnCustomerClose.setOnClickListener {

            intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
            finish()

        }

    }

}