package com.matico.dz

import io.github.jan.supabase.postgrest.from

class MaticoRepository {

    suspend fun getMaticos(): List<Matico> {
        return supabase
            .from("maticos")
            .select(
                columns = io.github.jan.supabase.postgrest.query.Columns.list(
                    "id",
                    "name",
                    "location",
                    "price",
                    "is_active",
                    "owner_id",
                    "image_url"
                )
            )
            .decodeList<Matico>()
    }

    suspend fun deleteMatico(
        maticoId: Long,
        ownerId: String
    ) {
        supabase
            .from("maticos")
            .delete {
                filter {
                    eq("id", maticoId)
                    eq("owner_id", ownerId)
                }
            }
    }
}