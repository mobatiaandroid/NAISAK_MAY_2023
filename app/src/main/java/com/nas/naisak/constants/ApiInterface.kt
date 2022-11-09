package com.nas.naisak.constants

import com.nas.naisak.activity.cca.model.*
import com.nas.naisak.activity.communication.model.SocialMediaResponse
import com.nas.naisak.activity.gallery.model.GetPhotoRequestModel
import com.nas.naisak.activity.gallery.model.GetPhotoResponseModel
import com.nas.naisak.activity.login.model.LoginResponse
import com.nas.naisak.activity.notification.model.MessageDetailResponse
import com.nas.naisak.activity.notification.model.NotificationDetailApiModel
import com.nas.naisak.activity.parents_meeting.model.PostSlotRequestModel
import com.nas.naisak.activity.parents_meeting.model.PostSlotResponseModel
import com.nas.naisak.activity.parents_meeting.model.ReviewAppointmentsResponseModel
import com.nas.naisak.activity.payment.payhere.model.*
import com.nas.naisak.commonmodels.*
import com.nas.naisak.fragment.aboutus.model.NAEResponseModel
import com.nas.naisak.fragment.calendar.model.CalendarResponseModel
import com.nas.naisak.fragment.calendar.model.TermCalendarResponseModel
import com.nas.naisak.fragment.cca.model.BannerResponseModel
import com.nas.naisak.fragment.communications.model.CommunicationResponseModel
import com.nas.naisak.fragment.contactus.model.Contactusresponse
import com.nas.naisak.fragment.gallery.model.GetAlbumsRequestModel
import com.nas.naisak.fragment.gallery.model.GetAlbumsResponseModel
import com.nas.naisak.fragment.gallerynew.model.GetAlbumResponseModelnew
import com.nas.naisak.fragment.gallerynew.model.GetAlbumsRequestModelNew
import com.nas.naisak.fragment.home.model.Bannerresponse
import com.nas.naisak.fragment.home.model.DeviceRegistrationApiModel
import com.nas.naisak.fragment.home.model.HomeBadgeResponse
import com.nas.naisak.fragment.home.model.LogoutResponseModel
import com.nas.naisak.fragment.notification.model.NotificationResponseModel
import com.nas.naisak.fragment.parents_meeting.model.ConfirmApi
import com.nas.naisak.fragment.parents_meeting.model.PTAConfirmResponseModel
import com.nas.naisak.fragment.parentsessentials.Model.ParentsEssentialResponseModel
import com.nas.naisak.fragment.payment.model.PaymentBannerResponse
import com.nas.naisak.fragment.payment.model.PaymentResponseModel
import com.nas.naisak.fragment.settings.model.ChangePasswordApiModel
import com.nas.naisak.fragment.settings.model.TermsOfServiceModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    /*************FORGOT PASSWORD****************/
    @POST("api/v1/parent/auth/forgotpassword")
    @FormUrlEncoded
    fun forgotpassword(
        @Field("email") email: String
    ): Call<ResponseBody>

    /*************DEVICE REGISTRATION****************/
    @POST("api/v1/parent/auth/deviceregistration")
    @Headers("Content-Type: application/json")
    fun deviceRegistration(
        @Header("Authorization") token: String,
        @Body deviceReg: DeviceRegistrationApiModel
    ): Call<ResponseBody>

    /*************SIGNUP****************/
    @POST("api/v1/parent/auth/register")
    @FormUrlEncoded
    fun registeruser(
        @Field("email") email: String,
        @Field("devicetype") devicetype: String
    ): Call<ResponseBody>

    /*************LOGIN****************/
    @POST("api/v1/parent/auth/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("devicetype") devicetype: Int,
        @Field("deviceid") fcmid: String,
        @Field("device_identifier") deviceid: String
    ): Call<LoginResponse>

    /*************Notification List****************/
    @POST("api/v1/parent/notifications")
    @Headers("Content-Type: application/json")
    fun notificationList(
        @Body notificationList: ModelWithPageNumberOnly,
        @Header("Authorization") token: String
    ): Call<NotificationResponseModel>

    /*************NOTIFICATION DETAIL****************/
    @POST("api/v1/parent/notification/detail")
    @Headers("Content-Type: application/json")
    fun notificationdetail(
        @Body detaiApi: NotificationDetailApiModel,
        @Header("Authorization") token: String
    ): Call<MessageDetailResponse>

    /*************CONTACT US****************/
    @GET("api/v1/parent/contact_us")
    fun contact_us(): Call<Contactusresponse>

    /*************PAYMENT BANNER****************/
    @GET("api/v1/parent/payment/banner")
    @Headers("Content-Type: application/json")
    fun paymentBanner(
        @Header("Authorization") token: String
    ): Call<PaymentBannerResponse>

    /*************PAYMENT List****************/
    @POST("api/v1/parent/payment_categories")
    @Headers("Content-Type: application/json")
    fun paymentlist(
        @Body reportListModel: PaymentApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentResponseModel>

    /*************PAYMENT DETAIL****************/
    @POST("api/v1/parent/payment_category/detail")
    @Headers("Content-Type: application/json")
    fun paymentdetail(
        @Body paymentid: PaymentDetailApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentDetailResponseModel>

    /*************PAYMENT GATEWAY****************/
    @POST("api/v1/payment/getpaymentlink")
    @Headers("Content-Type: application/json")
    fun paymentGateway(
        @Body paymentid: PaymentGatewayApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentGatewayResponseModel>

    /*************PAYMENT CREDIT INITIATE****************/
    @POST("api/v1/payment/init-cc-payment")
    @Headers("Content-Type: application/json")
    fun paymentCreditInitiate(
        @Body paymentid: PaymentGatewayApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentGatewayCreditInitiateResponseModel>


    /*************PAYMENT DEBIT INITIATE****************/
    @POST("api/v1/payment/init-dc-payment")
    @Headers("Content-Type: application/json")
    fun paymentDebitInitiate(
        @Body paymentid: PaymentGatewayApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentGatewayCreditInitiateResponseModel>


    /*************PAYMENT GATEWAY****************/
    @POST("api/v1/parent/payment_submission")
    @Headers("Content-Type: application/json")
    fun paymentSubmission(
        @Body paymentid: PaymentSubmissionApiModel,
        @Header("Authorization") token: String
    ): Call<PaymentSubmissionResponseModel>

    /************PAYMENT INFORMATION****************/
    @POST("api/v1/parent/payment_informations")
    @Headers("Content-Type: application/json")
    fun paymentInformation(
        @Body notificationList: ModelWithPageNumberOnly,
        @Header("Authorization") token: String
    ): Call<CommonDetailResponse>

    /*************STUDENT_LIST****************/
    @GET("api/v1/parent/studentlist")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun studentList(
        @Header("Authorization") token: String
    ): Call<StudentListModel>


    /*************NAE  DETAILS****************/
    @GET("api/v1/parent/nordangliaeducation/get/nordangliaeducation")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun naeDetails(
        @Header("Authorization") token: String
    ): Call<NAEResponseModel>

    /*************NAE  DETAILS****************/
    @GET("api/v1/parent/term_calendar")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun termCalendar(
        @Header("Authorization") token: String
    ): Call<TermCalendarResponseModel>

    /*************NAE  DETAILS****************/
    @GET("api/v1/parent/parentessential/get/parent_essentials")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun parentessentials(
        @Header("Authorization") token: String
    ): Call<ParentsEssentialResponseModel>

    /*************COMMUNICATION****************/
    @GET("api/v1/parent/communication/get/communications")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun communication(
        @Header("Authorization") token: String
    ): Call<CommunicationResponseModel>

    /*************COMMUNICATION****************/
    @GET("api/v1/parent/communication/get/socialmedia")
    @Headers("Content-Type: application/x-www-form-urlencode", "Accept: application/json")
    fun socialmedia(
        @Header("Authorization") token: String
    ): Call<SocialMediaResponse>

    /************PAYMENT INFORMATION****************/
    @POST("api/v1/parent/auth/logout")
    @Headers("Content-Type: application/json")
    fun logout(
        @Header("Authorization") token: String
    ): Call<LogoutResponseModel>

    /*************CHANGE PASSWORD****************/
    @POST("api/v1/parent/auth/changepassword")
    @Headers("Content-Type: application/json")
    fun changePassword(
        @Body changePassword: ChangePasswordApiModel,
        @Header("Authorization") token: String
    ): Call<LogoutResponseModel>

    /*************TERMS OF SERVICES****************/
    @GET("api/v1/parent/terms_of_services")
    fun termsofservice(): Call<TermsOfServiceModel>

    /*************HOME BANNER****************/

    @GET("api/v1/parent/home_banner_images")
    fun bannerimages(): Call<Bannerresponse>


    /*************HOME BADGE****************/
    @GET("api/v1/parent/badge_counts")
    @Headers("Content-Type: application/json")
    fun homebadge(
        @Header("Authorization") token: String
    ): Call<HomeBadgeResponse>

    /*************CALENDAR****************/
    @GET("api/v1/parent/calendar")
    @Headers("Content-Type: application/json")
    fun calendar(
        @Header("Authorization") token: String
    ): Call<CalendarResponseModel>

    /*************CHANGE PASSWORD****************/
    @POST("api/v1/payment/generate_receipt")
    @Headers("Content-Type: application/json")
    fun generateReceipt(
        @Body receipt: PaymentReceiptApiModel,
        @Header("Authorization") token: String
    ): Call<GeneretReceiptResponseModel>

    /*SEND EMAIL TO STAFF*/
    @POST("api/v1/parent/send_email")
    @Headers("Content-Type: application/json")
    fun sendStaffMail(
        @Body sendMail: SendStaffMailApiModel,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    /*GET STAFF BY STUDENT*/
    @POST("api/v1/parent/stafflist")
    @Headers("Content-Type: application/json")
    fun getStaffByStudent(
        @Body body: GetStaffByStudentApiModel,
        @Header("Authorization") token: String
    ): Call<StaffListByStudentResponseModel>

    /*GET STUDENT*/
    @GET("api/v1/parent/studentlist")
    @Headers("Content-Type: application/json")
    fun getStudent(
        @Header("Authorization") token: String
    ): Call<StudentListReponseModel>

    /*GET PTA ALLOTED DATES*/
    @POST("api/v1/parent/pta_allotted_dates")
    @Headers("Content-Type: application/json")
    fun getPtaAllottedDates(
        @Body body: AllottedDatesRequestModel,
        @Header("Authorization") token: String
    ): Call<AllottedDatesResponseModel>

    /*GET PTA TIME SLOTS*/
    @POST("api/v1/parent/listingpta")
    @Headers("Content-Type: application/json")
    fun getPtaTimeSlots(
        @Body body: TimeSlotsRequestModel,
        @Header("Authorization") token: String
    ): Call<TimeSlotsResponseModel>

    /*GET PTA TIME SLOTS*/
    @GET("api/v1/parent/pta_reviewlist")
    @Headers("Content-Type: application/json")
    fun reviewPta(
        @Header("Authorization") token: String
    ): Call<ReviewAppointmentsResponseModel>

    /*GET ALBUMS*/
    @POST("api/v1/album/get_albums")
    @Headers("Content-Type: application/json")
    fun getAlbums(
        @Body body: GetAlbumsRequestModel,
        @Header("Authorization") token: String
    ): Call<GetAlbumsResponseModel>

    /*GET PHOTOS*/
    @POST("api/v1/album/view_albums")
    @Headers("Content-Type: application/json")
    fun getPhotos(
        @Body body: GetPhotoRequestModel,
        @Header("Authorization") token: String
    ): Call<GetPhotoResponseModel>

    /*POST SELECTED SLOT*/
    @POST("api/v1/parent/insertpta")
    @Headers("Content-Type: application/json")
    fun postSlot(
        @Body body: PostSlotRequestModel,
        @Header("Authorization") token: String
    ): Call<PostSlotResponseModel>

//    /*CONFIRM SLOT*/
//    @POST("api/v1/parent/pta_confirmation")
//    @Headers("Content-Type: application/json")
//    fun confirmSlot(
//        @Body body: ConfirmSlotRequestModel,
//        @Header("Authorization") token:String
//    ): Call<ConfirmSlotResponseModel>

    /* CCA LIST */
    @GET("api/v1/parent/cca/banner")
    @Headers("Content-Type: application/json")
    fun getBanner(
        @Header("Authorization") token: String
    ): Call<BannerResponseModel>

    /* EXTERNAL PROVIDERS*/
    @POST("api/v1/parent/external_providers")
    @Headers("Content-Type: application/json")
    fun getExternalProviders(
        @Body body: ExternalProvidersRequestModel,
        @Header("Authorization") token: String
    ): Call<ExternalProvidersResponseModel>

    /* CCA INFO*/
    @POST("api/v1/parent/cca_informations")
    @Headers("Content-Type: application/json")
    fun getCCAInfo(
        @Body body: CCAInfoRequestModel,
        @Header("Authorization") token: String
    )
            : Call<CCAInfoResponseModel>

    /* CCA INFO*/
    @POST("api/v1/parent/cca_details")
    @Headers("Content-Type: application/json")
    fun getCCAList(
        @Body body: CCAListRequestModel,
        @Header("Authorization") token: String
    ): Call<CCAListResponseModel>

    /*GET ALBUMS APARNA*/
    @POST("api/v1/album/get_albums")
    @Headers("Content-Type: application/json")
    fun getAlbumsNew(
        @Body body: GetAlbumsRequestModelNew,
        @Header("Authorization") token: String
    ): Call<GetAlbumResponseModelnew>

    /*POST SELECTED SLOT*/
    @POST("api/v1/parent/pta_confirmation")
    @Headers("Content-Type: application/json")
    fun confirmSlot(
        @Body body: ConfirmApi,
        @Header("Authorization") token: String
    ): Call<PTAConfirmResponseModel>


    /*CCA SUBMIT*/
    @POST("api/v1/parent/cca_submit")
    @Headers("Content-Type: application/json")
    fun ccaSubmit(
        @Body body: CCASumbitRequestModel,
        @Header("Authorization") token: String
    ): Call<CCASubmitResponseModel>

    /*CCA REVIEW*/
    @POST("api/v1/parent/cca_reviews")
    @Headers("Content-Type: application/json")
    fun ccaReview(
        @Body body: CCAReviewRequestModel,
        @Header("Authorization") token: String
    ): Call<CCAReviewResponseModel>

    /*CCA CANCEL*/
    @POST("api/v1/parent/cca_selection_cancel")
    @Headers("Content-Type: application/json")
    fun ccaCancel(
        @Body body: CCACancelRequestModel,
        @Header("Authorization") token: String
    ): Call<CCACancelResponseModel>

}