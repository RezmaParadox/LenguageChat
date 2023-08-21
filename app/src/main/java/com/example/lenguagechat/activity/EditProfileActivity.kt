package com.example.lenguagechat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lenguagechat.R
import com.example.lenguagechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activtiy_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                editName.setText(user!!.userName)
                editLast.setText(user!!.lastName)
                editLenguageM.setText(user!!.lenguageM)
                editLearning.setText(user!!.lenguageA)
            }
        })

        saveButton.setOnClickListener {
            val updateName = editName.text.toString()
            val updateLast = editLast.text.toString()
            val updateLenguageM = editLenguageM.text.toString()
            val updateLenguageA = editLearning.text.toString()

            if (TextUtils.isEmpty(updateName)){
                Toast.makeText(applicationContext,"Nombre es requerido",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(updateLast)){
                Toast.makeText(applicationContext,"Apellido es requerido",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(updateLenguageM)){
                Toast.makeText(applicationContext,"Lenguaje Materno es requerido",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(updateLenguageA)){
                Toast.makeText(applicationContext,"Idioma a aprender es requerido",Toast.LENGTH_SHORT).show()
            }

            updateData(updateName,updateLast,updateLenguageM,updateLenguageA)
        }
    }

    private fun updateData(name: String, last: String, lenguageM: String, lenguageA: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user = mapOf<String,String>(
            "userName" to name,
            "lastName" to last,
            "lenguageM" to lenguageM,
            "lenguageA" to lenguageA
        )
        databaseReference.child(firebaseUser.uid).updateChildren(user).addOnSuccessListener {
            editName.text.clear()
            editLast.text.clear()
            editLenguageM.text.clear()
            editLearning.text.clear()
            Toast.makeText(this,"Datos Actualizados",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@EditProfileActivity,
                ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo la Actualización",Toast.LENGTH_SHORT).show()
        }}
}
