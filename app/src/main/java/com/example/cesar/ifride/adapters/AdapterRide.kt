package com.example.cesar.ifride.adapters

import android.animation.ValueAnimator
import android.content.Context
import android.view.ContextThemeWrapper

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat.setTextAppearance
import androidx.recyclerview.widget.RecyclerView
import com.example.cesar.ifride.R
import com.example.cesar.ifride.models.RideModel
import com.example.cesar.ifride.utils.Util
import org.w3c.dom.Text


class AdapterRide(

    private val context: Context,
    private val ridesList: List<RideModel>

) : RecyclerView.Adapter<AdapterRide.MyViewHolder>() {

    var isOpened = false

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rideLayout: LinearLayout = itemView.findViewById(R.id.ll_ride)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rideLayout = LayoutInflater.from(parent.context).inflate(R.layout.ride_adapter, parent, false)
        return  MyViewHolder(rideLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ride = ridesList[position]

        putRide(holder.rideLayout, ride)
    }

    override fun getItemCount() = ridesList.size

    private fun putRide(linearLayout: LinearLayout,ride: RideModel) {
        Util.removeLinearLayoutChildren(linearLayout)

        val dateHourLayout = Util.standardLinearLayout(context)
        val priceLayout = Util.standardLinearLayout(context)

        val dateHourRide = TextView(context)
        val dateHourRideLabel = TextView(context)
        val priceRide = TextView(context)
        val priceRideLabel = TextView(context)


        dateHourRideLabel.apply {
            text = context.getString(R.string.date_hour_label)
            setTextAppearance(R.style.ride_label_text)
        }
        dateHourRide.apply {
            text = Util.formatDate(ride.dateHour)
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }
        dateHourLayout.addView(dateHourRideLabel)
        dateHourLayout.addView(dateHourRide)

        priceRideLabel.apply {
            text = context.getString(R.string.price_label)
            setTextAppearance(R.style.ride_label_text)
        }
        priceRide.apply {
            text = ride.price
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }
        priceLayout.addView(priceRideLabel)
        priceLayout.addView(priceRide)

        dateHourLayout.orientation = LinearLayout.VERTICAL
        priceLayout.orientation = LinearLayout.VERTICAL

        linearLayout.addView(dateHourLayout)
        linearLayout.addView(priceLayout)

        onOpenRideInformatins(linearLayout, ride)
    }

    private fun onOpenRideInformatins(rideLayout: LinearLayout, ride: RideModel) {
        rideLayout.setOnClickListener {
            if (!isOpened){
                val animator = ValueAnimator.ofInt(
                    Util.dpToPx(context, 80f).toInt(),
                    Util.dpToPx(context, 450f).toInt()
                )
                animator.duration = 1000

                animator.addUpdateListener { animation ->
                    val height = animation.animatedValue as Int
                    rideLayout.layoutParams.height = height
                    rideLayout.requestLayout()
                }
                animator.start()
                Util.removeLinearLayoutChildren(rideLayout)
                putRideInformations(rideLayout, ride)
                isOpened = true
            }
        }
    }

    private fun putRideInformations(rideLayout: LinearLayout, ride: RideModel) {
        rideLayout.orientation = LinearLayout.VERTICAL
        var closeBtn = setCloseBtn(rideLayout,ride)
        var dateAndPrice =  setDateAndPrice(ride)
        var driverAndSeats = setDriverAndSeats(ride)
        var confirmBtn = setConfirmBtn()

        rideLayout.addView(closeBtn)
        rideLayout.addView(viewTraceHorizontal())
        rideLayout.addView(dateAndPrice)
        rideLayout.addView(viewTraceHorizontal())
        rideLayout.addView(driverAndSeats)
        rideLayout.addView(viewTraceHorizontal())
        rideLayout.addView(confirmBtn)
    }

    private fun setCloseBtn(rideLayout: LinearLayout, ride: RideModel): LinearLayout {
        var linearLayout = Util.standardLinearLayout(context)
        linearLayout.apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                Util.dpToPx(context, 40f).toInt()
            )
        }

        var imageView = ImageView(context)
        imageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.close_x))
            background = resources.getDrawable(R.drawable.circle_border)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            foregroundGravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                Util.dpToPx(context, 25f).toInt(),
                Util.dpToPx(context, 25f).toInt()
            ).apply {
                setMargins(
                    Util.dpToPx(context, 2f).toInt(),
                    Util.dpToPx(context, 2f).toInt(),
                    Util.dpToPx(context, 2f).toInt(),
                    Util.dpToPx(context, 2f).toInt()
                )
            }
        }
        onCloseRideInformations(imageView, rideLayout, ride)
        linearLayout.addView(imageView)

        return linearLayout
    }

    private fun setDateAndPrice(ride: RideModel): LinearLayout {
        var linearLayout = Util.standardLinearLayout(context)
        var llDateHourLayout = Util.standardLinearLayout(context)
        var llPriceLayout = Util.standardLinearLayout(context)

        var dateHour = TextView(context)
        var dateHourLabel = TextView(context)
        var price = TextView(context)
        var priceLabel = TextView(context)

        dateHourLabel.apply {
            text = context.getString(R.string.date_hour_label)
            setTextAppearance(R.style.ride_label_text)
        }
        dateHour.apply {
            text = Util.formatDate(ride.dateHour)
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }

        priceLabel.apply {
            text = context.getString(R.string.price_label)
            setTextAppearance(R.style.ride_label_text)
        }
        price.apply {
            text = ride.price.toString()
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }

        llDateHourLayout.orientation = LinearLayout.VERTICAL
        llDateHourLayout.addView(dateHourLabel)
        llDateHourLayout.addView(dateHour)

        llPriceLayout.orientation = LinearLayout.VERTICAL
        llPriceLayout.addView(priceLabel)
        llPriceLayout.addView(price)

        linearLayout.addView(llDateHourLayout)
        linearLayout.addView(viewTraceVertical())
        linearLayout.addView(llPriceLayout)

        return linearLayout
    }

    private fun setDriverAndSeats(ride: RideModel): LinearLayout {
        var linearLayout = Util.standardLinearLayout(context)
        var llDriverLayout = Util.standardLinearLayout(context)
        var llCarSeatsLayout = Util.standardLinearLayout(context)

        var driver = TextView(context)
        var driverLabel = TextView(context)
        var carSeats = TextView(context)
        var carSeatsLabel = TextView(context)

        driverLabel.apply {
            text = context.getString(R.string.driver_label)
            setTextAppearance(R.style.ride_label_text)
        }
        driver.apply {
            text = ride.driverName
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }

        carSeatsLabel.apply {
            text = context.getString(R.string.car_seats_label)
            setTextAppearance(R.style.ride_label_text)
        }
        carSeats.apply {
            text = ride.availableCarSeats.toString()
            setTextAppearance(R.style.ride_value_text)
            gravity = Gravity.CENTER
        }

        llDriverLayout.orientation = LinearLayout.VERTICAL
        llDriverLayout.addView(driverLabel)
        llDriverLayout.addView(driver)

        llCarSeatsLayout.orientation = LinearLayout.VERTICAL
        llCarSeatsLayout.addView(carSeatsLabel)
        llCarSeatsLayout.addView(carSeats)

        linearLayout.addView(llDriverLayout)
        linearLayout.addView(viewTraceVertical())
        linearLayout.addView(llCarSeatsLayout)

        return linearLayout
    }

    private fun setConfirmBtn(): LinearLayout {
        var linearLayout = Util.standardLinearLayout(context)
        var confirmBtn = Button(ContextThemeWrapper(context, R.style.ride_confirm_btn))

        confirmBtn.apply {
            text = resources.getText(R.string.confirm_ride_btn)
            //setTextAppearance(R.style.ride_confirm_btn)
            setBackgroundResource(R.drawable.border_confirm_ride)
        }

        linearLayout.gravity = Gravity.CENTER
        linearLayout.addView(confirmBtn)

        return linearLayout
    }

    private fun viewTraceHorizontal(): View? {
        var view = View(context)
        view.apply {
            setBackgroundColor(resources.getColor(R.color.ciano))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Util.dpToPx(context,1f).toInt()
            )
        }
        return view
    }

    private fun viewTraceVertical(): View? {
        var view = View(context)
        view.apply {
            setBackgroundColor(resources.getColor(R.color.ciano))
            layoutParams = ViewGroup.LayoutParams(
                Util.dpToPx(context,1f).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return view
    }

    private fun onCloseRideInformations(imageView: ImageView,rideLayout: LinearLayout, ride: RideModel)     {
        imageView.setOnClickListener {
            if (isOpened) {
                val animator = ValueAnimator.ofInt(
                    Util.dpToPx(context, 450f).toInt(),
                    Util.dpToPx(context, 80f).toInt()
                )
                animator.duration = 1000

                animator.addUpdateListener { animation ->
                    val height = animation.animatedValue as Int
                    rideLayout.layoutParams.height = height
                    rideLayout.requestLayout()
                }
                animator.start()
                Util.removeLinearLayoutChildren(rideLayout)
                putRide(rideLayout, ride)
                rideLayout.orientation = LinearLayout.HORIZONTAL
                isOpened = false
            }
        }
    }
}