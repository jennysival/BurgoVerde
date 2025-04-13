package com.jennysival.burgoverde.ui.plantRegister

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.databinding.PlantItemBinding

class PlantsAdapter(
    private val onPlantClick: (plant: PlantModel) -> Unit
) : RecyclerView.Adapter<PlantsAdapter.ViewHolder>() {

    private val plantsList = mutableListOf<PlantModel>()

    class ViewHolder(val binding: PlantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: PlantModel, onClick: (PlantModel) -> Unit) {
            with(binding) {
                plantNameTextView.text = plant.name

                Glide.with(root)
                    .load(plant.localUri.takeIf { it.isNotBlank() } ?: plant.firebaseUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).error(R.drawable.burgo_image)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(plantImageView)

                plantImageView.contentDescription = plant.name

                itemCardView.setOnClickListener {
                    onClick(plant)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PlantItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = plantsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plantsList[position], onPlantClick)
    }

    fun updatePlants(newList: List<PlantModel>) {
        val diffResult = DiffUtil.calculateDiff(PlantDiffCallback(plantsList, newList))
        plantsList.clear()
        plantsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class PlantDiffCallback(
        private val oldList: List<PlantModel>, private val newList: List<PlantModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
