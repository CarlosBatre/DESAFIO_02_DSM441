package udb.edu.sv.ventaexpressmuebles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var tvHello: TextView
    private lateinit var tvUserInfo: TextView
    private lateinit var btnProductos: Button
    private lateinit var btnClientes: Button
    private lateinit var btnVentas: Button
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        initViews()

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtiene el usuario actual y muestra información
        displayUserInfo()

        // Configurar listeners de botones
        setupButtonListeners()
    }

    private fun initViews() {
        tvHello = findViewById(R.id.tvHello)
        tvUserInfo = findViewById(R.id.tvUserInfo)
        btnProductos = findViewById(R.id.btnProductos)
        btnClientes = findViewById(R.id.btnClientes)
        btnVentas = findViewById(R.id.btnVentas)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupButtonListeners() {
        // Botón Productos
        btnProductos.setOnClickListener {
            val intent = Intent(this, ProductoActivity::class.java)
            startActivity(intent)
        }

        // Botón Clientes
        btnClientes.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }

        // Botón Ventas
        btnVentas.setOnClickListener {
            val intent = Intent(this, VentasActivity::class.java)
            startActivity(intent)
        }

        // Botón Cerrar Sesión
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun displayUserInfo() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Prioriza el nombre de usuario, si no está disponible usa el email
            val userIdentifier = user.displayName?.takeIf { it.isNotEmpty() }
                ?: user.email
                ?: "Usuario"

            tvHello.text = "¡Bienvenido, $userIdentifier!"

            // Información mínima adicional
            val email = user.email ?: "Email no disponible"
            tvUserInfo.text = "Email: $email"
        }
    }
}