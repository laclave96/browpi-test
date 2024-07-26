package com.bowpi.test.data.remote

import com.bowpi.test.data.remote.model.Drink
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DrinkDeserializer : JsonDeserializer<Drink> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Drink {
        val jsonObject = json!!.asJsonObject

        val id = jsonObject["idDrink"].asInt
        val name = jsonObject["strDrink"].asString
        val category = jsonObject["strCategory"]?.asString
        val alcoholic = jsonObject["strAlcoholic"]?.asString
        val glass = jsonObject["strGlass"]?.asString
        val instructions = jsonObject["strInstructions"]?.asString
        val thumbUrl = jsonObject["strDrinkThumb"]?.asString

        val ingredientsAndMeasures = hashMapOf<String, String>()
        for (i in 1..15) {
            val ingredientElement = jsonObject["strIngredient$i"]
            val ingredient = if (ingredientElement.isJsonNull) null else ingredientElement.asString
            ingredient?.let {
                if (it.isNotEmpty()) {
                    val measureElement = jsonObject["strMeasure$i"]
                    val measure = if (measureElement.isJsonNull) null else measureElement.asString
                    ingredientsAndMeasures[it] = measure ?: ""
                }
            }
        }

        return Drink(
            id = id,
            name = name,
            category = category,
            alcoholic = alcoholic,
            glass = glass,
            instructions = instructions,
            thumbUrl = thumbUrl,
            ingredientsAndMeasures = ingredientsAndMeasures
        )

    }


}
