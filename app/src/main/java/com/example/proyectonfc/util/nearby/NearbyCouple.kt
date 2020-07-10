package com.example.proyectonfc.util.nearby

import java.io.*


class NearbyCouple(val androidId: String, val msg: String) : Serializable {

    companion object {
        fun getCouple(bytes: ByteArray): NearbyCouple {
            val bs = ByteArrayInputStream(bytes)

            val ois = ObjectInputStream(bs)
            val couple: NearbyCouple = ois.readObject() as NearbyCouple
            ois.close()

            return couple;
        }
    }

    fun serialize(): ByteArray {
        val bs = ByteArrayOutputStream()
        val os = ObjectOutputStream(bs)
        os.writeObject(this)

        os.close()
        return bs.toByteArray()
    }
}