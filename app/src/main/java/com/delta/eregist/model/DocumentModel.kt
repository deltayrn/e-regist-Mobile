package com.delta.eregist.model

import com.delta.eregist.R
import com.google.firebase.Timestamp

data class KTPCreate(
    var nama: String? = "",
    var jenis_kelamin: String? = "",
    var tempat_lahir: String? = "",
    var tanggal_lahir: String? = "",
    var agama: String? = "",
    var pekerjaan: String? = "",
    var status_perkawinan: String? = ""
)

data class KTPUpdate(
    var nik: String? = "",
    var nama: String? = "",
    var jenis_kelamin: String? = "",
    var tempat_lahir: String? = "",
    var tanggal_lahir: String? = "",
    var agama: String? = "",
    var pekerjaan: String? = "",
    var status_perkawinan: String? = ""
)

data class RequestKTPNew(
    var code: String? = "",
    var user: UserModel? = null,
    var type: String? = "",
    var admin_approval: Int? = 0,
    var status: Int? = 0,
    var createdAt: Timestamp? = null,
    var data_ktp: KTPCreate
) {
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

    fun generateCode() {
        code = "doc-" + getRandomString(8)
    }
}

data class RequestKTPUpdate(
    var code: String? = "",
    var user: UserModel? = UserModel(),
    var type: String? = "",
    var admin_approval: Int? = 0,
    var status: Int? = 0,
    var createdAt: Timestamp? = null,
    var data_ktp: KTPUpdate
) {
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

    fun generateCode() {
        code = "doc-" + getRandomString(8)
    }
}

data class KKPerson(
    var nik: String? = "",
    var nama: String? = "",
    var jenis_kelamin: String? = "",
    var tempat_lahir: String? = "",
    var tanggal_lahir: String? = "",
    var nama_ibu: String? = "",
    var nama_ayah: String? = "",
    var role: String? = "",
    var agama: String? = "",
    var pendidikan: String? = "",
    var pekerjaan: String? = "",
    var status_perkawinan: String? = ""
)

data class requestKKNew(
    var code: String? = "",
    var user: UserModel? = UserModel(),
    var type: String? = "",
    var admin_approval: Int? = 0,
    var status: Int? = 0,
    var createdAt: Timestamp? = null,
    var data_kk: ArrayList<KKPerson>?
) {
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

    fun generateCode() {
        code = "doc-" + getRandomString(8)
    }
}

data class requestKKUpdate(
    var code: String? = "",
    var no_kk: String? = "",
    var user: UserModel? = UserModel(),
    var type: String? = "",
    var admin_approval: Int? = 0,
    var status: Int? = 0,
    var createdAt: Timestamp? = null,
    var data_kk: ArrayList<KKPerson>?
) {
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

    fun generateCode() {
        code = "doc-" + getRandomString(8)
    }
}

data class DocumentRequest(
    var firebaseId: String? = "",
    var code: String? = "",
    var user: UserModel? = UserModel(),
    var type: String? = "",
    var admin_approval: Int? = 0,
    var status: Int? = 0,
    var createdAt: Timestamp? = null,
    var data_ktp: Any? = null,
    var data_kk: List<KKPerson>? = emptyList<KKPerson>()
) {
    var type_formatted: String = ""
    var type_image: Int = 0
    var type_image_color: Int = 0
    var status_formatted: String = ""

    fun fillData() {
        when (type) {
            "ktp_new" -> {
                type_formatted = "KTP Baru"
                type_image = R.drawable.ic_id_card
            }
            "ktp_update" -> {
                type_formatted = "Update KTP"
                type_image = R.drawable.ic_id_card
            }
            "kk_new" -> {
                type_formatted = "KK Baru"
                type_image = R.drawable.ic_kartu_keluarga
            }
            "kk_update" -> {
                type_formatted = "Update KK"
                type_image = R.drawable.ic_kartu_keluarga
            }
        }

        status_formatted = when {
            admin_approval == 2 -> "Dokumen Tidak Disetujui"
            admin_approval == 0 -> "Menunggu Persetujuan Admin"
            status == 0 || status == 1 -> "Menunggu Proses Administrasi"
            status == 2 -> "Menunggu Proses Foto"
            status == 3 -> "Menunggu Proses Pembuatan Dokumen"
            status == 4 -> "Dokumen Dapat Diambil"
            status == 5 -> "Transaksi Selesai"
            else -> "???"
        }
    }
}