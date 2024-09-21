package com.example.recuperacion_android_kendrick

import Modelo.ClaseConexion
import Modelo.ListaAbogados
import RecyclerViewHelper.AdaptadorAbogados
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.Calendar
import java.util.UUID

class MainActivity : AppCompatActivity() {
    companion object {
        const val EDITAR_ABOGADO_REQUEST = 1
    }

    private lateinit var rcvAbogados: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //TODO las cards tardan un poquito en cargar

        // Mandar a llamar todos los elementos de la vista
        val txtNombre = findViewById<EditText>(R.id.txtNombreAbogado)
        val txtEdad = findViewById<EditText>(R.id.txtEdadAbogado)
        val txtPeso = findViewById<EditText>(R.id.txtPesoAbogado)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoAbogado)

        val btnIngresarAbogado = findViewById<Button>(R.id.btnIngresarAbogado)
        rcvAbogados = findViewById(R.id.rcvAbogados) // Cambiado a propiedad de clase

        rcvAbogados.layoutManager = LinearLayoutManager(this)

        // Ejecutamos la función
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val abogadosDB = obtenerAbogados()
                withContext(Dispatchers.Main) {
                    val adapter = AdaptadorAbogados(abogadosDB)
                    rcvAbogados.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al cargar abogados: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón para agregar abogados
        btnIngresarAbogado.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val nombreAbogado = txtNombre.text.toString().trim()
                    val edadAbogado = txtEdad.text.toString().trim().toInt()
                    val pesoAbogado = txtPeso.text.toString().trim().toDouble()
                    val correoAbogado = txtCorreo.text.toString().trim()

                    val objConexion = ClaseConexion().cadenaConexion()
                    val agregarAbogados = objConexion?.prepareStatement("INSERT INTO tbAbogado (UUID_Abogado, Nombre_Abogado, Edad_Abogado, Peso_Abogado, Correo_Abogado) VALUES (? , ? , ? , ? , ?)")!!
                    agregarAbogados.setString(1, UUID.randomUUID().toString())
                    agregarAbogados.setString(2, nombreAbogado)
                    agregarAbogados.setInt(3, edadAbogado)
                    agregarAbogados.setDouble(4, pesoAbogado)
                    agregarAbogados.setString(5, correoAbogado)

                    agregarAbogados.executeUpdate()

                    // Refrescar la lista
                    val nuevoAbogado = obtenerAbogados()
                    withContext(Dispatchers.Main) {
                        txtNombre.setText("")
                        txtEdad.setText("")
                        txtPeso.setText("")
                        txtCorreo.setText("")
                        Toast.makeText(this@MainActivity, "Abogado ingresado correctamente", Toast.LENGTH_SHORT).show() // Mensaje de éxito
                        (rcvAbogados.adapter as? AdaptadorAbogados)?.actualizarLista(nuevoAbogado)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error al ingresar abogado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDITAR_ABOGADO_REQUEST && resultCode == RESULT_OK) {
            // Refrescar la lista de abogados
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val abogadosDB = obtenerAbogados() // O tu método de obtención de datos
                    withContext(Dispatchers.Main) {
                        (rcvAbogados.adapter as? AdaptadorAbogados)?.actualizarLista(abogadosDB)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error al actualizar la lista: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun obtenerAbogados(): List<ListaAbogados> {
        val listadoAbogados = mutableListOf<ListaAbogados>()
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT UUID_Abogado, Nombre_Abogado, Edad_Abogado, Peso_Abogado, Correo_Abogado FROM tbAbogado")!!

        while (resultSet.next()) {
            val uuid = resultSet.getString("UUID_Abogado")
            val nombre = resultSet.getString("Nombre_Abogado")
            val edad = resultSet.getInt("Edad_Abogado")
            val peso = resultSet.getDouble("Peso_Abogado")
            val correo = resultSet.getString("Correo_Abogado")

            val valoresJuntos = ListaAbogados(uuid, nombre, edad, peso, correo)
            listadoAbogados.add(valoresJuntos)
        }
        return listadoAbogados
    }
}
