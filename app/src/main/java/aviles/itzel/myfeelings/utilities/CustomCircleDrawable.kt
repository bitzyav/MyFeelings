package aviles.itzel.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import aviles.itzel.myfeelings.R

class CustomCircleDrawable: Drawable {
    var coordenadas: RectF? = null
    var angulo_barrido: Float = 0.0F
    var angulo_inicio: Float = 0.0F
    var grosor_metrica: Int = 0
    var grosor_fondo: Int = 0
    var context: Context? = null
    var emociones = ArrayList<Emociones>()


    constructor(context: Context, emociones: ArrayList<Emociones>){
        this.context = context
        grosor_metrica = context.resources.getDimensionPixelSize(R.dimen.graphWith)
        grosor_fondo = context.resources.getDimensionPixelSize(R.dimen.graphBackground)
        this.emociones = emociones
    }

    override fun draw(canvas: Canvas) {
        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosor_fondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val ancho: Float = (canvas.width - 25).toFloat()
        val alto: Float = (canvas.height - 25).toFloat()

        coordenadas = RectF(25.0F, 25.0F, ancho, alto)

        canvas.drawArc(coordenadas!!, 0.0F, 360.0F, false, fondo)

        if(emociones.size != 0){
            for(e in emociones){
                val degree: Float = (e.porcentaje * 360)/100
                this.angulo_barrido = degree

                var seccion: Paint = Paint()
                seccion.style = Paint.Style.STROKE
                seccion.isAntiAlias = true
                seccion.strokeWidth = (this.grosor_metrica).toFloat()
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = ContextCompat.getColor(this.context!!, e.color)

                canvas.drawArc(coordenadas!!, this.angulo_inicio, this.angulo_barrido, false, seccion)

                this.angulo_inicio += this.angulo_barrido
            }
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}