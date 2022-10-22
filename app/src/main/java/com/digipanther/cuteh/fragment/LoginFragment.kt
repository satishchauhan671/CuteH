package com.digipanther.cuteh.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.DashboardActivity
import com.digipanther.cuteh.activity.LoginActivity
import com.digipanther.cuteh.common.Utility.showSnackBar
import com.digipanther.cuteh.databinding.FragmentHotelDetailsBinding
import com.digipanther.cuteh.databinding.FragmentLoginBinding
import com.digipanther.cuteh.dbHelper.UserDataHelper
import com.digipanther.cuteh.model.UserInfoModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*

class LoginFragment : Fragment() {
    private var mActivity: FragmentActivity? = null
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userInfoModel: UserInfoModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtLogin.setOnClickListener {

            if (TextUtils.isEmpty(binding.edtUserName.text)) {
                showSnackBar(binding.root, "Please Enter User Name")
            } else if (TextUtils.isEmpty(binding.edtMobileNo.text)) {
                showSnackBar(binding.root, "Please Enter Mobile No")
            } else {
                userInfoModel = UserInfoModel();
                if (userInfoModel != null) {
                    userInfoModel!!.userName = binding.edtUserName.text.toString()
                    userInfoModel!!.mobile = binding.edtMobileNo.text.toString()
                    val isSaved = UserDataHelper.saveUserData(userInfoModel!!, mActivity)
                    if (isSaved) {
                        val intent = Intent(mActivity, DashboardActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        mActivity?.finish()
                    }
                }
            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as LoginActivity?
    }
}