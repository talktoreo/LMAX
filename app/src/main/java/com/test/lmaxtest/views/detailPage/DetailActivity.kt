package com.test.lmaxtest.views.detailPage

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.lmaxtest.data.Instrument
import com.test.lmaxtest.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        bundle?.let {
            val myData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable("itemData", Instrument::class.java)
            } else {
                it.getSerializable("itemData") as Instrument
            }

            myData?.let { data ->
                binding.tvInName.text = data.name
                binding.tvInSymbol.text = data.symbol
                binding.tvInId.text = data.id
                binding.tvAssClass.text = data.assetClass
                binding.tvQtyIncre.text = data.quantityIncrement
                binding.tvprcIncre.text = data.priceIncrement
            }
        }

        binding.imBack.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
}