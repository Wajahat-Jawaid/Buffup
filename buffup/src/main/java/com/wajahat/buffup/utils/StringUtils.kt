package com.wajahat.buffup.utils

import androidx.annotation.IntRange

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
object StringUtils {

    /** Since our according to our business logic, answers can range from 2 to 5 whose
     * alphabetical index representation will range from A -> E whose
     * ASCII indexes will range from 65 -> 69 */
    fun asciiToAlphabetBuffAnswerIndexes(@IntRange(from = 65, to = 69) i: Int): String? {
        return i.toChar().toString()
    }
}