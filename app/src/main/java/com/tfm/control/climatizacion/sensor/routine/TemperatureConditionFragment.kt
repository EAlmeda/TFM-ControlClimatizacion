package com.tfm.control.climatizacion.sensor.routine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Condition

class TemperatureConditionFragment : Fragment() {
    private var sensorId: String? = null
    private var temperature: Double = 15.0
    private var condition: Condition = Condition.EQUAL
    private var action: Boolean = false

    private lateinit var rngTemperature: Slider
    private lateinit var txtTemperature: TextView

    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    private lateinit var txtSetCondition: TextInputLayout
    private lateinit var txtSetAction: TextInputLayout

    private lateinit var txtRoutineName: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sensorId = it.getString("sensorId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_temperature_condition, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    override fun onResume() {
        super.onResume()
    }
    private fun initComponents(view: View) {
        //Condition name
        txtRoutineName = view.findViewById(R.id.txtNameRoutine)

        //Buttons
        btnBack = view.findViewById(R.id.btnBackCondition)
        btnNext = view.findViewById(R.id.btnNexCondition)

        //Temperature
        txtTemperature = view.findViewById(R.id.txtTemperature)
        txtTemperature.text = "%.1f".format(temperature) + " ºC"
        rngTemperature = view.findViewById(R.id.rngTemperature)
        rngTemperature.value = temperature.toFloat()


        //ComboBox condition
        txtSetCondition = view.findViewById(R.id.menuCondition)
        val options = listOf("Menor que", "Igual a", "Mayor que")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_condition, options)
        (txtSetCondition.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        //ComboBox action
        txtSetAction = view.findViewById(R.id.menuAction)
        val actionOptions = listOf("Apagar","Encender")
        val actionAdapter = ArrayAdapter(requireContext(), R.layout.list_action, actionOptions)
        (txtSetAction.editText as? AutoCompleteTextView)?.setAdapter(actionAdapter)
    }

    private fun initListeners() {
        //Listener de temperatura
        rngTemperature.addOnChangeListener { _, value, _ ->
            temperature = value.toDouble()
            txtTemperature.text = "%.1f".format(temperature) + " ºC"
        }

        //Boton atras
        btnBack.setOnClickListener {
            activity?.finish()
        }

        //Boton next
        btnNext.setOnClickListener {
            nextStep()
        }

    }

    private fun nextStep() {
        val routineName = txtRoutineName.editText?.text.toString()
        condition = when (txtSetCondition.editText?.text.toString()) {
            "Menor que" -> Condition.LESS
            "Mayor que" -> Condition.GREATER
            "Igual a" -> Condition.EQUAL
            else -> Condition.LESS
        }

        action = when (txtSetCondition.editText?.text.toString()) {
            "Encender" -> true
            "Apagar" -> false
            else -> true
        }
        findNavController().navigate(
            TemperatureConditionFragmentDirections.actionTemperatureConditionFragmentToPlugSelectorRoutineFragment(
                routineName,
                temperature.toFloat(),
                sensorId!!,
                condition,
                action
            )
        )


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TemperatureConditionFragment().apply {
                arguments = Bundle().apply {
                    putString("sensorId", param1)
                }
            }
    }
}