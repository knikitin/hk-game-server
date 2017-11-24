# Game server

The test server work on 9101 port. 

The server have the next endoints:

##### Put commands to server
PUT /commands
{ 
    id : String,
    pwd : String,
    cmd : String,
}

##### Simple get commands
GET /simple-commands  
{ 
    id : String,
    cmd : String,
}

##### SSE chanel
GET /commands 
{ 
    id : String,
    cmd : String,
}

### List of commands
left, right, up, down
