package com.strkapps.MediTrack

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class MainActivity : AppCompatActivity() {

 //   private lateinit var appBarConfiguration: AppBarConfiguration
//private lateinit var binding: ActivityMainBinding
    var c = Calendar.getInstance()
    var pdate:String = "";

    var mintent : HashMap<String,String> = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        updateDate()
        // calendar
        val pickDateBtn = findViewById<Button>(R.id.pickDateBtn)
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
                loadxml()
            }
        }
        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        pickDateBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val dialog:DatePickerDialog = DatePickerDialog(this@MainActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH))
                dialog.datePicker.maxDate = Date().time
                dialog.show()
            }
        })
        // read xml if exists on present day
        //val format = "MM_dd_yyyy"
        //val sdf = SimpleDateFormat(format,Locale.US)
        //val pdate = sdf.format(c.getTime())
        loadxml()
    }

    override fun onResume() {
        super.onResume()
        val i = getIntent()
        val medicine:String? = getIntent().getStringExtra("medicine")
        if (i != null) {
            if (i.getSerializableExtra("mintent") != null) {
                Log.d("DEBUG SATYA ","HERE")
                mintent = getIntent().getSerializableExtra("mintent") as HashMap<String, String>
            }
        }
        for ((key ,value) in mintent.entries) {
            Log.d("DEBUG_SATYAM M=",medicine.toString())
            Log.d("DEBUG_SATYAM K=",key)
            Log.d("DEBUG_SATYAM V=",value.toString())
            var resID_b = 0
            var resID_cb_m = 0
            var resID_cb_a = 0
            var resID_cb_n = 0
            val attrs = value.split(',')
            val id_b = attrs[0].split('=')
            resID_b = id_b[1].toInt()
            val id_cb_m = attrs[1].split('=')
            resID_cb_m = id_cb_m[1].toInt()
            val id_cb_a = attrs[2].split('=')
            resID_cb_a = id_cb_a[1].toInt()
            val id_cb_n = attrs[3].split('=')
            resID_cb_n = id_cb_n[1].toInt()
            val mbf_a = attrs[4].split('=')
            val maf_a = attrs[5].split('=')
            Log.d("ATTRS=",attrs.toString())
            val cb_m = findViewById<CheckBox>(resID_cb_m)
            val cb_a = findViewById<CheckBox>(resID_cb_a)
            val cb_n = findViewById<CheckBox>(resID_cb_n)
            if (mbf_a[1].equals("true") || maf_a[1].equals("true")) {
                    Log.d("CB_M_ID=",resID_cb_m.toString())
                            cb_m.setChecked(true)
            }
            if (mbf_a[1].equals("false") && maf_a[1].equals("false")) {
                Log.d("CB_M_ID=",resID_cb_m.toString())
                cb_m.setChecked(false)
            }
            val abf_a = attrs[6].split('=')
            val aaf_a = attrs[7].split('=')
            if (abf_a[1].equals("true") || aaf_a[1].equals("true")) {
                    Log.d("CB_A_ID=",resID_cb_a.toString())
                    cb_a.setChecked(true)
            }
            if (abf_a[1].equals("false") && aaf_a[1].equals("false")) {
                Log.d("CB_M_ID=",resID_cb_a.toString())
                cb_a.setChecked(false)
            }
            val nbf_a = attrs[8].split('=')
            val naf_a = attrs[9].split('=')
            if (nbf_a[1].equals("true") || naf_a[1].equals("true")) {
                    Log.d("CB_N_ID=",resID_cb_n.toString())
                    cb_n.setChecked(true)
            }
            if (nbf_a[1].equals("false") && naf_a[1].equals("false")) {
                Log.d("CB_M_ID=",resID_cb_n.toString())
                cb_n.setChecked(false)
            }
            cb_m.isClickable = false
            cb_a.isClickable = false
            cb_n.isClickable = false
            if (key.equals(medicine.toString())) {
                Log.d("LOOP=","MATCHED")
                updatexml(medicine.toString(),mbf_a[1],maf_a[1],abf_a[1],aaf_a[1],nbf_a[1],naf_a[1])
            }
        }
        Log.d("LOOP=","END")
    }

    fun setPdate(view:View){
        val Btn:Button = view as Button
        val BtnTxt = Btn.text
        Log.d("IN SETPDATE=",BtnTxt.toString())
        Log.d("DATE_SEL=",BtnTxt.toString())
    }

    fun updateDate () {
        val format = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(format,Locale.US)
        val pickDateBtn = findViewById<Button>(R.id.pickDateBtn)
        pickDateBtn.setText(sdf.format(c.getTime()))
        this.pdate = SimpleDateFormat("MM_dd_yyyy",Locale.US).format(c.time)
        Log.d("DATE_SEL=",pdate.toString())
    }
    fun deleteRow(view: View) {
        val Btn:Button = view as Button
        val attrs = Btn.getTag().toString().split('_')
        val medicine = attrs[0]
        val rownum = attrs[1]
        //Log.d("BUTTON DELETE NO.medicine,rownum=",medicine+","+rownum)
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        val title:TextView = TextView(this)
        title.setText("Delete Medicine")
        title.setBackgroundColor(Color.parseColor("#9c33ff"))
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20f);
        builder.setCustomTitle(title);
        //builder.setTitle("Delete Medicine")

// Set up the input
        val tv = TextView(this)
        tv.textSize=20f
        tv.text = "Do you want to delete the medicine "+medicine+ "?"
        builder.setView(tv)

// Set up the buttons
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            //Log.d("DEBUG_SATYAM NEW MEDICINE = ",m_Text)
            deleteTableRow(medicine.toString(),rownum.toInt())
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    fun deleteTableRow(medicine:String,rownum:Int) {
        val layout = findViewById<View>(R.id.IdTable) as TableLayout
        val delrow:View = layout.getChildAt(rownum)
       // layout.removeView(delrow)
        Log.d("DELETE TABLE=",medicine+','+rownum.toString())
        // update XML
        //val format = "MM_dd_yyyy"
        //val sdf = SimpleDateFormat(format, Locale.US)
        //val pdate = sdf.format(c.getTime())
        val filename="medicines_"+pdate
        val f: File = File(getFilesDir(),filename)
        val myFile: FileInputStream = FileInputStream(f)
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        if (f.exists() && f.canRead() && f.length() != 0L) {
            val doc: Document = builder.parse(myFile)
            val nl: NodeList = doc.getElementsByTagName("medicine")
            Log.d("XML DELETE UPDATE SIZE", nl.length.toString())

            for (i in 0..nl.length-1) {
                val node: Node = nl.item(i)
                //val nodeMap: NamedNodeMap = node.attributes
                if (node.nodeType == Node.ELEMENT_NODE) {
                    if (node.attributes.getNamedItem("name").textContent.equals(medicine.toString())) {
                        node.parentNode.removeChild(node)
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

    fun medicineTime(view: View) {
        //val vId = view.getTag() as String?
        val btn:Button = view as Button
        val medicine:String = btn.getText() as String
        Log.d("DEBUG_SATYA1 = ",medicine.lowercase())
        val intent = Intent(this,MedicineTiming::class.java)
        intent.putExtra("medicine",medicine.lowercase())
        intent.putExtra("mintent",mintent)
       // mintent.put("mtag",vId)
        startActivity(intent)
    }

    fun addMedicine(view: View) {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        val title:TextView = TextView(this)
        title.setText("Add New Medicine")
        title.setBackgroundColor(Color.parseColor("#9c33ff"))
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20f);
        builder.setCustomTitle(title);
        //builder.setTitle("New Medicine")

// Set up the input
        val input = EditText(this)
// Specify the type of input expected; this, for com.strkapps, sets the input as a password, and will mask the text
        input.setHint("Enter Medicine Name")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            val m_Text = input.text.toString()
            //Log.d("DEBUG_SATYAM NEW MEDICINE = ",m_Text)
            addTableRow(m_Text,true)
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
    fun addTableRow(medicine:String, xmlupdate:Boolean) {
       // val inflater:LayoutInflater = layoutInflater//this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        val tableParams:FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT)
        val rowParams:TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
        //val tableLayout:TableLayout = inflater.inflate<TableLayout>(R.id.IdTable, null, false)
        val tableLayout:TableLayout = findViewById<TableLayout>(R.id.IdTable)
        tableLayout.setLayoutParams(tableParams)
        //val format = "MM_dd_yyyy"
        //val sdf = SimpleDateFormat(format,Locale.US)
        val pickDateBtn = findViewById<Button>(R.id.pickDateBtn)
        //val pdate = sdf.format(c.getTime())
        //if (!File(getResources().getXml(getResources.getIdentifier("medicines_"+$pdate,"xml",packageName)))) {
        //    val output:XMLOutputFactory = XMLOutputFactory.newI
        //}
       // val tableRow:TableRow = inflater.inflate(R.layout.new_medicine,tableLayout,false) as TableRow//TableRow(applicationContext)
        val tableRow:TableRow = TableRow(this)
        tableRow.setLayoutParams(tableParams)
        tableRow.setBackgroundColor(Color.parseColor("#DAE8FC"))
        val textBtn:Button = Button(this)
        rowParams.setMargins(5,5,5,5)
        //val resID_btn = this.getResources()
         //   .getIdentifier("b_" + medicine, "id", getPackageName())
        var resID_b:Int = 0
        var resID_cb_m:Int = 0
        var resID_cb_a:Int = 0
        var resID_cb_n:Int = 0
        Log.d("MINTENT SIZE=",mintent.size.toString())
        if (mintent.containsKey(medicine.lowercase()) == true) {
            val value:String = mintent.get(medicine.lowercase()) as String
            val attrs = value.split(',')
            val id_b = attrs[0].split('=')
            resID_b = id_b[1].toInt()
            val id_cb_m = attrs[1].split('=')
            resID_cb_m = id_cb_m[1].toInt()
            val id_cb_a = attrs[2].split('=')
            resID_cb_a = id_cb_a[1].toInt()
            val id_cb_n = attrs[3].split('=')
            resID_cb_n = id_cb_n[1].toInt()
        } else {
            val size:Int = mintent.size
                resID_b = (size  * 4) + 1
                resID_cb_m = (size  * 4) + 2
                resID_cb_a = (size  * 4) + 3
                resID_cb_n = (size  * 4) + 4
            //resID_b = View.generateViewId()
            //resID_cb_m = View.generateViewId()
            //resID_cb_a = View.generateViewId()
            //resID_cb_n = View.generateViewId()
        }
        Log.d("BUTTON ID=",resID_b.toString())
        textBtn.setId(resID_b)
        textBtn.setLayoutParams(rowParams)
        textBtn.setText(medicine)
        textBtn.setBackgroundColor(Color.parseColor("#0079D6"))
        textBtn.setOnClickListener() {
            medicineTime(it)
        }
        tableRow.addView(textBtn)
        val checkbox_m:CheckBox = CheckBox(this)
        //val resID_cb_m = this.getResources()
        //    .getIdentifier("cb_m_" + medicine, "id", getPackageName())
        Log.d("CB_M ID=",resID_cb_m.toString())
        checkbox_m.setId(resID_cb_m)
        checkbox_m.setLayoutParams(rowParams)
        checkbox_m.setClickable(false)
        tableRow.addView(checkbox_m)
        val checkbox_a:CheckBox = CheckBox(this)
        //val resID_cb_a = this.getResources()
         //   .getIdentifier("cb_a_" + medicine, "id", getPackageName())
        Log.d("CB_A ID=",resID_cb_a.toString())
        checkbox_a.setId(resID_cb_a)
        checkbox_a.setLayoutParams(rowParams)
        checkbox_a.setClickable(false)
        tableRow.addView(checkbox_a)
        val checkbox_n:CheckBox = CheckBox(this)
        //val resID_cb_n = this.getResources()
         //   .getIdentifier("cb_n_" + medicine, "id", getPackageName())
        Log.d("CB_N ID=",resID_cb_n.toString())
        checkbox_n.setId(resID_cb_n)
        checkbox_n.setLayoutParams(rowParams)
        checkbox_n.setClickable(false)
        tableRow.addView(checkbox_n)
        val delBtn:Button = Button(this)
        rowParams.setMargins(5,5,5,5)
        val delBtnTxt='X'
        delBtn.setTag(medicine+"_"+resID_cb_n/4)
        delBtn.setLayoutParams(rowParams)
        delBtn.setText(delBtnTxt.toString())
        delBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20F)
        delBtn.setTextColor(Color.parseColor("#ff0000"));
        delBtn.setBackgroundColor(0xffffff)
        //delBtn.setBackgroundResource(R.drawable.roundedbutton)
        delBtn.layoutParams.width=120
        delBtn.setOnClickListener() {
            deleteRow(it)
        }
        tableRow.addView(delBtn)
        tableLayout.addView(tableRow)
        if (xmlupdate == true) {
            val t = Calendar.getInstance()
            val format = "MM_dd_yyyy"
            val sdf = SimpleDateFormat(format,Locale.US)
            val filename = "medicines_" + pdate
            val f: File = File(getFilesDir(), filename)
            val myFile: FileOutputStream = FileOutputStream(f, true)
            val myFileIn: FileInputStream = FileInputStream(f)//FileInputStream(f, true)
            val docFactory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder:DocumentBuilder = docFactory.newDocumentBuilder()
            val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
            val transformer:Transformer = transformerFactory.newTransformer()
            val value: Boolean = false
            if (f.exists() && f.canRead() && f.length() != 0L) {
                val doc:Document = docBuilder.parse(myFileIn)
                //val nodelist: NodeList = doc.getElementsByTagName("medicine")
                //Log.d("XML UPDATE SIZE", nodelist.length.toString())
                val rootElement:Node = doc.documentElement
                val item:Element = doc.createElement("medicine")
                rootElement.appendChild(item)
                item.setAttribute("name",medicine)
                item.setAttribute("id_b",resID_b.toString())
                item.setAttribute("id_cb_m",resID_cb_m.toString())
                item.setAttribute("id_cb_a",resID_cb_a.toString())
                item.setAttribute("id_cb_n",resID_cb_n.toString())
                item.setAttribute("mbf",value.toString())
                item.setAttribute("maf",value.toString())
                item.setAttribute("abf",value.toString())
                item.setAttribute("aaf",value.toString())
                item.setAttribute("nbf",value.toString())
                item.setAttribute("naf",value.toString())
                val dSource: DOMSource = DOMSource(doc)
                val result: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_"+pdate,0))
                val result_t: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_recent",0))
                transformer.transform(dSource,result)
                transformer.transform(dSource,result_t)
            } else {
              val doc:Document = docBuilder.newDocument()
              val rootElement:Element = doc.createElement("medicines")
              doc.appendChild(rootElement)
              val item:Element = doc.createElement("medicine")
              rootElement.appendChild(item)
              val value: Boolean = false
              item.setAttribute("name",medicine)
              item.setAttribute("id_b",resID_b.toString())
              item.setAttribute("id_cb_m",resID_cb_m.toString())
              item.setAttribute("id_cb_a",resID_cb_a.toString())
              item.setAttribute("id_cb_n",resID_cb_n.toString())
              item.setAttribute("mbf",value.toString())
              item.setAttribute("maf",value.toString())
              item.setAttribute("abf",value.toString())
              item.setAttribute("aaf",value.toString())
              item.setAttribute("nbf",value.toString())
              item.setAttribute("naf",value.toString())
              val dSource: DOMSource = DOMSource(doc)
              val result: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_"+pdate,0))
              val result_t: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_recent",0))
              transformer.transform(dSource,result)
              transformer.transform(dSource,result_t)
            }
            if (mintent.containsKey(medicine) == false) {
                mintent.put(medicine.lowercase(),"id_b="+resID_b.toString()+
                        ",id_cb_m="+resID_cb_m.toString()+
                        ",id_cb_a="+resID_cb_a.toString()+
                        ",id_cb_n="+resID_cb_n.toString()+
                        ",mbf="+value.toString()+
                        ",maf="+value.toString()+
                        ",abf="+value.toString()+
                        ",aaf="+value.toString()+
                        ",nbf="+value.toString()+
                        ",naf="+value.toString())
            }
        }
    }
    fun updatexml(medicine:String,
                  mbf:String, maf:String,
                  abf:String, aaf:String,
                  nbf:String, naf:String) {
        // update XML
        //val format = "MM_dd_yyyy"
        //val sdf = SimpleDateFormat(format, Locale.US)
        //val pdate = sdf.format(c.getTime())
        val filename="medicines_"+pdate
        val f: File = File(getFilesDir(),filename)
        val myFile: FileInputStream = FileInputStream(f)
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        if (f.exists() && f.canRead() && f.length() != 0L) {
            val doc: Document = builder.parse(myFile)
            //val root:Element = doc.documentElement
            //val nodelist: NodeList = root.childNodes
            val nodelist: NodeList = doc.getElementsByTagName("medicine")
            Log.d("XML UPDATE SIZE", nodelist.length.toString())

            for (i in 0..nodelist.length-1) {
                val node: Node = nodelist.item(i)
                //val node:Element = nodelist.item(i)
                Log.d("XML NODE IS->", node.attributes.getNamedItem("name").textContent)
                //val nodeMap: NamedNodeMap = node.attributes
                if (node.nodeType == Node.ELEMENT_NODE) {
                    if (node.attributes.getNamedItem("name").textContent.lowercase().equals(medicine.toString()) || medicine.toString().equals("####")) {
                        node.attributes.getNamedItem("mbf").setTextContent(mbf.toString())
                        node.attributes.getNamedItem("maf").setTextContent(maf.toString())
                        node.attributes.getNamedItem("abf").setTextContent(abf.toString())
                        node.attributes.getNamedItem("aaf").setTextContent(aaf.toString())
                        node.attributes.getNamedItem("nbf").setTextContent(nbf.toString())
                        node.attributes.getNamedItem("naf").setTextContent(naf.toString())
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
            val result_t: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_recent",0))
            transformer.transform(dSource,result)
            transformer.transform(dSource,result_t)
        }

    }
    fun loadxml() {

        // delete earlier view
        val layout = findViewById<View>(R.id.IdTable) as TableLayout
        val rowcount = layout.childCount
        Log.d("LOADXML_CNT=",rowcount.toString())
        if (rowcount > 3) {
             layout.removeViews(3,rowcount-3)
        }
        Log.d("PDATE_SEL=",pdate.toString())
        //val format = "MM_dd_yyyy"
        //val cdate = SimpleDateFormat("MM_dd_yyyy", Locale.US).format(c.getTime())
        val cdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_yyyy"))
        //val pdate = sdf.format(c.getTime())
        Log.d("CDATE_SEL=",cdate.toString())
        val is_before = SimpleDateFormat("MM_dd_yyyy").parse(pdate).before(SimpleDateFormat("MM_dd_yyyy").parse(cdate))
        val is_after = SimpleDateFormat("MM_dd_yyyy").parse(pdate).after(SimpleDateFormat("MM_dd_yyyy").parse(cdate))
        Log.d("PDATE_IS_BEFORE=",is_before.toString())
        Log.d("PDATE_IS_AFTER=",is_after.toString())
        val filename="medicines_"+pdate
        val f: File = File(getFilesDir(), filename)
        if (File(getFilesDir(),filename).exists() == false) {
            // if the date selected is pastdate and no medicine file exists with that date, show empty
            if (is_before.equals(false)) {
                val rf: File = File(getFilesDir(), "medicines_recent")
                if (rf.isFile && rf.canRead() && rf.length() != 0L) {
                    rf.copyTo(File(getFilesDir(), filename), true)
                    updatexml("####","false","false","false","false","false","false")
                }
            }
        }
        if (f.isFile && f.canRead() && f.length() != 0L) {
            val myFileIn: FileInputStream = FileInputStream(f)//FileInputStream(f, true)
            val docFactory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder:DocumentBuilder = docFactory.newDocumentBuilder()
            val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
            val transformer:Transformer = transformerFactory.newTransformer()
            val doc:Document = docBuilder.newDocument()
            val rootElement:Element = doc.createElement("medicines")
            doc.appendChild(rootElement)
            val factory:XmlPullParserFactory = XmlPullParserFactory.newInstance()
            val parser:XmlPullParser = factory.newPullParser()
            parser.setInput(FileReader(f))
            var event:Int = parser.eventType
            while (event !=XmlPullParser.END_DOCUMENT) {
                //var tag_name:String = parser.name
                when (event) {
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "medicine") {
                            val name = parser.getAttributeValue(null,"name")
                            val id_b = parser.getAttributeValue(null,"id_b")
                            val id_cb_m = parser.getAttributeValue(null,"id_cb_m")
                            val id_cb_a = parser.getAttributeValue(null,"id_cb_a")
                            val id_cb_n = parser.getAttributeValue(null,"id_cb_n")
                            val mbf = parser.getAttributeValue(null,"mbf")
                            val maf = parser.getAttributeValue(null,"maf")
                            val abf = parser.getAttributeValue(null,"abf")
                            val aaf = parser.getAttributeValue(null,"aaf")
                            val nbf = parser.getAttributeValue(null,"nbf")
                            val naf = parser.getAttributeValue(null,"naf")
                            mintent.put(name.lowercase(),"id_b="+id_b+
                                    ",id_cb_m="+id_cb_m+
                                    ",id_cb_a="+id_cb_a+
                                    ",id_cb_n="+id_cb_n+
                                    ",mbf="+mbf+
                                    ",maf="+maf+
                                    ",abf="+abf+
                                    ",aaf="+aaf+
                                    ",nbf="+nbf+
                                    ",naf="+naf)
                            addTableRow(name,false)
                            val item:Element = doc.createElement("medicine")
                            rootElement.appendChild(item)
                            val value: Boolean = false
                            item.setAttribute("name",name)
                            item.setAttribute("id_b",id_b.toString())
                            item.setAttribute("id_cb_m",id_cb_m.toString())
                            item.setAttribute("id_cb_a",id_cb_a.toString())
                            item.setAttribute("id_cb_n",id_cb_n.toString())
                            item.setAttribute("mbf",value.toString())
                            item.setAttribute("maf",value.toString())
                            item.setAttribute("abf",value.toString())
                            item.setAttribute("aaf",value.toString())
                            item.setAttribute("nbf",value.toString())
                            item.setAttribute("naf",value.toString())
                            val cb_m = findViewById<CheckBox>(id_cb_m.toInt())
                            val cb_a = findViewById<CheckBox>(id_cb_a.toInt())
                            val cb_n = findViewById<CheckBox>(id_cb_n.toInt())
                            if (mbf.toBoolean() == true || maf.toBoolean() == true) {
                                cb_m.setChecked(true)
                            }
                            if (abf.toBoolean() == true || aaf.toBoolean() == true) {
                                cb_a.setChecked(true)
                            }
                            if (nbf.toBoolean() == true || naf.toBoolean() == true) {
                                cb_n.setChecked(true)
                            }
                            cb_m.isClickable = false
                            cb_a.isClickable = false
                            cb_n.isClickable = false
                        }
                    }
                }
                event = parser.next()
            }
            val t = Calendar.getInstance()
            t.add(Calendar.DAY_OF_YEAR,1)
            val tdate = SimpleDateFormat("MM_dd_yyyy",Locale.US).format(t.time)
            val dSource: DOMSource = DOMSource(doc)
            val result: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_"+tdate,0))
            val result_t: StreamResult = StreamResult(applicationContext.openFileOutput("medicines_recent",0))
            transformer.transform(dSource,result)
            transformer.transform(dSource,result_t)
        }

    }
}