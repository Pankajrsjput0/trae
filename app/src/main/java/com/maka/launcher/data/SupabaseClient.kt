package com.maka.launcher.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    private const val SUPABASE_URL = "https://lrezxzngdtxzlmrrwjyl.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxyZXp4em5nZHR4emxtcnJ3anlsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzOTc0MTAsImV4cCI6MjA1OTk3MzQxMH0.IQOHNDfmNOPW8n2ZxvuZGTqtCBbuhv7Stf_t9IuN7G4"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }
}