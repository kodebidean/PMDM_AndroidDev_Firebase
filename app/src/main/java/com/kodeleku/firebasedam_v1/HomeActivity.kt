package com.kodeleku.firebasedam_v1

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompatSideChannelService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.kodeleku.firebasedam_v1.databinding.ActivityHomeBinding

// ENUM de TIPOS de PROVIDER para AUTH
enum class ProviderType {
    BASIC, // Authentication BASIC (email, password)
    GOOGLE // Authentication GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // SETUP
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setup(email ?: "No email provided", provider ?: "No provider provided")

        // GUARDADO DE DATOS
        // Acceso a shared preferences
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email) // añadimos el email
        prefs.putString("provider", provider) // añadimos el provider
        prefs.apply() // Asegurmaos que se hayan guardado los nuevos datos en nuestra app

        // REMOTE CONFIG
        // Recuperar los datos
        binding.btnCrash.visibility = View.INVISIBLE
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener{ task ->
            if (task.isSuccessful){
                val showErrorButton: Boolean = Firebase.remoteConfig.getBoolean("show_error_button")
                val errorButtonText: String = Firebase.remoteConfig.getString("error_button_text")

                if(showErrorButton){
                    binding.btnCrash.visibility = View.VISIBLE
                }
                binding.btnCrash.text = errorButtonText
            }
        }

    }
    private fun setup(email:String, provider:String) {
        title="Inicio"
        binding.tvEmail.text=email
        binding.tvProveedor.text=provider

        binding.btnLogout.setOnClickListener {
            // Borrado de datos -- Una vez delogeemos debemos asegurar de borrar los datos de shared preferences
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear() // Limpiamos las preferencias
            prefs.apply() // Aseguramos el guardado de cambios

            FirebaseAuth.getInstance().signOut()
            onBackPressedDispatcher.onBackPressed()
        }

        // CRASHLYTICS TEST BUTTON
        binding.btnCrash.setOnClickListener{
            // Nos permite enviar información de quien ha producido el error en este caso
            FirebaseCrashlytics.getInstance().setUserId(email)
            // Tipo de proveedor que ha utilizado
            FirebaseCrashlytics.getInstance().setCustomKey("provider", provider)
            // Enviar log de contexto (info del error)
            FirebaseCrashlytics.getInstance().log("Se ha pulsado el botón de testeo de error para Crashlytics")

            // Forzado de error
            throw RuntimeException("Forzado de error")
        }
    }
}