import annotations.*;
import handlers.ExpectedHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String[] args){

        Method[] methods = MyTest.class.getDeclaredMethods();
        Map<Class,Method> methMap;
        if((methMap = checkAnnotations(methods)) == null){
            System.out.println("Check number of annotations");
            System.out.println("@SetUp, @Destroy, @Before, @After");
        }
        Map<TestStatus, ArrayList<String>> statusMap = new HashMap<>();
        try {
            invokeMethod(methMap,SetUp.class);
            for(Method m : methods){
                if(m.isAnnotationPresent(Test.class)){
                    if(m.getAnnotation(Test.class).isEnabled()){
                        invokeMethod(methMap,Before.class);
                        try {
                            m.invoke(MyTest.class.newInstance());
                            addStatistic(statusMap,TestStatus.PASSED,m.getName());
                        }catch (Exception e){
                            if(m.isAnnotationPresent(Expected.class)){
                                if(ExpectedHandler.check(m.getAnnotation(Expected.class),e)){
                                    addStatistic(statusMap,TestStatus.PASSED,m.getName());
                                }else {
                                    addStatistic(statusMap,TestStatus.FAILD,m.getName());
                                }
                            }else {
                                addStatistic(statusMap,TestStatus.FAILD,m.getName());
                            }
                        }
                        invokeMethod(methMap,After.class);
                    }else {
                        addStatistic(statusMap,TestStatus.SKIPPED,m.getName());
                    }
                }
            }
            invokeMethod(methMap,Destroy.class);
        }catch (Exception e){
            System.out.println("Testing faild");
        }
        printStatistic(statusMap);

    }

    public static Map checkAnnotations(Method[] methods){
        Map<Class,Method> map = new HashMap<>();
        for(Method m : methods){
            Annotation[] annotations = m.getDeclaredAnnotations();
            for(Annotation a : annotations){
                Class cl = a.annotationType();
                if(cl.equals(SetUp.class) || cl.equals(Destroy.class) ||
                        cl.equals(Before.class) || cl.equals(After.class)){
                    if(map.put(cl,m) != null){
                        return null;
                    }
                }
            }
        }
        return map;
    }

    public static void invokeMethod(Map map, Class cl) throws IllegalAccessException,
            InstantiationException, InvocationTargetException {
        Method m = (Method) map.get(cl);
        if(m != null){
            m.invoke(MyTest.class.newInstance());
        }
    }

    public static void printStatistic(Map map){
        map.forEach((k,v)->{
            System.out.println(k + ":");
            for(String s : (ArrayList<String>)v){
                System.out.println(" " + s);
            }
        });

    }

    public static void addStatistic(Map map, TestStatus stat, String name){
        ArrayList<String> list = (ArrayList<String>) map.get(stat);
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(name);
        map.put(stat,list);
    }

}
