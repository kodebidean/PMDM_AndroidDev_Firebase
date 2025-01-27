package com.kodeleku.firebasedam_v1

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.kodeleku.firebasedam_v1.databinding.ActivityAuthBinding
import com.kodeleku.firebasedam_v1.databinding.ActivityHomeBinding
// ENUM de TIPOS de PROVIDER para AUTH
enum class ProviderType {
    // Authentication BASIC (email, password)
    BASIC
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // SETUP
        val bundle:Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setup(email ?: "No email provided", provider ?: "No provider provided" )
    }

    private fun setup(email:String, provider:String) {
        title="Inicio"
        binding.tvEmail.text=email
        binding.tvProveedor.text=provider

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressedDispatcher.onBackPressed()
        }

    }


}