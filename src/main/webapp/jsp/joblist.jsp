<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Job List</title>
</head>
<body>
 <center><h2>Job List</h2></center>
 <h3>JobFlows</h3>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
    	<tbody>
			<tr>
				<td><strong>Name</strong></td>
				<td><strong>Status</strong></td>
				
				<td><strong>StartTime</strong></td>
				<td><strong>Elapsed</strong></td>
				<td><strong>StopTime</strong></td>
			
				<td><strong>Action</strong></td>

			</tr>
   			<c:forEach items="${jobFlowList}" var="jobFlow">
     		<tr>
				<td>${jobFlow.name}</td>
				<td>${jobFlow.jobStatus.status}</td>
				
				<td>${jobFlow.jobStatus.stopTime!=null?jobFlow.jobStatus.startTime:jobFlow.jobStatus.scheduledStartTime}</td>
				<td>${jobFlow.jobStatus.elapsedTime}</td>	
				<td>${jobFlow.jobStatus.stopTime!=null?jobFlow.jobStatus.stopTime:jobFlow.jobStatus.scheduledStopTime}</td>
						
				<c:url value="" var="startUrl">
					<c:param name="type" value="flow" />
					<c:param name="name" value="${jobFlow.name}" />
 					<c:param name="action" value="start" />
 				</c:url>				
				<c:url value="" var="pauseUrl">
					<c:param name="type" value="flow" />
					<c:param name="name" value="${jobFlow.name}" />
 					<c:param name="action" value="pause" />
 				</c:url>
 				<c:url value="" var="resumeUrl">
 					<c:param name="type" value="flow" />
 					<c:param name="name" value="${jobFlow.name}" />
 					<c:param name="action" value="resume" />
 				</c:url>
 				<c:url value="" var="stopUrl">
 					<c:param name="type" value="flow" />
					<c:param name="name" value="${jobFlow.name}" />
 					<c:param name="action" value="stop" />
 				</c:url>
 				<td><a href="${startUrl}">start</a> | 
 					<a href="${stopUrl}">stop</a> |
 					<a href="${pauseUrl}">pause</a> |
 					<a href="${resumeUrl}">resume</a> 					  
 				</td>
			</tr>
      		</c:forEach>
    </table>
 <h3>JobGroups</h3>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
    	<tbody>
			<tr>
				<td><strong>Name</strong></td>
				<td><strong>Status</strong></td>
				
				<td><strong>StartTime</strong></td>
				<td><strong>Elapsed</strong></td>
				<td><strong>StopTime</strong></td>
			
				<td><strong>Action</strong></td>

			</tr>
   			<c:forEach items="${jobGroupList}" var="jobGroup">
     		<tr>
				<td>${jobGroup.name}</td>
				<td>${jobGroup.jobStatus.status}</td>
				
				<td>${jobGroup.jobStatus.stopTime!=null?jobGroup.jobStatus.startTime:jobGroup.jobStatus.scheduledStartTime}</td>
				<td>${jobGroup.jobStatus.elapsedTime}</td>	
				<td>${jobGroup.jobStatus.stopTime!=null?jobGroup.jobStatus.stopTime:jobGroup.jobStatus.scheduledStopTime}</td>
						
				<c:url value="" var="startUrl">
					<c:param name="type" value="group" />
					<c:param name="name" value="${jobGroup.name}" />
 					<c:param name="action" value="start" />
 				</c:url>				
				<c:url value="" var="pauseUrl">
					<c:param name="type" value="group" />
					<c:param name="name" value="${jobGroup.name}" />
 					<c:param name="action" value="pause" />
 				</c:url>
 				<c:url value="" var="resumeUrl">
 					<c:param name="type" value="group" />
 					<c:param name="name" value="${jobGroup.name}" />
 					<c:param name="action" value="resume" />
 				</c:url>
 				<c:url value="" var="stopUrl">
 					<c:param name="type" value="group" />
					<c:param name="name" value="${jobGroup.name}" />
 					<c:param name="action" value="stop" />
 				</c:url>
 				<td><a href="${startUrl}">start</a> | 
 					<a href="${stopUrl}">stop</a> |
 					<a href="${pauseUrl}">pause</a> |
 					<a href="${resumeUrl}">resume</a> 					  
 				</td>
			</tr>
      		</c:forEach>
    </table>
 <h3>Jobs</h3>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
    	<tbody>
			<tr>
				<td><strong>Name</strong></td>
				<td><strong>Status</strong></td>
				
				<td><strong>StartTime</strong></td>
				<td><strong>Elapsed</strong></td>
				<td><strong>StopTime</strong></td>
				
				<td><strong>Tasks</strong></td>
				<td><strong>Active</strong></td>				
				<td><strong>ThreadPool</strong></td>
				
				<!-- <td><strong>InputQueue</strong></td>				
				<td><strong>WorkQueue</strong></td>				
				<td><strong>OutputQueue</strong></td> -->
				
				<td><strong>Action</strong></td>

			</tr>
   			<c:forEach items="${jobList}" var="job">
     		<tr>
				<td>${job.name}</td>
				<td>${job.jobStatus.status}</td>
				
				<td>${job.jobStatus.stopTime!=null?job.jobStatus.startTime:job.jobStatus.scheduledStartTime}</td>
				<td>${job.jobStatus.elapsedTime}</td>	
				<td>${job.jobStatus.stopTime!=null?job.jobStatus.stopTime:job.jobStatus.scheduledStopTime}</td>
				
				<td>${job.jobStatus.completedTaskCount}/${job.jobStatus.submittedTaskCount}</td>
				<td>${job.jobStatus.activeTaskCount}</td>	
				<td>${job.jobStatus.currentThreadCount}/${job.maxThreadCount}</td>
				
				<%-- <td>${job.inputQueue.size()}</td>				
				<td>${job.workQueue.size()}</td>		
				<td>${job.outputQueue.size()}</td>	 --%>	
						
				<c:url value="" var="startUrl">
					<c:param name="type" value="job" />
					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="start" />
 				</c:url>				
				<c:url value="" var="pauseUrl">
					<c:param name="type" value="job" />
					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="pause" />
 				</c:url>
 				<c:url value="" var="resumeUrl">
 					<c:param name="type" value="job" />
 					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="resume" />
 				</c:url>
 				<c:url value="" var="stopUrl">
 					<c:param name="type" value="job" />
					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="stop" />
 				</c:url>
 				<td><a href="${startUrl}">start</a> | 
 					<a href="${stopUrl}">stop</a> |
 					<a href="${pauseUrl}">pause</a> |
 					<a href="${resumeUrl}">resume</a> 					  
 				</td>
			</tr>
      		</c:forEach>
    </table>
</body>
</html>