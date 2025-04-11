package com.koru.capital.core.ui.navigation

object Routes {
    const val INITIAL_LOADING = "initial_loading"

    const val HOME = "home_root"
    const val MY_BUSINESSES = "my_businesses_root"
    const val MY_ACCOUNT = "my_account_root"


    const val HOME_SCREEN = "home"


    const val MY_BUSINESSES_SCREEN = "my_businesses"
    const val EDIT_BUSINESS_SCREEN = "edit_business/{businessId}"

    const val MY_ACCOUNT_SCREEN = "my_account"
    const val EDIT_PROFILE_SCREEN = "edit_profile"
    const val SETTINGS_SCREEN = "settings"


    const val LOGIN_SCREEN = "login"

    const val REGISTER_EMAIL_PASSWORD_SCREEN = "register_email_password"
    const val REGISTER_PERSONAL_INFO_SCREEN = "register_personal_info"

    const val BUSINESS_DETAIL_SCREEN = "business_detail/{businessId}"
    const val ADD_BUSINESS_SCREEN = "add_business"


    fun businessDetail(businessId: String) = "business_detail/$businessId"

    fun editBusiness(businessId: String) = "edit_business/$businessId"
}