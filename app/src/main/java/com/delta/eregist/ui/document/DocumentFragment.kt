package com.delta.eregist.ui.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.delta.eregist.R
import com.delta.eregist.model.DocumentRequest
import com.delta.eregist.ui.document.adapter.DocumentListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DocumentFragment : Fragment() {
    lateinit var list: MutableList<DocumentRequest>
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_document, container, false)
        listView = root.findViewById(R.id.lvDocuments)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val userAuth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        list = mutableListOf()
        db.collection("requests")
            .whereEqualTo("user.id", userAuth!!.uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (d in documents) {
                    val doc = d.toObject(DocumentRequest::class.java)
                    doc.fillData()
                    doc.firebaseId = d.id
                    list.add(doc)
                }
                val adapter =
                    context?.let {
                        DocumentListAdapter(
                            it,
                            R.layout.fragment_document_item,
                            list,
                            parentFragmentManager,
                            this
                        )
                    }
                listView.adapter = adapter
            }

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        private const val TAG = "DocumentFragment"
    }
}