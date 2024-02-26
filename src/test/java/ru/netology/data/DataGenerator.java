package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
  private DataGenerator() {
  }

  public static Faker faker = new Faker(new Locale("ru"));

  public static String generateDate(int plusDays) {
    return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public static String generateCity(String locale) {
    return faker.address().city();
  }

  public static String generateName(String locale) {
    return faker.name().firstName() + " " + faker.name().lastName();
  }

  public static String generatePhone(String locale) {
    return faker.phoneNumber().phoneNumber();
  }

  public static class Registration {
    private Registration() {

    }

    public static UserInfo generateUser(String locale) {
      return new UserInfo(generateCity(locale), generateName(locale), generatePhone(locale));

    }
  }

}