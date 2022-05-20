/*
 * Written by WeiLiang
 * Do not replicate.
 */

// Module aliases
const Engine = Matter.Engine;
const Render = Matter.Render;
const Runner = Matter.Runner;
const Body = Matter.Body;
const Bodies = Matter.Bodies;
const World = Matter.World;
const Events = Matter.Events;
const Composite = Matter.Composite;
const Composites = Matter.Composites;
const Query = Matter.Query;
const Vertices = Matter.Vertices;
const Svg = Matter.Svg;

// Otherworldly devices
var key = 'level-0';
var texts = [ {
	color : '#37bc9b',
	weight : 700,
	size : '120px',
	font : 'Raleway',
	message : 'Hello!',
	position : {
		x : 400,
		y : 500
	}
}, {
	color : '#37bc9b',
	weight : 300,
	size : '36px',
	font : 'Raleway',
	message : 'Use the arrow keys or wasd to move.',
	position : {
		x : 405,
		y : 550
	}
} ];
var bodies = [];
var loading = false;

function query() {
	$.ajax({
		url : '/challenges/genesis',
		type : 'post',
		data : JSON.stringify({
			key : key
		}),
		contentType : 'application/json',
		beforeSend : function(xhr) {
			xhr.setRequestHeader(_csrf_header, _csrf_token);
			loader = setTimeout(function() {
				$('.loader').addClass('loading');
			}, 100);
			loading = true;
		},
		success : function(data, textStatus, jqXHR) {
			if (data.status === 'Perfect')
				default_challenge_success(data);
			else if (data.points > 0)
				awardPartial(data);
			key = data.data.code;
			bodies.forEach(function(body) {
				World.remove(engine.world, body);
			});
			texts = [];
			bodies = [];
			eval(data.data.raw);
		},
		complete : function(jqXHR, textStatus) {
			clearTimeout(loader);
			$('.loader').removeClass('loading');
			loading = false;
		}
	});
}

function awardPartial(response) {
	$.notify({
		icon : "fa fa-hourglass-half",
		title : "Challenge in progress...",
		message : "Mimosa has detected and granted partial "
				+ response.points + " points."
	}, {
		type : "warning",
		allow_dismiss : false
	});
	
	$.get(document.URL, function(html) {
		$("#side-menu").metisMenu("dispose");
		$(".challenge-status").first().html(
				$(html).find(".challenge-status").first().html());
		$("#side-menu").html($(html).find("#side-menu").html());

		$('ul.nav a').filter(function() {
			return this.href == window.location;
		}).addClass('active').parents('#side-menu li').addClass('active');
		$("#side-menu").metisMenu();
	});
}

function prepareSVG(url, callback) {
	var select = function(root, selector) {
		return Array.prototype.slice.call(root.querySelectorAll(selector));
	};
	var loadSvg = function(url) {
		return fetch(url).then(function(response) {
			return response.text();
		}).then(
				function(raw) {
					return (new window.DOMParser()).parseFromString(raw,
							'image/svg+xml');
				});
	};
	loadSvg(url).then(function(root) {
		callback(select(root, 'path').map(function(path) {
			return Svg.pathToVertices(path);
		}));
	});
}

// Prepare canvas
var canvas = $('#game-canvas')[0];
canvas.width = 1920;
canvas.height = 1080;

// Prepare game engine
var engine = Engine.create();
var render = Render.create({
	canvas : canvas,
	engine : engine,
	options : {
		width : canvas.width,
		height : canvas.height,
		pixelRatio : 1,
		wireframes : false,
		background : '#333'
	}
});
Render.run(render);

// Prepare runner
var runner = Runner.create();
Runner.run(runner, engine);

// Prepare borders
World.add(engine.world, [
		Bodies.rectangle(-26, canvas.height / 2, 50, canvas.height, {
			isStatic : true
		}),
		Bodies.rectangle(canvas.width / 2, -26, canvas.width, 50, {
			isStatic : true
		}),
		Bodies.rectangle(canvas.width / 2, canvas.height - 50, canvas.width,
				15, {
					isStatic : true,
					render : {
						fillStyle : '#e6e9ed'
					}
				}) ]);

// Prepare player
var player = Bodies.circle(100, 960, 25, {
	friction : 0,
	frictionStatic : 0,
	inertia : Infinity,
	grounded : false,
	render : {
		fillStyle : '#37bc9b'
	}
});
World.add(engine.world, player);

// Set up key listeners
var keys = [];
$(document.body).keydown(function(event) {
	keys[event.key] = true;
});
$(document.body).keyup(function(event) {
	keys[event.key] = false;
});

// Main game loop
Events.on(runner, "beforeTick", function(event) {
	// Check if above ground
	let bodies = Composite.allBodies(engine.world);
	let startPoint = {
		x : player.position.x - 5,
		y : player.position.y + 30
	}
	let endPoint = {
		x : player.position.x + 5,
		y : player.position.y + 30
	}
	let collisions = Query.ray(bodies, startPoint, endPoint);
	player.grounded = collisions.length > 0;

	// Prevent movement
	if (loading)
		return;

	// Move character
	if ((keys['w'] || keys['ArrowUp'] || keys[' ']) && player.grounded) {
		Body.applyForce(player, player.position, {
			x : 0,
			y : -0.1
		});
	}
	if ((keys['a'] || keys['ArrowLeft']) && player.velocity.x > -15) {
		Body.applyForce(player, player.position, {
			x : -0.01,
			y : 0
		});
	} else if ((keys['d'] || keys['ArrowRight']) && player.velocity.x < 15) {
		Body.applyForce(player, player.position, {
			x : 0.01,
			y : 0
		});
	} else {
		Body.setVelocity(player, {
			x : player.velocity.x * 0.9,
			y : player.velocity.y
		})
	}

	// Reset field after fetch
	if (player.position.x > canvas.width) {
		Body.setPosition(player, {
			x : 25,
			y : player.position.y
		})
		query();
	}
});

// Post rendering text
Events.on(render, 'afterRender', function(event) {
	var ctx = render.context;
	texts.forEach(function(entry) {
		ctx.fillStyle = entry.color;
		ctx.font = entry.weight + ' ' + entry.size + ' ' + entry.font;
		ctx.fillText(entry.message, entry.position.x, entry.position.y);
	});
});
