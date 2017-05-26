// var socket = require('socket.io-client')('https://sdis-cardsagainsthumanity.herokuapp.com');
 var socket = require('socket.io-client')('http://localhost:8001');

socket.on('newPlayer', function(data){
  console.log(data);
});
socket.on('getPlayers', function(data){
  console.log("\nPLAYERS\n");
  console.log(data);
});
socket.on('')

socket.emit('room', {room: process.argv[2], name:process.argv[3]})