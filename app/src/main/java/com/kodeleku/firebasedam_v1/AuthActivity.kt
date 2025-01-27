package com.kodeleku.firebasedam_v1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.kodeleku.firebasedam_v1.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAuthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ANALYTICS EVENT
        // Instanciamos FirebaseAnalytics para llamarlo en nuestro proyecto
        val analaytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Creamos una instanciación de Bundle
        val bundle = Bundle()
        // Damos una clave y valor a nuestra instancia "bundle"
        bundle.putString("message", "Integración de Firebase completa")
        // llamamos al método logEvent de la instancia "analytics" que necesita una clave "InitScreen" y unos parámetros, que recibe de "bundle"
        analaytics.logEvent("InitScreen", bundle)

        // SETUP
        setup()
    }

    private fun setup() {
        // Titulo de la pantalla
        title = "Authentication"
        // Acceder al botón de registro
        binding.btnSignUp.setOnClickListener{
            // Comprobar datos correctos, en este ejemplo simplemente comprobamos que los editText no esten vacíos
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){
                // Accedemos a FirebaseAuth, crear instancia y usamos la función que nos permite crear un usuario con email y password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .addOnCompleteListener{ // Nos notifica si la operación de registro ha sido correcta o no

                        if (it.isSuccessful){
                            // Como indicamos en la fun showHome debemos pasarle el email y el tipo de proveedor
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                            // En el caso de email, para evitar el error debemos añadir los interrogantes por si pudiese ser null y en el caso de que lo sea nos mande un string vacío
                        }else { // Si no es correcto le mostramos la Alerta de que ha habido un error
                            showAlert()
                        }
                    }
            }
        }
        // Listener de botón de login (Solo cambia el método de Firebase de createUserWithEmailAndPassword a signInWithEmailAndPassword)
        binding.btnLogin.setOnClickListener{
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        }else {
                            showAlert()
                        }
                    }
            }
        }

    }
    private fun showAlert() {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage("Se ha producido un error autenticando al usuario")
            setPositiveButton("Aceptar", null) // No hacer nada al hacer clic
            create()
        }.show()
    }
    private fun showHome(email: String, provider: ProviderType) {
        // Crear un Intent para iniciar HomeActivity
        val homeIntent = Intent(this, HomeActivity::class.java)

        // Pasar datos a la nueva activity
        homeIntent.putExtra("email", email)
        homeIntent.putExtra("provider", provider.name)

        // Iniciar la nueva activity
        startActivity(homeIntent)
    }


}