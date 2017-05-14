package statistics;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import statistics.WordCount.Map;
import statistics.WordCount.Reduce;

public class Statistics {

	/**
	 * @param args
	 */
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> 
	{
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
		{
			StringTokenizer itr= new StringTokenizer(value.toString());
			
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one); 
			}
		}
		
		public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>
		{
			private IntWritable result= new IntWritable();
			
			public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException
			{
				int sum=0;
				for(IntWritable number: values)
				{
					sum=sum+number.get();
				}
				result.set(sum);
				
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration(); 
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); // get all args
	    if (otherArgs.length != 2) {
	      System.err.println("Usage: WordCount <in> <out>");
	      System.exit(2);
	    }

	    // create a job with name "wordcount"
	    Job job = new Job(conf, "wordcount");
	    job.setJarByClass(WordCount.class);
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	   
	    // Add a combiner here, not required to successfully run the wordcount program  

	    // set output key type   
	    job.setOutputKeyClass(Text.class);
	    // set output value type
	    job.setOutputValueClass(IntWritable.class);
	    //set the HDFS path of the input data
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    // set the HDFS path for the output
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

	    //Wait till job completion
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }

}
