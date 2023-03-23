package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.server.CityForm;
import com.hexanome.fourteen.form.server.GameBoardForm;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

/**
 * City.
 */
public class City extends Image {

  private static final Map<CityForm, String> CITY_FORM_MAP = buildMap();

  public final CityForm cityForm;

  /**
   * Constructor.
   *
   * @param cityForm The city form.
   */
  public City(CityForm cityForm) {
    super(CITY_FORM_MAP.get(cityForm));

    this.cityForm = cityForm;
  }

  private static Map<CityForm, String> buildMap() {
    final Type mapType = new TypeToken<Map<String, CityForm>>() {
    }.getType();
    final Map<String, CityForm> map = Main.GSON.fromJson(new Scanner(
        Objects.requireNonNull(City.class.getResourceAsStream("images/Cities.json")),
        StandardCharsets.UTF_8).useDelimiter("\\A").next(), mapType);

    return map.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getValue,
            e -> Objects.requireNonNull(
                City.class.getResource("images/cities/" + e.getKey())).toString()));
  }

  public static ArrayList<City> interpretCities(GameBoardForm gameBoardForm) {
    ArrayList<City> cities = new ArrayList<>();

    for (CityForm cf : gameBoardForm.availableCities()) {
      cities.add(new City(cf));
    }

    return cities;
  }
}
