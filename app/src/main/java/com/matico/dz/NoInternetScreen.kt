package com.matico.dz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoInternetScreen(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "📡",
            fontSize = 50.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "لا يوجد اتصال بالإنترنت",
            fontSize = 22.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "يرجى التحقق من اتصالك بالإنترنت ثم المحاولة مرة أخرى.",
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text(
                text = "إعادة المحاولة"
            )
        }
    }
}