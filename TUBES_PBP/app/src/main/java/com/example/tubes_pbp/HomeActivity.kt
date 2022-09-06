package com.example.tubes_pbp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tubes_pbp.fragments.AkunFragment
import com.example.tubes_pbp.fragments.BerandaFragment
import com.example.tubes_pbp.fragments.BookmarkFragment
import com.example.tubes_pbp.fragments.PesananFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    val berandaFragment: Fragment = BerandaFragment()
    val pesananFragment: Fragment = PesananFragment()
    val bookmarkFragment: Fragment = BookmarkFragment()
    val akunFragment: Fragment = AkunFragment()

    setCurrentFragment(bookmarkFragment)

    val bottom_navigation : BottomNavigationView = findViewById(R.id.bottom_navigation)

    bottom_navigation.setOnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.ic_beranda->setCurrentFragment(berandaFragment)
            R.id.ic_pesanan->setCurrentFragment(pesananFragment)
            R.id.ic_bookmark->setCurrentFragment(bookmarkFragment)
            R.id.ic_akun->setCurrentFragment(akunFragment)
        }
        true
    }
}

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}