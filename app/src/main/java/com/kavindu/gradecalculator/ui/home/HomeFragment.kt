package com.kavindu.gradecalculator.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.kavindu.gradecalculator.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subject: TextInputLayout = binding.subject
        val marks: TextInputLayout = binding.marks
        val calculate: Button = binding.calculate
        val save: Button = binding.save
        val retake: Button = binding.retake
        val result: TextView = binding.grade
        val buttonLayout: LinearLayout = binding.buttonLayout

        // calculate button click event
        calculate.setOnClickListener {
            //  error for subject
            if (subject.editText?.text.toString().isEmpty()) {
                subject.error = "Subject is required"
                return@setOnClickListener
            }

            //  error for marks : required  field
            if (marks.editText?.text.toString().isEmpty()) {
                marks.error = "Marks is required"
                return@setOnClickListener
            }

            //  error for marks : marks should be between 0 and 100
            if (marks.editText?.text.toString().toInt() > 100 || marks.editText?.text.toString()
                    .toInt() < 0
            ) {
                marks.error = "Marks should be between 0 and 100"
                return@setOnClickListener
            }

            // calculate grade
            val marksValue = marks.editText?.text.toString().toInt()
            val grade = when {
                marksValue >= 85 -> "A+"
                marksValue >= 75 -> "A"
                marksValue >= 70 -> "A-"
                marksValue >= 65 -> "B+"
                marksValue >= 60 -> "B"
                marksValue >= 55 -> "B-"
                marksValue >= 50 -> "C+"
                marksValue >= 45 -> "C"
                marksValue >= 40 -> "C-"
                marksValue >= 35 -> "D+"
                marksValue >= 30 -> "D"
                else -> "E"
            }

            // color for grade
            val color = when {
                marksValue >= 85 -> "#4CAF50"
                marksValue >= 75 -> "#4CAF50"
                marksValue >= 70 -> "#4CAF50"
                marksValue >= 65 -> "#4CAF50"
                marksValue >= 60 -> "#4CAF50"
                marksValue >= 55 -> "#4CAF50"
                marksValue >= 50 -> "#FFC107"
                marksValue >= 45 -> "#FFC107"
                marksValue >= 40 -> "#FFC107"
                marksValue >= 35 -> "#FF5722"
                marksValue >= 30 -> "#FF5722"
                else -> "#FF5722"
            }

            // set grade and color
            binding.grade.text = grade
            binding.grade.setTextColor(Color.parseColor(color))

            // hide input fields and calculate button
            subject.visibility = View.GONE
            marks.visibility = View.GONE
            calculate.visibility = View.GONE

            // show result
            result.visibility = View.VISIBLE
            buttonLayout.visibility = View.VISIBLE
        }

        // save button click event
        save.setOnClickListener {
            // save subject , grade and marks in preferences
            val subjectValue = subject.editText?.text.toString()
            val marksValue = marks.editText?.text.toString()
            val gradeValue = result.text.toString()
            val preferences = requireActivity().getSharedPreferences("grades", 0)
            val editor = preferences.edit()
            editor.putString(subjectValue, "$marksValue - $gradeValue")
            editor.apply()

            // retake button click
            retake.performClick()

            // show success toast
            val toast = android.widget.Toast.makeText(
                requireContext(),
                "Saved successfully",
                android.widget.Toast.LENGTH_SHORT
            )
            toast.show()
        }

        // retake button click event
        retake.setOnClickListener {
            // show input fields and calculate button
            subject.visibility = View.VISIBLE
            marks.visibility = View.VISIBLE
            calculate.visibility = View.VISIBLE

            // hide result
            result.visibility = View.GONE
            buttonLayout.visibility = View.GONE

            // clear input fields
            subject.editText?.setText("")
            marks.editText?.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}