package ua.nure.mossurd.blooddonation.request

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ua.nure.mossurd.blooddonation.dto.DonationEventDto
import ua.nure.mossurd.blooddonation.dto.DonorDataDto
import ua.nure.mossurd.blooddonation.dto.MedicDataDto
import ua.nure.mossurd.blooddonation.dto.LoginResponseDto
import ua.nure.mossurd.blooddonation.dto.NotificationDto
import ua.nure.mossurd.blooddonation.misc.Constants

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    fun loginCall(@Body body: Map<String, String>): Call<LoginResponseDto>

    @GET("/donor/self")
    fun getSelfDonorCall(@Header(Constants.AUTH_HEADER) token: String): Call<DonorDataDto>

    @GET("/medic/self")
    fun getSelfMedicCall(@Header(Constants.AUTH_HEADER) token: String): Call<MedicDataDto>

    @POST("/user/language/{lang}")
    fun updateLanguageCall(@Header(Constants.AUTH_HEADER) token: String, @Path("lang") language: String): Call<Void>

    @POST("/user/pi/agree")
    fun piAgreeCall(@Header(Constants.AUTH_HEADER) token: String): Call<Void>

    @DELETE("/user")
    fun deleteSelf(@Header(Constants.AUTH_HEADER) token: String): Call<Void>

    @GET("/donor/event/available")
    fun getAvailableEvents(@Header(Constants.AUTH_HEADER) token: String): Call<List<DonationEventDto>>

    @POST("/donor/event/{id}/appointment/new")
    fun createEventAppointment(@Header(Constants.AUTH_HEADER) token: String, @Path("id") eventId: Integer): Call<Void>

    @GET("/donor/event/appointment/all")
    fun getMyAppointments(@Header(Constants.AUTH_HEADER) token: String): Call<List<DonationEventDto>>

    @DELETE("/donor/event/{id}/appointment/delete")
    fun deleteEventAppointment(@Header(Constants.AUTH_HEADER) token: String, @Path("id") eventId: Integer): Call<Void>

    @GET("/donor/donations")
    fun getMyDonations(@Header(Constants.AUTH_HEADER) token: String): Call<List<DonationEventDto>>

    @GET("/user/notifications/all")
    fun getAllNotifications(@Header(Constants.AUTH_HEADER) token: String): Call<List<NotificationDto>>

    @GET("/user/notifications/new")
    fun getNewNotifications(@Header(Constants.AUTH_HEADER) token: String): Call<List<NotificationDto>>
}