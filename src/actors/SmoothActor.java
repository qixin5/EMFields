package actors;

import java.util.Random;

import other.Vector;

public class SmoothActor {
	static final Random randgen = new Random(System.currentTimeMillis());

	Vector location;
	Vector velocity;
	Vector acceleration;

	public SmoothActor() {
		location = new Vector();
		velocity = new Vector();
		acceleration = new Vector();
	}

	public void applyMovement() {
		location.add(velocity);
		velocity.add(acceleration);
	}

	public Vector getLocation() {
		return location;
	}
	public Vector getVelocity() {
		return velocity;
	}
	public Vector getAcceleration() {
		return acceleration;
	}

	public void shiverLocation(double strength) {
		location.setX(location.getX()+randgen.nextDouble()*strength);
		location.setY(location.getY()+randgen.nextDouble()*strength);
	}
	public void shiverAcceleration(double strength) {
		acceleration.setX(acceleration.getX()+randgen.nextDouble()*strength);
		acceleration.setY(acceleration.getY()+randgen.nextDouble()*strength);
	}
}
