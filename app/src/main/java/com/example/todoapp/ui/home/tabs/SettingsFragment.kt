package com.example.todoapp.ui.home.tabs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import com.example.todoapp.ui.Constants
import com.example.todoapp.ui.home.HomeActivity
import java.util.Locale

class SettingsFragment: Fragment() {

    lateinit var viewBinding: FragmentSettingsBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsBinding.inflate(
            inflater, container, false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        viewBinding.languageSpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language,
            android.R.layout.simple_dropdown_item_1line
        )
        viewBinding.languageSpinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> return
                    1 -> {
                        setLocale(requireContext().applicationContext, "en")
                        refreshApp()
                    }
                    2 -> {
                        setLocale(requireContext().applicationContext, "ar")
                        refreshApp()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBinding.modeSpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.mode,
            android.R.layout.simple_dropdown_item_1line
        )
        viewBinding.modeSpinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 1){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else if(position == 2){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun refreshApp() {
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(Constants.LANGUAGE_KEY, language)
        }
        return context.createConfigurationContext(configuration)
    }

}