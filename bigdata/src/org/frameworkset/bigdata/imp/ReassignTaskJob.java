package org.frameworkset.bigdata.imp;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.frameworkset.bigdata.imp.monitor.JobStatic;

public class ReassignTaskJob {

	public ReassignTaskJob() {
		// TODO Auto-generated constructor stub
	}
	
	public void execute(ReassignTask reassignTask)
	{
		String localnode = Imp.getImpStaticManager().getLocalNode();
		 JobStatic jobStatic = Imp.getImpStaticManager().addReassignTaskJobStatic(reassignTask);
		 JobStatic localjobStatic  = Imp.getImpStaticManager().getLocalJobStatic(localnode);
		 if(localjobStatic == null)
		 {
			 jobStatic.setErrormsg("作业"+reassignTask.getReassigntaskJobname()+"不存在.");
		 }
		 else if(localjobStatic.stopped() )
		 {
			 jobStatic.setErrormsg("作业"+reassignTask.getReassigntaskJobname()+"已经结束.");
		 }
		 else//分配重新分配reassigntaskJobname作业开始
		 {
		 
			 Map<String, Integer> hostTaskInfos =reassignTask.getHostTaskInfos();
			 int servers = hostTaskInfos.size() + 1;
			 int[] perservertasks = new int[servers];//存储每个节点应该分配的任务数
			 String[] servseradd = new String[servers];
			 servseradd[0] = localnode;
			 synchronized(localjobStatic)
			 {
				 int unhandletask = localjobStatic.getUnruntasks();
				 if(unhandletask == 0)
				 {
					 jobStatic.setErrormsg("作业"+reassignTask.getReassigntaskJobname()+"未处理任务已经分派完毕，不需要进行重新分配.");
				 }
				 else
				 {
					 //计算每个节点需要重新分配的任务数，根据节点监控数据来分配：排队任务，未处理任务，正在处理任务
					int unhandletasks = localjobStatic.getUnruntasks() ;
					int selfinhandle = localjobStatic.getWaittasks() + localjobStatic.getRuntasks();
					int totaltasks = unhandletasks + selfinhandle;
					Iterator<Entry<String, Integer>> it = hostTaskInfos.entrySet().iterator();
					int i = 1;
					while(it.hasNext())
					{
						Entry<String, Integer> tasks  = it.next();
						totaltasks = totaltasks + tasks.getValue().intValue(); 
						servseradd[i] = tasks.getKey();
						i ++;
						
					}
					int newtasks = totaltasks / servers;
					int div = totaltasks % servers;
					for(int j = 0 ; j < servers; j ++)
					{
						
						if(j < div)
							perservertasks[j] = newtasks + 1;
						else
							perservertasks[j] = newtasks;
						if(j == 0 )
							perservertasks[j] = perservertasks[j] - selfinhandle;
						else
							perservertasks[j] = perservertasks[j] - hostTaskInfos.get(servseradd[j]).intValue();						
					}
					
					
					
					 
				 }
			 }
			 StringBuilder builder = new StringBuilder();
	//		 for(String dbname:dbnames)
	//		 {
	//			 try {
	//				DBUtil.stopPool(dbname);
	//				
	//			} catch (Exception e) {
	//				builder.append("停止dbname[").append(dbname).append("]失败:\r\n").append(SimpleStringUtil.exceptionToString(e)).append("\r\n");
	//			}
	//		 }
			 if(builder.length() > 0)
			 {
				 jobStatic.setErrormsg(builder.toString());
				
			 }
		 }
		 jobStatic.setEndTime(System.currentTimeMillis());
		 jobStatic.setStatus(1);
		
	}

}
