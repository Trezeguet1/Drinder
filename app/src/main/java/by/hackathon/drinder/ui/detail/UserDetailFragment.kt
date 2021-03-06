package by.hackathon.drinder.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.di.ViewModelFactory
import by.hackathon.drinder.util.USER_ID
import by.hackathon.drinder.util.daggerAppComponent
import kotlinx.android.synthetic.main.fragment_user_detail_show.*
import javax.inject.Inject

class UserDetailFragment : Fragment(R.layout.fragment_user_detail_show) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: UserDetailViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = arguments?.getString(USER_ID)
        if (userId != null) {
            viewModel.notifyDifferentUserIdSent(userId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.edit_profile) {
            findNavController().navigate(R.id.action_userDetailFragment_to_userDetailEditFragment)
            false
        } else super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        lifecycle.addObserver(viewModel)
    }

    private fun setupViewModelObservers() {
        viewModel.apply {
            nameData.observe(viewLifecycleOwner) {
                tv_name.text = it
            }
            ageData.observe(viewLifecycleOwner) {
                tv_age.text = it.toString()
            }
            genderData.observe(viewLifecycleOwner) {
                tv_gender.text = it
            }
            alcoholData.observe(viewLifecycleOwner) {
                tv_alcohol.text = it
            }
            connectionError.observe(viewLifecycleOwner) { isError ->
                if (isError)
                    Toast.makeText(
                        context,
                        R.string.error_unable_receive_profile,
                        Toast.LENGTH_LONG
                    ).show()
            }
            isEditDisabled.observe(viewLifecycleOwner) { isDifferent ->
                if (isDifferent) {
                    setHasOptionsMenu(false)
                } else {
                    setHasOptionsMenu(true)
                }
            }
        }
    }
}
