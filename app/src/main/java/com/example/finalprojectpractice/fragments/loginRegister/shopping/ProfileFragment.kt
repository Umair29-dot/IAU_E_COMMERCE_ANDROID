package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.activities.LoginRegisterActivity
import com.example.finalprojectpractice.data.User
import com.example.finalprojectpractice.databinding.FragmentProfileBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

	private lateinit var binding: FragmentProfileBinding
	private val viewModel by viewModels<ProfileViewModel>()
	private var currentUser: User? = null
	private var imageUri: Uri? = null
	private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentProfileBinding.inflate(inflater)

		//It will be executed when we press the edit button
		imageActivityResultLauncher =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
				imageUri = it.data?.data
				Glide.with(this@ProfileFragment).load(imageUri).error(ColorDrawable(Color.GRAY))
					.into(binding.ivUser)

				if (currentUser != null) {
					viewModel.updateUser(currentUser!!, imageUri)
				}
			}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.ivEdit.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "image/*"
			imageActivityResultLauncher.launch(intent)
		}

		binding.tvSignOut.setOnClickListener {
			val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
			startActivity(intent)
			requireActivity().finish()
		}

		binding.tvDeleteAccount.setOnClickListener {
			showDeleteDialog()
		}

		lifecycleScope.launchWhenStarted {
			viewModel.user.collectLatest {
				when (it) {
					is Resource.Loading -> {
					}

					is Resource.Success -> {
						currentUser = it.data!!
						showUserInformation(it.data!!)
					}

					is Resource.Error -> {
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
							.show()
					}

					else -> Unit
				}
			}
		}

		lifecycleScope.launchWhenStarted {
			viewModel.updateUser.collectLatest {
				when (it) {
					is Resource.Loading -> {
					}

					is Resource.Success -> {
					}

					is Resource.Error -> {
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
							.show()
					}

					else -> Unit
				}
			}
		}

	}

	private fun showUserInformation(data: User) {
		binding.apply {
			Glide.with(this@ProfileFragment).load(data.imagePath).error(ColorDrawable(Color.GRAY))
				.into(ivUser)
			tvName.text = data.firstName + " " + data.lastName
			tvEmail.text = data.email
		}
	}

	private fun showDeleteDialog() {
		val dialog = Dialog(requireContext())
		dialog.setContentView(R.layout.delete_dialog_layout)
		dialog.setCancelable(false)

		val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
		val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

		btnDelete.setOnClickListener {

		}

		btnCancel.setOnClickListener {
			dialog.dismiss()
		}

		dialog.show()
	}


}