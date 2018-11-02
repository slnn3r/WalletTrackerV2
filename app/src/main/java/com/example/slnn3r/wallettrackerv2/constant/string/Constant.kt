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
        const val EXPENSE_STATUS = "Expense"
        const val INCOME_STATUS = "Income"
    }

    object KeyId {
        const val ACCOUNT_DETAILS_ARG = "accountDetailsArg"
        const val CATEGORY_CREATE_ARG = "categoryCreateArg"
        const val CATEGORY_DETAILS_ARG = "categoryDetailsArg"

        const val CALCULATE_DIALOG_ARG = "calculatorDialogArg"
    }

    object RealmTableName {
        const val ACCOUNT_REALM_TABLE = "account.realm"
        const val CATEGORY_REALM_TABLE = "category.realm"
    }

    object RealmVariableName {
        const val USER_UID_VARIABLE = "userUid"
        const val ACCOUNT_ID_VARIABLE = "accountId"
        const val CATEGORY_ID_VARIABLE = "categoryId"
        const val CATEGORY_TYPE_VARIABLE = "categoryType"

    }

    object RegularExpression {
        const val REX_NAME = "^[a-zA-Z]+(?: [a-zA-Z]+){0,3}\$"
    }

    object ConditionalFigure {
        const val MIN_ACC_NAME = 4
    }

    object Format {
        const val DATE_FORMAT = "yyyy/MM/dd"
        const val MONTH_FORMAT = "MMM"

        const val TIME_12HOURS_FORMAT = "hh:mm:ss a"
        const val TIME_24HOURS_FORMAT = "HH:mm:ss"

        const val DECIMAL_FORMAT = "#.##"
    }

    object Calculator {
        const val MAX_AMOUNT = 999999999.99
        const val MIN_AMOUNT = -999999999.99

        const val ZERO = "0"
        const val ONE = "1"
        const val TWO = "2"
        const val THREE = "3"
        const val FOUR = "4"
        const val FIVE = "5"
        const val SIX = "6"
        const val SEVEN = "7"
        const val EIGHT = "8"
        const val NINE = "9"
    }

}