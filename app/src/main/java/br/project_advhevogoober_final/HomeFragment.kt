package br.project_advhevogoober_final

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.project_advhevogoober_final.Model.Offer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment:Fragment() {

    val TAG ="HomeFragment"
    var db = FirebaseFirestore.getInstance()
    val user= FirebaseAuth.getInstance().currentUser!!

    private fun onPostItemClick(offer: Offer) {
        val intent = Intent(activity, OfferDetails::class.java)
        intent.putExtra("offerlocation", offer.location)
        intent.putExtra("offerdate", offer.date)
        intent.putExtra("offerdescription", offer.description)
        intent.putExtra("offerjurisdiction", offer.jurisdiction)
        intent.putExtra("offerrequirements", offer.requirements)
        intent.putExtra("offerid", offer.id)
        intent.putExtra("offerprice", offer.price)
        intent.putExtra("offerofferer", offer.offerer)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG,"onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")
        val view: View =inflater!!.inflate(R.layout.fragment_home,container,false)
        var offers = mutableListOf<Offer>()
        var adapter = OfferRecycleAdapter(offers, this::onPostItemClick)
        db.collection("Offers").get().addOnSuccessListener { result ->

            for (document in result) {
                if(document.toObject(Offer::class.java).id != user.uid)
                offers.add(document.toObject(Offer::class.java))
            }
            view.recycler_view_home.layoutManager = LinearLayoutManager(activity)
            view.recycler_view_home.adapter = adapter
        }
        view.btn_post_create.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            val fragment = CreateOffer()
            transaction?.replace(R.id.nav_host_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }
}