package coded.lens.amaral;

import org.junit.Test;

import static org.junit.Assert.*;

public class FoodWebTest {
    @Test
    public void generateInitialFoodWeb() throws Exception {
        FoodWeb foodWeb = FoodWeb.primedFoodWeb(6, 3, 5, 4, 2);

        System.out.println(foodWeb);
    }

    @Test
    public void speciationEventsDifferences() throws Exception {
        FoodWeb foodWeb = FoodWeb.primedFoodWeb(6, 2, 3, 2);

        System.out.println(foodWeb);

        foodWeb = foodWeb.speciationEvent();

        System.out.println(foodWeb);
    }
}