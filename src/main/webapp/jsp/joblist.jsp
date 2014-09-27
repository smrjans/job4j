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
 <h2>Job List</h2>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
    	<tbody>
			<tr>
				<td><strong>Name</strong></td>
				<td><strong>Status</strong></td>
				
				<td><strong>StartTime</strong></td>
				<td><strong>Elapsed</strong></td>
				<td><strong>EndTime</strong></td>
				<td><strong>NextFireTime</strong></td>				
				
				<td><strong>Tasks</strong></td>
				<td><strong>Active</strong></td>				
				<td><strong>ThreadPool</strong></td>
				
				<td><strong>InputQueue</strong></td>				
				<td><strong>WorkQueue</strong></td>				
				<td><strong>OutputQueue</strong></td>
				
				<td><strong>Action</strong></td>

			</tr>
   			<c:forEach items="${jobList}" var="job">
     		<tr>
				<td>${job.name}</td>
				<td>${job.status}</td>
				
				<td>${job.jobStatus.startTime}</td>
				<td>${job.jobStatus.elapsedTime}</td>	
				<td>${job.jobStatus.scheduledEndTime}</td>
				<td>${job.jobStatus.scheduledStartTime}</td>
				
				<td>${job.jobStatus.completedTaskCount}/${job.jobStatus.submittedTaskCount}</td>
				<td>${job.jobStatus.activeTaskCount}</td>	
				<td>${job.jobStatus.currentThreadCount}/${job.maxThreadCount}</td>
				
				<td>${job.inputQueue.size()}</td>				
				<td>${job.workQueue.size()}</td>		
				<td>${job.outputQueue.size()}</td>		
						
				<c:url value="" var="startUrl">
					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="start" />
 				</c:url>				
				<c:url value="" var="pauseUrl">
					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="pause" />
 				</c:url>
 				<c:url value="" var="resumeUrl">
 					<c:param name="name" value="${job.name}" />
 					<c:param name="action" value="resume" />
 				</c:url>
 				<c:url value="" var="stopUrl">
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