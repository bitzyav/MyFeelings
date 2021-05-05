package aviles.itzel.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import aviles.itzel.myfeelings.utilities.CustomBarDrawable
import aviles.itzel.myfeelings.utilities.CustomCircleDrawable
import aviles.itzel.myfeelings.utilities.Emociones
import aviles.itzel.myfeelings.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile? = null
    var very_happy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var very_sad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()

        fetchingData()
        if (!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graph_very_happy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, very_happy))
            graph_happy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graph_neutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graph_sad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graph_very_sad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, very_sad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        save_btn.setOnClickListener{
            guardar()
        }

        very_happy_btn.setOnClickListener{
            very_happy++
            iconoMayoria()
            actualizarGrafica()
        }

        happy_btn.setOnClickListener{
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutral_btn.setOnClickListener{
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sad_btn.setOnClickListener{
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        very_sad_btn.setOnClickListener{
            very_sad++
            iconoMayoria()
            actualizarGrafica()
        }
    }

    fun fetchingData(){
        try {
            var json : String = jsonFile?.getData(this) ?: ""
            if(json != ""){
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for(i in lista){
                    when (i.nombre){
                        "Muy feliz" -> very_happy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> very_sad = i.total
                    }
                }
            } else {
                this.data = false
            }
        }catch (exception: JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria(){
        if(happy > very_happy && happy > neutral && happy > sad && happy > very_sad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(very_happy > happy && very_happy > neutral && very_happy > sad && very_happy > very_sad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_very_happy))
        }
        if(neutral > very_happy && neutral > happy && neutral > sad && neutral > very_sad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if(sad > happy && sad > neutral && sad > very_happy && sad > very_sad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if(very_sad > happy && very_sad > neutral && very_sad > sad && sad < very_happy){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_very_sad))
        }
    }

    fun actualizarGrafica(){
        var total = very_happy + happy + neutral + very_sad + sad

        var pVH: Float = (very_happy * 100 / total).toFloat()
        var pH: Float = (happy * 100 / total).toFloat()
        var pN: Float = (neutral * 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (very_sad * 100 / total).toFloat()

        Log.d("porcentajes", "very happy" + pVH)
        Log.d("porcentajes", "happy" + pH)
        Log.d("porcentajes", "neutral" + pN)
        Log.d("porcentajes", "sad" + pS)
        Log.d("porcentajes", "very sad" + pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard ,very_happy ))
        lista.add(Emociones("Feliz", pH, R.color.orange , happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie , neutral))
        lista.add(Emociones("Triste", pS, R.color.blue , sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue , very_sad))

        val fondo = CustomCircleDrawable(this, lista)

        graph_very_happy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, very_happy))
        graph_happy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graph_neutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graph_sad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graph_very_sad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, very_sad))

        graph.background = fondo
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{
        var lista =  ArrayList<Emociones>()

        for(i in 0..jsonArray.length()){
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            }catch (exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o :  Int = 0
        for(i in lista){
            Log.d("objetos", i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }


}