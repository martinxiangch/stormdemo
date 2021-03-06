package martin.com.stormdemo1;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new Wordspout(), 1);
		builder.setBolt("split", new SplitSentenceBolt(), 2).shuffleGrouping("spout");
		builder.setBolt("count", new WordCountBolt(), 2).fieldsGrouping("split", new Fields("word"));
 
		Config conf = new Config();
		conf.setDebug(false);
		
		if (args != null && args.length > 0) {
			// 集群模式
			conf.setNumWorkers(2);
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			// 本地模式
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word-count", conf, builder.createTopology());
		    Thread.sleep(10000);  
	        cluster.shutdown(); 
		}
	}
    
}
