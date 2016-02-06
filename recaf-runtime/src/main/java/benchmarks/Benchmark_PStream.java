package benchmarks;

import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_PStream {

	  // For map, count, operations
    private static final int N = Integer.getInteger("benchmark.N", 1000);
    private Integer[] v;

    @Setup
    public void setUp() {
    	v = Helper.fillArray(N);
    }
    
    // Baseline Benchmarks
    @Benchmark
    public long reduce_Baseline() {
        long value = 0L;
        for (int i = 0; i < v.length; i++) {
            value += v[i];
        }
        return value;
    }

    @Benchmark
    public long filter_reduce_Baseline() {
        long value = 0L;
        for (int i = 0; i < v.length; i++) {
            if (v[i] % 2 == 0)
                value += v[i];
        }
        return value;
    }

    @Benchmark
    public long filter_map_reduce_Baseline() {
        long value = 0L;
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
                .filter(x -> x % 2L == 0L)
                .reduce(0, Integer::sum);
        return value;
    }

    @Benchmark
    public Integer filter_map_reduce_Java8Streams() {
        Integer value = java.util.stream.Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(d -> d * d)
                .reduce(0, Integer::sum);
        return value;
    }
    
    //TODO: PStream Benchmarks (fundamentally Pull)
//    @Benchmark
//    public Integer reduce_PStream() {
//    	Integer value = PStream.of(v).sum();
//        return value;
//    }
//
//    @Benchmark
//    public Integer filter_reduce_PStream() {
//    	Integer value = PStream.of(v)
//                .filter(x -> x % 2L == 0L)
//                .sum();
//        return value;
//    }
//
//    @Benchmark
//    public Integer filter_map_reduce_PStream() {
//    	Integer value = PStream.of(v)
//                .filter(x -> x % 2L == 0L)
//                .map(d -> d * d)
//                .sum();
//    	return value;
//    }

}