package benchmarks;

public class Helper {

    public static Integer[] fillArray(int range) {
    	Integer[] array = new Integer[range];
        for (int i = 0; i < range; i++) {
            array[i] = i % 1000;
        }
        return array;
    }

}