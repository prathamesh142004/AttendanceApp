package com.example.newattendanceapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.newattendanceapp.databinding.ItemsSignInOutEntryBinding

class SignInAdapter(private val signInEntries:List<SignInEntry>):RecyclerView.Adapter<SignInAdapter.SignInViewHolder>() {
    class SignInViewHolder(val binding: ItemsSignInOutEntryBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignInViewHolder {
        val binding=ItemsSignInOutEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SignInViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return signInEntries.size
    }

    override fun onBindViewHolder(holder: SignInViewHolder, position: Int) {
        val entry=signInEntries[position]
        holder.binding.signInDate.text=entry.date
        holder.binding.signInTime.text=entry.signInTime
        holder.binding.signOutTime.text=entry.signOutTime
    }


}