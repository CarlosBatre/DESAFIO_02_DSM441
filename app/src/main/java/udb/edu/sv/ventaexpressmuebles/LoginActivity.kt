package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGitHubLogin: Button
    private lateinit var loginButton: LoginButton
    private lateinit var tvRedirectSignUp: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    companion object {
        private const val TAG = "FacebookLogin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth y CallbackManager
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        // Verificar si el usuario ya está logueado
        checkCurrentUser()

        // Inicializar vistas
        initViews()

        // Configurar listeners
        setupListeners()

        // Configurar Facebook Login
        setupFacebookLogin()
    }
    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuario ya está logueado, ir a MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initViews() {
        btnLogin = findViewById(R.id.button_login)
        btnGitHubLogin = findViewById(R.id.button_github_login)
        loginButton = findViewById(R.id.login_button)  // LoginButton oficial
        etEmail = findViewById(R.id.editTextTextEmailAddress)
        etPass = findViewById(R.id.editTextTextPassword)
        tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            login()
        }

        btnGitHubLogin.setOnClickListener {
            signInWithGitHub()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupFacebookLogin() {
        // Configurar permisos - Solo public_profile para evitar revisión
        loginButton.setReadPermissions(listOf("public_profile"))  // Cambio: quitar "email"

        // Registrar callback
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$result")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                Toast.makeText(this@LoginActivity, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Éxito en la autenticación
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val displayName = user?.displayName ?: user?.email ?: "Usuario"
                    Toast.makeText(this, "¡Bienvenido $displayName!", Toast.LENGTH_SHORT).show()

                    // Ir a MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Error en la autenticación
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Autenticación fallida: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
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
        provider.scopes = listOf("user:email")

        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                val displayName = user?.displayName ?: user?.email ?: "Usuario de GitHub"
                Toast.makeText(this, "¡Bienvenido $displayName!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error con GitHub: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pasar el resultado a Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}