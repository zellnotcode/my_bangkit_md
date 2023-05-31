package com.zell.submissionpemula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.zell.submissionpemula.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val food = intent.getParcelableExtra<Food>(EXTRA_FOOD)

        if(food != null) {
            binding.tvDetailName.text = food.name
            binding.tvDetailDescription.text = food.description
            binding.ivDetailImage.setImageResource(food.photo!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean = when (item.itemId) {

        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_share -> {
            val food = intent.getParcelableExtra<Food>(EXTRA_FOOD)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, food?.description)
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, "Bagikan dengan :"))

            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}