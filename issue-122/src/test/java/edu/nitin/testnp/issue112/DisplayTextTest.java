package edu.nitin.testnp.issue112;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by nitin.verma on 12/22/15.
 */
public class DisplayTextTest {
    @DataProvider(
            name = "dp", parallel = false, indices = {2, 3}
    )
    public Object[][] testStrings() {
        return new Object[][]{
                new Object[]{"1", new String[]{"one"}},
                new Object[]{"2", new String[]{"two"}},
                new String[]{"1", "two"},
                new Object[]{"3", new String[]{"three", "four"}}
        };
    }

    @Test(dataProvider = "dp")
    public void badTest0(String s, String... strings) {
        for (String item : strings) {
            System.out.println("An item is \"" + item + "\"");
        }
    }

    @Test(dataProvider = "dp")
    public void goodTest(String s, String[] strings) {
        for (String item : strings) {
            System.out.println("An item is \"" + item + "\"");
        }
    }

    @Test(dataProvider = "dp")
    public void badTest(String s, String... strings) {
        for (String item : strings) {
            System.out.println("An item is \"" + item + "\"");
        }
    }
}
