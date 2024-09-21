package com.example.recuperacion_android_kendrick

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_abogado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_abogado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar los datos del Intent
        val uuidAbogado = intent.getStringExtra("UUID_Abogado")
        val nombreAbogado = intent.getStringExtra("Nombre_Abogado")
        val edadAbogado = intent.getIntExtra("Edad_Abogado", 0)
        val pesoAbogado = intent.getDoubleExtra("Peso_Abogado", 0.0)
        val correoAbogado = intent.getStringExtra("Correo_Abogado")

        val txtuuidAbogadoDetalle = findViewById<TextView>(R.id.txtUUIDabogadoDetalle)
        val txtNombreAbogadoDetalle = findViewById<TextView>(R.id.txtNombreAbogadoDetalle)
        val txtEdadAbogadoDetalle = findViewById<TextView>(R.id.txtEdadAbogadoDetalle)
        val txtPesoAbogadoDetalle = findViewById<TextView>(R.id.txtPesoAbogadoDetalle)
        val txtCorreoAbogadoDetalle = findViewById<TextView>(R.id.txtCorreoAbogadoDetalle)

        txtuuidAbogadoDetalle.setText(uuidAbogado)
        txtNombreAbogadoDetalle.setText(nombreAbogado)
        txtEdadAbogadoDetalle.setText(edadAbogado.toString())
        txtPesoAbogadoDetalle.setText(pesoAbogado.toString())
        txtCorreoAbogadoDetalle.setText(correoAbogado)

    }
}