var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var rooms = {};

port = (process.env.PORT || 8001);

server.listen(port, function(){
  //console.log("Server is now running...");
  console.log('Node app is running on port', port);
});


// COMUNICATION




io.on('connection', function(socket){
  socket.on('room', function(arg){
    socket.join(arg.room);
    if(socket.room){
      leaveRoom(socket.room, socket.name);
      socket.leave(socket.room);
    }
    socket.room = arg.room;
    socket.name = arg.name;
    if(!(socket.room in rooms))
      rooms[socket.room] = new room(socket.room);
    socket.join(socket.room);
    var newPlayer = new player(socket.id, socket.name, rooms[socket.room].players.length);
    rooms[socket.room].players.push(newPlayer);
    // console.log(socket.handshake.address.address);
    console.log(socket.request.connection.remoteAddress);
    console.log(socket.request.connection.remotePort);
    // if(rooms.room.gameStarted || rooms.room.playerIndex == 9)
      // socket.disconnect('unauthorized');
    // else if (!socket.room.gameEnded){
    console.log(rooms[socket.room]);
      console.log("Player Connected!");
      socket.emit('getPlayers', rooms[socket.room].players); // sendds
      // players.push(new player(socket.id, playerIndex++));
      // socket.emit('idAndPosition', { id : socket.id, position : playerIndex - 1});
      io.to(socket.room).emit('newPlayer', newPlayer);
      socket.on('disconnect', function(){
        console.log("Disconnected");
        io.to(socket.room).emit('playerDisconnected', socket.id);
        leaveRoom(socket.room, socket.name);
        //TODO: disconnectUser();
      //  console.log("Player Disconnected!");
        // for(var i = 0; i < players.length; i++){
        //   if(players[i].id == socket.id){
        //     socket.broadcast.emit('playerDisconnected', { position : players[i].position});
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
        rooms.room.gameStarted = true;
        // socket.emit('setPlayers', { players : players });
        // socket.broadcast.emit('setPlayers', { players : players });
        // noActivePlayers = players.length;
      });
    // }
  });
});





function player(id, name, position){
  this.id = id;
  this.name = name;
  this.position = position;
  this.active = true;
}

function room(id) {
  this.id = id;
  // this.noActivePlayers = 0;
  this.players = [];
  
  
  this.gameStarted = false;
  this.gameEnded = false;
}

function leaveRoom(id, playerName) {
  var room = rooms[id];
  
  for(i = 0; i < room.players.length; i++)
    if(room.players[i].name == playerName) room.players.splice(i, 1);
}

function fixPlayerPositions(){
  for(var i = 0; i < players.length ; i++){
    players[i].position = i;
  }
}
