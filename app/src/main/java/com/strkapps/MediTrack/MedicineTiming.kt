package com.strkapps.MediTrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.util.Log
import android.widget.CheckBox
import org.w3c.dom.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class MedicineTiming : AppCompatActivity() {

    var mintent : HashMap<String, String> = HashMap<String,String>()
    var c = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_timing)
        val medicine = getIntent().getStringExtra("medicine")
        mintent = getIntent().getSerializableExtra("mintent") as HashMap<String, String>
        val value:String = mintent.get(medicine) as String
        val attrs = value.split(',')
        val mbf_a = attrs[4].split('=')
        val mbf = mbf_a[1]
        val maf_a = attrs[5].split('=')
        val maf = maf_a[1]
        val abf_a = attrs[6].split('=')
        val abf = abf_a[1]
        val aaf_a = attrs[7].split('=')
        val aaf = aaf_a[1]
        val nbf_a = attrs[8].split('=')
        val nbf = nbf_a[1]
        val naf_a = attrs[9].split('=')
        val naf = naf_a[1]
        if (mbf.equals("true")) {
            val resID_m_bf = this.getResources()
                .getIdentifier("cb_m_bf" , "id", getPackageName())
            val cb_m_bf = findViewById<CheckBox>(resID_m_bf)
            cb_m_bf.setChecked(true)
        }
        if (maf.equals("true")) {
            val resID_m_af = this.getResources()
                .getIdentifier("cb_m_af" , "id", getPackageName())
            val cb_m_af = findViewById<CheckBox>(resID_m_af)
            cb_m_af.setChecked(true)
        }
        if (abf.equals("true")) {
            val resID_a_bf = this.getResources()
                .getIdentifier("cb_a_bf" , "id", getPackageName())
            val cb_a_bf = findViewById<CheckBox>(resID_a_bf)
            cb_a_bf.setChecked(true)
        }
        if (aaf.equals("true")) {
            val resID_a_af = this.getResources()
                .getIdentifier("cb_a_af" , "id", getPackageName())
            val cb_a_af = findViewById<CheckBox>(resID_a_af)
            cb_a_af.setChecked(true)
        }
        if (nbf.equals("true")) {
            val resID_n_bf = this.getResources()
                .getIdentifier("cb_n_bf" , "id", getPackageName())
            val cb_n_bf = findViewById<CheckBox>(resID_n_bf)
            cb_n_bf.setChecked(true)
        }
        if (naf.equals("true")) {
            val resID_n_af = this.getResources()
                .getIdentifier("cb_n_af" , "id", getPackageName())
            val cb_n_af = findViewById<CheckBox>(resID_n_af)
            cb_n_af.setChecked(true)
        }
    }

    fun saveTimingInfo(view: View) {
        val medicine:String = getIntent().getStringExtra("medicine") as String
        mintent = getIntent().getSerializableExtra("mintent") as HashMap<String, String>
        Log.d("DEBUG_SATYA2 = ",medicine)
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("medicine",medicine.lowercase())
        val cb_m_bf = findViewById<CheckBox>(R.id.cb_m_bf)
        val cb_m_af = findViewById<CheckBox>(R.id.cb_m_af)
        val cb_a_bf = findViewById<CheckBox>(R.id.cb_a_bf)
        val cb_a_af = findViewById<CheckBox>(R.id.cb_a_af)
        val cb_n_bf = findViewById<CheckBox>(R.id.cb_n_bf)
        val cb_n_af = findViewById<CheckBox>(R.id.cb_n_af)
        val mvalue:String = mintent.get(medicine) as String
        val attrs = mvalue.split(',')
        val mbf_a = attrs[4].split('=')
        val mbf = mbf_a[1]
        val maf_a = attrs[5].split('=')
        val maf = maf_a[1]
        val abf_a = attrs[6].split('=')
        val abf = abf_a[1]
        val aaf_a = attrs[7].split('=')
        val aaf = aaf_a[1]
        val nbf_a = attrs[8].split('=')
        val nbf = nbf_a[1]
        val naf_a = attrs[9].split('=')
        val naf = naf_a[1]
        var mbf_m:String = "false"
        var maf_m:String = "false"
        var abf_m:String = "false"
        var aaf_m:String = "false"
        var nbf_m:String = "false"
        var naf_m:String = "false"

        if (cb_m_bf.isChecked() == true) {
            mbf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("mbf=false","mbf=true")
            mintent.put(medicine,value_n)
        } else {
            if (mbf == "true") {
                mbf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("mbf=true","mbf=false")
                mintent.put(medicine,value_n)
            }
        }
        if (cb_m_af.isChecked() == true) {
            maf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("maf=false","maf=true")
            mintent.put(medicine,value_n)
        } else {
            if (maf == "true") {
                maf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("maf=true","maf=false")
                mintent.put(medicine,value_n)
            }
        }
        if (cb_a_bf.isChecked() == true) {
            abf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("abf=false","abf=true")
            mintent.put(medicine,value_n)
        } else {
            if (abf == "true") {
                abf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("abf=true","abf=false")
                mintent.put(medicine,value_n)
            }
        }
        if (cb_a_af.isChecked() == true) {
            aaf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("aaf=false","aaf=true")
            mintent.put(medicine,value_n)
        } else {
            if (aaf == "true") {
                aaf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("aaf=true","aaf=false")
                mintent.put(medicine,value_n)
            }
        }
        if (cb_n_bf.isChecked() == true) {
            nbf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("nbf=false","nbf=true")
            mintent.put(medicine,value_n)
        } else {
            if (nbf == "true") {
                nbf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("nbf=true","nbf=false")
                mintent.put(medicine,value_n)
            }
        }
        if (cb_n_af.isChecked() == true) {
            naf_m = "true"
            val value:String =mintent.get(medicine) as String
            val value_n = value.replace("naf=false","naf=true")
            mintent.put(medicine,value_n)
        } else {
            if (naf == "true") {
                naf_m = "false"
                val value:String =mintent.get(medicine) as String
                val value_n = value.replace("naf=true","naf=false")
                mintent.put(medicine,value_n)
            }
        }
        val value:String =mintent.get(medicine) as String
        Log.d("MEDICINE VALUE=",value)
        //updatexml(medicine.toString(),
        //              mbf_m.toString(), maf_m.toString(),
        //              abf_m.toString(), aaf_m.toString(),
        //              nbf_m.toString(), naf_m.toString())
        intent.putExtra("mintent",mintent)
        startActivity(intent)
    }

    fun back2Main(view: View) {
      val intent = Intent(this,MainActivity::class.java)
      startActivity(intent)
      //setContentView(R.layout.activity_main)
    }

    fun updatexml(medicine:String,
                  mbf:String, maf:String,
                  abf:String, aaf:String,
                  nbf:String, naf:String) {
        // update XML
        val format = "MM_dd_yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        val pdate = sdf.format(c.getTime())
        val filename="medicines_"+pdate
        val f: File = File(getFilesDir(),filename)
        val myFile: FileInputStream = FileInputStream(f)
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        if (f.exists() && f.canRead() && f.length() != 0L) {
            val doc: Document = builder.parse(myFile)
            val nodelist: NodeList = doc.getElementsByTagName("medicine")
            Log.d("XML UPDATE SIZE", nodelist.length.toString())

            for (i in 0..nodelist.length-1) {
                val node: Node = nodelist.item(i)
                //val nodeMap: NamedNodeMap = node.attributes
                if (node.nodeType == Node.ELEMENT_NODE) {
                    if (node.attributes.getNamedItem("name").textContent.lowercase().equals(medicine.toString())) {
                        val name_x: String = node.attributes.getNamedItem("name").textContent
                        val id_b_x: String = node.attributes.getNamedItem("id_b").textContent
                        val id_cb_m_x: String = node.attributes.getNamedItem("id_cb_m").textContent
                        val id_cb_a_x: String = node.attributes.getNamedItem("id_cb_a").textContent
                        val id_cb_n_x: String = node.attributes.getNamedItem("id_cb_n").textContent
                        val mbf_x: String = node.attributes.getNamedItem("mbf").textContent
                        val maf_x: String = node.attributes.getNamedItem("maf").textContent
                        val abf_x: String = node.attributes.getNamedItem("abf").textContent
                        val aaf_x: String = node.attributes.getNamedItem("aaf").textContent
                        val nbf_x: String = node.attributes.getNamedItem("nbf").textContent
                        val naf_x: String = node.attributes.getNamedItem("naf").textContent
                        node.parentNode.removeChild(node)
                        val rootElement:Node = doc.documentElement
                        val new_item: Element = doc.createElement("medicine")
                        rootElement.appendChild(new_item)
                        new_item.setAttribute("name",name_x)
                        new_item.setAttribute("id_b",id_b_x.toString())
                        new_item.setAttribute("id_cb_m",id_cb_m_x.toString())
                        new_item.setAttribute("id_cb_a",id_cb_a_x.toString())
                        new_item.setAttribute("id_cb_n",id_cb_n_x.toString())
                        new_item.setAttribute("mbf",mbf.toString())
                        new_item.setAttribute("maf",maf.toString())
                        new_item.setAttribute("abf",abf.toString())
                        new_item.setAttribute("aaf",aaf.toString())
                        new_item.setAttribute("nbf",nbf.toString())
                        new_item.setAttribute("naf",naf.toString())
                    }
                }
            }
            val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
            val transformer: Transformer = transformerFactory.newTransformer()
            val dSource: DOMSource = DOMSource(doc)
            //val filename="medicines_new_"+pdate
            //val f: File = File(getFilesDir(),filename)
            val myFileOut: FileOutputStream = FileOutputStream(f)
            val result: StreamResult = StreamResult(myFileOut)
            transformer.transform(dSource,result)
        }

    }
}