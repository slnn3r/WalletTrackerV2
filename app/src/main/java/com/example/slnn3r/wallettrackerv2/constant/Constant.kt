package com.example.slnn3r.wallettrackerv2.constant

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

        const val HISTORY_VIEW_LOGGING = "HIS VIEW ERROR"
        const val HISTORY_DIALOG_LOGGING = "HIS DIALOG ERROR"
    }

    object GoogleLoginApi {
        const val REQUEST_ID_TOKEN = "942197312071-ufvdrsq664ovqhhb3tr4mr9uqord5u3m.apps.googleusercontent.com"
        const val REQUEST_CODE = 1
    }

    object ConditionalKeyword {
        const val DEFAULT_STATUS = "Default"
        const val NON_DEFAULT_STATUS = "New"

        // Localize Value
        const val EXPENSE_STATUS = "Expense"
        const val INCOME_STATUS = "Income"

        const val All_YEAR_STATUS = "All Year"
        const val All_MONTH_STATUS = "All Month"
        const val All_DAY_STATUS = "All Day"

        const val All_TYPE_STATUS = "All Type"
        const val ALL_CATEGORY_STATUS = "All Category"

        const val SPECIFIC_DATE = "Specific Date"
        const val DATE_RANGE = "Date Range"
        // Localize Value
    }

    object KeyId {
        const val ACCOUNT_DETAILS_ARG = "accountDetailsArg"
        const val CATEGORY_CREATE_ARG = "categoryCreateArg"
        const val CATEGORY_DETAILS_ARG = "categoryDetailsArg"
        const val TRANSACTION_DETAILS_ARG = "transactionDetailsArg"

        const val CALCULATE_DIALOG_ARG = "calculatorDialogArg"

        const val SHARE_PREF = "sharePreference:"
        const val SELECTED_ACCOUNT_KEY = "selectedAccount"

        const val FILTER_INPUT_SHARE_PREF = "filterInput"
        const val FILTER_INPUT_ACCOUNT = "filterAccount"
        const val FILTER_INPUT_CATTYPE = "filterCatType"
        const val FILTER_INPUT_CATEGORY = "filterCategory"
        const val FILTER_INPUT_REMARK = "filterRemark"
        const val FILTER_INPUT_DATEOPTION = "filterDateOption"
        const val FILTER_INPUT_DAY = "filterDay"
        const val FILTER_INPUT_MONTH = "filterMonth"
        const val FILTER_INPUT_YEAR = "filterYear"
        const val FILTER_INPUT_STARTDATE = "filterStartDate"
        const val FILTER_INPUT_ENDDATE = "filterEndDate"
    }

    object NavigationKey {
        const val NAV_DRAWER = "NavDrawer"
        const val NAV_MENU = "MenuNavGraph"
        const val NAV_DISABLE = "NavDisable"
    }

    object RealmTableName {
        const val ACCOUNT_REALM_TABLE = "account.realm"
        const val CATEGORY_REALM_TABLE = "category.realm"
        const val TRANSACTION_REALM_TABLE = "transaction.realm"
        const val PREVIOUSREMARK_REALM_TABLE = "previousremark.realm"
    }

    object RealmVariableName {
        const val USER_UID_VARIABLE = "userUid"
        const val ACCOUNT_ID_VARIABLE = "accountId"
        const val CATEGORY_ID_VARIABLE = "categoryId"
        const val CATEGORY_TYPE_VARIABLE = "categoryType"
        const val TRANSACTION_DATETIME_VARIABLE = "transactionDateTime"
        const val TRANSACTION_ID_VARIABLE = "transactionId"
        const val TRANSACTION_REMARK_VARIABLE = "transactionRemark"
    }

    object RegularExpression {
        const val REX_NAME = "^[a-zA-Z]+(?: [a-zA-Z]+){0,3}\$"
    }

    object ConditionalFigure {
        const val MIN_ACCOUNT_NAME = 4
        const val MAX_RECENT_TRANSACTION_LIST = 10
    }

    object DefaultValue {
        // Localize Value
        const val DEFAULT_ACCOUNT_DESC = "Initial Default Account"
        const val DEFAULT_ACCOUNT_NAME = "Personal"

        const val CATEGORY_DATA_DEFAULT1 = "Meals"
        const val CATEGORY_DATA_DEFAULT2 = "Food and Drink"
        const val CATEGORY_DATA_DEFAULT3 = "Transport"
        const val CATEGORY_DATA_DEFAULT4 = "Bills"
        const val CATEGORY_DATA_DEFAULT5 = "Other"
        const val CATEGORY_DATA_DEFAULT6 = "Salary"
        const val CATEGORY_DATA_DEFAULT7 = "Initial Balance"

        const val JAN = "January"
        const val FEB = "February"
        const val MAR = "March"
        const val APR = "April"
        const val MAY = "May"
        const val JUN = "June"
        const val JUL = "July"
        const val AUG = "August"
        const val SEP = "September"
        const val OCT = "October"
        const val NOV = "November"
        const val DEC = "December"
        // Localize Value
    }

    object Format {
        const val DATE_FORMAT = "yyyy/MM/dd"
        const val MONTH_FORMAT = "MMM"
        const val DAY_FORMAT = "EEE"

        const val TIME_12HOURS_FORMAT = "hh:mm:ss a"
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