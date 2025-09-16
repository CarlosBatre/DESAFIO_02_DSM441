// ProductoActivity.kt
package udb.edu.sv.ventaexpressmuebles

import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class ProductoActivity : AppCompatActivity() {

    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        btnBack = findViewById(R.id.btnBack)

        // Configurar callback para el botón físico de atrás
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // Funcionalidad del botón de regresar
        btnBack.setOnClickListener {
            finish()
        }
    }
}