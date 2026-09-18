package com.tharunbirla.librecuts.customviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tharunbirla.librecuts.R

class StickerPickerBottomSheet : BottomSheetDialogFragment() {

    var onStickerSelectedListener: ((String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (400 * resources.displayMetrics.density).toInt()
        )
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        val stickerNames = requireContext().assets.list("stickers")?.toList() ?: emptyList()
        recyclerView.adapter = StickerAdapter(stickerNames) { fileName ->
            onStickerSelectedListener?.invoke(fileName)
            dismiss()
        }
        recyclerView.setPadding(16, 16, 16, 16)
        recyclerView.clipToPadding = false
        return recyclerView
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = (dialog as? com.google.android.material.bottomsheet.BottomSheetDialog)
            ?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            com.google.android.material.bottomsheet.BottomSheetBehavior.from(it).state =
                com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private class StickerAdapter(
        private val items: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<StickerAdapter.VH>() {

        class VH(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
            val imageView = ImageView(parent.context)
            val size = (80 * parent.context.resources.displayMetrics.density).toInt()
            imageView.layoutParams = RecyclerView.LayoutParams(size, size).apply {
                setMargins(8, 8, 8, 8)
            }
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setPadding(12, 12, 12, 12)
            imageView.setBackgroundResource(com.tharunbirla.librecuts.R.drawable.bg_sticker_item)
            return VH(imageView)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val fileName = items[position]
            holder.imageView.setImageBitmap(
                android.graphics.BitmapFactory.decodeStream(
                    holder.imageView.context.assets.open("stickers/$fileName")
                )
            )
            holder.imageView.setOnClickListener { onClick(fileName) }
        }

        override fun getItemCount() = items.size

        private fun Int.dpToPx(context: android.content.Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }
    }
}