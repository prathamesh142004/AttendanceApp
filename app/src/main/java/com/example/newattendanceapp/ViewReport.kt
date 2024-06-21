package com.example.newattendanceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newattendanceapp.databinding.ActivityViewReportBinding
import kotlin.math.sign
// Assuming you have Firebase imports and authentication set up
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth


class ViewReport : AppCompatActivity() {
    private val binding:ActivityViewReportBinding by lazy{
        ActivityViewReportBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: SignInAdapter
    private val signInEntries= mutableListOf<SignInEntry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val entries=intent.getParcelableArrayListExtra<SignInEntry>("SIGN_IN_OUT_ENTRIES")
        if (entries!=null){
            signInEntries.addAll(entries)
        }

        adapter= SignInAdapter(signInEntries)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter

        retrieveAttendanceData()
    }
    private fun retrieveAttendanceData() {
        // Get current user ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Reference to Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Query Firestore for attendance data
        db.collection("users").document(userId)
            .collection("attendance")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Clear existing data in signInEntries
                signInEntries.clear()

                // Iterate through query snapshot documents
                for (document in querySnapshot.documents) {
                    val date = document.getString("date") ?: ""
                    val signInTime = document.getString("signInTime") ?: ""
                    val signOutTime = document.getString("signOutTime") ?: ""

                    // Create SignInEntry object and add to list
                    val signInEntry = SignInEntry(date, signInTime, signOutTime)
                    signInEntries.add(signInEntry)
                }

                // Update RecyclerView adapter with new data
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle failures, such as network issues or permission errors
                Toast.makeText(this, "Error retrieving data: $e", Toast.LENGTH_SHORT).show()
            }
    }

}