package org.lin.changeexcal;

import org.lin.changeexcal.test.TestMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ChangeexcalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ChangeexcalApplication.class, args);

        TestMain bean = applicationContext.getBean(TestMain.class);
        try {
            bean.testReader();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
