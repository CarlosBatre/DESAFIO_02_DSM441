package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGitHubLogin: Button  // ¡FALTABA DECLARAR ESTA VARIABLE!
    private lateinit var tvRedirectSignUp: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar todas las vistas
        btnLogin = findViewById(R.id.button_login)
        btnGitHubLogin = findViewById(R.id.button_github_login)  // ¡FALTABA ESTA LÍNEA!
        etEmail = findViewById(R.id.editTextTextEmailAddress)
        etPass = findViewById(R.id.editTextTextPassword)
        tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)

        auth = FirebaseAuth.getInstance()

        // Configurar listeners
        btnLogin.setOnClickListener {
            login()
        }

        // ¡FALTABA TODO ESTE BLOQUE!
        btnGitHubLogin.setOnClickListener {
            signInWithGitHub()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text.toString().trim()
        val pass = etPass.text.toString().trim()

        // Validación campos vacíos
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
            etPass.error = "La contraseña debe tener al menos 6 caracteres"
            etPass.requestFocus()
            return
        }

        // Firebase login
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al iniciar sesión: ${it.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInWithGitHub() {
        val provider = OAuthProvider.newBuilder("github.com")

        // Agregar scopes necesarios
        provider.scopes = listOf("user:email")

        // Iniciar autenticación
        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener { authResult ->
                // Éxito
                val user = authResult.user
                val displayName = user?.displayName ?: user?.email ?: "Usuario de GitHub"
                Toast.makeText(this, "¡Bienvenido $displayName!", Toast.LENGTH_SHORT).show()

                // Ir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                // Error
                Toast.makeText(this, "Error con GitHub: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}