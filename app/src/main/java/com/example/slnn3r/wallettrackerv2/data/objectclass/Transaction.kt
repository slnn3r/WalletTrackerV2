package com.example.slnn3r.wallettrackerv2.data.objectclass

class Transaction(val transactionId: String?,
                  val transactionDateTime: Long?,
                  val transactionAmount: Double?,
                  val transactionRemark: String?,
                  val category: Category?,
                  val account: Account?)
