package com.android.service.androidproject.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.android.service.androidproject.R
import com.android.service.androidproject.room.Profile
import com.android.service.androidproject.view.ProfileViewModel
import com.bumptech.glide.Glide
import java.util.regex.Pattern

class ProfileUpdate : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profName: EditText
    private lateinit var profAddress: EditText
    private lateinit var profPhone: EditText
    private lateinit var profImg: ImageView
    private lateinit var profEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnPick: Button
    private var imageUri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile_update, container, false)
        profName = root.findViewById(R.id.profName)
        profAddress = root.findViewById(R.id.profAddress)
        profEmail = root.findViewById(R.id.profEmail)
        profPhone = root.findViewById(R.id.profPhone)
        profImg = root.findViewById(R.id.profImg)
        btnSave = root.findViewById(R.id.btnModify)
        btnPick = root.findViewById(R.id.btnPick)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.allProfile.observe(viewLifecycleOwner, Observer { profile ->
            //if the list is not empty, putting data to the boxes
            if (profile.isNotEmpty()) {
                val index = profile.lastIndex
                profName.setText(profile[index].name)
                profAddress.setText(profile[index].adr)
                profEmail.setText(profile[index].email)
                profPhone.setText(profile[index].phoneNr)
                Glide.with(profImg)
                    .load(profile[index].img.toUri())
                    .centerCrop()
                    .override(500, 500)
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(profImg)
            }

        })
        btnSave.setOnClickListener { view: View ->
            //if the save button is clicked navigate vack to the profile fragment
            updateProfile()
            view.findNavController().navigate(R.id.action_profileUpdate_to_navigation_profile)
        }
        btnPick.setOnClickListener {
            pickImageFromGallery()
        }
        return root
    }

    private fun pickImageFromGallery() {
        //image pick intent
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //image pick result
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            Glide.with(profImg)
                .load(imageUri)
                .centerCrop()
                .override(500, 500)
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(profImg)
        }
    }

    //e-mail validation
    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    //name validation
    private fun String.isValidName(): Boolean {
        val NAME_PATTERN: Pattern = Pattern.compile("[ .a-zA-Z]+")
        return !TextUtils.isEmpty(this) && NAME_PATTERN.matcher(this).matches()
    }

    // phone number validation
    private fun String.isValidPhone(): Boolean {
        val PHONE_PATTERN: Pattern = Pattern.compile("[0-9$]{10}")
        return !TextUtils.isEmpty(this) && PHONE_PATTERN.matcher(this).matches()
    }

    //address validation
    private fun String.isValidAddress(): Boolean {
        val ADDRESS_PATTERN: Pattern = Pattern.compile("[ -.,a-zA-Z0-9]+")
        return !TextUtils.isEmpty(this) && ADDRESS_PATTERN.matcher(this).matches()
    }

    //profile update if the inserted data is valid
    private fun updateProfile() {
        if (!profEmail.text.toString().isEmailValid()) {
            Toast.makeText(context, "Invalid e-mail address.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!profName.text.toString().isValidName()) {
            Toast.makeText(context, "Invalid name.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!profAddress.text.toString().isValidAddress()) {
            Toast.makeText(context, "Invalid address.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!profPhone.text.toString().isValidPhone()) {
            Toast.makeText(context, "Invalid phone number.", Toast.LENGTH_SHORT).show()
            return
        }
        profileViewModel.update(
            Profile(
                profName.text.toString(),
                profAddress.text.toString(),
                profPhone.text.toString(),
                profEmail.text.toString(),
                "",
                imageUri.toString()

            )
        )
    }
}