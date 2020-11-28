package com.ags.annada.characters.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ags.annada.characters.main.CharactersAdapter
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import com.squareup.picasso.Picasso


@BindingAdapter("app:items")
fun setCharacters(listView: RecyclerView, items: List<CharacterItem>?) {
    items?.let {
        (listView.adapter as CharactersAdapter).submitList(items)
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .into(view);
}