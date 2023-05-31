package com.zell.submissionpemula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.zell.submissionpemula.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listItemFood = ArrayList<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFoods.setHasFixedSize(true)
        listItemFood.addAll(getListFoods())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_page -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListFoods(): ArrayList<Food> {
        val dataName = resources.getStringArray(R.array.data_food_name)
        val dataDescription = resources.getStringArray(R.array.data_foods_desc)
        val dataPhoto = resources.obtainTypedArray(R.array.data_foods_photo)
        val listFood = ArrayList<Food>()
        for (i in dataName.indices) {
            val food = Food(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listFood.add(food)
        }
        dataPhoto.recycle()
        return listFood
    }

    private fun showRecyclerList() {
        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        val listFoodAdapter = FoodAdapter(listItemFood)
        binding.rvFoods.adapter = listFoodAdapter

        listFoodAdapter.setOnItemClickCallback(object : FoodAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Food) {
                goDetailPage(data)
            }
        })
    }

    private fun goDetailPage(data: Food) {
        val food = Food(data.name, data.description, data.photo)
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_FOOD, food)
        startActivity(intent)
    }
}