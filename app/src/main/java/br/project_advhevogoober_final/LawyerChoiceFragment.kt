package br.project_advhevogoober_final

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.project_advhevogoober_final.BuildConfig.DEBUG
import br.project_advhevogoober_final.Model.LawyerProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_lawyer_choice.*
import kotlinx.android.synthetic.main.fragment_lawyer_choice.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.TemporalQueries.localDate
import java.util.*


class LawyerChoiceFragment:Fragment() {

    val TAG = "LawyerChoiceFragment"

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        container?.removeAllViews()// fix milagroso
        val view: View = inflater!!.inflate(R.layout.fragment_lawyer_choice, container, false)
        view.btnSalvar.setOnClickListener {
            if (view.lawyer_name.text.toString() != "" &&
                view.lawyer_surname.text.toString() != "" &&
                view.lawyer_phone.text.toString() != "" &&
                view.lawyer_ssn.text.toString() != "" &&
                view.lawyer_oab_code.text.toString() != "" &&
                view.lawyer_birthdate.text.toString() != "" && legalDoB()==true
            ) {
//                var testol=LocalDate.parse(lawyer_birthdate.text.toString())
//                val dato: Date = java.sql.Date.valueOf(testol.toString())

                var dateFormat=SimpleDateFormat("dd/MM/yyyy")
                var date=dateFormat.parse(lawyer_birthdate.text.toString())

                var lawyer=LawyerProfile(view.lawyer_name.text.toString(),view.lawyer_surname.text.toString(),null,view.lawyer_phone.text.toString(),view.lawyer_ssn.text.toString(),view.lawyer_oab_code.text.toString(),date)
                val db= FirebaseFirestore.getInstance()
                val uid=FirebaseAuth.getInstance().currentUser!!.uid
                db.collection("lawyers").document(uid).set(lawyer).addOnSuccessListener {
                    Toast.makeText(activity,"Funcionou",Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(activity,it.toString(),Toast.LENGTH_LONG).show()
                }
                var intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(activity, "Preencha todos os campos corretamente!!", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

    private fun legalDoB():Boolean{
        var dateFormat=SimpleDateFormat("dd/MM/yyyy")
        return try{
            var date=dateFormat.parse(lawyer_birthdate.text.toString())
            true
        } catch (e:ParseException){
            Log.d(DEBUG.toString(),"Not legal date")
            false
        }
    }
}


