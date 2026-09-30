package com.matico.dz

import androidx.compose.material3.CircularProgressIndicator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.github.jan.supabase.postgrest.from
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.keyframes
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.postgrest.query.Columns
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SelectableDates
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.matico.dz.ui.theme.MaticoDZTheme
import io.github.jan.supabase.postgrest.postgrest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.Image

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale


import androidx.compose.material.icons.filled.AddAPhoto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope

import io.github.jan.supabase.storage.storage
import coil.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material.icons.filled.Delete


class MainActivity : ComponentActivity() {

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val soundUri = Uri.parse(
                "android.resource://$packageName/${R.raw.son_notf}"
            )

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val channel = NotificationChannel(
                "reservations_v2",
                "Réservations",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.setSound(
                soundUri,
                audioAttributes
            )

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val reservationIdFromNotification =
            intent.getStringExtra("reservation_id")

        Log.d(
            "FCM",
            "RESERVATION ID NOTIFICATION = $reservationIdFromNotification"
        )
        createNotificationChannel()
        enableEdgeToEdge()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "Erreur récupération token", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FCM", "TOKEN = $token")
            }

        if (
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        setContent {
            MaticoDZTheme {

                var isConnected by remember {
                    mutableStateOf(
                        isInternetAvailable(this@MainActivity)
                    )
                }

                LaunchedEffect(Unit) {
                    while (true) {

                        isConnected =
                            isInternetAvailable(this@MainActivity)

                        delay(2_000)
                    }
                }

                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Rtl
                ) {
                    if (isConnected) {

                        MaticoDZApp()

                    } else {

                        NoInternetScreen(
                            onRetry = {
                                isConnected =
                                    isInternetAvailable(
                                        this@MainActivity
                                    )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MaticoDZApp() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedPage by remember {
        mutableStateOf(0)
    }

    var selectedMaticoForEdit by remember {
        mutableStateOf<Matico?>(null)
    }

    var showAddMaticoScreen by remember {
        mutableStateOf(false)
    }

    var ownerDashboardRefreshKey by remember {
        mutableStateOf(0)
    }

    var ownerDashboardSection by remember {
        mutableStateOf("home")
    }

    var selectedMatico by remember {
        mutableStateOf<Matico?>(null)
    }

    var selectedOwnerReservation by remember {
        mutableStateOf<Reservation?>(null)
    }

    var selectedOwnerClient by remember {
        mutableStateOf<OwnerReservationClient?>(null)
    }

    var selectedOwnerMatico by remember {
        mutableStateOf<Matico?>(null)
    }

    var ownerActionMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showLoginScreen by remember {
        mutableStateOf(false)
    }

    var showRegisterScreen by remember {
        mutableStateOf(false)
    }

    var isLoggedIn by remember {
        mutableStateOf(false)
    }

    var userRole by remember {
        mutableStateOf<String?>(null)
    }
    var isLoadingUserRole by remember {
        mutableStateOf(true)
    }

    var notificationReservationId by remember {
        mutableStateOf<Long?>(null)
    }

    var lastBackPressTime by remember {
        mutableStateOf(0L)
    }

    BackHandler {

        when {

            // 1. Écran de modification d'un matico
            selectedMaticoForEdit != null -> {
                selectedMaticoForEdit = null
            }

            // 2. Écran d'ajout d'un matico
            showAddMaticoScreen -> {
                showAddMaticoScreen = false
            }

            // 3. Inscription
            showRegisterScreen -> {
                showRegisterScreen = false
                showLoginScreen = true
            }

            // 4. Connexion
            showLoginScreen -> {
                showLoginScreen = false
            }

            // 5. Détails d'un matico côté client
            selectedMatico != null -> {
                selectedMatico = null
            }

            // 6. Détails d'une réservation côté propriétaire
            selectedOwnerReservation != null -> {
                selectedOwnerReservation = null
                selectedOwnerClient = null
                selectedOwnerMatico = null
            }

            // 7. PROPRIÉTAIRE
            userRole == "proprietaire" -> {

                if (ownerDashboardSection != "home") {

                    // Depuis statistiques / maticos / réservations
                    // → retour à l'accueil du OwnerDashboard
                    ownerDashboardSection = "home"

                } else {

                    // Déjà sur l'accueil du OwnerDashboard
                    // → double pression pour quitter

                    val currentTime = System.currentTimeMillis()

                    if (currentTime - lastBackPressTime < 2000L) {

                        (context as? MainActivity)?.finish()

                    } else {

                        lastBackPressTime = currentTime

                        Toast.makeText(
                            context,
                            "اضغط مرة أخرى للخروج من التطبيق",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            // 8. Client sur une autre page
            selectedPage != 0 -> {
                selectedPage = 0
            }

            // 9. Client déjà sur l'accueil
            else -> {

                val currentTime = System.currentTimeMillis()

                if (currentTime - lastBackPressTime < 2000L) {

                    (context as? MainActivity)?.finish()

                } else {

                    lastBackPressTime = currentTime

                    Toast.makeText(
                        context,
                        "اضغط مرة أخرى للخروج de l'application",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {

        try {

            // Attendre que Supabase termine de restaurer
            // la session sauvegardée
            supabase.auth.awaitInitialization()

            val user = supabase.auth.currentUserOrNull()

            println(
                "TEST USER APRES INITIALISATION = ${user?.id}"
            )

            if (user != null) {

                try {

                    val profile = supabase
                        .from("profiles")
                        .select()
                        .decodeSingle<Profile>()

                    userRole = profile.role
                    isLoggedIn = true

                    if (profile.role == "proprietaire") {
                        selectedPage = 4
                    }

                    println(
                        "ROLE UTILISATEUR = ${profile.role}"
                    )

                } catch (e: Exception) {

                    println(
                        "ERREUR CHARGEMENT ROLE = ${
                            e.stackTraceToString()
                        }"
                    )

                    isLoggedIn = false
                    userRole = null

                } finally {

                    isLoadingUserRole = false
                }

            } else {

                isLoggedIn = false
                userRole = null
                isLoadingUserRole = false

                println(
                    "AUCUNE SESSION SUPABASE"
                )
            }

        } catch (e: Exception) {

            println(
                "ERREUR INITIALISATION SUPABASE = ${
                    e.stackTraceToString()
                }"
            )

            isLoggedIn = false
            userRole = null
            isLoadingUserRole = false
        }
    }

    LaunchedEffect(Unit) {

        val reservationId =
            (context as? MainActivity)
                ?.intent
                ?.getStringExtra("reservation_id")
                ?.toLongOrNull()

        if (reservationId != null) {

            notificationReservationId = reservationId

            println(
                "FCM NAVIGATION RESERVATION ID = $reservationId"
            )

            try {

                val currentUser =
                    supabase.auth.currentUserOrNull()

                val userRole =
                    if (currentUser != null) {
                        supabase
                            .from("profiles")
                            .select()
                            .decodeList<Profile>()
                            .firstOrNull {
                                it.id == currentUser.id
                            }
                            ?.role
                    } else {
                        null
                    }

                println(
                    "FCM ROLE UTILISATEUR = $userRole"
                )

                val reservation =
                    ReservationRepository()
                        .getReservationById(reservationId)

                val client =
                    supabase
                        .from("profiles")
                        .select()
                        .decodeList<Profile>()
                        .firstOrNull {
                            it.id == reservation.userId
                        }

                val matico =
                    supabase
                        .from("maticos")
                        .select()
                        .decodeList<Matico>()
                        .firstOrNull {
                            it.id == reservation.maticoId
                        }

                selectedOwnerReservation = reservation

                selectedOwnerClient =
                    client?.let {
                        OwnerReservationClient(
                            reservationId = reservation.id,
                            fullName = it.fullName,
                            phone = it.phone
                        )
                    }

                selectedOwnerMatico = matico

                if (userRole == "proprietaire") {

                    selectedPage = 4
                    ownerDashboardSection = "reservations"

                } else if (userRole == "client") {

                    selectedPage = 2
                }

                println(
                    "FCM RESERVATION CHARGEE = ${reservation.id}"
                )

            } catch (e: Exception) {

                println(
                    "FCM ERREUR NAVIGATION = ${
                        e.stackTraceToString()
                    }"
                )
            }
        }
    }

    suspend fun loadUserProfile(): String? {
        return try {
            val profile = supabase
                .from("profiles")
                .select()
                .decodeSingle<Profile>()

            userRole = profile.role

            println("ROLE APRES CONNEXION = ${profile.role}")

            profile.role

        } catch (e: Exception) {

            println(
                "ERREUR ROLE APRES CONNEXION = ${
                    e.stackTraceToString()
                }"
            )

            userRole = null
            null
        }
    }

    if (ownerActionMessage != null) {

        AlertDialog(
            onDismissRequest = {
                ownerActionMessage = null
            },
            title = {
                Text(
                    text = "تم بنجاح",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = ownerActionMessage!!,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        ownerActionMessage = null
                    }
                ) {
                    Text("حسناً")
                }
            }
        )
    }
    if (isLoadingUserRole) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            if (
                !isLoadingUserRole &&
                userRole != "proprietaire" &&
                selectedMatico == null &&
                !showLoginScreen &&
                !showRegisterScreen
            ) {
                MaticoBottomBar(
                    selectedPage = selectedPage,
                    onPageSelected = { selectedPage = it },
                    userRole = userRole
                )
            }
        },

        contentWindowInsets = if (userRole == "proprietaire") {
            WindowInsets(0)
        } else {
            ScaffoldDefaults.contentWindowInsets
        }
    ) { innerPadding ->

        when {

            selectedMaticoForEdit != null -> {

                EditMaticoScreen(
                    matico = selectedMaticoForEdit!!,
                    modifier = Modifier.padding(innerPadding),

                    onBack = {
                        selectedMaticoForEdit = null
                    },

                    onMaticoUpdated = {
                        selectedMaticoForEdit = null
                        ownerDashboardRefreshKey++
                    }
                )
            }


            showAddMaticoScreen -> {

                AddMaticoScreen(
                    modifier = Modifier.padding(innerPadding),

                    onBack = {
                        showAddMaticoScreen = false
                    },

                    onMaticoAdded = {
                        showAddMaticoScreen = false
                        ownerDashboardRefreshKey++
                    }
                )
            }


            showRegisterScreen -> {

                RegisterScreen(
                    onRegistered = {
                        showRegisterScreen = false
                        showLoginScreen = false
                    },
                    onBackToLogin = {
                        showRegisterScreen = false
                        showLoginScreen = true
                    }
                )
            }

            showLoginScreen -> {

                LoginScreen(
                    onLoggedIn = {
                        kotlinx.coroutines.CoroutineScope(
                            kotlinx.coroutines.Dispatchers.Main
                        ).launch {

                            val role = loadUserProfile()

                            isLoggedIn = role != null
                            showLoginScreen = false

                            if (role == "proprietaire") {
                                selectedPage = 4
                            }
                        }
                    },
                    onCreateAccount = {
                        showLoginScreen = false
                        showRegisterScreen = true
                    }
                )
            }

            selectedMatico != null -> {

                MaticoDetailsScreen(
                    matico = selectedMatico!!,
                    modifier = Modifier.padding(innerPadding),
                    onBack = {
                        selectedMatico = null
                    },
                    onLoginRequired = {
                        selectedMatico = null
                        selectedPage = 3
                        showLoginScreen = true
                    }
                )
            }

            else -> {

                when (selectedPage) {

                    0 -> HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

                    1 -> FieldsScreen(
                        modifier = Modifier.padding(innerPadding),
                        onMaticoSelected = { matico ->
                            selectedMatico = matico
                        }
                    )

                    2 -> {

                        if (isLoggedIn) {

                            ReservationsScreen(
                                modifier = Modifier.padding(innerPadding)
                            )

                        } else {

                            LoginScreen(
                                onLoggedIn = {

                                    kotlinx.coroutines.CoroutineScope(
                                        kotlinx.coroutines.Dispatchers.Main
                                    ).launch {

                                        val role = loadUserProfile()

                                        isLoggedIn = role != null
                                        showLoginScreen = false

                                        if (role == "proprietaire") {
                                            selectedPage = 4
                                        }
                                    }
                                },
                                onCreateAccount = {
                                    showLoginScreen = false
                                    showRegisterScreen = true
                                }
                            )
                        }
                    }


                    3 -> AccountScreen(
                        modifier = Modifier.padding(innerPadding),
                        isLoggedIn = isLoggedIn,
                        userRole = userRole,
                        onLogin = {
                            showLoginScreen = true
                        },
                        onRegister = {
                            showRegisterScreen = true
                        },
                        onLogout = {
                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.Main
                            ).launch {
                                try {
                                    supabase.auth.signOut()
                                    isLoggedIn = false
                                    userRole = null
                                } catch (e: Exception) {
                                    println("ERREUR DECONNEXION = ${e.stackTraceToString()}")
                                }
                            }
                        },
                        onOwnerDashboard = {
                            selectedPage = 4
                        }
                    )

                    4 -> {
                        if (selectedOwnerReservation != null) {

                            OwnerReservationDetailsScreen(
                                reservation = selectedOwnerReservation!!,
                                client = selectedOwnerClient,
                                matico = selectedOwnerMatico,
                                modifier = Modifier.padding(innerPadding),

                                onBack = {
                                    selectedOwnerReservation = null
                                    selectedOwnerClient = null
                                    selectedOwnerMatico = null
                                },

                                onConfirm = {

                                    kotlinx.coroutines.CoroutineScope(
                                        kotlinx.coroutines.Dispatchers.Main
                                    ).launch {

                                        try {

                                            ReservationRepository()
                                                .confirmReservation(
                                                    selectedOwnerReservation!!.id
                                                )

                                            selectedOwnerReservation =
                                                selectedOwnerReservation!!.copy(
                                                    paymentStatus = "paid",
                                                    reservationStatus = "confirmed",
                                                    validatedAt =
                                                        java.time.Instant.now().toString()
                                                )
                                            ownerActionMessage = "تم تأكيد الحجز بنجاح"
                                            selectedOwnerReservation = null
                                            selectedOwnerClient = null
                                            selectedOwnerMatico = null

                                        } catch (e: Exception) {

                                            println(
                                                "ERREUR CONFIRMATION RESERVATION = ${
                                                    e.stackTraceToString()
                                                }"
                                            )
                                        }
                                    }
                                },
                                onCancel = {

                                    kotlinx.coroutines.CoroutineScope(
                                        kotlinx.coroutines.Dispatchers.Main
                                    ).launch {

                                        try {

                                            val wasConfirmed =
                                                selectedOwnerReservation!!.reservationStatus == "confirmed"

                                            ReservationRepository()
                                                .cancelReservation(
                                                    selectedOwnerReservation!!.id
                                                )

                                            selectedOwnerReservation =
                                                selectedOwnerReservation!!.copy(
                                                    reservationStatus = "cancelled"
                                                )

                                            ownerActionMessage =
                                                if (wasConfirmed) {
                                                    "تم إلغاء الحجز"
                                                } else {
                                                    "تم رفض الحجز"
                                                }

                                            selectedOwnerReservation = null
                                            selectedOwnerClient = null
                                            selectedOwnerMatico = null

                                        } catch (e: Exception) {

                                            println(
                                                "ERREUR ANNULATION RESERVATION = ${
                                                    e.stackTraceToString()
                                                }"
                                            )
                                        }
                                    }
                                }
                            )

                        } else {


                            OwnerDashboardScreen(
                                modifier = Modifier.padding(innerPadding),

                                refreshKey = ownerDashboardRefreshKey,

                                onRefresh = {
                                    ownerDashboardRefreshKey++
                                },

                                onLogout = {
                                    scope.launch {
                                        try {
                                            supabase.auth.signOut()

                                            isLoggedIn = false
                                            userRole = null
                                            selectedPage = 0
                                            ownerDashboardSection = "home"

                                        } catch (e: Exception) {
                                            println(
                                                "ERREUR DECONNEXION = ${
                                                    e.stackTraceToString()
                                                }"
                                            )
                                        }
                                    }
                                },

                                initialSection = ownerDashboardSection,
                                onSectionChanged = {
                                    ownerDashboardSection = it
                                },

                                onReservationSelected = { reservation, client, matico ->
                                    ownerDashboardSection = "reservations"
                                    selectedOwnerReservation = reservation
                                    selectedOwnerClient = client
                                    selectedOwnerMatico = matico
                                },
                                onEditMatico = { matico ->
                                    ownerDashboardSection = "maticos"
                                    selectedMaticoForEdit = matico
                                },

                                onAddMatico = {
                                    ownerDashboardSection = "maticos"
                                    showAddMaticoScreen = true
                                }
                            )


                        }
                    }

                }
            }
        }
    }
}


// ═══════════════════════════════════════
// الصفحة الرئيسية
// ═══════════════════════════════════════


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD2FFE0),
                        Color(0xFFF8FAFC),
                        Color(0xFFD2FFE0)
                    )
                )
            )
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // =========================
            // HEADER
            // =========================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = Color(0xFFE1F5E8)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚽",
                            fontSize = 25.sp
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "ماتيكو DZ",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF16A34A)
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "احجز ملعبك بسهولة",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =========================
            // HERO
            // =========================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF16A34A)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Decorative circles
                    Surface(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.TopStart),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.08f)
                    ) {}

                    Surface(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomEnd),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.08f)
                    ) {}

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "جاهز للعب؟",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Right
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "اكتشف الملاعب القريبة منك\nواحجز وقتك الآن",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.90f),
                            textAlign = TextAlign.Right
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = "⚡ حجز سريع",
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 9.dp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }


                    val infiniteTransition = rememberInfiniteTransition(label = "ballAnimation")

                    val ballOffsetY by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 5500

                                // 1er saut
                                0f at 0
                                -25f at 250
                                0f at 500

                                // 2e saut
                                -25f at 750
                                0f at 1000

                                // 3e saut
                                -25f at 1250
                                0f at 1500

                                // 4e saut
                                -25f at 1750
                                0f at 2000

                                // 5e saut
                                -25f at 2250
                                0f at 2500

                                // Attente de 3 secondes
                                0f at 5500
                            }
                        ),
                        label = "ballJump"
                    )

                    Text(
                        text = "⚽",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                            .offset(y = ballOffsetY.dp),
                        fontSize = 70.sp
                    )

                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =========================
            // PAYMENT NOTICE
            // =========================
            PaymentNotice()

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =========================
            // AVAILABLE FIELDS
            // =========================
            /*Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE1F5E8)
                ) {
                    Text(
                        text = "عرض الكل",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF15803D),
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "الماتيكوات المتاحة",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Right
                )
            }
*/
            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =========================
            // EMPTY MATIKOS
            // =========================
            /*EmptyMaticosCard()*/

            Spacer(
                modifier = Modifier.height(28.dp)
            )
            Text(
                text = " — نسخة تجريبية —",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


// ═══════════════════════════════════════
// صفحة الملاعب
// ═══════════════════════════════════════

@Composable
fun FieldsScreen(

    modifier: Modifier = Modifier,
    onMaticoSelected: (Matico) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var showAvailableOnly by remember {
        mutableStateOf(false)
    }

    var maticos by remember {
        mutableStateOf<List<Matico>>(emptyList())
    }

    var isLoadingMaticos by remember {
        mutableStateOf(true)
    }

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    var maticosError by remember {
        mutableStateOf<String?>(null)
    }


    val maticoRepository = remember {
        MaticoRepository()
    }
    val scope = rememberCoroutineScope()

    suspend fun loadMaticos() {
        try {
            isLoadingMaticos = true
            maticosError = null

            maticos = maticoRepository.getMaticos()
            println("NOMBRE MATICOS = ${maticos.size}")
            maticos.forEach {
                println("MATICO: ${it.name} | isActive = ${it.isActive}")
            }

            println("MATICOS SUPABASE = $maticos")

        } catch (e: Exception) {
            maticosError =
                e.message ?: "Erreur lors du chargement des terrains"

            println(
                "ERREUR MATICOS = ${e.stackTraceToString()}"
            )
        } finally {
            isLoadingMaticos = false
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        loadMaticos()
    }

    val filteredMaticos = maticos.filter { matico ->

        val matchesSearch =
            matico.name.contains(searchText, ignoreCase = true) ||
                    matico.location.contains(searchText, ignoreCase = true)

        val matchesAvailability =
            !showAvailableOnly || matico.isActive

        matchesSearch && matchesAvailability
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
                loadMaticos()
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF1F5F2),
                            Color(0xFF71B973),
                            Color(0xFFF6FCF7)
                        )
                    )
                )
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                Spacer(modifier = Modifier.height(24.dp))

                PageTitle(
                    title = "الملاعب",
                    subtitle = "اكتشف الماتيكوات المتاحة للحجز"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // البحث
                SearchField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Filtre
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {

                    OutlinedButton(
                        onClick = {
                            showAvailableOnly = !showAvailableOnly
                        }
                    ) {

                        Text(
                            text = if (showAvailableOnly) {
                                "عرض الكل"
                            } else {
                                "المتاح فقط"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (showAvailableOnly) {
                        "الماتيكوات المتاحة"
                    } else {
                        "جميع الماتيكوات"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (filteredMaticos.isEmpty()) {

                item {

                    NoResultsCard()
                }

            } else {

                items(filteredMaticos) { matico ->

                    MaticoFieldCard(
                        matico = matico,
                        onClick = {
                            onMaticoSelected(matico)
                            // الصفحة تفاصيل الملعب
                            // سيتم إضافتها في الخطوة التالية
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onCreateAccount: () -> Unit
) {

    var emailText by remember {
        mutableStateOf("")
    }

    var passwordText by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F5F2),
                        Color(0xFF71B973),
                        Color(0xFFF1F5F2)
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "تسجيل الدخول",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = emailText,
            onValueChange = {
                emailText = it
            },
            label = {
                Text("البريد الإلكتروني")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,

                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordText,
            onValueChange = {
                passwordText = it
            },
            label = {
                Text("كلمة المرور")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) {
                            "إخفاء كلمة المرور"
                        } else {
                            "إظهار كلمة المرور"
                        },
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,

                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        errorMessage?.let { message ->

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {

                if (
                    emailText.isBlank() ||
                    passwordText.isBlank()
                ) {
                    errorMessage = "يرجى ملء جميع الحقول"
                    return@Button
                }

                isLoading = true
                errorMessage = null

                kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.Dispatchers.Main
                ).launch {

                    try {

                        supabase.auth.signInWith(
                            Email
                        ) {

                            email = emailText.trim()
                            password = passwordText
                        }

                        saveFcmToken()

                        isLoading = false
                        onLoggedIn()

                    } catch (e: Exception) {

                        isLoading = false

                        val message = e.message?.lowercase() ?: ""

                        errorMessage = when {
                            "invalid login credentials" in message ->
                                "البريد الإلكتروني أو كلمة المرور غير صحيحة"

                            "email not confirmed" in message ->
                                "يرجى تأكيد البريد الإلكتروني أولاً"

                            "user not found" in message ->
                                "هذا الحساب غير موجود"

                            "invalid email" in message ->
                                "البريد الإلكتروني غير صحيح"

                            "network" in message ||
                                    "connection" in message ||
                                    "timeout" in message ->
                                "تعذر الاتصال بالخادم. تحقق من اتصال الإنترنت."

                            else ->
                                "حدث خطأ أثناء تسجيل الدخول. حاول مرة أخرى."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Text(
                text = if (isLoading) {
                    "جاري تسجيل الدخول..."
                } else {
                    "تسجيل الدخول"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onCreateAccount,
            enabled = !isLoading
        ) {

            Text(
                text = "ليس لديك حساب؟ إنشاء حساب",
                color = Color.Black
            )
        }
    }
}

@Serializable
data class NotificationTokenInsert(
    @SerialName("user_id")
    val userId: String,
    val token: String
)

fun saveFcmToken() {

    Log.d("FCM", "saveFcmToken() APPELÉE")

    val user = supabase.auth.currentUserOrNull()

    if (user == null) {
        Log.e("FCM", "AUCUN UTILISATEUR CONNECTÉ")
        return
    }

    FirebaseMessaging.getInstance()
        .token
        .addOnCompleteListener { task ->

            if (!task.isSuccessful) {
                Log.e(
                    "FCM",
                    "Erreur récupération token",
                    task.exception
                )
                return@addOnCompleteListener
            }

            val token = task.result

            kotlinx.coroutines.CoroutineScope(
                kotlinx.coroutines.Dispatchers.IO
            ).launch {

                try {

                    supabase
                        .from("notification_tokens")
                        .upsert(
                            NotificationTokenInsert(
                                userId = user.id,
                                token = token
                            )
                        ) {
                            onConflict = "user_id,token"
                            ignoreDuplicates = true
                        }

                    Log.d(
                        "FCM",
                        "Token enregistré dans Supabase"
                    )

                } catch (e: Exception) {

                    Log.e(
                        "FCM",
                        "Erreur enregistrement token",
                        e
                    )
                }
            }
        }
}

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    /*var emailConfirmationSent by remember { mutableStateOf(false) }*/
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F5F2),
                        Color(0xFF71B973),
                        Color(0xFFF1F5F2)
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "إنشاء حساب",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
            },
            label = {
                Text("الاسم الكامل")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it.filter { char -> char.isDigit() }.take(10)
            },
            label = {
                Text("رقم الهاتف")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )

        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("البريد الإلكتروني")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("كلمة المرور")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) {
                            "إخفاء كلمة المرور"
                        } else {
                            "إظهار كلمة المرور"
                        },
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,

                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text("تأكيد كلمة المرور")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (confirmPasswordVisible) {
                            "إخفاء كلمة المرور"
                        } else {
                            "إظهار كلمة المرور"
                        },
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,

                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        /* if (emailConfirmationSent) {
            Text(
                text = "تم إنشاء الحساب بنجاح.\nيرجى التحقق من بريدك الإلكتروني والضغط على رابط التأكيد لتفعيل حسابك.",
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }*/

        errorMessage?.let { message ->

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {

                email = email.trim()

                if (
                    fullName.isBlank() ||
                    phone.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmPassword.isBlank()
                ) {
                    errorMessage = "يرجى ملء جميع الحقول"
                    return@Button
                }
                if (!phone.matches(Regex("^0\\d{9}$"))) {
                    errorMessage = "رقم الهاتف يجب أن يبدأ بـ 0 ويتكون من 10 أرقام"
                    return@Button
                }
                if (!email.matches(
                        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                    )
                ) {
                    errorMessage = "البريد الإلكتروني غير صحيح"
                    return@Button
                }

                if (password != confirmPassword) {
                    errorMessage = "كلمتا المرور غير متطابقتين"
                    return@Button
                }

                isLoading = true
                errorMessage = null

                kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.Dispatchers.Main
                ).launch {

                    try {

                        supabase.auth.signUpWith(Email) {
                            this.email = email.trim()
                            this.password = password

                            data = buildJsonObject {
                                put("full_name", fullName)
                                put("phone", phone)
                            }
                        }

                        isLoading = false
                        onRegistered()

                    } catch (e: Exception) {

                        isLoading = false

                        val message = e.message?.lowercase() ?: ""

                        errorMessage = when {
                            "user already registered" in message ||
                                    "already registered" in message ->
                                "هذا البريد الإلكتروني مستخدم بالفعل"

                            "invalid email" in message ->
                                "البريد الإلكتروني غير صحيح"

                            "password" in message &&
                                    ("short" in message || "length" in message) ->
                                "كلمة المرور قصيرة جداً"

                            "weak password" in message ->
                                "كلمة المرور ضعيفة. اختر كلمة مرور أقوى"

                            "email not confirmed" in message ->
                                "يرجى تأكيد البريد الإلكتروني أولاً"

                            "network" in message ||
                                    "connection" in message ||
                                    "timeout" in message ->
                                "تعذر الاتصال بالخادم. تحقق من اتصال الإنترنت."

                            else ->
                                e.message ?: "خطأ غير معروف"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Text(
                text = if (isLoading) {
                    "جاري إنشاء الحساب..."
                } else {
                    "إنشاء الحساب"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBackToLogin,
            enabled = !isLoading
        ) {
            Text(
                text = "لدي حساب بالفعل",
                color = Color.Black
            )

        }
    }
}


@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = {
            Text("البحث")
        },
        placeholder = {
            Text("ابحث عن ماتيکو ...")
        }
    )
}

@Composable
fun MaticoFieldCard(
    matico: Matico,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            if (!matico.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = matico.imageUrl,
                    contentDescription = "صورة ${matico.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = matico.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = matico.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (matico.isActive) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                ) {

                    Text(
                        text = if (matico.isActive) {
                            "متاح"
                        } else {
                            "غير متاح"
                        },
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "سعر الساعة",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${matico.price.toInt()} دج",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = matico.isActive
            ) {

                Text(
                    text = if (matico.isActive) {
                        "عرض الملعب والحجز"
                    } else {
                        "غير متاح للحجز"
                    }
                )
            }
        }
    }
}

@Composable
fun NoResultsCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "لم يتم العثور على أي ماتيکو",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "جرّب البحث باسم آخر أو غيّر الفلتر.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
// ═══════════════════════════════════════
// صفحة حجوزاتي
// ═══════════════════════════════════════

@Composable
fun ReservationsScreen(
    modifier: Modifier = Modifier
) {
    var reservations by remember {
        mutableStateOf<List<Reservation>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val reservationRepository = remember {
        ReservationRepository()
    }

    var reservationToDelete by remember {
        mutableStateOf<Reservation?>(null)
    }

    suspend fun loadReservations() {

        val user = supabase.auth.currentUserOrNull()

        if (user == null) {
            isLoading = false
            isRefreshing = false
            return
        }

        try {

            isLoading = reservations.isEmpty()
            errorMessage = null

            reservations =
                reservationRepository
                    .getMyReservations(user.id)
                    .filter { !it.clientDeleted }
                    .sortedByDescending { it.createdAt }

        } catch (e: Exception) {

            errorMessage =
                e.message ?: "Erreur lors du chargement des réservations"

            println(
                "ERREUR RESERVATIONS = ${
                    e.stackTraceToString()
                }"
            )

        } finally {

            isLoading = false
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {

        while (true) {

            loadReservations()

            kotlinx.coroutines.delay(30_000)
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true

            scope.launch {
                loadReservations()
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF1F5F2),
                            Color(0xFF71B973),
                            Color(0xFFF1F5F2)
                        )
                    )
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 60.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                Spacer(modifier = Modifier.height(24.dp))

                PageTitle(
                    title = "حجوزاتي",
                    subtitle = "تابع حجوزاتك وحالة الدفع والتأكيد"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            when {

                isLoading -> {

                    item {

                        CircularProgressIndicator()

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "جاري تحميل الحجوزات..."
                        )
                    }
                }

                errorMessage != null -> {

                    item {

                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                reservations.isEmpty() -> {

                    item {
                        EmptyReservationsCard()
                    }
                }

                else -> {

                    items(reservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onDelete = {
                                reservationToDelete = reservation
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    if (reservationToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                reservationToDelete = null
            },
            title = {
                Text(
                    text = "تأكيد الحذف",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "هل تريد فعلاً إخفاء هذا الحجز من سجل حجوزاتك؟",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        val reservationId =
                            reservationToDelete!!.id

                        scope.launch {
                            try {

                                reservationRepository
                                    .hideReservationForClient(
                                        reservationId
                                    )

                                reservations =
                                    reservations.filter {
                                        it.id != reservationId
                                    }

                            } catch (e: Exception) {

                                errorMessage =
                                    e.message
                                        ?: "حدث خطأ أثناء حذف الحجز"

                            } finally {

                                reservationToDelete = null
                            }
                        }
                    }
                ) {
                    Text("تأكيد")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        reservationToDelete = null
                    }
                ) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun ReservationCard(
    reservation: Reservation,
    onDelete: () -> Unit
) {
    val borderColor = when (reservation.reservationStatus) {
        "confirmed" -> Color(0xFF4CAF50)
        "cancelled" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    val slotStartReached = remember(reservation.reservationDate, reservation.timeSlot) {
        try {
            val date = java.time.LocalDate.parse(
                reservation.reservationDate
            )

            val startTime = java.time.LocalTime.parse(
                reservation.timeSlot
                    .split(" - ")
                    .first()
                    .trim()
            )

            val slotStart = java.time.LocalDateTime
                .of(date, startTime)
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()

            java.time.Instant.now().isAfter(slotStart) ||
                    java.time.Instant.now() == slotStart

        } catch (e: Exception) {
            false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8EEE7)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (slotStartReached) {
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer la réservation",
                            tint = Color(0xFFF44336)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "الحجز رقم #${reservation.id}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "📅 التاريخ: ${reservation.reservationDate}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🕐 التوقيت: ${reservation.timeSlot}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "💰 السعر: ${reservation.price} دج",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(14.dp))

            val paymentText = when (reservation.paymentStatus) {
                "paid" -> "مدفوع"
                "pending" -> "في انتظار الدفع"
                "unpaid" -> "الدفع نقداً — يُدفع عند الحضور"
                else -> reservation.paymentStatus
            }

            Text(
                text = "💳 الدفع: $paymentText",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(8.dp))

            val reservationText = when (reservation.reservationStatus) {
                "pending" -> "تم إرسال الطلب — في انتظار موافقة المالك"
                "confirmed" -> "تم تأكيد الحجز"
                "expired" -> "انتهى الحجز"
                "cancelled" -> "تم إلغاء الحجز"
                else -> reservation.reservationStatus
            }

            Text(
                text = "📌 حالة الحجز: $reservationText",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        }
    }
}

// ═══════════════════════════════════════
// صفحة حسابي
// ═══════════════════════════════════════


@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onLogout: () -> Unit,
    userRole: String? = null,
    onOwnerDashboard: () -> Unit = {}
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F5F2),
                        Color(0xFF71B973),
                        Color(0xFFF1F5F2)
                    )
                )
            )
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {

            Spacer(modifier = Modifier.height(24.dp))

            PageTitle(
                title = "حسابي",
                subtitle = "إدارة معلومات حسابك"
            )

            Spacer(modifier = Modifier.height(24.dp))

            AccountCard(
                isLoggedIn = isLoggedIn,
                onLogin = onLogin,
                onRegister = onRegister,
                onLogout = onLogout
            )

            if (isLoggedIn && userRole == "proprietaire") {

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOwnerDashboard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🏟️ لوحة تحكم المالك")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun OwnerDashboardScreen(
    modifier: Modifier = Modifier,
    onReservationSelected: (
        Reservation,
        OwnerReservationClient?,
        Matico?
    ) -> Unit,
    onAddMatico: () -> Unit,
    onEditMatico: (Matico) -> Unit,
    initialSection: String = "home",
    onSectionChanged: (String) -> Unit,
    refreshKey: Int,
    onLogout: () -> Unit,
    onRefresh: () -> Unit
) {
    var reservationToCancel by remember {
        mutableStateOf<Reservation?>(null)
    }
    var showLogoutConfirmation by remember {
        mutableStateOf(false)
    }

    var dashboardSection by remember {
        mutableStateOf(initialSection)
    }
    LaunchedEffect(initialSection) {
        dashboardSection = initialSection
    }

    var reservationFilter by remember {
        mutableStateOf("all")
    }

    var maticoToDelete by remember {
        mutableStateOf<Matico?>(null)
    }

    var selectedReservation by remember {
        mutableStateOf<Reservation?>(null)
    }

    var myMaticos by remember {
        mutableStateOf<List<Matico>>(emptyList())
    }

    var ownerReservations by remember {
        mutableStateOf<List<Reservation>>(emptyList())
    }

    var reservationClients by remember {
        mutableStateOf<List<OwnerReservationClient>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var reservationToDelete by remember {
        mutableStateOf<Reservation?>(null)
    }
        var currentTime by remember {
            mutableStateOf(java.time.Instant.now())
        }

        LaunchedEffect(Unit) {
            while (true) {
                currentTime = java.time.Instant.now()
                kotlinx.coroutines.delay(30_000)
            }
        }

        LaunchedEffect(refreshKey) {

            val user = supabase.auth.currentUserOrNull()

            if (user == null) {
                isLoading = false
                return@LaunchedEffect
            }

            try {

                isLoading = true
                errorMessage = null

                myMaticos = supabase
                    .from("maticos")
                    .select()
                    .decodeList<Matico>()
                    .filter { it.ownerId == user.id }

                val myMaticoIds = myMaticos.map {
                    it.id
                }

                ownerReservations =
                    if (myMaticoIds.isNotEmpty()) {

                        supabase
                            .from("reservations")
                            .select()
                            .decodeList<Reservation>()
                            .filter { reservation ->
                                reservation.maticoId in myMaticoIds &&
                                        !reservation.ownerDeleted
                            }
                            .sortedByDescending { reservation ->
                                reservation.createdAt
                            }

                    } else {
                        emptyList()
                    }

                reservationClients =
                    supabase
                        .postgrest
                        .rpc(
                            function = "get_owner_reservation_clients"
                        )
                        .decodeList<OwnerReservationClient>()

            } catch (e: Exception) {

                errorMessage =
                    e.message
                        ?: "حدث خطأ أثناء تحميل البيانات"

                println(
                    "ERREUR DASHBOARD PROPRIETAIRE = ${
                        e.stackTraceToString()
                    }"
                )

            } finally {

                isLoading = false
                isRefreshing = false
            }
        }

        val filteredReservations =
            when (reservationFilter) {

                "pending" ->
                    ownerReservations.filter {
                        it.reservationStatus == "pending"
                    }

                "confirmed" ->
                    ownerReservations.filter {
                        it.reservationStatus == "confirmed"
                    }

                "expired" ->
                    ownerReservations.filter {
                        it.reservationStatus == "expired"
                    }

                "cancelled" ->
                    ownerReservations.filter {
                        it.reservationStatus == "cancelled"
                    }

                else ->
                    ownerReservations
            }

        val pendingCount =
            ownerReservations.count {
                it.reservationStatus == "pending"
            }

        val confirmedCount =
            ownerReservations.count {
                it.reservationStatus == "confirmed"
            }

        val expiredCount =
            ownerReservations.count {
                it.reservationStatus == "expired"
            }

        val cancelledCount =
            ownerReservations.count {
                it.reservationStatus == "cancelled"
            }

        val confirmedRevenue = ownerReservations
            .filter {
                it.reservationStatus == "confirmed" &&
                        it.paymentStatus == "paid"
            }
            .sumOf {
                it.price
            }

        val activeMaticosCount = myMaticos.count {
            it.isActive
        }

        val averageConfirmedRevenue =
            if (confirmedCount > 0) {
                confirmedRevenue / confirmedCount
            } else {
                0.0
            }

        val reservationsByMatico = myMaticos.map { matico ->
            matico to ownerReservations.count {
                it.maticoId == matico.id
            }
        }

        val revenueByMatico = myMaticos.map { matico ->
            matico to ownerReservations
                .filter {
                    it.maticoId == matico.id &&
                            it.reservationStatus == "confirmed" &&
                            it.paymentStatus == "paid"
                }
                .sumOf {
                    it.price
                }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onRefresh()
            },
            modifier = modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF1F5F2),
                                Color(0xFF71B973),
                                Color(0xFFF1F5F2)
                            )
                        )
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 100.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (dashboardSection == "home") {

                    item {

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            OutlinedButton(
                                onClick = {
                                    showLogoutConfirmation = true
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFFDC2626)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFB91C1C)
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "تسجيل الخروج"
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(
                                    text = "تسجيل الخروج",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        PageTitle(
                            title = "لوحة تحكم المالك",
                            subtitle = "إدارة ملاعبك وحجوزاتك"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dashboardSection = "maticos"
                                    onSectionChanged("maticos")
                                },
                            shape = RoundedCornerShape(20.dp)

                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "🏟️ ملاعبي",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${myMaticos.size} ملعب",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "إدارة ملاعبك",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dashboardSection = "reservations"
                                    onSectionChanged("reservations")
                                },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "📋 الحجوزات",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${ownerReservations.size} حجز",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "⏳ $pendingCount في الانتظار",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "إدارة حجوزات ملاعبك",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                        }



                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dashboardSection = "statistics"
                                    onSectionChanged("statistics")
                                },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📊 الإحصائيات",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "عرض إحصائيات ملاعبك وحجوزاتك",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }


                if (dashboardSection == "maticos") {

                    item {

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = {
                                    dashboardSection = "home"
                                    onSectionChanged("home")
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFF16A34A)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF15803D)
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "العودة"
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "العودة",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {

                        Button(
                            onClick = {
                                onAddMatico()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("➕ إضافة ملعب")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    when {

                        isLoading -> {

                            item {

                                CircularProgressIndicator()

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("جاري تحميل ملاعبك...")
                            }
                        }

                        errorMessage != null -> {

                            item {

                                Text(
                                    text = errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        myMaticos.isEmpty() -> {

                            item {

                                Text(
                                    text = "لا توجد ملاعب مرتبطة بحسابك.",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        else -> {

                            items(myMaticos) { matico ->

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    ) {

                                        Text(
                                            text = matico.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "📍 ${matico.location}"
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "💰 ${matico.price} دج "
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = if (matico.isActive) {
                                                "🟢 نشط"
                                            } else {
                                                "🔴 غير نشط"
                                            }
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        OutlinedButton(
                                            onClick = {
                                                onEditMatico(matico)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("✏️ تعديل")
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        OutlinedButton(
                                            onClick = {
                                                maticoToDelete = matico
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("🗑️ حذف الملعب")
                                        }

                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (dashboardSection == "reservations") {

                    item {

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = {
                                    dashboardSection = "home"
                                    onSectionChanged("home")
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFF16A34A)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF15803D)
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "العودة"
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = "العودة",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "الحجوزات",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            FilterChip(
                                selected = reservationFilter == "all",
                                onClick = {
                                    reservationFilter = "all"
                                },
                                label = {
                                    Text(
                                        text = "الكل (${ownerReservations.size})",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )

                            FilterChip(
                                selected = reservationFilter == "pending",
                                onClick = {
                                    reservationFilter = "pending"
                                },
                                label = {
                                    Text(
                                        text = "في الانتظار ($pendingCount)",
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            FilterChip(
                                selected = reservationFilter == "confirmed",
                                onClick = {
                                    reservationFilter = "confirmed"
                                },
                                label = {
                                    Text(
                                        text = "مؤكدة ($confirmedCount)",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )

                            FilterChip(
                                selected = reservationFilter == "expired",
                                onClick = {
                                    reservationFilter = "expired"
                                },
                                label = {
                                    Text(
                                        text = "منتهية ($expiredCount)",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )

                            FilterChip(
                                selected = reservationFilter == "cancelled",
                                onClick = {
                                    reservationFilter = "cancelled"
                                },
                                label = {
                                    Text(
                                        text = "ملغاة ($cancelledCount)",
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                            )
                        }

                    }
                    if (ownerReservations.isEmpty()) {

                        item {

                            Text(
                                text = "لا توجد حجوزات حالياً.",
                                textAlign = TextAlign.Center
                            )
                        }

                    } else {

                        items(filteredReservations) { reservation ->

                            val client = reservationClients.find {
                                it.reservationId == reservation.id
                            }
                            val borderColor = when (reservation.reservationStatus) {
                                "confirmed" -> Color(0xFF4CAF50)
                                "cancelled" -> Color(0xFFF44336)
                                else -> Color.Gray
                            }

                            val slotStartReached =
                                try {
                                    val slotStart =
                                        java.time.LocalDateTime.of(
                                            java.time.LocalDate.parse(
                                                reservation.reservationDate
                                            ),
                                            java.time.LocalTime.parse(
                                                reservation.timeSlot
                                                    .split(" - ")
                                                    .first()
                                                    .trim()
                                            )
                                        ).atZone(
                                            java.time.ZoneId.systemDefault()
                                        ).toInstant()

                                    !currentTime.isBefore(slotStart)

                                } catch (e: Exception) {
                                    false
                                }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onReservationSelected(
                                            reservation,
                                            client,
                                            myMaticos.find {
                                                it.id == reservation.maticoId
                                            }
                                        )
                                    },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = borderColor
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {

                                    if (slotStartReached) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    reservationToDelete = reservation
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "حذف الحجز",
                                                    tint = Color(0xFFF44336)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                    }

                                    Text(
                                        text = "👤 ${
                                            client?.fullName ?: "عميل غير معروف"
                                        }",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Right
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "🕐 ${reservation.timeSlot}",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Right
                                    )

                                    if (reservation.reservationStatus == "pending") {

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "⏳ ${getWaitingTimeText(reservation.createdAt)}",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Right,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        if (
                                            reservation.paymentStatus == "unpaid" &&
                                            reservation.reservationDate >
                                            java.time.LocalDate.now().toString()
                                        ) {

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = "⌛ ${getRemainingExpirationText(reservation.createdAt)}",
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Right,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }

                                    if (
                                        reservation.reservationStatus == "pending" &&
                                        reservation.paymentStatus == "unpaid" &&
                                        run {
                                            val now = java.time.Instant.now()

                                            val expiration24h =
                                                java.time.Instant.parse(
                                                    reservation.createdAt
                                                ).plus(
                                                    java.time.Duration.ofHours(24)
                                                )

                                            val slotStart =
                                                try {
                                                    java.time.LocalDateTime.of(
                                                        java.time.LocalDate.parse(
                                                            reservation.reservationDate
                                                        ),
                                                        java.time.LocalTime.parse(
                                                            reservation.timeSlot
                                                                .split(" - ")
                                                                .first()
                                                                .trim()
                                                        )
                                                    ).atZone(
                                                        java.time.ZoneId.systemDefault()
                                                    ).toInstant()
                                                } catch (e: Exception) {
                                                    null
                                                }

                                            val expirationFinale =
                                                if (slotStart != null &&
                                                    slotStart.isBefore(expiration24h)
                                                ) {
                                                    slotStart
                                                } else {
                                                    expiration24h
                                                }

                                            now.isBefore(expirationFinale)
                                        }

                                    ) {

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {

                                            Button(
                                                onClick = {
                                                    kotlinx.coroutines.CoroutineScope(
                                                        kotlinx.coroutines.Dispatchers.Main
                                                    ).launch {

                                                        try {

                                                            ReservationRepository()
                                                                .confirmReservation(
                                                                    reservation.id
                                                                )

                                                            ownerReservations =
                                                                ownerReservations.map {
                                                                    if (it.id == reservation.id) {
                                                                        it.copy(
                                                                            paymentStatus = "paid",
                                                                            reservationStatus = "confirmed",
                                                                            validatedAt =
                                                                                java.time.Instant.now()
                                                                                    .toString()
                                                                        )
                                                                    } else {
                                                                        it
                                                                    }
                                                                }

                                                        } catch (e: Exception) {

                                                            errorMessage =
                                                                "حدث خطأ أثناء تأكيد الحجز."

                                                            println(
                                                                "ERREUR CONFIRMATION RESERVATION = ${
                                                                    e.stackTraceToString()
                                                                }"
                                                            )
                                                        }
                                                    }
                                                },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("✅ تأكيد")
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    reservationToCancel = reservation
                                                },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("❌ رفض")
                                            }
                                        }
                                    }
                                    if (reservation.reservationStatus == "confirmed") {

                                        Spacer(modifier = Modifier.height(14.dp))

                                        OutlinedButton(
                                            onClick = {
                                                reservationToCancel = reservation
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("❌ إلغاء الحجز")
                                        }
                                    }

                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }


                }
                if (dashboardSection == "statistics") {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = {
                                    dashboardSection = "home"
                                    onSectionChanged("home")
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFF16A34A)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF15803D)
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "العودة"
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = "العودة",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📊 الإحصائيات",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "إحصائيات ملاعبك وحجوزاتك",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📋 إجمالي الحجوزات",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${ownerReservations.size}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "✅ الحجوزات المؤكدة",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "$confirmedCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⏳ الحجوزات في الانتظار",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "$pendingCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⌛ الحجوزات المنتهية",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "$expiredCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "❌ الحجوزات الملغاة",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "$cancelledCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "💰 الإيرادات",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${formatPrice(confirmedRevenue)} دج",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🏟️ الملاعب النشطة",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "$activeMaticosCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📈 متوسط الإيرادات للحجز",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${formatPrice(averageConfirmedRevenue)} دج",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "🏟️ الحجوزات حسب الملعب",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        reservationsByMatico.forEach { (matico, reservationCount) ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = matico.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "$reservationCount حجز",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        if (reservationToCancel != null) {

            AlertDialog(
                onDismissRequest = {
                    reservationToCancel = null
                },
                title = {
                    Text(
                        text = if (reservationToCancel?.reservationStatus == "confirmed") {
                            "تأكيد إلغاء الحجز"
                        } else {
                            "تأكيد رفض الحجز"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (reservationToCancel?.reservationStatus == "confirmed") {
                            "هل أنت متأكد من إلغاء هذا الحجز؟ سيصبح هذا الوقت متاحاً للحجز من جديد."
                        } else {
                            "هل أنت متأكد من رفض هذا الحجز؟"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {

                            val reservation =
                                reservationToCancel ?: return@Button

                            reservationToCancel = null

                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.Main
                            ).launch {

                                try {

                                    ReservationRepository()
                                        .cancelReservation(
                                            reservation.id
                                        )

                                    ownerReservations =
                                        ownerReservations.map {
                                            if (it.id == reservation.id) {
                                                it.copy(
                                                    reservationStatus = "cancelled"
                                                )
                                            } else {
                                                it
                                            }
                                        }

                                } catch (e: Exception) {

                                    errorMessage =
                                        e.message
                                            ?: "حدث خطأ أثناء رفض الحجز"

                                    println(
                                        "ERREUR ANNULATION RESERVATION = ${
                                            e.stackTraceToString()
                                        }"
                                    )
                                }
                            }
                        }
                    ) {
                        Text(
                            if (reservationToCancel?.reservationStatus == "confirmed") {
                                "إلغاء الحجز"
                            } else {
                                "رفض الحجز"
                            }
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            reservationToCancel = null
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }
        if (maticoToDelete != null) {

            AlertDialog(
                onDismissRequest = {
                    maticoToDelete = null
                },

                title = {
                    Text(
                        text = "تأكيد حذف الملعب",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Text(
                        text = "هل أنت متأكد من حذف هذا الملعب؟ لا يمكن التراجع عن هذا الإجراء.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },

                confirmButton = {
                    TextButton(
                        onClick = {

                            val matico =
                                maticoToDelete ?: return@TextButton

                            maticoToDelete = null

                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.Main
                            ).launch {

                                try {

                                    val user =
                                        supabase.auth.currentUserOrNull()

                                    if (user == null) {
                                        errorMessage = "يجب تسجيل الدخول أولاً."
                                        return@launch
                                    }

                                    MaticoRepository()
                                        .deleteMatico(
                                            maticoId = matico.id,
                                            ownerId = user.id
                                        )

                                    myMaticos =
                                        myMaticos.filter {
                                            it.id != matico.id
                                        }

                                    ownerReservations =
                                        ownerReservations.filter {
                                            it.maticoId != matico.id
                                        }

                                } catch (e: Exception) {

                                    errorMessage =
                                        e.message
                                            ?: "حدث خطأ أثناء حذف الملعب"

                                    println(
                                        "ERREUR SUPPRESSION MATİCO = ${
                                            e.stackTraceToString()
                                        }"
                                    )
                                }
                            }
                        }
                    ) {
                        Text("حذف الملعب")
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            maticoToDelete = null
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }
        if (showLogoutConfirmation) {

            AlertDialog(
                onDismissRequest = {
                    showLogoutConfirmation = false
                },

                title = {
                    Text(
                        text = "تأكيد تسجيل الخروج",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Text(
                        text = "هل أنت متأكد أنك تريد تسجيل الخروج؟",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutConfirmation = false
                            onLogout()
                        }
                    ) {
                        Text("تسجيل الخروج")
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            showLogoutConfirmation = false
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }
        reservationToDelete?.let { reservation ->

            AlertDialog(
                onDismissRequest = {
                    reservationToDelete = null
                },
                title = {
                    Text(
                        text = "تأكيد الحذف",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "هل تريد فعلاً إخفاء هذا الحجز من قائمة حجوزاتك؟",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {

                            scope.launch {
                                try {

                                    ReservationRepository()
                                        .hideReservationForOwner(
                                            reservation.id
                                        )

                                    ownerReservations =
                                        ownerReservations.filter {
                                            it.id != reservation.id
                                        }

                                } catch (e: Exception) {

                                    errorMessage =
                                        e.message
                                            ?: "حدث خطأ أثناء حذف الحجز"

                                    println(
                                        "ERREUR SUPPRESSION RESERVATION PROPRIETAIRE = ${
                                            e.stackTraceToString()
                                        }"
                                    )

                                } finally {

                                    reservationToDelete = null
                                }
                            }
                        }
                    ) {
                        Text("تأكيد")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            reservationToDelete = null
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }

    }

    fun formatPrice(price: Double): String {
        return if (price % 1.0 == 0.0) {
            price.toLong().toString()
        } else {
            String.format("%.2f", price)
        }
    }

    fun getWaitingTimeText(createdAt: String): String {

        return try {

            val createdTime =
                java.time.Instant.parse(createdAt)

            val now =
                java.time.Instant.now()

            val minutes =
                java.time.Duration
                    .between(createdTime, now)
                    .toMinutes()

            when {

                minutes < 1 ->
                    "منذ أقل من دقيقة"

                minutes < 60 ->
                    "منذ $minutes دقيقة"

                minutes < 120 ->
                    "منذ ساعة واحدة"

                minutes < 1440 -> {
                    val hours = minutes / 60
                    "منذ $hours ساعات"
                }

                else -> {
                    val days = minutes / 1440
                    "منذ $days يوم"
                }
            }

        } catch (e: Exception) {

            ""
        }
    }

    fun getRemainingExpirationText(
        createdAt: String
    ): String {

        return try {

            val createdTime =
                java.time.Instant.parse(createdAt)

            val expirationTime =
                createdTime.plus(
                    java.time.Duration.ofHours(24)
                )

            val now =
                java.time.Instant.now()

            val remaining =
                java.time.Duration.between(
                    now,
                    expirationTime
                )

            if (remaining.isNegative || remaining.isZero) {
                return "انتهت مدة الدفع"
            }

            val totalMinutes =
                remaining.toMinutes()

            val hours =
                totalMinutes / 60

            val minutes =
                totalMinutes % 60

            when {

                hours > 0 ->
                    "متبقي $hours ساعة و $minutes دقيقة"

                minutes > 0 ->
                    "متبقي $minutes دقيقة"

                else ->
                    "متبقي أقل من دقيقة"
            }

        } catch (e: Exception) {

            ""
        }
    }

    @Composable
    fun OwnerReservationDetailsScreen(
        reservation: Reservation,
        client: OwnerReservationClient?,
        matico: Matico?,
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        var showCancelConfirmation by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF1F5F2),
                            Color(0xFF71B973),
                            Color(0xFFF1F5F2)
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {

                Text(
                    text = "تفاصيل الحجز",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF16A34A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF15803D)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "العودة"
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "العودة",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(

                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8EEE7)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = "الحجز رقم #${reservation.id}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "👤 العميل: ${
                            client?.fullName ?: "غير معروف"
                        }",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "📞 الهاتف: ${
                            client?.phone ?: "غير متوفر"
                        }",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🏟️ الملعب: ${
                            matico?.name ?: "غير معروف"
                        }",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "📍 الموقع: ${
                            matico?.location ?: "غير معروف"
                        }",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "📅 التاريخ: ${reservation.reservationDate}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🕐 التوقيت: ${reservation.timeSlot}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "💰 السعر: ${reservation.price} دج",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (reservation.paymentStatus) {
                            "paid" -> "💳 الدفع: مدفوع"
                            "pending" -> "💳 الدفع: في انتظار الدفع"
                            "unpaid" -> "💳 الدفع: لم يتم الدفع بعد"
                            else -> "💳 الدفع: ${reservation.paymentStatus}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (reservation.reservationStatus) {
                            "pending" -> "📌 الحجز: في انتظار موافقة المالك"
                            "confirmed" -> "📌 الحجز: تم التأكيد"
                            "expired" -> "📌 الحجز: منتهي"
                            "cancelled" -> "📌 الحجز: ملغى"
                            else -> "📌 الحجز: ${reservation.reservationStatus}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )

                    if (
                        reservation.reservationStatus == "pending" &&
                        reservation.paymentStatus == "unpaid"
                    ) {

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = {
                                    onConfirm()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("✅ تأكيد")
                            }

                            OutlinedButton(
                                onClick = {
                                    showCancelConfirmation = true
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("❌ رفض")
                            }
                        }
                    }
                    if (reservation.reservationStatus == "confirmed") {

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = {
                                showCancelConfirmation = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("❌ إلغاء الحجز")
                        }
                    }
                }
            }
            if (showCancelConfirmation) {

                AlertDialog(
                    onDismissRequest = {
                        showCancelConfirmation = false
                    },

                    title = {
                        Text(
                            text = if (reservation.reservationStatus == "confirmed") {
                                "تأكيد إلغاء الحجز"
                            } else {
                                "تأكيد رفض الحجز"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right,
                            fontWeight = FontWeight.Bold
                        )
                    },

                    text = {
                        Text(
                            text = if (reservation.reservationStatus == "confirmed") {
                                "هل أنت متأكد من إلغاء هذا الحجز؟ سيصبح هذا الوقت متاحاً للحجز من جديد."
                            } else {
                                "هل أنت متأكد من رفض هذا الحجز؟"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                showCancelConfirmation = false
                                onCancel()
                            }
                        ) {
                            Text(
                                if (reservation.reservationStatus == "confirmed") {
                                    "تأكيد الإلغاء"
                                } else {
                                    "تأكيد الرفض"
                                }
                            )
                        }
                    },

                    dismissButton = {
                        TextButton(
                            onClick = {
                                showCancelConfirmation = false
                            }
                        ) {
                            Text("إلغاء")
                        }
                    }
                )
            }
        }
    }


// ═══════════════════════════════════════
// Titre des pages
// ═══════════════════════════════════════

    @Composable
    fun PageTitle(
        title: String,
        subtitle: String
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF16A34A))
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Right,
                lineHeight = 22.sp
            )
        }
    }


// ═══════════════════════════════════════
// Notification paiement
// ═══════════════════════════════════════


@Composable
fun PaymentNotice() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7F60B7)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "تنبيه مهم!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF671C1C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "لتأكيد الحجز نهائياً، يجب دفع المبلغ وتأكيد الدفع يدا بيد بعد 24 ساعة كحد أقصى من موعد الحجز."
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "أما الحجوزات المبرمجة في نفس اليوم: فلا تنطبق عليها مهلة 24 ساعة، ويجب تأكيد الدفع قبل موعد الحجز."
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "بعد انتهاء المهلة، قد يتم إلغاء الحجز وإعادة الموعد إلى المواعيد المتاحة."
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "بالنسبة لأصحاب الماتيكوات:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF671C1C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "يرجى إنشاء حساب عبر التطبيق ثم التواصل مع مطوّر التطبيق لتحويل الحساب إلى مالك (Propriétaire)."
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "يرجى عند التواصل إرسال وثيقة من البلدية تثبت ملكية الماتيكو، وذلك للتحقق من صفة المالك وتفعيل حساب Propriétaire."
            )
        }
    }
}


// ═══════════════════════════════════════
// Aucun matico
// ═══════════════════════════════════════
    /*
@Composable
fun EmptyMaticosCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "لا توجد ملاعب متاحة حالياً",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ستظهر هنا الملاعب التي يضيفها أصحابها.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
*/

// ═══════════════════════════════════════
// Aucun réservation
// ═══════════════════════════════════════

    @Composable
    fun EmptyReservationsCard() {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "لا توجد حجوزات حالياً",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ستظهر حجوزاتك هنا بعد إجراء أول حجز.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }


// ═══════════════════════════════════════
// Carte d'un matico
// ═══════════════════════════════════════

    @Composable
    fun MaticoCard(
        matico: Matico,
        onClick: () -> Unit
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = matico.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = matico.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {

                        Text(
                            text = "متاح",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "السعر: ${matico.price.toInt()} دج",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "عرض الملعب والحجز"
                    )
                }
            }
        }
    }


// ═══════════════════════════════════════
// Carte compte
// ═══════════════════════════════════════


    @Composable
    fun AccountCard(
        isLoggedIn: Boolean,
        onLogin: () -> Unit,
        onRegister: () -> Unit,
        onLogout: () -> Unit
    ) {
        var showLogoutConfirmation by remember {
            mutableStateOf(false)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                if (isLoggedIn) {

                    Text(
                        text = "مرحباً بك 👋",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "أنت متصل بحسابك.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showLogoutConfirmation = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "تسجيل الخروج"
                        )
                    }

                } else {

                    Text(
                        text = "مرحباً بك في ماتيكو DZ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "قم بتسجيل الدخول للوصول إلى حجوزاتك ومعلومات حسابك.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onLogin,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "تسجيل الدخول"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onRegister,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "إنشاء حساب"
                        )
                    }
                }
            }
        }
        if (showLogoutConfirmation) {

            AlertDialog(
                onDismissRequest = {
                    showLogoutConfirmation = false
                },
                title = {
                    Text(
                        text = "تأكيد تسجيل الخروج",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "هل أنت متأكد أنك تريد تسجيل الخروج؟",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutConfirmation = false
                            onLogout()
                        }
                    ) {
                        Text("تسجيل الخروج")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showLogoutConfirmation = false
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }


// ═══════════════════════════════════════
// Option compte
// ═══════════════════════════════════════

    @Composable
    fun AccountOption(
        title: String,
        onClick: () -> Unit
    ) {

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        }
    }


// ═══════════════════════════════════════
// Navigation du bas
// ═══════════════════════════════════════


    @Composable
    fun MaticoBottomBar(
        selectedPage: Int,
        onPageSelected: (Int) -> Unit,
        userRole: String?
    ) {
        NavigationBar(
            containerColor = Color(0xFFFAF3EC),
            tonalElevation = 8.dp
        ) {

            // الرئيسية — uniquement pour les clients
            if (userRole != "proprietaire") {
                NavigationBarItem(
                    selected = selectedPage == 0,
                    onClick = {
                        onPageSelected(0)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "الرئيسية"
                        )
                    },
                    label = {
                        Text(
                            text = "الرئيسية",
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF16A34A),
                        selectedTextColor = Color(0xFF16A34A),
                        indicatorColor = Color(0xFFE8F5E9),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    )
                )
            }

            // الملاعب — uniquement pour les clients
            if (userRole != "proprietaire") {
                NavigationBarItem(
                    selected = selectedPage == 1,
                    onClick = {
                        onPageSelected(1)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = "الملاعب"
                        )
                    },
                    label = {
                        Text(
                            text = "الملاعب",
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF16A34A),
                        selectedTextColor = Color(0xFF16A34A),
                        indicatorColor = Color(0xFFE8F5E9),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    )
                )
            }

            // حجوزاتي — uniquement pour les clients
            if (userRole != "proprietaire") {
                NavigationBarItem(
                    selected = selectedPage == 2,
                    onClick = {
                        onPageSelected(2)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = "حجوزاتي"
                        )
                    },
                    label = {
                        Text(
                            text = "حجوزاتي",
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF16A34A),
                        selectedTextColor = Color(0xFF16A34A),
                        indicatorColor = Color(0xFFE8F5E9),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    )
                )
            }

            // حسابي — clients + propriétaires
            NavigationBarItem(
                selected = selectedPage == 3,
                onClick = {
                    onPageSelected(3)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "حسابي"
                    )
                },
                label = {
                    Text(
                        text = "حسابي",
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF16A34A),
                    selectedTextColor = Color(0xFF16A34A),
                    indicatorColor = Color(0xFFE8F5E9),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                )
            )
        }
    }


// ═══════════════════════════════════════
// Modèle Matico
// ═══════════════════════════════════════

    @Serializable
    data class Profile(
        val id: String,
        val fullName: String? = null,
        val phone: String? = null,
        val role: String
    )

    @Serializable
    data class OwnerReservationClient(
        @SerialName("reservation_id")
        val reservationId: Long,

        @SerialName("full_name")
        val fullName: String? = null,

        val phone: String? = null
    )


    @Serializable
    data class Matico(
        val id: Long,
        val name: String,
        val location: String,
        val price: Double,

        @SerialName("image_url")
        val imageUrl: String? = null,

        @SerialName("is_active")
        val isActive: Boolean,

        @SerialName("owner_id")
        val ownerId: String? = null
    )

    @Serializable
    data class ReservationInsert(
        @SerialName("user_id")
        val userId: String,

        @SerialName("matico_id")
        val maticoId: Long,

        @SerialName("reservation_date")
        val reservationDate: String,

        @SerialName("time_slot")
        val timeSlot: String,

        val price: Double,

        @SerialName("payment_status")
        val paymentStatus: String = "unpaid",

        @SerialName("reservation_status")
        val reservationStatus: String = "pending"
    )

    @Serializable
    data class MaticoInsert(
        val name: String,
        val location: String,
        val price: Double,

        @SerialName("owner_id")
        val ownerId: String,

        @SerialName("image_url")
        val imageUrl: String? = null,

        @SerialName("is_active")
        val isActive: Boolean = true
    )


    @Serializable
    data class Reservation(
        val id: Long,

        @SerialName("user_id")
        val userId: String,

        @SerialName("matico_id")
        val maticoId: Long,

        @SerialName("reservation_date")
        val reservationDate: String,

        @SerialName("time_slot")
        val timeSlot: String,

        val price: Double,

        @SerialName("payment_status")
        val paymentStatus: String,

        @SerialName("reservation_status")
        val reservationStatus: String,

        @SerialName("created_at")
        val createdAt: String,

        @SerialName("validated_at")
        val validatedAt: String? = null,

        @SerialName("expired_at")
        val expiredAt: String? = null,

        @SerialName("client_deleted")
        val clientDeleted: Boolean = false,

        @SerialName("owner_deleted")
        val ownerDeleted: Boolean = false
    )


// ═══════════════════════════════════════
// Preview
// ═══════════════════════════════════════

    @Preview(showBackground = true)
    @Composable
    fun MaticoDZPreview() {

        MaticoDZTheme {

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {

                MaticoDZApp()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MaticoDetailsScreen(
        matico: Matico,
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onLoginRequired: () -> Unit
    ) {
        var selectedDate by remember { mutableStateOf("لم يتم اختيار تاريخ") }
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedTimeSlot by remember { mutableStateOf<String?>(null) }

        var reservedTimeSlots by remember {
            mutableStateOf<List<String>>(emptyList())
        }

        var isLoadingTimeSlots by remember {
            mutableStateOf(false)
        }

        var showBookingConfirmation by remember { mutableStateOf(false) }
        var currentTime by remember { mutableStateOf(java.time.LocalTime.now()) }

        var isCreatingReservation by remember { mutableStateOf(false) }
        var reservationError by remember { mutableStateOf<String?>(null) }

        var createdReservationId by remember {
            mutableStateOf<Long?>(null)
        }

        val reservationRepository = remember {
            ReservationRepository()
        }

        LaunchedEffect(Unit) {
            while (true) {
                currentTime = java.time.LocalTime.now()
                kotlinx.coroutines.delay(30_000)
            }
        }

        LaunchedEffect(selectedDate) {

            if (selectedDate == "لم يتم اختيار تاريخ") {
                reservedTimeSlots = emptyList()
                return@LaunchedEffect
            }

            try {

                val formatter =
                    java.time.format.DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy",
                        java.util.Locale.ENGLISH
                    )

                val date =
                    java.time.LocalDate.parse(
                        selectedDate,
                        formatter
                    )

                val databaseDate =
                    date.format(
                        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
                    )

                isLoadingTimeSlots = true

                reservedTimeSlots =
                    reservationRepository.getReservedTimeSlots(
                        maticoId = matico.id,
                        reservationDate = databaseDate
                    )

                println(
                    "CRENEAUX RESERVES = $reservedTimeSlots"
                )

            } catch (e: Exception) {

                println(
                    "ERREUR CRENEAUX = ${
                        e.stackTraceToString()
                    }"
                )

                reservedTimeSlots = emptyList()

            } finally {

                isLoadingTimeSlots = false
            }
        }

        val reservationExpired = remember(selectedDate, currentTime) {
            try {
                if (selectedDate == "لم يتم اختيار تاريخ") {
                    false
                } else {
                    val formatter =
                        java.time.format.DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy",
                            java.util.Locale.ENGLISH
                        )

                    val reservationDate =
                        java.time.LocalDate.parse(selectedDate, formatter)

                    val today = java.time.LocalDate.now()

                    // Date passée → réservation impossible
                    if (reservationDate.isBefore(today)) {
                        true
                    }
                    // Aujourd'hui après 12:00 → réservation impossible
                    else if (
                        reservationDate == today &&
                        currentTime >= java.time.LocalTime.NOON
                    ) {
                        true
                    }
                    // Date future ou aujourd'hui avant 12:00
                    else {
                        false
                    }
                }
            } catch (e: Exception) {
                false
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(16.dp))

                // ─────────────────────────
                // Retour
                // ─────────────────────────

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF16A34A)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF15803D)
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "العودة إلى الملاعب"
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "العودة إلى الملاعب",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ─────────────────────────
// Image du terrain
// ─────────────────────────

                if (!matico.imageUrl.isNullOrBlank()) {

                    AsyncImage(
                        model = matico.imageUrl,
                        contentDescription = "صورة ${matico.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "⚽",
                                    style = MaterialTheme.typography.displayLarge
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "صورة الملعب",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ─────────────────────────
                // Nom
                // ─────────────────────────

                Text(
                    text = matico.name,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = matico.location,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ─────────────────────────
                // Informations
                // ─────────────────────────

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "معلومات الملعب",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        DetailRow(
                            label = "السعر",
                            value = "${matico.price.toInt()} دج / الساعة"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        DetailRow(
                            label = "الحالة",
                            value = if (matico.isActive) {
                                "متاح للحجز"
                            } else {
                                "غير متاح"
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        DetailRow(
                            label = "الموقع",
                            value = matico.location
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ─────────────────────────
                // Choix date
                // ─────────────────────────

                Text(
                    text = "اختر التاريخ",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text("اختيار التاريخ")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = selectedDate,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ─────────────────────────
                // Créneaux
                // ─────────────────────────

                Text(
                    text = "المواعيد المتاحة",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(12.dp))


                TimeSlot(
                    time = "17:00 - 18:00",
                    available = !reservedTimeSlots.contains("17:00 - 18:00"),
                    selected = selectedTimeSlot == "17:00 - 18:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("17:00 - 18:00")) {
                            selectedTimeSlot = "17:00 - 18:00"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeSlot(
                    time = "18:00 - 19:00",
                    available = !reservedTimeSlots.contains("18:00 - 19:00"),
                    selected = selectedTimeSlot == "18:00 - 19:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("18:00 - 19:00")) {
                            selectedTimeSlot = "18:00 - 19:00"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeSlot(
                    time = "19:00 - 20:00",
                    available = !reservedTimeSlots.contains("19:00 - 20:00"),
                    selected = selectedTimeSlot == "19:00 - 20:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("19:00 - 20:00")) {
                            selectedTimeSlot = "19:00 - 20:00"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeSlot(
                    time = "20:00 - 21:00",
                    available = !reservedTimeSlots.contains("20:00 - 21:00"),
                    selected = selectedTimeSlot == "20:00 - 21:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("20:00 - 21:00")) {
                            selectedTimeSlot = "20:00 - 21:00"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeSlot(
                    time = "21:00 - 22:00",
                    available = !reservedTimeSlots.contains("21:00 - 22:00"),
                    selected = selectedTimeSlot == "21:00 - 22:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("21:00 - 22:00")) {
                            selectedTimeSlot = "21:00 - 22:00"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeSlot(
                    time = "22:00 - 23:00",
                    available = !reservedTimeSlots.contains("22:00 - 23:00"),
                    selected = selectedTimeSlot == "22:00 - 23:00",
                    onClick = {
                        if (!reservedTimeSlots.contains("22:00 - 23:00")) {
                            selectedTimeSlot = "22:00 - 23:00"
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (selectedTimeSlot != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "الموعد المختار",
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Right
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = selectedTimeSlot!!,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ─────────────────────────
                // Réservation
                // ─────────────────────────

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "تأكيد الحجز",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "اختر التاريخ والموعد المناسب لك ثم تابع لإتمام الحجز."
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {

                                val currentUser = supabase.auth.currentUserOrNull()

                                println("UTILISATEUR ACTUEL = $currentUser")

                                if (currentUser == null) {
                                    onLoginRequired()
                                } else {
                                    showBookingConfirmation = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = matico.isActive &&
                                    selectedDate != "لم يتم اختيار تاريخ" &&
                                    selectedTimeSlot != null &&
                                    !reservationExpired
                        ) {
                            Text(
                                text = "احجز الآن"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))


                        Spacer(modifier = Modifier.height(8.dp))


                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

            }
        }
        if (showBookingConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    showBookingConfirmation = false
                },
                title = {
                    Text(
                        text = "تأكيد الحجز",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "الملعب: ${matico.name}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "التاريخ: $selectedDate",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "الساعة: ${selectedTimeSlot ?: ""}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "السعر: ${matico.price.toInt()} دج",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "⚠ يجب تأكيد الدفع بعد 24 ساعة كحد أقصى من موعد الحجز حتى يتم تثبيت الحجز.",
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val user = supabase.auth.currentUserOrNull()

                            if (user == null) {
                                showBookingConfirmation = false
                                onLoginRequired()
                                return@Button
                            }

                            if (
                                selectedDate == "لم يتم اختيار تاريخ" ||
                                selectedTimeSlot == null
                            ) {
                                return@Button
                            }

                            isCreatingReservation = true
                            reservationError = null

                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.Main
                            ).launch {
                                try {

                                    val displayFormatter =
                                        java.time.format.DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy",
                                            java.util.Locale.ENGLISH
                                        )
                                    val databaseFormatter =
                                        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

                                    val date =
                                        java.time.LocalDate.parse(
                                            selectedDate,
                                            displayFormatter
                                        )

                                    val reservation = ReservationInsert(
                                        userId = user.id,
                                        maticoId = matico.id,
                                        reservationDate = date.format(databaseFormatter),
                                        timeSlot = selectedTimeSlot!!,
                                        price = matico.price,
                                        paymentStatus = "unpaid",
                                        reservationStatus = "pending"
                                    )

                                    reservationRepository.createReservation(
                                        reservation
                                    )

                                    showBookingConfirmation = false

                                    println(
                                        "RESERVATION ENVOYEE AU PROPRIETAIRE"
                                    )

                                } catch (e: Exception) {

                                    val errorMessage = e.message ?: ""

                                    reservationError = when {
                                        errorMessage.contains(
                                            "unique_active_user_reservation",
                                            ignoreCase = true
                                        ) -> {
                                            "لديك حجز نشط بالفعل. لا يمكنك إنشاء حجز آخر حتى ينتهي أو يتم إلغاؤه."
                                        }

                                        errorMessage.contains(
                                            "unique_active_matico_slot",
                                            ignoreCase = true
                                        ) -> {
                                            "عذراً، هذا الموعد محجوز بالفعل. يرجى اختيار موعد آخر."
                                        }

                                        else -> {
                                            "ERREUR : ${e.message ?: "Erreur inconnue"}"
                                        }
                                    }

                                    println(
                                        "ERREUR CREATION RESERVATION = ${
                                            e.stackTraceToString()
                                        }"
                                    )

                                } finally {
                                    isCreatingReservation = false
                                }
                            }
                        },
                        enabled = !isCreatingReservation
                    ) {
                        Text(
                            text = if (isCreatingReservation) {
                                "جاري إرسال الطلب..."
                            } else {
                                "إرسال طلب الحجز"
                            }
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showBookingConfirmation = false
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            )
        }



        if (reservationError != null) {

            AlertDialog(
                onDismissRequest = {
                    reservationError = null
                },
                title = {
                    Text(
                        text = "خطأ",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = reservationError ?: "",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                confirmButton = {
                    CompositionLocalProvider(
                        LocalLayoutDirection provides LayoutDirection.Rtl
                    ) {
                        TextButton(
                            onClick = {
                                reservationError = null
                            }
                        ) {
                            Text("حسناً")
                        }
                    }
                }
            )
        }

        if (showDatePicker) {

            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {

                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selectedCalendar = java.util.Calendar.getInstance(
                            java.util.TimeZone.getTimeZone("UTC")
                        )

                        selectedCalendar.timeInMillis = utcTimeMillis

                        val todayCalendar = java.util.Calendar.getInstance(
                            java.util.TimeZone.getTimeZone("UTC")
                        )

                        return selectedCalendar.get(java.util.Calendar.YEAR) >
                                todayCalendar.get(java.util.Calendar.YEAR) ||
                                (
                                        selectedCalendar.get(java.util.Calendar.YEAR) ==
                                                todayCalendar.get(java.util.Calendar.YEAR) &&
                                                selectedCalendar.get(java.util.Calendar.DAY_OF_YEAR) >=
                                                todayCalendar.get(java.util.Calendar.DAY_OF_YEAR)
                                        )
                    }
                }
            )

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            val millis = datePickerState.selectedDateMillis

                            if (millis != null) {
                                val formatter =
                                    java.text.SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        java.util.Locale.ENGLISH
                                    )

                                selectedDate = formatter.format(
                                    java.util.Date(millis)
                                )
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("تأكيد")
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("إلغاء")
                    }
                }
            ) {

                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
        }

    }


    @Composable
    fun AddMaticoScreen(
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onMaticoAdded: () -> Unit
    ) {

        var name by remember {
            mutableStateOf("")
        }

        var location by remember {
            mutableStateOf("")
        }

        var price by remember {
            mutableStateOf("")
        }

        var isLoading by remember {
            mutableStateOf(false)
        }

        var errorMessage by remember {
            mutableStateOf<String?>(null)
        }
        var selectedImageBytes by remember {
            mutableStateOf<ByteArray?>(null)
        }

        var selectedImageBitmap by remember {
            mutableStateOf<android.graphics.Bitmap?>(null)
        }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val imagePickerLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->

                if (uri == null) return@rememberLauncherForActivityResult

                scope.launch {
                    try {
                        val bytes =
                            withContext(Dispatchers.IO) {
                                context.contentResolver
                                    .openInputStream(uri)
                                    ?.use { it.readBytes() }
                            }

                        if (bytes == null) {
                            errorMessage = "تعذر قراءة الصورة."
                            return@launch
                        }

                        val bitmap =
                            withContext(Dispatchers.Default) {
                                android.graphics.BitmapFactory
                                    .decodeByteArray(
                                        bytes,
                                        0,
                                        bytes.size
                                    )
                            }

                        if (bitmap == null) {
                            errorMessage = "الصورة غير صالحة."
                            return@launch
                        }

                        selectedImageBytes = bytes
                        selectedImageBitmap = bitmap
                        errorMessage = null

                    } catch (e: Exception) {
                        errorMessage = "تعذر اختيار الصورة."
                        println(
                            "ERREUR SELECTION IMAGE = ${
                                e.stackTraceToString()
                            }"
                        )
                    }
                }
            }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF1F5F2),
                            Color(0xFF71B973),
                            Color(0xFFF1F5F2)
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF16A34A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF15803D)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "العودة"
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "العودة",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PageTitle(
                title = "إضافة ملعب",
                subtitle = "أضف ملعبك إلى التطبيق"
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedImageBitmap != null) {

                Image(
                    bitmap = selectedImageBitmap!!.asImageBitmap(),
                    contentDescription = "صورة الملعب",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            OutlinedButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    if (selectedImageBitmap == null)
                        "إضافة صورة للملعب"
                    else
                        "تغيير الصورة"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("اسم الملعب")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text("الموقع")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("السعر بالدج")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (errorMessage != null) {

                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {

                    val user =
                        supabase.auth.currentUserOrNull()

                    if (user == null) {

                        errorMessage =
                            "يجب تسجيل الدخول أولاً."

                        return@Button
                    }

                    if (name.isBlank()) {

                        errorMessage =
                            "يرجى إدخال اسم الملعب."

                        return@Button
                    }

                    if (location.isBlank()) {

                        errorMessage =
                            "يرجى إدخال موقع الملعب."

                        return@Button
                    }

                    val priceValue =
                        price.toDoubleOrNull()

                    if (priceValue == null || priceValue <= 0) {

                        errorMessage =
                            "يرجى إدخال سعر صحيح."

                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    kotlinx.coroutines.CoroutineScope(
                        kotlinx.coroutines.Dispatchers.Main
                    ).launch {

                        try {
                            var imageUrl: String? = null

                            if (selectedImageBytes != null) {

                                val fileName =
                                    "${user.id}/${java.util.UUID.randomUUID()}.jpg"

                                val bucket =
                                    supabase.storage
                                        .from("matico-images")

                                bucket.upload(
                                    path = fileName,
                                    data = selectedImageBytes!!
                                )

                                imageUrl =
                                    bucket.publicUrl(fileName)
                            }

                            val matico =
                                MaticoInsert(
                                    name = name.trim(),
                                    location = location.trim(),
                                    price = priceValue,
                                    ownerId = user.id,
                                    imageUrl = imageUrl,
                                    isActive = true
                                )

                            supabase
                                .from("maticos")
                                .insert(matico)

                            onMaticoAdded()

                        } catch (e: Exception) {

                            errorMessage =
                                "خطأ: ${e.message ?: "خطأ غير معروف"}"

                            println(
                                "ERREUR AJOUT MATICO = ${
                                    e.stackTraceToString()
                                }"
                            )

                        } finally {

                            isLoading = false

                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {

                Text(
                    text = if (isLoading) {
                        "جاري الإضافة..."
                    } else {
                        "إضافة الملعب"
                    }
                )
            }
        }
    }


    @Composable
    fun EditMaticoScreen(
        matico: Matico,
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onMaticoUpdated: () -> Unit
    ) {

        var name by remember {
            mutableStateOf(matico.name)
        }

        var location by remember {
            mutableStateOf(matico.location)
        }

        var price by remember {
            mutableStateOf(matico.price.toString())
        }

        var isActive by remember {
            mutableStateOf(matico.isActive)
        }

        var isLoading by remember {
            mutableStateOf(false)
        }

        var errorMessage by remember {
            mutableStateOf<String?>(null)
        }
        var selectedImageBytes by remember {
            mutableStateOf<ByteArray?>(null)
        }

        var selectedImageBitmap by remember {
            mutableStateOf<android.graphics.Bitmap?>(null)
        }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val imagePickerLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->

                if (uri == null) return@rememberLauncherForActivityResult

                scope.launch {
                    try {

                        val bytes: ByteArray? =
                            withContext(Dispatchers.IO) {
                                context.contentResolver
                                    .openInputStream(uri)
                                    ?.use { it.readBytes() }
                            }

                        if (bytes == null) {
                            errorMessage = "تعذر قراءة الصورة."
                            return@launch
                        }

                        val bitmap =
                            withContext(Dispatchers.Default) {
                                android.graphics.BitmapFactory.decodeByteArray(
                                    bytes,
                                    0,
                                    bytes.size
                                )
                            }

                        if (bitmap == null) {
                            errorMessage = "الصورة غير صالحة."
                            return@launch
                        }

                        selectedImageBytes = bytes
                        selectedImageBitmap = bitmap
                        errorMessage = null

                    } catch (e: Exception) {

                        errorMessage = "تعذر اختيار الصورة."

                        println(
                            "ERREUR SELECTION IMAGE = ${
                                e.stackTraceToString()
                            }"
                        )
                    }
                }
            }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF1F5F2),
                            Color(0xFF71B973),
                            Color(0xFFF1F5F2)
                        )
                    )
                )
                .padding(16.dp),


            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF16A34A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF15803D)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "العودة"
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "العودة",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PageTitle(
                title = "تعديل الملعب",
                subtitle = "تعديل معلومات ملعبك"
            )


            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(20.dp))

            if (selectedImageBitmap != null) {

                Image(
                    bitmap = selectedImageBitmap!!.asImageBitmap(),
                    contentDescription = "صورة الملعب",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

            } else if (!matico.imageUrl.isNullOrBlank()) {

                AsyncImage(
                    model = matico.imageUrl,
                    contentDescription = "صورة الملعب",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    if (
                        selectedImageBitmap == null &&
                        matico.imageUrl.isNullOrBlank()
                    ) {
                        "إضافة صورة للملعب"
                    } else {
                        "تغيير الصورة"
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("اسم الملعب")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text("الموقع")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("السعر بالدج")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (isActive) {
                        "🟢 الملعب نشط"
                    } else {
                        "🔴 الملعب غير نشط"
                    },
                    fontWeight = FontWeight.Bold
                )

                Switch(
                    checked = isActive,
                    onCheckedChange = {
                        isActive = it
                    }
                )
            }



            Spacer(modifier = Modifier.height(20.dp))

            if (errorMessage != null) {

                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {

                    val user =
                        supabase.auth.currentUserOrNull()

                    if (user == null) {

                        errorMessage =
                            "يجب تسجيل الدخول أولاً."

                        return@Button
                    }

                    if (name.isBlank()) {

                        errorMessage =
                            "يرجى إدخال اسم الملعب."

                        return@Button
                    }

                    if (location.isBlank()) {

                        errorMessage =
                            "يرجى إدخال موقع الملعب."

                        return@Button
                    }

                    val priceValue =
                        price.toDoubleOrNull()

                    if (priceValue == null || priceValue <= 0) {

                        errorMessage =
                            "يرجى إدخال سعر صحيح."

                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    kotlinx.coroutines.CoroutineScope(
                        kotlinx.coroutines.Dispatchers.Main
                    ).launch {

                        try {

                            var imageUrl = matico.imageUrl

                            if (selectedImageBytes != null) {

                                val fileName =
                                    "${user.id}/${java.util.UUID.randomUUID()}.jpg"

                                val bucket =
                                    supabase.storage
                                        .from("matico-images")

                                bucket.upload(
                                    path = fileName,
                                    data = selectedImageBytes!!
                                )

                                imageUrl =
                                    bucket.publicUrl(fileName)
                            }

                            supabase
                                .from("maticos")
                                .update(
                                    {
                                        set(
                                            "name",
                                            name.trim()
                                        )

                                        set(
                                            "location",
                                            location.trim()
                                        )

                                        set(
                                            "price",
                                            priceValue
                                        )

                                        set(
                                            "is_active",
                                            isActive
                                        )

                                        set(
                                            "image_url",
                                            imageUrl
                                        )
                                    }
                                ) {
                                    filter {
                                        eq(
                                            "id",
                                            matico.id
                                        )

                                        eq(
                                            "owner_id",
                                            user.id
                                        )
                                    }
                                }

                            onMaticoUpdated()

                        } catch (e: Exception) {

                            errorMessage =
                                "حدث خطأ أثناء تعديل الملعب."

                            println(
                                "ERREUR MODIFICATION MATICO = ${
                                    e.stackTraceToString()
                                }"
                            )

                        } finally {

                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {

                Text(
                    text = if (isLoading) {
                        "جاري الحفظ..."
                    } else {
                        "حفظ التعديلات"
                    }
                )
            }
        }
    }


    @Composable
    fun DetailRow(
        label: String,
        value: String
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = value,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    @Composable
    fun TimeSlot(
        time: String,
        available: Boolean,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = available) {
                    onClick()
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    selected -> MaterialTheme.colorScheme.primaryContainer
                    available -> MaterialTheme.colorScheme.surface
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = time,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = when {
                        selected -> MaterialTheme.colorScheme.primary
                        available -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                ) {

                    Text(
                        text = when {
                            selected -> "محدد"
                            available -> "متاح"
                            else -> "محجوز"
                        },
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
