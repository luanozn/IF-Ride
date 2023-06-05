package com.example.cesar.ifride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cesar.ifride.adapters.AdapterRideAsPassenger
import com.example.cesar.ifride.databinding.ActivityMineRidesBinding
import com.example.cesar.ifride.models.RideModel
import com.example.cesar.ifride.utils.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MineRidesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMineRidesBinding
    private lateinit var resultsRC: RecyclerView

    companion object {
        private var instance: MineRidesActivity?  = null

        fun getInstance(): MineRidesActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMineRidesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        resultsRC = binding.rcResults2
        instance = this

        MainActivity.getInstance()!!.verifyAuthetication()

        seeRidesAsPassenger()

        binding.txtPassengerOption.setOnClickListener {
            seeRidesAsPassenger()
        }

        binding.txtDriverOption.setOnClickListener {
            Util.verifyIsDriver { isDriver ->
                if (isDriver) {
                    seeRidesAsDriver()
                } else {
                    val intent = Intent(this, RegisterDriverActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        configureBottomNavigation()
    }


    fun seeRidesAsPassenger() {
        resultsRC.removeAllViews()

        val currentUserEmail = auth.currentUser!!.email
        val ridesList: MutableMap<String, RideModel> = mutableMapOf()

        val query = db.collection("Users").whereEqualTo("email", currentUserEmail)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {

                val document = querySnapshot.documents[0]
                val userRegistration = document.get("registration").toString()

                val queryRide = db.collection("Rides")
                queryRide.get().addOnSuccessListener { querySnapshot2 ->
                    for (document in querySnapshot2) {
                        val passengers = document.get("passengers") as? List<String>
                        if (passengers != null) {
                            for (passenger in passengers) {
                                if (passenger == userRegistration) {
                                    val ride = document.toObject(RideModel::class.java)

                                    ridesList[document.id] = ride
                                }
                            }
                        }
                    }
                    initRecyclerView(ridesList)
                }
                .addOnFailureListener { e ->
                    Log.d("TAG", "Erro ao buscar corridas com a matricula do passageiro", e)
                }
            }
        }
        .addOnFailureListener { e ->
            Log.d("TAG", "Erro ao buscar usuario com o email $currentUserEmail", e)
        }
        personalizeBtn(binding.txtPassengerOption)
    }

    fun initRecyclerView(ridesList: Map<String, RideModel>) {
        resultsRC.layoutManager = LinearLayoutManager(this)
        resultsRC.setHasFixedSize(true)
        resultsRC.adapter = AdapterRideAsPassenger(this, ridesList)
    }

    private fun seeRidesAsDriver() {
        resultsRC.removeAllViews()

        val currentUserEmail = auth.currentUser!!.email
        val ridesList: MutableMap<String, RideModel> = mutableMapOf()

        val query = db.collection("Users").whereEqualTo("email", currentUserEmail)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {

                val document = querySnapshot.documents[0]
                val userRegistration = document.get("registration").toString()

                val query = db.collection("Rides").whereEqualTo("driverRegistration", userRegistration)
                query.get().addOnSuccessListener { querySnapshot2 ->
                    if (!querySnapshot2.isEmpty) {
                        for (document in querySnapshot2) {
                            val ride = document.toObject(RideModel::class.java)
                            ridesList[document.id] = ride
                        }
                    }
                }
            }
        }

        personalizeBtn(binding.txtDriverOption)
    }

    private fun personalizeBtn(txtView: TextView) {
        val linearLayout = binding.llOptionsRides
        val currentIndex = linearLayout.indexOfChild(txtView)
        val previousTextView: TextView? = linearLayout.getChildAt(currentIndex - 1) as? TextView
        val nextTextView: TextView? = linearLayout.getChildAt(currentIndex + 1) as? TextView

        txtView.setTextColor(resources.getColor(R.color.white_smoke))

        if (txtView.text == "Passageiro") {
            if (nextTextView != null) {
                txtView.setBackgroundResource(R.drawable.border_directions_left_selected)
                nextTextView.setBackgroundResource(R.drawable.border_directions_right)
                nextTextView.setTextColor(resources.getColor(R.color.solid_gray))
            }
        } else {
            if (previousTextView != null) {
                txtView.setBackgroundResource(R.drawable.border_directions_right_selected)
                previousTextView.setBackgroundResource(R.drawable.border_directions_left)
                previousTextView.setTextColor(resources.getColor(R.color.solid_gray))
            }
        }
    }


    private fun configureBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.mine_rides

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cities -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                    finish()
                }
                R.id.rides -> {
                    Util.verifyIsDriver { isDriver ->
                        if (isDriver) {
                            val intent = Intent(this, RegisterRideActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            true
                            finish()
                        } else {
                            val intent = Intent(this, RegisterDriverActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
                R.id.mine_rides -> {
                    true
                }
                R.id.account -> {
                    val intent = Intent(this, AccountActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                    finish()
                }
                else -> Unit
            }
            false
        }
    }
}