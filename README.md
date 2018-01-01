# performance-testing
Sample java application and performance tests

##bottlenecks
1. Data source rejected establishment of connection,  message from server: "Too many connections"]
2. 2018-01-01 21:00:43.881 ERROR 6460 --- [o-8080-exec-107] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: The driver was unable to create a connection due to an inability to establish the client portion of a socket.
   
   This is usually caused by a limit on the number of sockets imposed by the operating system. This limit is usually configurable. 
   
   For Unix-based platforms, see the manual page for the 'ulimit' command. Kernel or system reconfiguration may also be required.
   
   For Windows-based platforms, see Microsoft Knowledge Base Article 196271 (Q196271).] with root cause
   
   java.net.BindException: Address already in use: connect
