package com.example.proyectonfc.model

import java.io.Serializable

class Group (
        var code: String,
        var name: String,
        var classroom: String,
        var hour: String,
        var end: String
) : Serializable