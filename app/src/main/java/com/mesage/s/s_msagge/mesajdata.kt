package com.mesage.s.s_msagge

class mesajdata {
    var mesaj:String?=null
    var taraf:String?=null
    var Saat:String?=null
    var Okunmamis:String?=null
    var Okundubilgisi:Boolean?=null
    constructor(mesaj:String,taraf:String,Okundubilgisi:Boolean){
        this.mesaj=mesaj
        this.taraf=taraf
        this.Okundubilgisi=Okundubilgisi
    }
    constructor(mesaj:String,saat:String,Okunmamis:String){
        this.mesaj=mesaj
        this.Saat=saat
        this.Okunmamis=Okunmamis
    }
    constructor(){}
}