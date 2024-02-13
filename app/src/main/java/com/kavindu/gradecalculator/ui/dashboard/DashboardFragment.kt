package com.kavindu.gradecalculator.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.kavindu.gradecalculator.R
import com.kavindu.gradecalculator.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get values from shared preferences
        val sharedPreferences = requireActivity().getSharedPreferences("grades", 0)
        // get all keys from shared preferences
        val keys = sharedPreferences.all
        //if there are no keys, display a message
        if (keys.isEmpty()) {
            val textView = TextView(requireContext())
            textView.text = buildString {
                append("No data available")
            }
            // change the text gravity to center
            textView.gravity = 1
            textView.textSize = 20f
            binding.dashboardFragment.addView(textView)
        }

        // iterate through all keys
        for (entry in keys.entries) {
            val key = entry.key
            val value = entry.value
            // get custom card from layout folder
            val cardView = layoutInflater.inflate(R.layout.card, null)
            val subjectText = cardView.findViewById<TextView>(R.id.subjectText)
            val marksText = cardView.findViewById<TextView>(R.id.marksText)
            val deleteButton = cardView.findViewById<Button>(R.id.deleteButton)

            // set the text of the card
            subjectText.text = key
            marksText.text = value.toString()
            // set the margins of the card
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 15)
            cardView.layoutParams = params

            // add the card to the layout
            binding.dashboardFragment.addView(cardView)

            // delete button click event
            deleteButton.setOnClickListener {
                // show a confirmation dialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete")
                builder.setMessage("Are you sure you want to delete this record?")
                // if the user clicks yes, remove the key from shared preferences and remove the card from the layout
                builder.setPositiveButton("Yes") { _, _ ->
                    // remove the key from shared preferences
                    sharedPreferences.edit().remove(key).apply()
                    // remove the card from the layout
                    binding.dashboardFragment.removeView(cardView)
                    onViewCreated(view, savedInstanceState)
                }
                // if the user clicks no, close the dialog
                builder.setNegativeButton("No") { _, _ ->
                    // close the dialog
                    builder.setCancelable(true)
                }
                // show the dialog
                builder.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}