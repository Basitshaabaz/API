package com.ili.digital.assessmentproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.ili.digital.assessmentproject.adapters.ViewPagerAdapter
import com.ili.digital.assessmentproject.databinding.ActivityMainBinding
import com.ili.digital.assessmentproject.ui.curiosity.CuriosityFragment
import com.ili.digital.assessmentproject.data.model.RoverType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        ViewPagerAdapter(supportFragmentManager, lifecycle)
    }

    // tab titles
    private val titles = arrayOf(
        RoverType.CURIOSITY,
        RoverType.OPPORTUNITY,
        RoverType.SPIRIT
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setUpViewPager()
    }

    /**
     * Sets up the view pager with the required fragments
     */
    private fun setUpViewPager() {
        if(adapter.itemCount == 0) {
            titles.forEach { roverType ->
                adapter.addFragment(CuriosityFragment.newInstance(roverType))
            }
        }

        binding.viewpager.adapter = adapter

        // Attaching TabLayoutMediator to synchronize TabLayout and ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = titles[position].typeName
        }.attach()
    }


}