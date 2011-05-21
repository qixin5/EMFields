package other;

/**
 * In the 2D arrays the first index gives the x-component and the second index the y-component of the position in the map.
 */
public interface ChargedParticle {
	/**
	 * @return the charge of this particle.
	 */
    public double getCharge();
    /**
     * @return the map of the potentials.
     */
    public double[][] getEMMap();
    /**
     * The map of the casted shadow is used to clarify the user the positions of the charged particles.<br>
     * The values in the 2D-array range from 0 to 255, where 0 is no shadow and 255 is the strongest shadow.
     * The shadow map does not have to have the same size as the em map.
     * @return the map of the shadow casted by this particle.
     */
    public int[][] getShadowMap();

    /**
     * @return the ringbuffer with the track of this particle. <code>null</code> when no track is recorded.
     */
    public int[][] getTrack();
    /**
     * @return the position in the ringbuffer for the track. <code>-1</code> when no track is recorded.
     */
    public int getTrackPos();

    /**
     * @return the approximated x-component of the position of this particle.
     */
    public int getX();
    /**
     * @return the approximated y-component of the position of this particle.
     */
    public int getY();
    /**
     * @return the vector of the location of this particle.
     */
    public Vector getLocation();
    /**
     * @return the vector of the velocity of this particle.
     */
    public Vector getVelocity();

    /**
     * Recalculate the forces influencing this particle.<br>
     * You also have to apply the effect of acceleration on velocity and velocity on location here.
     * @param map the map with information about potentials
     */
    public void act(double[][] map);
}
