package com.example.schedulecam.teste

//modelo de builder para testar a lib
class ClasseTesteParaLibs private constructor(
    val nome: String,
    val idade: Int,
    val endereco: String?
){
    data class Builder (
        var nome: String = "",
        var idade: Int = 0,
        var endereco: String? = null
    ){
        fun nome(nome: String) = apply { this.nome = nome }
        fun idade(idade: Int) = apply { this.idade = idade }
        fun endereco(endereco: String?) = apply { this.endereco = endereco }

        fun build(): ClasseTesteParaLibs {
            return ClasseTesteParaLibs(nome, idade, endereco)
        }
    }
}