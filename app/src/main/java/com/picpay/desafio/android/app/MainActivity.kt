package com.picpay.desafio.android.app

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.util.CheckConnectivity
import com.picpay.desafio.android.util.Constants
import com.picpay.desafio.android.util.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var checkConnectivity: CheckConnectivity

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPrefs.init(baseContext)
        checkConnectivity = CheckConnectivity(application)

        setUpAdapter()

        binding.errorInfo.btnReload.setOnClickListener {
            binding.itemsShimmer.visibility = View.VISIBLE
            binding.errorInfo.root.visibility = View.GONE
            viewModel.loadUsers(
                SharedPrefs.load(Constants.SharedPrefs.LAST_REQUEST, 0),
                checkConnectivity.isInternetWorking(baseContext)
            )
        }

        viewModel.loadUsers(
            SharedPrefs.load(Constants.SharedPrefs.LAST_REQUEST, 0),
            checkConnectivity.isInternetWorking(baseContext)
        )

        prepareSearchView()
    }

   override fun onStart() {
      super.onStart()
       handleObservables()
   }

    private fun setUpAdapter() {
        adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun handleObservables() {
        viewModel.showedUsersList.observe(this, Observer { response ->
            binding.itemsShimmer.visibility = View.GONE
            binding.errorInfo.root.visibility = View.GONE
            SharedPrefs.save(Constants.SharedPrefs.LAST_REQUEST, System.currentTimeMillis().toInt())
            adapter.users = response
        })

        viewModel.errorMessage.observe(this, Observer { response ->
            if (!response.equals("")) {
                binding.itemsShimmer.visibility = View.GONE
                binding.errorInfo.root.visibility = View.VISIBLE
                binding.errorInfo.tvLoadFailed.text = response
            }
        })

        viewModel.isLoading.observe(this, Observer { response ->
            if (response) {
                binding.itemsShimmer.visibility = View.VISIBLE
                binding.itemsLayout.visibility = View.GONE
            } else {
                binding.itemsShimmer.visibility = View.GONE
                binding.itemsLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun prepareSearchView() {
        val txtSearch = binding.searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        txtSearch.setHintTextColor(Color.WHITE)
        txtSearch.setTextColor(Color.WHITE)
        val closeIcon: ImageView = binding.searchView.findViewById(
            androidx.appcompat.R.id.search_close_btn
        )
        closeIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val searchIcon: ImageView = binding.searchView.findViewById(
            androidx.appcompat.R.id.search_button
        )
        searchIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.isSearching = true
                viewModel.searchUsers(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.isSearching = true
                viewModel.searchUsers(query)
                return false
            }
        })

        binding.searchView.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            viewModel.isSearching = hasFocus
            binding.title.visibility = View.INVISIBLE
            if (!viewModel.isSearching) {
                viewModel.searchUsers("")
            }
        }

        binding.searchView.setOnCloseListener {
            binding.searchView.visibility = View.GONE
            binding.title.visibility = View.VISIBLE
            binding.searchView.visibility = View.VISIBLE
            false
        }
    }
}
