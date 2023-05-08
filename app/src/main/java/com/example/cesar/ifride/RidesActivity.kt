package com.example.cesar.ifride

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cesar.ifride.databinding.ActivityRidesBinding
import com.example.cesar.ifride.models.RideModel
import com.example.cesar.ifride.utils.CustomLayouts
import com.example.cesar.ifride.utils.CustomLayouts.Companion.dpToPx
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRidesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var chosenCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRidesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        chosenCity = intent.getStringExtra("city").toString()

        putAvailableRides()
    }

    private fun putAvailableRides() {
        val queryOneWay = db.collection("Rides")
            .whereEqualTo("city", chosenCity)
            .whereEqualTo("direction", "Ida")

        val queryReturn = db.collection("Rides")
            .whereEqualTo("city", chosenCity)
            .whereEqualTo("direction", "Volta")


        putOneWayRides(queryOneWay)
        putReturnRides(queryReturn)
    }


    private fun putOneWayRides(queryOneWay: Query) {
        binding.txtOneWayRide.text = "$chosenCity - Caronas Campus Urutaí"
        val linearLayout = binding.llOneWayRide

        queryOneWay.get()
            .addOnSuccessListener { documents ->

                for(document in documents) {

                    val ride  = document.toObject(RideModel::class.java)
                    linearLayout.addView(putRide(ride))
                    Log.d(TAG, "deu bão")
                }

            }
            .addOnFailureListener {exception ->
                Log.d(TAG, "deu ruim", exception)
            }
    }

    private fun putReturnRides(queryReturn: Query) {
        binding.txtOneWayRide.text = "Caronas Campus Urutaí - $chosenCity"
        val linearLayout = binding.llOneWayRide

        queryReturn.get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "deu bão também")
                for(document in documents) {
                    val ride  = document.toObject(RideModel::class.java)
                    linearLayout.addView(putRide(ride))
                }
            }.addOnFailureListener {exception ->
            Log.d(TAG, "deu ruim também", exception)
        }

    }

    private fun putRide(ride: RideModel): LinearLayout {
        val hourRide = TextView(this)
        val driverRide = TextView(this)
        val newLinearLayout = LinearLayout(this)

        hourRide.apply {
            text = ride.dateHour.toString()
            textSize = 20f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        driverRide.apply {
            text = ride.driverName
            textSize = 25f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        newLinearLayout.apply {
            background = resources.getDrawable(R.drawable.border_rides)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(this@RidesActivity, 60f).toInt()
            ).apply {
                setMargins(dpToPx(this@RidesActivity, 15f).toInt(),
                        dpToPx(this@RidesActivity, 15f).toInt(),
                        dpToPx(this@RidesActivity, 15f).toInt(),
                        dpToPx(this@RidesActivity, 15f).toInt())
            }

            addView(hourRide)
            addView(driverRide)
        }

        return newLinearLayout
    }

}