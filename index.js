var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var MAX_NUM_PLAYERS = 6;
var MIN_NUM_PLAYERS = 3;
var rooms = {};

var port = (process.env.PORT || 8001);

server.listen(port, function(){
  //console.log("Server is now running...");
  console.log('Node app is running on port', port);
});


// COMUNICATION




io.on('connection', function(socket){
  socket.emit('getRooms', rooms);
  socket.on('room', function(arg){
    if(!isJoinable(arg.room))
      return; //TODO: edit, send message to reload
    socket.join(arg.room);
    if(socket.room){
      leaveRoom(socket.room, socket.name);
      socket.leave(socket.room);
    }
    socket.room = arg.room;
    socket.name = arg.name;
    var playerIp = socket.request.connection.remoteAddress;
    var newPlayer = new player(socket.id, socket.name, -1, playerIp);
    socket.emit('youAre', newPlayer);
    if(!(socket.room in rooms))
      rooms[socket.room] = new room(socket.room);
    socket.join(socket.room);
    rooms[socket.room].players.push(newPlayer);
    // console.log(socket.handshake.address.address);
    // console.log(socket.request.connection.remotePort);
      socket.emit('getPlayers', rooms[socket.room].players); // sendds
      // players.push(new player(socket.id, playerIndex++));
      // socket.emit('idAndPosition', { id : socket.id, position : playerIndex - 1});
      io.to(socket.room).emit('newPlayer', newPlayer);
      socket.on('disconnect', function(){
        console.log("Disconnected");
        io.to(socket.room).emit('playerDisconnected', socket.id);
//        disconnectUser(socket.room, socket.id);
        leaveRoom(socket.room, socket.id);

      //  console.log("Player Disconnected!");
//             socket.broadcast.emit('playerDisconnected', { position : players[i].position});
        //     if(gameStarted){
        //       rooms[socket.room].players[i].playing = false;
        //       noActivePlayers--;
        //       if(noActivePlayers == 0){
        //         playerIndex = 0;
        //         players = [];
        //         noActivePlayers=0;
        // 
        //         gameStarted = true;
        //         gameEnded = true;
        //       }
        //     }
        //     else {
        //       players.splice(i, 1);
        //       playerIndex--;
        //       fixPlayerPositions();
        //     }
        //   }
        // }
        //console.log(players);
      });
      // socket.on('playerName', function(name){
      //   for(var i = 0; i < players.length; i++){
      //     if(players[i].id == socket.id){
      //       players[i].name = name;
      //       socket.broadcast.emit('newPlayer', { id : socket.id, name : players[i].name, position: players[i].position});
      //       //console.log(players);
      //     }
      //   }
      // });
      socket.on('gameStarted', function(){

        //console.log("Game Started " + socket.id);
        if(rooms[socket.room].players.length < MIN_NUM_PLAYERS)
            return;
            
        rooms[socket.room].gameStarted = true;

        for(var i = 0; i < rooms[socket.room].players.length; i++) {
            rooms[socket.room].players[i].position = i;
        }
        io.to(socket.room).emit('setPlayers', { players : rooms[socket.room].players });
        // socket.broadcast.emit('setPlayers', { players : players });
        // noActivePlayers = players.length;
      });
    // }
  });
});



function disconnectUser(roomId, playerId) { //TODO: IF Game started....
    var room = rooms[roomId];
    for(var i = 0; i < room.players.length; i++){
        if(room.players[i].id == playerId)
            return players.splice(i, 1)[0];
    }
}

function player(id, name, position, ip){
  this.id = id;
  this.name = name;
  this.position = position;
  this.active = true;

  //connection
  this.ip = ip;
}

function room(id) {
  this.id = id;
  // this.noActivePlayers = 0;
  this.players = [];
  
  
  this.gameStarted = false;
  this.gameEnded = false;
}

function leaveRoom(id, playerId) {
  var room = rooms[id];
  
  for(var i = 0; i < room.players.length; i++)
    if(room.players[i].id == playerId) room.players.splice(i, 1);

   if(room.players.length == 0)
     delete rooms[id];
  //TODO: test DELETE ROOM IF EMPTY
}

function isJoinable(roomId) {
  var room = rooms[roomId];
  if(room)
    return !room.gameStarted && !room.gameEnded && room.players.length < MAX_NUM_PLAYERS; //TODO: Check if player should be in game
   else
    return true;
}

function fixPlayerPositions(){
  for(var i = 0; i < players.length ; i++){
    players[i].position = i;
  }
}
