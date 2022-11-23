package com.example.tugasbesar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tugasbesar.camera.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import kotlinx.android.synthetic.main.image_choose_bottom_sheet.*


class DialogImageChooser : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.image_choose_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvClickImageText : TextView = view.findViewById(R.id.tvClickImageText);
        val tvChooseGalleryText: TextView = view.findViewById(R.id.tvChooseGalleryText);

        tvClickImageText.setOnClickListener(){
            val intent = Intent(getActivity(), CameraActivity::class.java)
            startActivity(intent)
        }

        tvChooseGalleryText.setOnClickListener(){

        }

    }


    public interface bottomSheetListener {

    }

    companion object {

    }
}