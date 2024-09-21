package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recuperacion_android_kendrick.R

class ViewHolderAbogados(view: View) : RecyclerView.ViewHolder(view) {
    val txtTextoCard: TextView = view.findViewById(R.id.txtNombreAbogadoCard)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditarAbogado)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrarAbogado)
}
