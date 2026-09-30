package com.matico.dz

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

val supabase = createSupabaseClient(
    supabaseUrl = "https://xayhmfsisipklxrubrez.supabase.co",
    supabaseKey = "sb_publishable_mBGddikkhXWcLAFTB8OYyQ_eRWPfNDo"
) {
    install(Auth) {
        // La session est conservée automatiquement
        // et restaurée au prochain lancement.
    }

    install(Postgrest)

    install(Storage)
}