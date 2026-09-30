package com.matico.dz

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put



@Serializable
data class ReservedTimeSlot(
    @SerialName("time_slot")
    val timeSlot: String
)
class ReservationRepository {


    suspend fun createReservation(
        reservation: ReservationInsert
    ): Reservation {
        println("=== DEBUT CREATE RESERVATION ===")
        println("RESERVATION = $reservation")
        return supabase
            .from("reservations")
            .insert(reservation) {
                select()
            }
            .decodeSingle<Reservation>()
    }

    suspend fun confirmReservation(
        reservationId: Long
    ) {
        supabase
            .postgrest
            .rpc(
                function = "confirm_reservation",
                parameters = buildJsonObject {
                    put("p_reservation_id", reservationId)
                }
            )
    }

    suspend fun hideReservationForClient(
        reservationId: Long
    ) {
        supabase
            .from("reservations")
            .update(
                {
                    set("client_deleted", true)
                }
            ) {
                filter {
                    eq("id", reservationId)
                }
            }
    }
    suspend fun hideReservationForOwner(
        reservationId: Long
    ) {
        supabase
            .from("reservations")
            .update(
                {
                    set("owner_deleted", true)
                }
            ) {
                filter {
                    eq("id", reservationId)
                }
            }
    }

    suspend fun cancelReservation(
        reservationId: Long
    ) {
        supabase
            .from("reservations")
            .update(
                {
                    set("reservation_status", "cancelled")
                }
            ) {
                filter {
                    eq("id", reservationId)
                }
            }
    }

    suspend fun getMyReservations(
        userId: String
    ): List<Reservation> {
        return supabase
            .from("reservations")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<Reservation>()
    }

    suspend fun getReservationById(
        reservationId: Long
    ): Reservation {

        return supabase
            .from("reservations")
            .select {
                filter {
                    eq("id", reservationId)
                }
            }
            .decodeSingle<Reservation>()
    }

    suspend fun getReservedTimeSlots(
        maticoId: Long,
        reservationDate: String
    ): List<String> {


        return supabase
            .postgrest
            .rpc(
                function = "get_reserved_time_slots",
                parameters = buildJsonObject {
                    put("p_matico_id", maticoId)
                    put("p_reservation_date", reservationDate)
                }
            )

            .decodeList<ReservedTimeSlot>()
            .map { it.timeSlot }
    }

}