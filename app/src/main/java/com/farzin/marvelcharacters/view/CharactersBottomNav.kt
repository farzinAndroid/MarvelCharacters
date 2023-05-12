package com.farzin.marvelcharacters.view

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.farzin.marvelcharacters.Destination
import com.farzin.marvelcharacters.R


@Composable
fun CharactersBottomNav(navHostController: NavHostController) {

    BottomNavigation(elevation = 5.dp) {

        val navBackStackEntry = navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val ic_library = painterResource(id = R.drawable.ic_library)
        val ic_collection = painterResource(id = R.drawable.ic_collection)

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Library.route,
            onClick = {
                navHostController.navigate(Destination.Library.route) {
                    popUpTo(Destination.Library.route)
                    launchSingleTop = true
                }
            },
            icon = { Icon(painter = ic_library, contentDescription = "") },
            label = { Text(text = Destination.Library.route) }
        )

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Collection.route,
            onClick = {
                navHostController.navigate(Destination.Collection.route) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(painter = ic_collection, contentDescription = "") },
            label = { Text(text = Destination.Collection.route) }
        )

    }


}