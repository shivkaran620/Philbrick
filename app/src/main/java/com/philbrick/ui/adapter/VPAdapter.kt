package com.philbrick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import com.philbrick.R
import com.philbrick.data.entity.product.ProductBanner


class VPAdapter(private var imageList: ArrayList<ProductBanner> = ArrayList(),val context : Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == (obj as ConstraintLayout)
    }

    override fun getCount(): Int = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner_image, container, false)
        val bannerImage = view.findViewById<ImageView>(R.id.ivBannerImage)
        val pb = view.findViewById<ProgressBar>(R.id.pb)

        bannerImage.load(imageList[position].imgUrl) {
            scale(Scale.FILL)
            listener(
                onSuccess = { imageRequest: ImageRequest, result ->
                    pb.visibility = View.GONE
                    bannerImage.visibility = View.VISIBLE
                },
                onError = { imageRequest: ImageRequest, error ->
                    placeholder(R.drawable.img_banner)
                    pb.visibility = View.GONE
                    bannerImage.visibility = View.VISIBLE
                },
                onStart = {
                    pb.visibility = View.VISIBLE
                })
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ConstraintLayout)
    }

    fun update(imageList: ArrayList<ProductBanner>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }
}