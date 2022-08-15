package com.delta.eregist.ui.document.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import com.delta.eregist.model.DocumentRequest
import com.delta.eregist.ui.document.DocumentFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_document_item.view.*

class DocumentListAdapter(
    val mCtx: Context,
    val layoutResId: Int,
    val list: List<DocumentRequest>,
    val parentFragmentManager: FragmentManager,
    val documentFragment: DocumentFragment
) : ArrayAdapter<DocumentRequest>(mCtx, layoutResId, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val document = list[position]

        view.tvTipeDokumen.text = document.type_formatted
        view.tvNomorDokumen.text = "Kode Permintaan: " + document.code
        view.tvStatusDokumen.text = "Status: " + document.status_formatted
        view.ivType.setImageResource(document.type_image)

        if (document.admin_approval != 0) {
            view.btnDocumentDelete.visibility = View.GONE
            val set = ConstraintSet()
            val currLayout = view.viewConstraintDocument
            set.clone(currLayout)
            set.connect(
                view.linearLayout2.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                30
            )
            set.applyTo(currLayout)
        }

        view.btnDocumentDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val db = Firebase.firestore

            with(builder)
            {
                setTitle("Batalkan Dokumen?")
                setMessage("Apakah anda yakin ingin membatalkan dokumen ini?")
                setPositiveButton("OK") { _, _ ->
                    // User clicked OK button
                    val deletedDocument = list[position]
                    db.collection("requests").document(deletedDocument.firebaseId!!)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!")
                            parentFragmentManager.beginTransaction().detach(documentFragment)
                                .commitNow()
                            parentFragmentManager.beginTransaction().attach(documentFragment)
                                .commitNow()
                            Toast.makeText(
                                mCtx,
                                "Pembatalan Dokumen Berhasil",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                setNegativeButton("Cancel") { _, _ ->
                    // User Clicked Cancel
                }
                show()
            }
        }

        return view

    }

    companion object {
        private const val TAG = "DocumentListAdapter"
    }
}