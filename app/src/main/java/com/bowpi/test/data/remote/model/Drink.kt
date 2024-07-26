package com.bowpi.test.data.remote.model

import com.google.gson.annotations.SerializedName

data class Drink(
    @SerializedName("idStr")
    val id: Int = 0,
    @SerializedName("strDrink")
    val name: String = "",
    @SerializedName("strCategory")
    val category: String? = null,
    @SerializedName("strAlcoholic")
    val alcoholic: String? = null,
    @SerializedName("strGlass")
    val glass: String? = null,
    @SerializedName("strInstructions")
    val instructions: String? = null,
    @SerializedName("strDrinkThumb")
    val thumbUrl: String? = null,
    val ingredientsAndMeasures: HashMap<String, String> = hashMapOf()

){
    val textIngredients: String
        get() {
            val stringBuilder = StringBuilder()
            for ((ingredient, measure) in ingredientsAndMeasures) {
                stringBuilder.append("• $measure $ingredient\n")
            }
            return stringBuilder.toString()
        }
}

class DrinkList(
    @SerializedName("drinks")
    val list: List<Drink>?
)