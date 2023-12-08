package com.mauroalexandro.anappstore.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mauroalexandro.anappstore.databinding.FragmentHomeBinding
import com.mauroalexandro.anappstore.models.App
import com.mauroalexandro.anappstore.network.Status
import com.mauroalexandro.anappstore.utils.Utils
import com.mauroalexandro.anappstore.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private var appList: List<App> = ArrayList()
    private var utils: Utils = Utils.getInstance()

    //List Control Element Positions
    private var firstElement = 0
    private var lastElement = 6
    private var listSize = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeAdapter = HomeAdapter(requireContext())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setAppList()

        binding.appsRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    //Reached the BOTTOM of the List
                    firstElement += 6
                    lastElement += 6
                    updateAdapter()
                }
            }
        })

        binding.appsRecyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            var downTouch = false
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> downTouch = true
                    MotionEvent.ACTION_UP -> if (downTouch) {
                        downTouch = false
                        binding.appsRecyclerview.findChildViewUnder(e.x, e.y)?.let {
                            val position = recyclerView.getChildAdapterPosition(it)
                            val appClicked = (recyclerView.adapter as HomeAdapter).getItemAtPosition(position)
                            activity?.let { fragmentActivity -> utils.openDetailsFragment(fragmentActivity, appClicked, utils) }
                        }
                    }
                    else -> downTouch = false
                }
                return super.onInterceptTouchEvent(recyclerView, e)
            }
        })

        //Call WorkManager to show a Notification every 30 Minutes
        utils.createWorkManager(requireContext())

        return root
    }

    private fun setAppList() {
        //Get Apps
        activity?.let { it ->
            homeViewModel.getAppsFromService().observe(it) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.appsProgressBar.visibility = View.GONE
                        binding.applistNoData.visibility = View.GONE

                        it.data?.let { response ->
                            this.appList = response.responses.listApps.datasets.all.data.list
                            listSize = this.appList.size

                            updateAdapter()

                            binding.appsRecyclerview.visibility = View.VISIBLE
                            binding.appsRecyclerview.adapter = homeAdapter
                            binding.appsRecyclerview.layoutManager = GridLayoutManager(context, 2)
                        }
                    }

                    Status.LOADING -> {
                        binding.appsProgressBar.visibility = View.VISIBLE
                        binding.appsRecyclerview.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        binding.appsProgressBar.visibility = View.GONE
                        binding.applistNoData.visibility = View.VISIBLE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateAdapter() {
        if(lastElement <= listSize-1 && homeAdapter.itemCount < listSize-1) {
            if(firstElement >= lastElement) {
                firstElement = 0
                lastElement = listSize - 1
            }

            if(listSize <= homeAdapter.itemCount + 6)
                lastElement = listSize-1

            val sublist = ArrayList(appList.subList(firstElement, lastElement))
            homeAdapter.setAppList(sublist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}