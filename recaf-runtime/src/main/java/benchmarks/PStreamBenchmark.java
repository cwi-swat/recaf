package benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import generated.PStream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PStreamBenchmark {

	  // For map, count, operations
    private static final int N = Integer.getInteger("benchmark.N", 1000000);
    private Integer[] v;

    @Setup
    public void setUp() {
    	v = Helper.fillArray(N);
    }
    
    // Baseline Benchmarks
    @Benchmark
    public int reduce_Baseline() {
    	int value = 0;
        for (int i = 0; i < v.length; i++) {
            value += v[i];
        }
        return value;
    }

    @Benchmark
    public int filter_reduce_Baseline() {
        int value = 0;
        for (int i = 0; i < v.length; i++) {
            if (v[i] % 2 == 0)
                value += v[i];
        }
        return value;
    }

    @Benchmark
    public int filter_map_reduce_Baseline() {
        int value = 0;
        for (int i = 0; i < v.length; i++) {
            if (v[i] % 2 == 0)
                value += v[i] * v[i];
        }
        return value;
    }
    
    // Java 8 Benchmarks (fundamentally Push)
    @Benchmark
    public Integer reduce_Java8Streams() {
        Integer value = java.util.stream.Stream.of(v)
                .reduce(0, Integer::sum);
        return value;
    }

    @Benchmark
    public Integer filter_reduce_Java8Streams() {
        Integer value = java.util.stream.Stream.of(v)
                .filter(x -> x % 2 == 0)
                .reduce(0, Integer::sum);
        return value;
    }

    @Benchmark
    public Integer filter_map_reduce_Java8Streams() {
        Integer value = java.util.stream.Stream.of(v)
                .filter(x -> x % 2 == 0)
                .map(d -> d * d)
                .reduce(0, Integer::sum);
        return value;
    }
    
    //PStream Benchmarks (fundamentally Pull)
//    @Benchmark
//    public Integer reduce_PStream() {
//    	Integer value = PStream.of(v).sum();
//        return value;
//    }
//
//    @Benchmark
//    public Integer filter_reduce_PStream() {
//    	Integer value = PStream.of(v)
//                .filter(x -> x % 2 == 0)
//                .sum();
//        return value;
//    }
//
//    @Benchmark
//    public Integer filter_map_reduce_PStream() {
//    	Integer value = PStream.of(v)
//                .filter(x -> x % 2 == 0)
//                .map(d -> d * d)
//                .sum();
//    	return value;
//    }
}