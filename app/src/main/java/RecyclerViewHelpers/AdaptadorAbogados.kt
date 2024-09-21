package RecyclerViewHelper

import Modelo.ClaseConexion
import Modelo.ListaAbogados
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.recuperacion_android_kendrick.MainActivity
import com.example.recuperacion_android_kendrick.R
import com.example.recuperacion_android_kendrick.detalle_abogado
import com.example.recuperacion_android_kendrick.editar_abogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdaptadorAbogados(private var Datos: List<ListaAbogados>) : RecyclerView.Adapter<ViewHolderAbogados>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbogados {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)

        return ViewHolderAbogados(vista)
    }
    fun actualizarLista(nuevaLista: List<ListaAbogados>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualizarPantalla(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.UUID_Abogado == uuid }
        Datos[index].Nombre_Abogado = nuevoNombre
        notifyDataSetChanged()
    }
    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(NombreAbogado: String, posicion: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            //1- Creamos un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val borrarTicket = objConexion?.prepareStatement("delete from tbAbogado where Nombre_Abogado = ?")!!
            borrarTicket.setString(1, NombreAbogado)
            borrarTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder:ViewHolderAbogados, position: Int) {
        //Controlar a la card
        val abogados = Datos[position]
        holder.txtTextoCard.text = abogados.Nombre_Abogado

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar al abogado?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(abogados.Nombre_Abogado, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context as AppCompatActivity
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar los datos del abogado?")

            builder.setPositiveButton("Sí") { dialog, which ->
                val EditarAbogado = Intent(context, editar_abogado::class.java)

                // Enviar datos actuales del abogado
                EditarAbogado.putExtra("UUID_Abogado", abogados.UUID_Abogado)
                EditarAbogado.putExtra("Nombre_Abogado", abogados.Nombre_Abogado)
                EditarAbogado.putExtra("Edad_Abogado", abogados.Edad_Abogado)
                EditarAbogado.putExtra("Peso_Abogado", abogados.Peso_Abogado)
                EditarAbogado.putExtra("Correo_Abogado", abogados.Correo_Abogado)

                // Iniciar la actividad para editar los detalles
                context.startActivityForResult(EditarAbogado, MainActivity.EDITAR_ABOGADO_REQUEST)
            }

            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }

            val dialog = builder.create()
            dialog.show()
        }



        holder.txtTextoCard.setOnClickListener{
            val context = holder.itemView.context
            val pantallaDetalle = Intent(context, detalle_abogado::class.java)

            // Envía los valores del ticket a la otra pantalla
            pantallaDetalle.putExtra("UUID_Abogado", abogados.UUID_Abogado)
            pantallaDetalle.putExtra("Nombre_Abogado", abogados.Nombre_Abogado)
            pantallaDetalle.putExtra("Edad_Abogado", abogados.Edad_Abogado)
            pantallaDetalle.putExtra("Peso_Abogado", abogados.Peso_Abogado)
            pantallaDetalle.putExtra("Correo_Abogado", abogados.Correo_Abogado)
            context.startActivity(pantallaDetalle)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val pantallaDetalle = Intent(context, detalle_abogado::class.java)

            // Envía los valores del ticket a la otra pantalla
            pantallaDetalle.putExtra("UUID_Abogado", abogados.UUID_Abogado)
            pantallaDetalle.putExtra("Nombre_Abogado", abogados.Nombre_Abogado)
            pantallaDetalle.putExtra("Edad_Abogado", abogados.Edad_Abogado)
            pantallaDetalle.putExtra("Peso_Abogado", abogados.Peso_Abogado)
            pantallaDetalle.putExtra("Correo_Abogado", abogados.Correo_Abogado)
            context.startActivity(pantallaDetalle)
        }

    }

}

