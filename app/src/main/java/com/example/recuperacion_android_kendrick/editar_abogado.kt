package com.example.recuperacion_android_kendrick

import Modelo.ClaseConexion
import RecyclerViewHelper.AdaptadorAbogados
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class editar_abogado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_abogado)
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

        val txtNombreAbogadoEditar = findViewById<EditText>(R.id.txtNombreAbogadoEditar)
        val txtEdadAbogadoEditar = findViewById<EditText>(R.id.txtEdadAbogadoEditar)
        val txtPesoAbogadoEditar = findViewById<EditText>(R.id.txtPesoAbogadoEditar)
        val txtCorreoAbogadoEditar = findViewById<EditText>(R.id.txtCorreoAbogadoEditar)

        txtNombreAbogadoEditar.setText(nombreAbogado)
        txtEdadAbogadoEditar.setText(edadAbogado.toString())
        txtPesoAbogadoEditar.setText(pesoAbogado.toString())
        txtCorreoAbogadoEditar.setText(correoAbogado)

        val btnActualizar = findViewById<Button>(R.id.btnIngresarAbogadoEditar)

        // Manejar la actualización
        btnActualizar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val nuevoNombre = txtNombreAbogadoEditar.text.toString().trim()
                    val nuevaEdad = txtEdadAbogadoEditar.text.toString().trim().toInt()
                    val nuevoPeso = txtPesoAbogadoEditar.text.toString().trim().toDouble()
                    val nuevoCorreo = txtCorreoAbogadoEditar.text.toString().trim()

                    // Crear un objeto de la clase conexión
                    val objConexion = ClaseConexion().cadenaConexion()

                    // Preparar la sentencia de actualización
                    val actualizarAbogado = objConexion?.prepareStatement(
                        "UPDATE tbAbogado SET Nombre_Abogado = ?, Edad_Abogado = ?, Peso_Abogado = ?, Correo_Abogado = ? WHERE UUID_Abogado = ?"
                    )
                    actualizarAbogado?.setString(1, nuevoNombre)
                    actualizarAbogado?.setInt(2, nuevaEdad)
                    actualizarAbogado?.setDouble(3, nuevoPeso)
                    actualizarAbogado?.setString(4, nuevoCorreo)
                    actualizarAbogado?.setString(5, uuidAbogado)

                    // Ejecutar la actualización
                    actualizarAbogado?.executeUpdate()
                    withContext(Dispatchers.Main) {
                        setResult(RESULT_OK) // Establecer resultado exitoso
                        Toast.makeText(this@editar_abogado, "Abogado actualizado correctamente", Toast.LENGTH_SHORT).show() // Mensaje de éxito
                        finish() // Cerrar la actividad después de actualizar
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@editar_abogado, "Error al actualizar abogado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}