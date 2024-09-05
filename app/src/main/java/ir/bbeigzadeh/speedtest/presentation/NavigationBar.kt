package ir.bbeigzadeh.speedtest.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.bbeigzadeh.speedtest.R

@Composable
fun NavigationView() {
    val items = listOf(
        R.drawable.speed, R.drawable.history, R.drawable.settings
    )
    val itemsLabel = listOf(
        "Test", "History", "Settings"
    )

    val selectedTabIndex = 0
    Surface(Modifier.padding(20.dp), color = Color(0xff191A2E), shape = RoundedCornerShape(25.dp)) {
        NavigationBar(containerColor = Color.Transparent) {

            items.forEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xff1C80F9),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    ),
                    selected = selectedTabIndex == index,
                    onClick = {
                    },
                    label = { Text(text = itemsLabel.get(index)) },
                    icon = {
                        Icon(painter = painterResource(id = items.get(index)), contentDescription = "")
                    })
            }
        }
    }


}
