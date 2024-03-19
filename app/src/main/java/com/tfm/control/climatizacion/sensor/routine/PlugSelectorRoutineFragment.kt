package com.tfm.control.climatizacion.sensor.routine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.indices
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Condition
import com.tfm.control.climatizacion.models.DatabaseHelper
import com.tfm.control.climatizacion.models.Routine
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import java.sql.DriverManager

class PlugSelectorRoutineFragment : Fragment() {
    val args: PlugSelectorRoutineFragmentArgs by navArgs()

    private var plugId: String? = null
    private lateinit var radioGroup: RadioGroup

    private lateinit var btnNext: Button

    private lateinit var sensorId: String
    private lateinit var routineName: String
    private var temperature: Double = 0.0
    private lateinit var condition: Condition
    private var action: Boolean = false
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_plug_selector_routine, container, false)
        initComponents(view)
        initList()
        initListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routineName = args.routineName
        condition = args.condition
        temperature = args.temperature.toDouble()
        sensorId = args.sensorId
        action = args.action
    }

    private fun initComponents(view: View) {
        radioGroup = view.findViewById(R.id.radioGroupPlugs)
        //Buttons
        btnNext = view.findViewById(R.id.btnNexCondition)

    }

    private fun initListeners() {
        //Boton next
        btnNext.setOnClickListener {
            if(plugId != null)
                saveRoutine()
        }
    }

    private fun initList() {
        ThingHomeSdk.newHomeInstance(172730637).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val deviceList: ArrayList<DeviceBean> = bean!!.deviceList as ArrayList<DeviceBean>
                deviceList.removeIf { d -> d.deviceCategory == "wsdcg" }
                createPlugList(deviceList)

            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                if (errorCode == "USER_SESSION_LOSS")
                    println(errorMsg)

                // do something
            }
        })
    }

    private fun createPlugList(plugs: ArrayList<DeviceBean>) {
        if (radioGroup != null) {
            for (i in plugs.indices) {
                val rdButton = RadioButton(context)
                rdButton.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                rdButton.text = plugs[i].devId
                rdButton.id = i
                radioGroup.addView(rdButton)
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId
                val radioBtn = requireView().findViewById(checkedRadioButtonId) as RadioButton
                plugId = radioBtn.text.toString()
            }
        }
    }

    private fun saveRoutine() {
        val routine = Routine(0,routineName,sensorId,plugId!!,true,temperature,condition,action)
        val result = db.insertRoutine(routine)
        if(result.compareTo(-1) == 0){
            Toast.makeText(
                requireActivity().applicationContext,
                "ERROR AL INSERTAR LA RUTINA",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            Toast.makeText(
                requireActivity().applicationContext,
                "RUTINA INSERTADA CON EXITO",
                Toast.LENGTH_SHORT
            ).show()
        }
        activity?.finish()


    }
}