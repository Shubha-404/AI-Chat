package com.example.aiconnect.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiconnect.ChatViewModel
import com.example.aiconnect.MessageModel
import com.example.aiconnect.R
import kotlinx.coroutines.delay


@Composable
fun chatscreen(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
            .background(Color.Black)
    ) {
        AppHeader()

        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )

        MessageInput(
            onMessageSend = {
                viewModel.sendMessage(it)
            }
        )

    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(60.dp),
                painter = painterResource(id = R.drawable.ic_ai_logo),
                contentDescription = "Icon",
            )
            Text(text = "Ask me anything", fontSize = 16.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()) { message ->
                // Pass whether it's the latest message and from the model
                val isLatestModelMessage = message == messageList.last() && message.role == "model"
                MessageRow(messageModel = message, isLatestModelMessage = isLatestModelMessage)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel, isLatestModelMessage: Boolean) {
    val isModel = messageModel.role=="model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
                    .border(
                        width = 1.dp, // border thickness
                        color = if (isModel) colorResource(id = R.color.model_outline) else colorResource(
                            id = R.color.user_outline
                        ),
                        shape = RoundedCornerShape(48f) // This should match the shape of the box
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(
                        if (isModel) colorResource(id = R.color.model_msg) else colorResource(
                            id = R.color.user_msg
                        )
                    )
                    .padding(12.dp)
            ) {

                SelectionContainer {
                    if (isLatestModelMessage) {
                        // Apply typing effect for the latest model message
                        TypingText(text = messageModel.message)
                    } else {
                        // Otherwise, just show the message
                        Text(
                            text = messageModel.message,
                            fontWeight = FontWeight.W500,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "AI Connect",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessageInput(onMessageSend : (String)-> Unit) {

    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .weight(1f)
                .background(color = colorResource(id = R.color.blue_green_3)),
            value = message,
            onValueChange = {
                message = it
            },
            placeholder = {
                Text(text = "Message AI...", color = Color.White.copy(alpha = 0.7f))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White, // Input text color
                focusedBorderColor = Color.Transparent, // No border when focused
                unfocusedBorderColor = Color.Transparent, // No border when unfocused
                cursorColor = Color.White // Cursor color
            ),
            maxLines = 10
        )

        Spacer(modifier = Modifier.width(5.dp))

        IconButton(
            onClick = {
                if(message.isNotEmpty()){
                    onMessageSend(message)
                    message = ""
                }
            },
            modifier = Modifier
                .size(48.dp)
                .background(
                    colorResource(id = R.color.blue_green_3),
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp)) // Rounded send button
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TypingText(text: String) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = "" // Clear the displayed text before starting
        text.forEachIndexed { index, char ->
            delay(5) // Delay between each character
            displayedText += char // Append the next character
        }
    }

    Text(text = displayedText)
}


