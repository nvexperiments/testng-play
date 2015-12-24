package edu.nitin.testnp.issue112;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class VarArgsDataProviderTest {
    private static final Object[] S0 = new Object[]{new String[]{"a"}};
    private static final String[] S1 = new String[]{"a", "b"};
    private static final Object[] S2 = new Object[]{new String[]{"a", "b", "c"}};
    private static final Object[] S3 = new Object[]{new String[]{"a", "b", "c", "d"}};


    @DataProvider(name = "data", indices = {1, 3})
    public Object[][] data() {
        return new Object[][]{S0, S1, S2, S3};
    }

    @Test(dataProvider = "data")
    public void testArray(String[] o) {
        System.out.println(Arrays.asList(o));
    }

    @Test(dataProvider = "data")
    public void testVarArgs(String... o) {
        System.out.println(Arrays.asList(o));
    }

    @Test(dataProvider = "data")
    public void testArray2(String[] o) {
        System.out.println(Arrays.asList(o));
    }

    @Test(dataProvider = "data")
    public void testVarArgs2(String... o) {
        System.out.println(Arrays.asList(o));
    }
}
