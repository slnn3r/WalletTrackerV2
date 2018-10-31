package com.example.slnn3r.wallettrackerv2.constant.string

class Constant {

    object LoggingTag {
        const val SPLASH_LOGGING = "SPLASH ERROR"
        const val LOGIN_LOGGING = "LOGIN ERROR"
        const val MENU_LOGGING = "MENU ERROR"
        const val DASHBOARD_LOGGING = "DASHBOARD ERROR"

        const val VIEW_ACCOUNT_LOGGING = "VIEW ACC ERROR"
        const val CREATE_ACCOUNT_LOGGING = "CREATE ACC ERROR"
        const val DETAILS_ACCOUNT_LOGGING = "DETAILS ACC ERROR"

        const val VIEW_CATEGORY_LOGGING = "VIEW CAT ERROR"
        const val CREATE_CATEGORY_LOGGING = "CREATE CAT ERROR"
        const val DETAILS_CATEGORY_LOGGING = "DETAILS CAT ERROR"

        const val CREATE_TRANSACTION_LOGGING = "CREATE TRANS ERROR"
        const val DETAILS_TRANSACTION_LOGGING = "DETAILS TRANS ERROR"
    }

    object GoogleLoginApi {
        const val REQUEST_ID_TOKEN = "942197312071-ufvdrsq664ovqhhb3tr4mr9uqord5u3m.apps.googleusercontent.com"
        const val REQUEST_CODE = 1
    }

    object ConditionalKeyword {
        const val DEFAULT_STATUS = "Default"
        const val NON_DEFAULT_STATUS = "New"
    }

    object KeyId {
        const val ACCOUNT_DETAILS_ARG = "accountDetailsArg"
    }

    object RealmTableName {
        const val ACCOUNT_REALM_TABLE = "account.realm"
    }

    object RealmVariableName {
        const val ACCOUNT_ID_VARIABLE = "accountId"
    }
}