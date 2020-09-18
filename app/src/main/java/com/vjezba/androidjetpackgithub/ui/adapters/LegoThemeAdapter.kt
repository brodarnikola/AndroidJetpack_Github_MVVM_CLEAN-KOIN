

package com.vjezba.androidjetpackgithub.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.databinding.ListItemThemeBinding
import com.vjezba.androidjetpackgithub.ui.fragments.LegoThemeFragment
import com.vjezba.data.database.model.LegoTheme

/**
 * Adapter for the [RecyclerView] in [LegoThemeFragment].
 */
class LegoThemeAdapter : ListAdapter<LegoTheme, LegoThemeAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val legoTheme = getItem(position)
        holder.apply {
            bind(createOnClickListener(legoTheme.id, legoTheme.name), legoTheme)
            itemView.tag = legoTheme
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemThemeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(id: Int, name: String): View.OnClickListener {
        return View.OnClickListener {
            /*val direction = LegoThemeFragmentLegoThemeFragmentDirections.actionThemeFragmentToSetsFragment(id, name)
            it.findNavController().navigate(direction)*/
        }
    }

    class ViewHolder(
        private val binding: ListItemThemeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: LegoTheme) {
            binding.apply {
                clickListener = listener
                legoTheme = item
                executePendingBindings()
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<LegoTheme>() {

    override fun areItemsTheSame(oldItem: LegoTheme, newItem: LegoTheme): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LegoTheme, newItem: LegoTheme): Boolean {
        return oldItem == newItem
    }
}