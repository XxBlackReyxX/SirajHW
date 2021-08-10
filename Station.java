import java.util.LinkedList;
import java.util.Queue;

public class Station {
	//Supposed "Workers"
	private Cars Cleaning = null;
	private Cars Refueling = null;
	//Queue for C type
	private Queue<Cars> CQ = new LinkedList<>();
	//Queue for R type
	private Queue<Cars> RQ = new LinkedList<>();
	//Queue for RC type
	private Queue<Cars> RCQ = new LinkedList<>();
	//Queue for RC type that has Refueled
	private Queue<Cars> CF = new LinkedList<>();
	//Queue for RC type that has Cleaned
	private Queue<Cars> RF = new LinkedList<>();

	public Station() {
	}

	public void addCar(Cars car) {
		switch (car.getAction()) {
		case R:
			RQ.offer(car);
			break;
		case C:
			CQ.offer(car);
			break;
		case RC:
			RCQ.offer(car);
		}
	}

	public boolean DoWork(int currentTime) {
		//1st
		//Check if all queues are empty (finished the work)
		if (CF.isEmpty() && RF.isEmpty() && RCQ.isEmpty() && RQ.isEmpty() && CQ.isEmpty() && Refueling == null
				&& Cleaning == null) {
			return false;
		}
		//Refuel a car if the worker is available
		if ((!RQ.isEmpty() || !RCQ.isEmpty()) && Refueling == null) {
			if (!RF.isEmpty()) {
				Refueling = RF.poll();
			} else if (RCQ.isEmpty()) {

				Refueling = RQ.poll();
			} else if (RQ.isEmpty()) {

				CF.offer(RCQ.poll());
				Refueling = CF.peek();
			} else {

				if (RCQ.peek().getCarID() < RQ.peek().getCarID()) {
					CF.offer(RCQ.poll());
					Refueling = CF.peek();
				} else {
					Refueling = RQ.poll();
				}
			}
			Refueling.setStartingTime(currentTime);
			Refueling.RefCln(currentTime);
			System.out.println("Vehicle " + Refueling.getCarID() + " with type " + Refueling.getType()
					+ " starts refueling in time " + Refueling.getStartingTime() + " .");
		}
		
		//2nd

		if (RQ.isEmpty() && RCQ.isEmpty() && RF.isEmpty()) {
			Refueling = null;
		}
		//Refuel a car that has already been cleaned
		if (Refueling != null && Refueling.isDone(currentTime) && !RF.isEmpty()) {
		if(Cleaning !=null && Cleaning.getCarID() != RF.peek().getCarID() ) {
			Refueling = RF.poll();
			Refueling.setStartingTime(currentTime);
			Refueling.RefCln(currentTime);
			System.out.println("Vehicle " + Refueling.getCarID() + " with type " + Refueling.getType()
					+ " starts refueling in time " + Refueling.getStartingTime() + " .");
		}
		else if(Cleaning == null) {
			Refueling = RF.poll();
			Refueling.setStartingTime(currentTime);
			Refueling.RefCln(currentTime);
			System.out.println("Vehicle " + Refueling.getCarID() + " with type " + Refueling.getType()
					+ " starts refueling in time " + Refueling.getStartingTime() + " .");
		}
		}
		//Refuel a car if the worker finished refueling a car
		if (Refueling != null && Refueling.isDone(currentTime)) {
			if ((!RQ.isEmpty() || !RCQ.isEmpty())) {

				  if(RCQ.isEmpty()) {
					Refueling = RQ.poll();
				} else if (RQ.isEmpty()) {
					CF.offer(RCQ.poll());
					Refueling = CF.peek();
				} else {
					if (RCQ.peek().getCarID() < RQ.peek().getCarID()) {
						CF.offer(RCQ.poll());
						Refueling = CF.peek();
					} else {
						Refueling = RQ.poll();
					}
				}
				Refueling.setStartingTime(currentTime);
				Refueling.RefCln(currentTime);
				System.out.println("Vehicle " + Refueling.getCarID() + " with type " + Refueling.getType()
						+ " starts refueling in time " + Refueling.getStartingTime() + " .");
			}
		}

		//3rd
		//Clean a car if the worker is available
		if ((!CQ.isEmpty() || !RCQ.isEmpty()) && Cleaning == null) {
			if (!CF.isEmpty() && Refueling.getCarID() != CF.peek().getCarID()) {

				Cleaning = CF.poll();
			}

			else if (RCQ.isEmpty()) {
				Cleaning = CQ.poll();
			} else if (CQ.isEmpty()) {
				RF.offer(RCQ.poll());
				Cleaning = RF.peek();
			} else {
				if (RCQ.peek().getCarID() < CQ.peek().getCarID()) {
					RF.offer(RCQ.poll());
					Cleaning = RF.peek();
				} else {
					Cleaning = CQ.poll();
				}
			}
			Cleaning.setStartingTime(currentTime);
			Cleaning.RefCln(currentTime);
			System.out.println("Vehicle " + Cleaning.getCarID() + " with type " + Cleaning.getType()
					+ " starts cleaning in time " + Cleaning.getStartingTime() + " .");
		}

		if (CQ.isEmpty() && RCQ.isEmpty() && CF.isEmpty()) {
			Cleaning = null;
		}
		//4th
		//Clean a car after the worker finished cleaning one
		if (Cleaning != null && Cleaning.getCleaningTime() <= currentTime) {
			if ((!CQ.isEmpty() || !RCQ.isEmpty() || !CF.isEmpty())) {
				if (!CF.isEmpty()) {
					Cleaning = CF.poll();
				}

				else if (RCQ.isEmpty()) {
					Cleaning = CQ.poll();
				} else if (CQ.isEmpty()) {
					RF.offer(RCQ.poll());
					Cleaning = RF.peek();
				} else {
					if (RCQ.peek().getCarID() < CQ.peek().getCarID()) {
						RF.offer(RCQ.poll());
						Cleaning = RF.peek();
					} else {
						Cleaning = CQ.poll();
					}
				}
				Cleaning.setStartingTime(currentTime);
				Cleaning.RefCln(currentTime);
				System.out.println("Vehicle " + Cleaning.getCarID() + " with type " + Cleaning.getType()
						+ " starts cleaning in time " + Cleaning.getStartingTime() + " .");

			}
		}

		return true;
	}
	
	public  void run(Cars[] cars) {
		for (Cars car : cars) {
			this.addCar(car);
		}
		int time = 0;
		while (this.DoWork(time)) {
			time++;
		}
	}
	//The given example
	public static void main(String[] args) {
		Station gs = new Station();
		Cars car1 = new Cars(0, Cars.ActionType.RC, Cars.CarType.MCYCLE);
		Cars car2 = new Cars(1, Cars.ActionType.R, Cars.CarType.TRAILER);
		Cars car3 = new Cars(2, Cars.ActionType.RC, Cars.CarType.MCYCLE);
		Cars car4 = new Cars(3, Cars.ActionType.C, Cars.CarType.PRIVATE);
		Cars car5 = new Cars(4, Cars.ActionType.C, Cars.CarType.PRIVATE);
		Cars car6 = new Cars(5, Cars.ActionType.R, Cars.CarType.PRIVATE);
		Cars car7 = new Cars(6, Cars.ActionType.R, Cars.CarType.PRIVATE);
		Cars car8 = new Cars(7, Cars.ActionType.R, Cars.CarType.MCYCLE);
		Cars car9 = new Cars(8, Cars.ActionType.R, Cars.CarType.MCYCLE);
		Cars car10 = new Cars(9, Cars.ActionType.RC, Cars.CarType.PRIVATE);
		Cars car11 = new Cars(10, Cars.ActionType.C, Cars.CarType.TRAILER);
		Cars car12 = new Cars(11, Cars.ActionType.R, Cars.CarType.MCYCLE);
		Cars car13 = new Cars(12, Cars.ActionType.R, Cars.CarType.PRIVATE);
		Cars car14 = new Cars(13, Cars.ActionType.RC, Cars.CarType.TRAILER);
		Cars car15 = new Cars(14, Cars.ActionType.R, Cars.CarType.PRIVATE);

		Cars arr[] = { car1, car2, car3, car4, car5, car6, car7, car8, car9, car10,car11,car12,car13,car14,car15};
		gs.run(arr);

	}

}
