package com.example.loginfirebase.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.loginfirebase.R


@Composable
fun LoginScreen(
    signInClicked: () -> Unit
) {
    val logo= painterResource(R.drawable.logincon)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            stringResource(R.string.Login_Title),
            style= MaterialTheme.typography.h3
        )
        Image(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(horizontal = 32.dp)
            , painter = logo,
            contentDescription = null)
        Text(
            stringResource(R.string.Sub_Tittle),
            style= MaterialTheme.typography.h5
        )//finsubtitulo
        Card(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .height(55.dp)
                .fillMaxWidth()
                .clickable {
                    signInClicked()
                },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(width = 1.5.dp, color = Color.Black),
            elevation = 5.dp
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(32.dp)
                        .padding(0.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.googleicon),
                    contentDescription = "google_logo"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically),
                    text = "Entrar con Google",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                )
            }
        }//finButton
        Spacer(modifier = Modifier.height(25.dp))
        Column() {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text( text = "Condiciones")
            }
            Row() {
                LegalText()
            }
        }

    }//finColumn
}//FinFuncion


@Composable
fun LegalText() {
        val anottatedString = buildAnnotatedString {
        append(stringResource(R.string.Text_legal1))
        append(" ")
        pushStringAnnotation(tag = "Url", annotation = "app://terms")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
        ) {
            append(stringResource(R.string.Text_legal2))
        }//finWithStyle
        append(" ")
        pop()
        append(stringResource(R.string.Text_legal3))
        append(" ")
        pushStringAnnotation(tag = "Url", annotation = "app://privacy")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
        ) {
            append(stringResource(R.string.Text_legal4))
        }//finWithStyle
        pop()
    }//finVal

    Box(contentAlignment = Alignment.Center) {
        ClickableText(
            modifier = Modifier.padding(24.dp),
            text = anottatedString
        ) { offset ->
            anottatedString.getStringAnnotations("Url", offset, offset)
                .firstOrNull()?.let { tag ->
                    Log.d("App", "Ha dado Click en ${tag.item}")
                }//fin offset
        }//fintext
    }//finBox
}//finLegalText