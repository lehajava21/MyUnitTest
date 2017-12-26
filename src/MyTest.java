import annotations.*;

import java.io.IOException;

public class MyTest {

    @SetUp
    public void setUp() {
        System.out.println("setUp");
    }

    @Destroy
    public void destroy() {
        System.out.println("destroy");
    }

    @Before
    public void before() {
        System.out.println("before");
    }

    @After
    public void after() {
        System.out.println("after");
    }

    @Test(isEnabled = false)
    public void test1() throws Exception {
        System.out.println("test1");
    }

    @Test
    @Expected(exceptionType = NullPointerException.class)
    public void test2() throws Exception {
        System.out.println("test2");
        String s = null;
        s.isEmpty();
    }

    @Test
    @Expected(exceptionType = IOException.class)
    public void test3() throws Exception {
        System.out.println("test3");
        String s = null;
        s.isEmpty();
    }

    @Test
    public void test4() throws Exception {
        System.out.println("test4");
        String s = null;
        s.isEmpty();
    }

}
