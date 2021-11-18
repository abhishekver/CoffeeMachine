import com.fasterxml.jackson.databind.ObjectMapper;
import models.InputData;
import services.CoffeeMachineService;
import services.CoffeeMachineServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) throws IOException {
        CoffeeMachineService coffeeMachineService = new CoffeeMachineServiceImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = new Scanner(new File("src/test/resources/TestData.json")).useDelimiter("\\Z").next();
        InputData inputData = objectMapper.readValue(content, InputData.class);
        coffeeMachineService.prepareBeverage(inputData);
    }
}
