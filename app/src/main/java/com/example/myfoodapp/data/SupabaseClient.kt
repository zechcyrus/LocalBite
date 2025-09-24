package com.example.myfoodapp.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.ktor.client.engine.android.Android

object SupabaseClientProvider {
    const val SUPABASE_URL: String = "https://agarrgtttqzvurwirykz.supabase.co"
    const val SUPABASE_ANON_KEY: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFnYXJyZ3R0dHF6dnVyd2lyeWt6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg2MjI0ODIsImV4cCI6MjA3NDE5ODQ4Mn0._4NIaI0PlGqUsofANtSKDNSyaqE0b42kcC74HSeCP8Y"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            httpEngine = Android.create()
            install(GoTrue)
        }
    }
}