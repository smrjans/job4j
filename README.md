job4j
=====

Java framework to create batch job and workflow easily 

Its not just another framework like JBatch/Spring Batch/Easy Batch

We have created the framework to support various batch processing technologies on a single platform.
As of now it only supports BlockingQueue & ThreadPoolExecuter based solution.

Basic Documentation:
1. Job can be controlled/monitored by web: http://localhost:8080/job4j-0.0.1/joblist
2. Jobs, JobGroups and JobFlow can be started/scheduled independently.
3. Multiple Job can be added to a JobGroup which runs parallely.
4. Multiple JobGroup can be added to a JobFlow which runs sequentially.
5. If a JobFlow contains Jobs in its two adjacent JobGroup, where output of a Job in 1st JobGroup is of same type as input of another Job in next JobGroup, a JobPipe gets created automatically fow data flow.  
6. Developers just need to implement 3 Interfaces(1 method in each): InputProducer, Task, OutputConsumer
		
	It's initial version of implementation...
	Design can change to accomodate future implementations.

Future Works:
1. Configure Job via DB
2. Fork Join Implementation
3. Map Reduce Implementation

Easy Configuration (currently using spring context file) to create Job:
----------
    <bean id="job1InputProducer" class="com.talentica.job4j.test.job1.Job1InputProducer"/>
    <bean id="job1Task" class="com.talentica.job4j.test.job1.Job1Task" scope="prototype"/>
    <bean id="job1OutputConsumer" class="com.talentica.job4j.test.job1.Job1OutputConsumer"/> 
 	
  	<bean id="testJob1" class="com.talentica.job4j.impl.queue.QueueJob">
	  	<property name="name" value="TestJob1" />
		<property name="startCronSchedule" value="0 * * * *" />
		<property name="stopCronSchedule" value="55 * * * *" />				
		<property name="timeZone" value="IST" />
		
  		<property name="inputProducer" ref="job1InputProducer" />		
	  	<property name="task" ref="job1Task" />
		<property name="outputConsumer" ref="job1OutputConsumer" />	
		
	  	<property name="maxThreadCount" value="5" />
  		<property name="threadSleepTime" value="4000" />
	</bean>
	----------
	
	To create JobFlow:
	-----------------
	<bean id="jobGroup1" class="com.talentica.job4j.model.JobGroup">
		<property name="jobList">
			<list>
				<ref bean="testJob1"/>
				<ref bean="testJob2"/>
			</list>
		</property>
	</bean>
	
	<bean id="jobGroup2" class="com.talentica.job4j.model.JobGroup">
		<property name="jobList">
			<list>
				<ref bean="testJob3"/>
			</list>
		</property>
	</bean>
	
	<bean id="jobFlow1" class="com.talentica.job4j.model.JobFlow">
		<property name="jobGroupList">
			<list>
				<ref bean="jobGroup1"/>
				<ref bean="jobGroup2"/>
			</list>
		</property>
	</bean>
	-----------------
		
		
