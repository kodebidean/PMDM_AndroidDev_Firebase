package com.kodeleku.firebasedam_v1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Instanciamos FirebaseAnalytics para llamarlo en nuestro proyecto
        val analaytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Creamos una instanciación de Bundle
        val bundle = Bundle()
        // Damos una clave y valor a nuestra instancia "bundle"
        bundle.putString("message", "Integración de Firebase completa")
        // llamamos al método logEvent de la instancia "analytics" que necesita una clave "InitScreen" y unos parámetros, que recibe de "bundle"
        analaytics.logEvent("InitScreen", bundle)

    }
}