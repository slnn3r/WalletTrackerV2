package com.example.slnn3r.wallettrackerv2.data.firebaseclass

class TransactionFirebase(var transactionId: String = "",
                          var transactionDateTime: Long = 0,
                          var transactionAmount: Double = 0.0,
                          var transactionRemark: String = "",
                          var category: CategoryFirebase = CategoryFirebase(),
                          var account: AccountFirebase = AccountFirebase())