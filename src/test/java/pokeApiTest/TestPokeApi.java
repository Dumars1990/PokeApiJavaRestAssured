package pokeApiTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class TestPokeApi {

    public static String url = "https://pokeapi.co/api/v2";

    @Test
    public static void getGhostIdType() {

        RestAssured.baseURI = url;

        Response response = null;
        String pokeGhost = "ghost";

        response = given().header("ContentType", ContentType.JSON, "application/json", ContentType.JSON).
                when().get("/type").
                then().contentType(ContentType.JSON).extract().response();

       /* //RELATED NAME
        int positionFound = -1;
        String typeList = response.jsonPath().getString("results.name");
        String[] nameTypes = parseJsonStringArray(typeList); // Lista de Pokemon
        for (int i = 0; i < nameTypes.length; i++) {
            if (pokeGhost.equals(nameTypes[i])) {
                positionFound = i; // Position ID GHOST
            }
        }*/

        //RELATED ID
        String typeIdList = response.jsonPath().getString("results.url");
        String idTypeList = (typeIdList.replace(url + "/type/", "").replace("/", ""));
        String[] idTypes = parseJsonStringArray(idTypeList);

        int positionType = getPokeTypeIdFromName(pokeGhost, response);

        System.out.println("Id type ghost: " + idTypes[positionType]);

    }

    public static String[] parseJsonStringArray(String jsonArray) {

        jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
        String[] parsedArray = jsonArray.split(", ");

        return parsedArray;
    }

    public static int getPokeTypeIdFromName(String typeOfPokemon, Response response) {


        //RELATED NAME
        int positionFound = -1;
        String typeList = response.jsonPath().getString("results.name");
        String[] nameTypes = parseJsonStringArray(typeList); // Lista de Pokemon
        for (int i = 0; i < nameTypes.length; i++) {
            if (typeOfPokemon.equals(nameTypes[i])) {
                positionFound = i; // Position ID GHOST
            }
        }

        return positionFound;
    }

    @Test
    public static void assertDittoIsNotFightingPokemonType() {

        RestAssured.baseURI = url;

        Response response = null;
        String pokeFighting = "fighting";
        String pokemonToFind = "ditto";

        response = given().header("ContentType", ContentType.JSON, "application/json", ContentType.JSON).
                when().get("/type").
                then().contentType(ContentType.JSON).extract().response();


        //RELATED ID
        String typeIdList = response.jsonPath().getString("results.url");
        String[] urlTypes = parseJsonStringArray(typeIdList);

        int positionType = getPokeTypeIdFromName(pokeFighting, response);

        RestAssured.baseURI = urlTypes[positionType];

        response = given().header("ContentType", ContentType.JSON, "application/json", ContentType.JSON).
                when().get("/").
                then().contentType(ContentType.JSON).extract().response();

        String pokemons = response.jsonPath().getString("pokemon.pokemon.name");
        String[] pokemonList = parseJsonStringArray(pokemons);

        boolean isDittoFound = isPokemonInList(pokemonList, pokemonToFind);

        Assert.assertFalse(isDittoFound, "Pokemon is in the list!!");
    }

    public static boolean isPokemonInList(String[] pokemonList, String pokemonToFind) {

        boolean hasFoundPokemon = false;
        for (int i = 0; i < pokemonList.length; i++) {

            if (pokemonToFind.equals(pokemonList[i])) {
                hasFoundPokemon = true;
            }
        }
        return hasFoundPokemon;
    }
}







