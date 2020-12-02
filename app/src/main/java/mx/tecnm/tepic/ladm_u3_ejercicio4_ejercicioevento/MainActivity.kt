package mx.tecnm.tepic.ladm_u3_ejercicio4_ejercicioevento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var datos = ArrayList<String>()
    var listaID = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        baseRemota.collection("evento")
            .addSnapshotListener {querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    mensaje("Error de inserción en base de datos");
                    return@addSnapshotListener;
                }
                datos.clear();
                listaID.clear();
                var cadena = "";
                for (registro in querySnapshot!!) {
                    cadena = "Descripción del evento: ${registro.getString("desc")}" + "\nLugar: ${registro.getString("place")}"+
                            "\nFecha: ${registro.getString("fecha")}"
                    datos.add(cadena);
                    listaID.add(registro.id);
                    var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datos);
                    Lista.adapter = adaptador;
                }
            }
        Insertar.setOnClickListener {
            insertar();
        }
        Salir.setOnClickListener {
            this.finish();
            exitProcess(0);
        }
    }
    private fun insertar() {
        var datosInsertar = hashMapOf(
            "desc" to desc.text.toString(),
            "place" to place.text.toString(),
            "fecha" to fecha.text.toString()
        );
        baseRemota.collection("evento")
            .add(datosInsertar as Any)
            .addOnSuccessListener {
                Toast.makeText(this,"Guardado con ID: ${it.id}", Toast.LENGTH_LONG)
                    .show();
                desc.setText("");
                place.setText("");
                fecha.setText("");
            }
            .addOnFailureListener {
                mensaje("Excepción al insertar:\n${it.message!!}")
            }
    }
    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setMessage(s)
            .setTitle("Atención!")
            .setPositiveButton("Vale"){d,i->}
            .show()
    }
}

//Alex Rodríguez
