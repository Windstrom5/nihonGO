package com.example.tugasbesar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tugasbesar.databinding.ActivityLibGlideBinding

class LibGlide : AppCompatActivity() {

    private lateinit var binding : ActivityLibGlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRandomImage.setOnClickListener{
            val url = "https://picsum.photos/300"

            Glide.with( this)
                .load(url)
                .fitCenter()
                .skipMemoryCache( true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo)
                .into(binding.imageView4)
        }
    }

}