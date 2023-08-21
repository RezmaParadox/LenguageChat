package com.example.lenguagechat.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lenguagechat.R
import com.example.lenguagechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activtiy_profile.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userId = intent.getStringExtra("userId")

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            userId?.let { FirebaseDatabase.getInstance().getReference("Users").child(it) }!!

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                titleUserName.setText(user!!.userName)
                titleUserLastName.setText(user!!.lastName)
                profileUserName.setText(user!!.userName)
                profileUserLast.setText(user!!.lastName)
                profileUserLenguageM.setText(user!!.lenguageM)
                profileUserLearning.setText(user!!.lenguageA)

                if (user.profileImage == "") {
                    profileUserImg.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@UserProfileActivity).load(user.profileImage).into(profileUserImg)
                }
            }
        })
    }
}