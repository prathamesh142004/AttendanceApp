package com.example.newattendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newattendanceapp.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private val binding:ActivityRegistrationBinding by lazy{
        ActivityRegistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        binding.signinBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        binding.signupBtn.setOnClickListener {
            val email=binding.email.text.toString()
            val password=binding.password.text.toString()
            val phone=binding.phone.text.toString()

            if(email.isEmpty()||password.isEmpty()||phone.isEmpty()){
                Toast.makeText(this, "Fill all the credentails", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Registration Successfull", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                        }else{
                            Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }
}