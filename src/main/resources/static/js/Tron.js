const SCL = 5; // pixel-scale of each tile

var player1, player2;

var gameState = "wait"

function setPlayerVelocityFromCmd(player, cmd){
    switch (cmd) {
      case "left":
        player.setVelocity(createVector(-1, 0));
        break;
      case "up":
        player.setVelocity(createVector(0, -1));
        break;
      case "right":
        player.setVelocity(createVector(1, 0));
        break;
      case "down":
        player.setVelocity(createVector(0, 1));
        break;
    }
}

function initialize() {
    const eventSource = new EventSource('/commands');
    eventSource.onmessage = e => {
        const msg = JSON.parse(e.data);
        if (msg.id != null) {
            if (gameState == "wait") {
                if (player1.id == null) {
                    player1.id = msg.id;
                    document.getElementById("player1").innerHTML = msg.id;
                } else if (player1.id != msg.id) {
                    player2.id = msg.id;
                    document.getElementById("player2").innerHTML = msg.id;
                    gameState = "started";
                    text("Start game", width / 2, height / 2);
                }
            } else {
                document.getElementById("userId").innerHTML = msg.id;
                document.getElementById("command").innerHTML = msg.cmd;

                switch (msg.id) {
                  case player1.id:
                    setPlayerVelocityFromCmd(player1, msg.cmd);
                    break;
                  case player2.id:
                    setPlayerVelocityFromCmd(player2, msg.cmd);
                    break;
                }
            }
        }
    };

    eventSource.onopen = e => console.log('open');
    eventSource.onerror = e => {
        if (e.readyState == EventSource.CLOSED) {
            console.log('close');
        }
        else {
            console.log(e);
        }
    };

    eventSource.addEventListener('second', function(e) {
          console.log('second', e.data);
        }, false);
}



function setup() {

  createCanvas(500, 500);

  frameRate(1);

	/* initialize players */
  player1 = new Bike(50 / SCL, height / 2 / SCL, 1, 0, color("#0000FF"));
  player2 = new Bike((width - 50) / SCL, height / 2 / SCL, -1, 0, color("#FF0000"));
  initialize();
}

function draw() {
    background(51);

    if (gameState == "started"){
	 handlePlayers();
    }
}

/**
 * update, draw, and check collision with players
 */
function handlePlayers() {

	/* update players */
	player1.update();
	player2.update();

	/* draw players */
	player1.draw();
	player2.draw();

	/* check collision */
	if ((player1.collidesWith(player2.trail) && player2.collidesWith(player1.trail)) ||
		(player1.collidesWith(player1.trail) && player2.collidesWith(player2.trail)) ||
		(player1.collidesWithBounds() && player2.collidesWithBounds()))	{

		// both players died at the same time

		endGame("Draw!");
	} else if (player1.collidesWith(player2.trail) ||
		player1.collidesWithBounds() || player1.collidesWith(player1.trail)) {

		// if player1 hits player2 or the side
		// or if player2 hits themself

		endGame("Red wins!");
	} else if (player2.collidesWith(player1.trail) ||
		player2.collidesWithBounds() || player2.collidesWith(player2.trail)) {

		// if player2 hits player1 or the side
		// or if player1 hits themself

		endGame("Blue wins!");
	}
}

/**
 * ends the game, displays the outcome
 */
function endGame(winner) {

  noStroke();
  textAlign(CENTER);
  textSize(60);
  fill(255);
  text(winner, width / 2, height / 2);
  noLoop();
}