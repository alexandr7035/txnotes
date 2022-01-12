package by.alexandr7035.txnotes.core.extensions

import androidx.navigation.fragment.NavHostFragment

fun NavHostFragment.checkIfBackStackEmpty(): Boolean {
    return childFragmentManager.backStackEntryCount == 0
}