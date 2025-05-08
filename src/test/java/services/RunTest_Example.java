package services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RunTest_Example extends BaseTest{

    @Test
    @DisplayName("Test Set - Example")
    public void runTest_Example(){
        runExample();
        runExample2();
        runExample3();
    }


    @Test
    public void runExample(){
        runKarateTests("@Example", 1);
    }

    @Test
    public void runExample2(){
        runKarateTests("@Example2", 1);
    }

    @Test
    public void runExample3(){
        runKarateTests("@Example3", 1);
    }
}
