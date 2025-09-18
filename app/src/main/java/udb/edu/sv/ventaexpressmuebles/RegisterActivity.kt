package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.jvm.java

class RegisterActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    lateinit var tvRedirectLogin: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etEmail = findViewById(R.id.etSEmailAddress)
        etConfPass = findViewById(R.id.etSConfPassword)
        etPass = findViewById(R.id.etSPassword)
        btnSignUp = findViewById(R.id.btnSSigned)
        tvRedirectLogin = findViewById(R.id.tvRedirectLogin)
        auth = Firebase.auth
        btnSignUp.setOnClickListener {
            signUpUser()
        }
        tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString().trim()
        val pass = etPass.text.toString().trim()
        val confirmPassword = etConfPass.text.toString().trim()

        // Validaciones
        if (email.isEmpty()) {
            etEmail.error = "El correo es obligatorio"
            etEmail.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Correo inválido"
            etEmail.requestFocus()
            return
        }
        if (pass.isEmpty()) {
            etPass.error = "La contraseña es obligatoria"
            etPass.requestFocus()
            return
        }
        if (pass.length < 6) {
            etPass.error = "Debe tener al menos 6 caracteres"
            etPass.requestFocus()
            return
        }
        if (confirmPassword.isEmpty()) {
            etConfPass.error = "Debe confirmar la contraseña"
            etConfPass.requestFocus()
            return
        }
        if (pass != confirmPassword) {
            etConfPass.error = "Las contraseñas no coinciden"
            etConfPass.requestFocus()
            return
        }

        // Firebase SignUp
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error en el registro: ${it.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

}