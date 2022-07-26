var w = c.width = window.innerWidth,
		h = c.height = window.innerHeight,
		ctx = c.getContext( '2d' ),
		
		cx = w / 2 |0,
		cy = h / 2 |0,
		
		opts = {
			baseSize: 8,
			addedSize: 8,
			baseSpeed: .1,
			addedSpeed: .1,
			baseAccel: .06,
			addedAccel: .1,
			colorAttenuator: .01,
			baseAlpha: .2,
			addedAlpha: .2,
			baseLineNum: 3,
			addedLineNum: 8,
			speedSizeMultiplier: .3,
			particles: 100,
			spawnChance: .5
		},
		
		particles = [],
		tick = 0;

function Particle(){
	this.reset();
}
Particle.prototype.reset = function(){
	
	this.x = this.y = 0;
	this.rad = Math.random() * Math.PI * 2;
	this.size = opts.baseSize + opts.addedSize * Math.random();
	
	this.speed = opts.baseSpeed + opts.addedSpeed * Math.random();
	this.accel = opts.baseAccel + opts.addedAccel * Math.random();
	
	this.cos = Math.cos( this.rad );
	this.sin = Math.sin( this.rad );
	
	this.vx = this.cos * this.speed;
	this.vy = this.sin * this.speed;
	this.ax = this.cos * this.accel;
	this.ay = this.sin * this.accel;
	
	this.tick = tick;
	this.color = 'hsla(hue,80%,50%,alp)';
}
Particle.prototype.step = function(){

	this.speed += this.accel;
	this.x += this.vx += this.ax;
	this.y += this.vy += this.ay;
	
	ctx.strokeStyle = this.color.replace( 'hue', this.tick + ( tick - this.tick ) * opts.colorAttenuator ).replace( 'alp', opts.baseAlpha + opts.addedAlpha * Math.random() );
	ctx.beginPath();
	
	var num = opts.baseLineNum + opts.addedLineNum * Math.random() |0,
			prevPointX = this.x - this.vx * 3,
			prevPointY = this.y - this.vy * 3,
			added = this.size + this.speed * opts.speedSizeMultiplier;
	
	for( var i = 0; i < num; ++i ){
		ctx.moveTo( this.x, this.y );
		ctx.lineTo( prevPointX + ( Math.random() - .5 ) * added |0, prevPointY + ( Math.random() - .5 ) * added |0 );
	}
	
	ctx.stroke();
	
	if( this.x < -cx || this.x > cx || this.y < -cy || this.y > cy )
		this.reset();
}

ctx.fillStyle = '#222';
ctx.fillRect( 0, 0, w, h );
function anim(){
	
	window.requestAnimationFrame( anim );
	
	++tick;
	
	ctx.globalCompositeOperation = 'source-over';
	ctx.fillStyle = 'rgba(20,20,20,.1)';
	ctx.fillRect( 0, 0, w, h );
	ctx.globalCompositeOperation = 'lighter';
	
	if( particles.length < opts.particles && Math.random() < opts.spawnChance )
		particles.push( new Particle );
	
	ctx.translate( cx, cy );
	particles.map( stepParticle );
	ctx.translate( -cx, -cy );
}
function stepParticle( particle ){
	particle.step();
}
anim();

window.onresize = function(){
	
	w = c.width = window.innerWidth;
	h = c.height = window.innerHeight;
	cx = w / 2;
	cy = h / 2;
	
	ctx.fillStyle = '#222';
	ctx.fillRect( 0, 0, w, h );
}

window.onclick = function( e ){
	
	var part = new Particle();
	part.x = e.clientX - cx;
	part.y = e.clientY - cy;
	
	particles.push( part );
}