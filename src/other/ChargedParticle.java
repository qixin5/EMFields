package other;


public interface ChargedParticle {
    public double getCharge();
    public double[][] getEMMap();

    public int getX();
    public int getY();
    public Vector getLocation();
    public Vector getVelocity();

    public void act(double[][] map);
}
