package com.example.befit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.befit.MainActivity
import com.example.befit.database.DatabaseManager
import com.example.befit.databinding.ProductItemBinding
import com.example.befit.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductAdapter(private val products: MutableList<Product>, private val mainActivity: MainActivity) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductItemBinding, private val products: MutableList<Product>, private val adapter: ProductAdapter, private val mainActivity: MainActivity) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productNameTextView.text = product.name
            binding.macros.text = "${product.kcal.toInt()}kcal      ${product.protein.toInt()} / ${product.fat.toInt()} / ${product.carb.toInt()}"
            binding.weight.text = "${product.weight}g"
            binding.removeButton.setOnClickListener{
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    products.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    val productDao = DatabaseManager.getInstance(binding.removeButton.context).productDao()
                    mainActivity.calculateTotalNutrition(products)
                    mainActivity.updateNutritionBars()
                    CoroutineScope(Dispatchers.IO).launch {
                        productDao.delete(product)
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, products, this, mainActivity)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    fun addProduct(product: Product) {
        products.add(product)
        notifyItemInserted(products.size - 1)
    }

    fun deleteProduct(position: Int) {
        products.removeAt(position)
        notifyItemRemoved(position)

    }

    override fun getItemCount() = products.size
}
