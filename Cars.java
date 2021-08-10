
public class Cars {

	enum CarType {
		MCYCLE, PRIVATE, TRAILER
	}

	enum ActionType {
		R, C, RC
	}


	private int CarID;
	private ActionType Action;
	private CarType type;

	private int RefuelingTime;
	private int CleaningTime;
	private int startingTime;

	public Cars(int CarID, ActionType Action, CarType type) {
		this.CarID = CarID;
		this.Action = Action;
		this.type = type;
	}
	//Check if a "worker" is done
	public boolean isDone(int currentTime) {
		switch (this.Action) {
		case R:
			return this.RefuelingTime <= currentTime;
		case C:
			return this.CleaningTime <= currentTime;
		case RC:
			return this.RefuelingTime <= currentTime;
		}
		return false;
	}
	//Update the time each vehicle take to do a task accordingly
	public void RefCln(int currentTime) {
		switch (this.type) {
		case MCYCLE:
			this.RefuelingTime = 1 + currentTime;
			this.CleaningTime = 2 + currentTime;
			break;
		case PRIVATE:
			this.RefuelingTime = 3 + currentTime;
			this.CleaningTime = 4 + currentTime;
			break;
		case TRAILER:
			this.RefuelingTime = 5 + currentTime;
			this.CleaningTime = 6 + currentTime;
			break;
		}
	}

	public int getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(int startingTime) {
		this.startingTime = startingTime;
	}

	public int getCarID() {
		return CarID;
	}

	public void setCarID(int carID) {
		CarID = carID;
	}

	public ActionType getAction() {
		return Action;
	}

	public void setAction(ActionType action) {
		Action = action;
	}

	public CarType getType() {
		return type;
	}

	public void setType(CarType type) {
		this.type = type;
	}

	public int getRefuelingTime() {
		return RefuelingTime;
	}

	public void setRefuelingTime(int refuelingTime) {
		RefuelingTime = refuelingTime;
	}

	public int getCleaningTime() {
		return CleaningTime;
	}

	public void setCleaningTime(int cleaningTime) {
		CleaningTime = cleaningTime;
	}

}
