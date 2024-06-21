package com.example.newattendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.newattendanceapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimetask = object : Runnable {
        override fun run() {
            updateTime()
            handler.postDelayed(this, 1000)
        }
    }

    private val signInEntries = mutableListOf<SignInEntry>()
    private var isSignedIn = false
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set initial UI state based on sign-in status
        updateUI(isSignedIn)

        // Set current date and start time updating task
        updateDateTime()

        // Click listener for View Report button
        binding.ViewReport.setOnClickListener {
            val intent = Intent(this, ViewReport::class.java).apply {
                putParcelableArrayListExtra("SIGN_IN_OUT_ENTRIES", ArrayList(signInEntries))
            }
            startActivity(intent)
        }

        // Click listener for Sign In button
        binding.SignUserIn.setOnClickListener {
            captureSignInTime()
            isSignedIn = true
            updateUI(isSignedIn)
        }

        // Click listener for Sign Out button
        binding.signUserOut.setOnClickListener {
            captureSignOutTime()
            isSignedIn = false
            updateUI(isSignedIn)
        }
    }

    private fun updateDateTime() {
        handler.post(updateTimetask)
    }

    private fun updateTime() {
        val currentTime = Date()
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime)
        binding.time.text = formattedTime
    }

    private fun updateUI(isSignedIn: Boolean) {
        if (isSignedIn) {
            binding.SignUserIn.visibility = View.INVISIBLE
            binding.signUserOut.visibility = View.VISIBLE
        } else {
            binding.SignUserIn.visibility = View.VISIBLE
            binding.signUserOut.visibility = View.INVISIBLE
        }
    }

    private fun captureSignInTime() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val formattedTime = timeFormat.format(currentDate)

        signInEntries.add(SignInEntry(formattedDate, formattedTime))
    }

    private fun captureSignOutTime() {
        val currentDate = Date()
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDate)

        // Find the last entry and update sign-out time
        val lastIndex = signInEntries.size - 1
        if (lastIndex >= 0) {
            signInEntries[lastIndex].signOutTime = formattedTime
            val entry = signInEntries[lastIndex]
            storeSignInEntryInFirestore(entry)
        }
    }

    private fun storeSignInEntryInFirestore(entry: SignInEntry) {
        // Example of storing SignInEntry in Firestore
        val entryMap = hashMapOf(
            "date" to entry.date,
            "signInTime" to entry.signInTime,
            "signOutTime" to entry.signOutTime
        )

        // Assuming you have a collection "attendance" under the user's document
        // Replace "users/${userId}/attendance" with your actual Firestore collection path
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        db.collection("users").document(userId)
            .collection("attendance")
            .add(entryMap)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

}
