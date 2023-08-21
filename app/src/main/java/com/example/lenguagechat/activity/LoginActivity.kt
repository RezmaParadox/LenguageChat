package com.example.lenguagechat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.lenguagechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private  var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        //firebaseUser = auth!!.currentUser!!

        //check if user login then navigate to user screen
        if (firebaseUser != null) {
            val intent = Intent(
                this@LoginActivity,
                UsersActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        btnIngresar.setOnClickListener {
            val email = EDTCorreoLogin2.text.toString()
            val password = EDTContraLogin2.text.toString()

            if (TextUtils.isEmpty(email))  {

                EDTCorreoLogin2!!.error = "Este campo no puede estar vacío"


            } else if (TextUtils.isEmpty(password)) {
                EDTContraLogin2!!.error = "Este campo no puede estar vacío"

            } else{

                auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            EDTCorreoLogin2.setText("")
                            EDTContraLogin2.setText("")
                            val intent = Intent(
                                this@LoginActivity,
                                UsersActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Correo o contraseña son incorrrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }
}