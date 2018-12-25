package ie.gmit.sw.mapbuilder;

import java.util.Map;
import java.util.concurrent.Callable;

import ie.gmit.sw.use.ConcurrentCounterHashMap;

public interface MapBuilder extends Callable<ConcurrentCounterHashMap<Integer>>
{

}
