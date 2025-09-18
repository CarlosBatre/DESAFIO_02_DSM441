package udb.edu.sv.ventaexpressmuebles

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import java.util.*

class GitHubAuthHelper(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()

    fun signInWithGitHub(onComplete: (Boolean, String?) -> Unit) {
        val provider = OAuthProvider.newBuilder("github.com")

        // Opcional: agregar scopes específicos
        provider.scopes = listOf("user:email")

        auth.startActivityForSignInWithProvider(context as androidx.appcompat.app.AppCompatActivity, provider.build())
            .addOnSuccessListener { authResult ->
                // Inicio de sesión exitoso
                val user = authResult.user
                onComplete(true, "Inicio de sesión exitoso con GitHub")
            }
            .addOnFailureListener { exception ->
                // Error en el inicio de sesión
                onComplete(false, "Error: ${exception.message}")
            }
    }
}