package AI;

import main.Location;

public interface Strategy {
	public Location decideNextLocationToPlay(Location loc);
}
