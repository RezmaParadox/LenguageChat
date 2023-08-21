package com.example.lenguagechat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.lenguagechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val userName = EDTNombre.text.toString()
            val lastName = EDTApellido.text.toString()
            val email = EDTCorreo.text.toString()
            val password = EDTContra.text.toString()
            val lenguageM = EDTMaterna.text.toString()
            val lenguageA = EDTrain.text.toString()

            if (TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext,"Nombre es requerido",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(lastName)){
                Toast.makeText(applicationContext,"Apellido es requerido",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Correo Electrónico es requerido",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Contraseña es requerida",Toast.LENGTH_SHORT).show()
            }

            if (password.length < 6){
                Toast.makeText(applicationContext,"Minimo 6 Caracteres",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(lenguageM)){
                Toast.makeText(applicationContext,"Lengua materna es requerida",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(lenguageA)){
                Toast.makeText(applicationContext,"Lengua de aprendizaje es requerida",Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,lastName,email,password,lenguageM,lenguageA)

        }

    }

    private fun registerUser(userName:String,lastName:String,email:String,password:String,lenguageM:String,lenguageA:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("lastName",lastName)
                    hashMap.put("lenguageM",lenguageM)
                    hashMap.put("lenguageA",lenguageA)
                    hashMap.put("email",email)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            //open home activity
                            EDTNombre.setText("")
                            EDTApellido.setText("")
                            EDTCorreo.setText("")
                            EDTContra.setText("")
                            EDTMaterna.setText("")
                            EDTrain.setText("")
                            val intent = Intent(this@SignUpActivity,
                                UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}