***Spring Security with JWT token for API(microservices)***
Now in this spring application we basically will
try to authenticate multiple user
(i.e we will have n numbers of user) from our
database first and one the user is authenticated we will provide 
JWT token in the API response. her we are creating JWT for Microservices 
i.e. for API only so the authentication is stateless which means session is not stored in 
server side. Every time client need/API need to carry the issued 
JWT token to authenticate itself. Session in the securityContextHolder will only last 
till the request is fulfilled. Once the response is sent the 
authentication object is removed from the SecurityContextHolder.


This type of authentication is used when you have Microservice architecture
and you have **n** number of users to login. So we authenticate 
user and give them(the client) a unique JWT token so that they can
verify themselves with server with every subsequent request they carry.


Also, we will be securing some selected
endpoints for selected role only as we did in earlier example
applications.
