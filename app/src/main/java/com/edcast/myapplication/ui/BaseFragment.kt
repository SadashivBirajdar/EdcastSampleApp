package com.edcast.myapplication.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    private var progressDialog: CustomProgressDialog? = null

    fun showProgress() {
        requireActivity().run {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0);
        }


        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.newInstance("Loading, Please wait...", true)
        }
        parentFragmentManager.let {
            if (progressDialog?.isAdded == false)
                progressDialog?.show(it, CustomProgressDialog.TAG)
        }
    }

    fun hideProgress() {
        progressDialog?.dismiss()
    }

    fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
