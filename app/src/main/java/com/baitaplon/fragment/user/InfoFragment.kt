package com.baitaplon.fragment.user

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.baitaplon.R
import com.baitaplon.adapter.user.CategoryItemRecyclerViewAdapter
import com.baitaplon.model.Category


class InfoFragment : Fragment() {

    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter : CategoryItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview = view.findViewById(R.id.cateRecyclerView)
        adapter = CategoryItemRecyclerViewAdapter()
        var cateList = mutableListOf<Category>()
        val newCate1 = Category(1, "Viễn Tưởng")
        val newCate2 = Category(2, "Kỳ Ảo")
        val newCate3 = Category(3, "Grimm Dark")

        cateList += newCate1
        cateList += newCate2
        cateList += newCate3

        adapter.setCateList(cateList)
        var manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


        val itemDecoration = SpaceItemDecoration(25)
        recyclerview.addItemDecoration(itemDecoration)

        recyclerview.layoutManager = manager
        recyclerview.adapter = adapter
    }

    class SpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            // Đặt khoảng cách giữa các item bằng spaceHeight
            outRect.bottom = spaceHeight

            // Bỏ qua khoảng cách cho item cuối cùng
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = 0
            }
        }
    }
}