This application is an example of securing some of the specific endpoints as it happens in real world.

For that we created a configuration class and extended **WebSecurityConfigurerAdapter** class.
Then overridden its configure method to specify that which url I want to secure and which should be publicly accessible.

Also I specified that, when request is from a browser then ask for form login and when the request is from non-browser tools like postman
then ask for http basic authentication.

Here I have used the default authentication i.e. default username and autogenerated default password for login.
default user for spring security authentication: user
default password for spring security authentication: auto-generated unique string. check the console log